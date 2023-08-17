package me.vasylkov.OpenAI;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.ModelType;
import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import me.vasylkov.bot.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class ChatRequest
{
    private final TelegramBotUser user;
    private final String message;
    private final GptModels model;
    private static final Encoding enc = Encodings.newDefaultEncodingRegistry().getEncodingForModel(ModelType.GPT_4);
    public static final Integer GPT3_MAX_TOKENS = 9000;
    public static final Integer GPT4_MAX_TOKENS = 1000;
    private static final Properties configProperties = PropertiesManager.getConfigProperties();
    private static final String GPT_3_TOKEN = configProperties.getProperty("openAiGpt3.token");
    private static final String GPT_4_TOKEN = configProperties.getProperty("openAiGpt4.token");

    public ChatRequest(TelegramBotUser user, String message, GptModels model)
    {
        this.user = user;
        this.message = message;
        this.model = model;
    }

    private int countPromptTokens()
    {
        int totalCount = 0;
        for (ChatMessage msg : user.getMessageList())
        {
            totalCount += enc.countTokens(msg.getContent());
        }
        return totalCount;
    }

    private int countCompletionTokens(String msg)
    {
        return enc.countTokens(msg);
    }

    private void createChat(TelegramBot instance, int tokens, Boolean isException)
    {
        Long chatId = user.getChatId();
        try
        {
            synchronized (user.getMessageList())
            {
                int waitMsgId = instance.sendMessage(chatId, ReplyMarkups.PREVIOUS, Messages.REQUEST_WAITING);
                if (!isException)
                {
                    //LogFiles.writeToRequestLog(user, message, model);
                }
                if (!isException)
                {
                    ChatMessage msg = new ChatMessage(ChatMessageRole.USER.value(), message);
                    user.getMessageList().add(msg);
                }

                if (model.equals(GptModels.GPT4) && user.getMessageList().size() > 6)
                {
                    user.getMessageList().remove(0);
                    user.getMessageList().remove(0);
                }

                OpenAiService service = new OpenAiService(model.equals(GptModels.GPT3) ? GPT_3_TOKEN : GPT_4_TOKEN, Duration.ofSeconds(10000));
                ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder().model(model.equals(GptModels.GPT3) ? "gpt-3.5-turbo-16k" : "gpt-4").messages(user.getMessageList()).n(1).maxTokens(tokens).logitBias(new HashMap<>()).build();
                List<ChatCompletionChoice> choices = service.createChatCompletion(chatCompletionRequest).getChoices();
                instance.deleteMessage(user.getChatId(), waitMsgId);
                instance.sendMessage(chatId, ReplyMarkups.CHAT_MENU, choices.get(0).getMessage().getContent());

                if (model.equals(GptModels.GPT4))
                {
                    user.setTokensBalance(user.getTokensBalance() - (countPromptTokens() + countCompletionTokens(choices.get(0).getMessage().getContent())));
                  //  LogFiles.writeUsersToFile();
                }
                user.getMessageList().add(choices.get(0).getMessage());
            }
        }
        catch (OpenAiHttpException e)
        {
            if (model.equals(GptModels.GPT3) && user.getMessageList().size() >= 6)
            {
                user.getMessageList().remove(0);
                user.getMessageList().remove(0);
                createChat(instance, GPT3_MAX_TOKENS, true);
            }
            else
            {
                instance.sendMessage(chatId, ReplyMarkups.NULL, Messages.REQUEST_ERROR);
                return;
            }
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
        }
    }

    public void sendNewChatRequest(TelegramBot instance, int tokens)
    {
        Thread thread = new Thread(() -> createChat(instance, tokens, false));
        thread.start();
    }
}
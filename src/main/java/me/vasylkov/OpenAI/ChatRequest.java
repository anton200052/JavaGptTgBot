package me.vasylkov.OpenAI;

import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.utils.TikTokensUtil;
import me.vasylkov.bot.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class ChatRequest
{
    private static final Properties configProperties = PropertiesManager.getConfigProperties();
    private static final String GPT_3_VERSION = configProperties.getProperty(PropertiesKeys.CONFIG_GPT3_VERSION.getProperty());
    private static final String GPT_4_VERSION = configProperties.getProperty(PropertiesKeys.CONFIG_GPT4_VERSION.getProperty());
    public static final Integer GPT3_MAX_TOKENS = Integer.valueOf(configProperties.getProperty(PropertiesKeys.CONFIG_GPT3_MAX_TOKENS.getProperty()));
    public static final Integer GPT4_MAX_TOKENS = Integer.valueOf(configProperties.getProperty(PropertiesKeys.CONFIG_GPT3_MAX_TOKENS.getProperty()));
    private static final String GPT_3_TOKEN = configProperties.getProperty(PropertiesKeys.CONFIG_GPT3_TOKEN.getProperty());
    private static final String GPT_4_TOKEN = configProperties.getProperty(PropertiesKeys.CONFIG_GPT4_TOKEN.getProperty());

    private static void createChat(TelegramBot instance, TelegramBotUser user, String message, GptModels model)
    {
        Long chatId = user.getChatId();
        try
        {
            synchronized (user.getMessageList())
            {
                List<ChatMessage> msgList = user.getMessageList();
                int waitMsgId = instance.sendMessage(chatId, ReplyMarkups.getPrevious(), user.getMsgProperties().getProperty(PropertiesKeys.CHAT_REQUEST_WAITING.getProperty()));
                LogManager.writeToLogFile(user, message);

                ChatMessage msg = new ChatMessage(ChatMessageRole.USER.value(), message);
                msgList.add(msg);

                while (model.equals(GptModels.GPT3) ? TikTokensUtil.tokens(TikTokensUtil.ModelEnum.GPT_3_5_TURBO.getName(), msgList) > GPT3_MAX_TOKENS - 700
                        : TikTokensUtil.tokens(TikTokensUtil.ModelEnum.GPT_4.getName(), msgList) > GPT4_MAX_TOKENS - 700)
                {
                    msgList.remove(0);
                    msgList.remove(0);
                }

                OpenAiService service = new OpenAiService(model.equals(GptModels.GPT3) ? GPT_3_TOKEN : GPT_4_TOKEN, Duration.ofSeconds(100));
                ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                        .model(model.equals(GptModels.GPT3) ? GPT_3_VERSION : GPT_4_VERSION)
                        .messages(msgList)
                        .n(1)
                        .maxTokens(model.equals(GptModels.GPT4) ? (user.getTokensBalance() < GPT4_MAX_TOKENS ? user.getTokensBalance() : GPT4_MAX_TOKENS) : GPT3_MAX_TOKENS)
                        .logitBias(new HashMap<>()).build();

                List<ChatCompletionChoice> choices = service.createChatCompletion(chatCompletionRequest).getChoices();
                instance.deleteMessage(user.getChatId(), waitMsgId);
                instance.sendMessage(chatId, ReplyMarkups.getReplyChatMenu(user.getLanguage()), choices.get(0).getMessage().getContent());
                msgList.add(choices.get(0).getMessage());

                if (model.equals(GptModels.GPT4))
                {
                    user.setTokensBalance(user.getTokensBalance() - (TikTokensUtil.tokens(TikTokensUtil.ModelEnum.GPT_4.getName(), msgList)));
                    DataSerializer.serializeUsersList();
                }
            }
        }
        catch (RuntimeException e)
        {
            instance.sendMessage(chatId, ReplyMarkups.getEmpty(), user.getMsgProperties().getProperty(PropertiesKeys.ERROR_REQUEST_ERROR.getProperty()));
            user.setCurrentStatus(UserStatus.USER_MAIN_MENU);
        }
    }

    public static void sendNewChatRequest(TelegramBot instance, TelegramBotUser user, String message, GptModels model)
    {
        Thread thread = new Thread(() -> createChat(instance, user, message, model));
        thread.start();
    }
}
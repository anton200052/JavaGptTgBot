package me.vasylkov.bot;

import me.vasylkov.OpenAI.ChatRequest;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TelegramBot extends TelegramLongPollingBot
{
    private static List<TelegramBotUser> userList = new ArrayList<>();

    public static List<TelegramBotUser> getUserList()
    {
        return userList;
    }

    private static final Properties configProperties = PropertiesManager.getConfigProperties();

    @Override
    public String getBotUsername()
    {
        return configProperties.getProperty("tgBot.username");
    }

    @Override
    public String getBotToken()
    {
        return configProperties.getProperty("tgBot.token");
    }
    @Override
    public void onUpdateReceived(Update update)
    {
        if (update.hasMessage())
        {
            String userName = update.getMessage().getFrom().getUserName();
            Long chatId = update.getMessage().getChatId();
            String msg = "";
            TelegramBotUser user = null;

            if ((user = getUserFromListByChatId(chatId)) == null)
            {
                user = new TelegramBotUser(userName, chatId);
                userList.add(user);
                //LogFiles.writeUsersToFile();
            }


            if (user.getUserName() == null || user.getUserName().equals("null") || !user.getUserName().equals(userName))
            {
                if (userName != null)
                {
                    user.setUserName(userName);
                    //LogFiles.writeUsersToFile();
                }
                else
                {
                    sendMessage(chatId, ReplyMarkups.NULL, Messages.USERNAME_NOT_AVALIABLE);
                    return;
                }
            }

            if (update.getMessage().hasText() || update.getMessage().hasDocument())
            {
                try
                {
                    msg = getMsg(update);
                    if (msg == null)
                    {
                        sendMessage(chatId, ReplyMarkups.PREVIOUS, Messages.INCORRECT_INPUT);
                        return;
                    }
                }
                catch (NotTxtFormatException e)
                {
                    sendMessage(chatId, ReplyMarkups.NULL, Messages.NOT_A_TXT_FORMAT);
                    return;
                }

                if (msg.charAt(0) == '/')
                {
                    handleCommands(user, msg);
                }
                else
                {
                    handleMessages(user, msg);
                }
            }

            else if (update.getMessage().hasPhoto())
            {
                Message message = update.getMessage();
                Long adminChatId = getUserFromListByUserName("lavviku").getChatId();
                sendMessage(adminChatId, ReplyMarkups.NULL, String.format(Messages.ADMINMODE_USER_SEND_PHOTO, userName));
                sendPhoto(adminChatId, new InputFile(message.getPhoto().get(0).getFileId()));
            }
        }

        else if (update.hasCallbackQuery())
        {
            String callbackData = update.getCallbackQuery().getData();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();

            handleCallbackData(callbackData, chatId, messageId);
        }
    }

    public int sendMessage(Long chatId, ReplyKeyboard markup, String textToSend)
    {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textToSend);
        sendMessage.enableMarkdown(true);
        try
        {
            if (!markup.equals(ReplyMarkups.PREVIOUS))
            {
                if (!markup.equals(ReplyMarkups.NULL))
                {
                    sendMessage.setReplyMarkup(markup);
                }
                else
                {
                    sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
                }
            }
            Message message = execute(sendMessage);
            return message.getMessageId();
        }
        catch (TelegramApiException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void sendPhoto(Long chatId, InputFile photo)
    {
        try
        {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            sendPhoto.setPhoto(photo);
            execute(sendPhoto);
        }
        catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }

    public void deleteMessage(Long chatId, Integer messageId)
    {
        try
        {
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setMessageId(messageId);
            deleteMessage.setChatId(chatId);
            execute(deleteMessage);
        }
        catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }

    public void editMessageText(Long chatId, Integer messageId, String newText)
    {
        try
        {
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setMessageId(messageId);
            editMessageText.setChatId(chatId);
            editMessageText.setText(newText);
            execute(editMessageText);
        }
        catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }

    public void editMessageInlineKeyboard(Long chatId, Integer messageId, InlineKeyboardMarkup newKeyboard)
    {
        try
        {
            EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
            editMessageReplyMarkup.setChatId(chatId);
            editMessageReplyMarkup.setMessageId(messageId);
            editMessageReplyMarkup.setReplyMarkup(newKeyboard);
            execute(editMessageReplyMarkup);
        }
        catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }

    private void handleCommands(TelegramBotUser user, String cmd)
    {
        Long chatId = user.getChatId();
        if (user.getCurrentStatus().equals(UserStatus.MAIN_MENU))
        {
            if (cmd.equals("/start"))
            {
                sendMessage(chatId, ReplyMarkups.NULL, Messages.START_1);
                sendMessage(chatId, ReplyMarkups.NULL, Messages.START_2);
            }
            else if (cmd.equals("/help"))
            {
                sendMessage(chatId, ReplyMarkups.NULL, Messages.HELP_1);
                sendMessage(chatId, ReplyMarkups.NULL, Messages.HELP_2);
            }
            else if (cmd.equals("/startchat"))
            {
                sendMessage(chatId, ReplyMarkups.MODEL_CHOSE, Messages.MODEL_CHOOSE);
                user.setCurrentStatus(UserStatus.MODEL_CHOOSE);
            }
            else if (cmd.equals("/balance"))
            {
                sendMessage(chatId, InlineMarkups.BUY_TOKENS, String.format(Messages.BALANCE, user.getTokensBalance()));
            }
            if (user.getUserName().equals("lavviku"))
            {
                if (cmd.equals("/addtokens"))
                {
                    sendMessage(chatId, ReplyMarkups.NULL, Messages.ADMINMODE_PRINT_DATA);
                    user.setCurrentStatus(UserStatus.ADMINMODE_ADD_TOKENS);
                }
                else if (cmd.equals("/sendmessage"))
                {
                    sendMessage(chatId, ReplyMarkups.NULL, Messages.ADMINMODE_SEND_MESSAGE);
                    user.setCurrentStatus(UserStatus.ADMINMODE_SEND_MESSAGE);
                }
            }
        }
        else
        {
            sendMessage(chatId, ReplyMarkups.PREVIOUS, Messages.NOT_IN_MAIN_MENU);
        }
    }

    private void handleMessages(TelegramBotUser user, String msg)
    {
        Long chatId = user.getChatId();
        if (user.getCurrentStatus().equals(UserStatus.MODEL_CHOOSE))
        {
            if (msg.equals("🌟GPT-3.5🌟"))
            {
                user.setCurrentStatus(UserStatus.GPT_3_CHAT);
                sendMessage(chatId, ReplyMarkups.CHAT_MENU, Messages.START_GPT3);
            }
            else if (msg.equals("⚡GPT-4⚡"))
            {
                if (user.getTokensBalance() < 300)
                {
                    sendMessage(chatId, ReplyMarkups.NULL, Messages.NOT_ENOUGH_TOKENS);
                    user.setCurrentStatus(UserStatus.MAIN_MENU);
                    return;
                }
                user.setCurrentStatus(UserStatus.GPT_4_CHAT);
                sendMessage(chatId, ReplyMarkups.CHAT_MENU, Messages.START_GPT4);
            }
            else
            {
                sendMessage(chatId, ReplyMarkups.PREVIOUS, Messages.INCORRECT_MODEL);
            }
        }

        else if (user.getCurrentStatus().equals(UserStatus.ADMINMODE_ADD_TOKENS))
        {
            try
            {
                String[] parsedStr = msg.split(" ");
                if (parsedStr.length > 2)
                {
                    throw new NumberFormatException();
                }

                String userName = parsedStr[0];
                int tokensToAdd = Integer.parseInt(parsedStr[1]);

                if (!isUserInListByUserName(userName))
                {
                    sendMessage(chatId, ReplyMarkups.NULL, Messages.ADMINMODE_USER_NOT_FOUND);
                    return;
                }

                TelegramBotUser u = getUserFromListByUserName(userName);
                u.setTokensBalance(u.getTokensBalance() + tokensToAdd);

                //LogFiles.writeUsersToFile();
                sendMessage(chatId, ReplyMarkups.NULL, Messages.ADMINMODE_TOKENS_ADDED);
                user.setCurrentStatus(UserStatus.MAIN_MENU);
            }
            catch (NumberFormatException e)
            {
                sendMessage(chatId, ReplyMarkups.NULL, Messages.ADMINMODE_PARSE_ERROR);
                user.setCurrentStatus(UserStatus.MAIN_MENU);
            }
        }

        else if (user.getCurrentStatus().equals(UserStatus.ADMINMODE_SEND_MESSAGE))
        {
            sendMessageToAllUsers(msg);
        }

        else if (user.getCurrentStatus().equals(UserStatus.GPT_3_CHAT) || user.getCurrentStatus().equals(UserStatus.GPT_4_CHAT))
        {
            if (msg.equals("Завершить чат \uD83D\uDCA7"))
            {
                user.getMessageList().clear();
                user.setCurrentStatus(UserStatus.MAIN_MENU);
                sendMessage(chatId, ReplyMarkups.NULL, Messages.RETURNED_TO_MAIN_MENU);
                return;
            }

            else if (msg.equals("Начать новый чат \uD83D\uDD25"))
            {
                user.getMessageList().clear();
                user.setCurrentStatus(UserStatus.MODEL_CHOOSE);
                sendMessage(chatId, ReplyMarkups.MODEL_CHOSE, Messages.MODEL_CHOOSE);
                return;
            }

            ChatRequest chatRequest;
            if (user.getCurrentStatus().equals(UserStatus.GPT_3_CHAT))
            {
                chatRequest = new ChatRequest(user, msg, GptModels.GPT3);
                chatRequest.sendNewChatRequest(this, ChatRequest.GPT3_MAX_TOKENS);
            }
            else if (user.getCurrentStatus().equals(UserStatus.GPT_4_CHAT))
            {
                chatRequest = new ChatRequest(user, msg, GptModels.GPT4);
                chatRequest.sendNewChatRequest(this, ChatRequest.GPT4_MAX_TOKENS);
            }
        }

        else
        {
            sendMessage(chatId, ReplyMarkups.NULL, Messages.NOT_IN_CHAT);
        }
    }

    private void handleCallbackData(String data, Long chatId, Integer messageId)
    {
        if (data.equals(CallbackDatas.PRESSED_BUY_BUTTON))
        {
            editMessageText(chatId, messageId, Messages.PRODUCT_LIST);
            editMessageInlineKeyboard(chatId, messageId, InlineMarkups.PRODUCT_LIST);
        }

        else if (data.equals(CallbackDatas.PRESSED_MINIMAL_PURCHASE_BUTTON))
        {
            sendMessage(chatId, ReplyMarkups.NULL, String.format(Messages.INVOICE, 119));
        }

        else if (data.equals(CallbackDatas.PRESSED_MEDIUM_PURCHASE_BUTTON))
        {
            sendMessage(chatId, ReplyMarkups.NULL, String.format(Messages.INVOICE, 199));
        }

        else if (data.equals(CallbackDatas.PRESSED_MAXIMUM_PURCHASE_BUTTON))
        {
            sendMessage(chatId, ReplyMarkups.NULL, String.format(Messages.INVOICE, 319));
        }
    }

    private void sendMessageToAllUsers(String msg)
    {
        for (TelegramBotUser user : userList)
        {
            sendMessage(user.getChatId(), ReplyMarkups.NULL, msg);
        }
    }

    private String getMsgFromChat(Update update)
    {
        return update.getMessage().getText();
    }

    private String getMsgFromDocument(Update update) throws NotTxtFormatException
    {
        Document document = update.getMessage().getDocument();
        GetFile getFile = new GetFile();
        getFile.setFileId(document.getFileId());
        String msg = "";

        try
        {
            File file = execute(getFile);
            if (!file.getFilePath().endsWith(".txt"))
            {
                throw new NotTxtFormatException();
            }

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL("https://api.telegram.org/file/bot" + getBotToken() + "/" + file.getFilePath()).openStream())))
            {
                while (bufferedReader.ready())
                {
                    msg += bufferedReader.readLine() + " ";
                }
            }
        }
        catch (TelegramApiException | IOException e)
        {
            msg = null;
            System.out.println("[Error!] Неизвестная ошибка считывания файла.");
        }

        return msg;
    }

    private boolean isUserInListByUserName(String userName)
    {
        for (TelegramBotUser u : userList)
        {
            if (u.getUserName().equals(userName))
            {
                return true;
            }
        }
        return false;
    }

    public TelegramBotUser getUserFromListByChatId(Long chatId)
    {
        for (TelegramBotUser u : userList)
        {
            if (u.getChatId().equals(chatId))
            {
                return u;
            }
        }
        return null;
    }

    public TelegramBotUser getUserFromListByUserName(String userName)
    {
        for (TelegramBotUser u : userList)
        {
            if (u.getUserName().equals(userName))
            {
                return u;
            }
        }
        return null;
    }


    public String getMsg(Update update) throws NotTxtFormatException
    {
        String msg = null;
        if (update.getMessage().hasText())
        {
            msg = getMsgFromChat(update);
        }

        if (update.getMessage().hasDocument())
        {
            msg = getMsgFromDocument(update);
        }
        return msg;
    }
}
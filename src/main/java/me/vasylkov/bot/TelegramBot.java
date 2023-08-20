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
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class TelegramBot extends TelegramLongPollingBot
{
    private static List<TelegramBotUser> usersList = new ArrayList<>();
    private static final Properties configProperties = PropertiesManager.getConfigProperties();
    private static final List<Long> adminsId = Arrays.stream(configProperties.getProperty(PropertiesKeys.CONFIG_ADMINS_ID.getProperty()).split(","))
            .map(str -> Long.parseLong(str.trim()))
            .toList();

    public static List<TelegramBotUser> getUsersList()
    {
        return usersList;
    }

    @Override
    public String getBotUsername()
    {
        return configProperties.getProperty(PropertiesKeys.CONFIG_BOT_USERNAME.getProperty());
    }

    @Override
    public String getBotToken()
    {
        return configProperties.getProperty(PropertiesKeys.CONFIG_BOT_TOKEN.getProperty());
    }

    @Override
    public void onUpdateReceived(Update update)
    {
        if (update.hasMessage())
        {
            Properties temp = PropertiesManager.getEnMsgProperties();
            LanguageCodes tempCode = LanguageCodes.EN;
            Message message = update.getMessage();
            User tempUser = message.getFrom();
            Long chatId = message.getChatId();
            String msg = "";
            TelegramBotUser user = null;

            if ((user = getUserFromList(tempUser.getId())) == null)
            {
                user = new TelegramBotUser(tempCode, temp, chatId, tempUser.getId(), tempUser.getFirstName(), tempUser.getIsBot(), tempUser.getLastName(), tempUser.getUserName(), tempUser.getLanguageCode(), tempUser.getCanJoinGroups(), tempUser.getCanReadAllGroupMessages(), tempUser.getSupportInlineQueries(), tempUser.getIsPremium(), tempUser.getAddedToAttachmentMenu());
                usersList.add(user);
                //LogFiles.writeUsersToFile();
            }


            if (update.getMessage().hasText() || update.getMessage().hasDocument())
            {
                try
                {
                    msg = getMsg(user, update);
                    if (msg == null)
                    {
                        sendMessage(chatId, ReplyMarkups.getPREVIOUS(), user.getMsgProperties().getProperty(PropertiesKeys.ERROR_INCORRECT_INPUT.getProperty()));
                        return;
                    }
                }
                catch (NotTxtFormatException e)
                {
                    sendMessage(chatId, ReplyMarkups.getPREVIOUS(), e.getMessage());
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
                handlePhotos(user, update.getMessage());
            }
        }

        else if (update.hasCallbackQuery())
        {
            String callbackData = update.getCallbackQuery().getData();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            TelegramBotUser user = getUserFromList(update.getCallbackQuery().getFrom().getId());

            handleCallbackData(user, callbackData, chatId, messageId);
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
            if (!markup.equals(ReplyMarkups.getPREVIOUS()))
            {
                if (!markup.equals(ReplyMarkups.getEMPTY()))
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
        LanguageCodes language = user.getLanguage();
        Long chatId = user.getChatId();
        if (user.getCurrentStatus().equals(UserStatus.MAIN_MENU))
        {
            if (cmd.equals("/start"))
            {
                sendMessage(chatId, ReplyMarkups.getEMPTY(), user.getMsgProperties().getProperty(PropertiesKeys.MENU_START_1.getProperty()));
                sendMessage(chatId, ReplyMarkups.getEMPTY(), user.getMsgProperties().getProperty(PropertiesKeys.MENU_START_2.getProperty()));
            }
            else if (cmd.equals("/help"))
            {
                sendMessage(chatId, ReplyMarkups.getEMPTY(), user.getMsgProperties().getProperty(PropertiesKeys.MENU_HELP_1.getProperty()));
                sendMessage(chatId, ReplyMarkups.getEMPTY(), user.getMsgProperties().getProperty(PropertiesKeys.MENU_HELP_2.getProperty()));
            }
            else if (cmd.equals("/startchat"))
            {
                sendMessage(chatId, ReplyMarkups.getReplyModelChoose(language), user.getMsgProperties().getProperty(PropertiesKeys.MENU_MODEL_CHOOSE.getProperty()));
                user.setCurrentStatus(UserStatus.MODEL_CHOOSE);
            }
            else if (cmd.equals("/balance"))
            {
                sendMessage(chatId, ReplyMarkups.getInlineBuyTokens(language), String.format(user.getMsgProperties().getProperty(PropertiesKeys.MENU_BALANCE.getProperty()), user.getTokensBalance()));
            }

            if (adminsId.contains(user.getId()))
            {
                if (cmd.equals("/addtokens"))
                {
                    sendMessage(chatId, ReplyMarkups.getEMPTY(), user.getMsgProperties().getProperty(PropertiesKeys.ADMIN_PRINT_USER_DATA.getProperty()));
                    user.setCurrentStatus(UserStatus.ADMIN_ADD_TOKENS);
                }
                else if (cmd.equals("/sendmessage"))
                {
                    sendMessage(chatId, ReplyMarkups.getEMPTY(), user.getMsgProperties().getProperty(PropertiesKeys.ADMIN_SEND_MESSAGE_TO_ALL.getProperty()));
                    user.setCurrentStatus(UserStatus.ADMIN_SEND_MESSAGE);
                }
            }
        }
        else
        {
            sendMessage(chatId, ReplyMarkups.getPREVIOUS(), user.getMsgProperties().getProperty(PropertiesKeys.ERROR_NOT_IN_MAIN_MENU.getProperty()));
        }
    }

    private void handleMessages(TelegramBotUser user, String msg)
    {
        LanguageCodes language = user.getLanguage();
        Long chatId = user.getChatId();
        if (user.getCurrentStatus().equals(UserStatus.MODEL_CHOOSE))
        {
            if (msg.equals(user.getMsgProperties().getProperty(PropertiesKeys.CHAT_GPT3_TITLE.getProperty())))
            {
                user.setCurrentStatus(UserStatus.GPT_3_CHAT);
                sendMessage(chatId, ReplyMarkups.getReplyChatMenu(language), user.getMsgProperties().getProperty(PropertiesKeys.MENU_STARTED_GPT3_CHAT.getProperty()));
            }
            else if (msg.equals(user.getMsgProperties().getProperty(PropertiesKeys.CHAT_GPT4_TITLE.getProperty())))
            {
                if (user.getTokensBalance() < 300)
                {
                    sendMessage(chatId, ReplyMarkups.getEMPTY(), user.getMsgProperties().getProperty(PropertiesKeys.ERROR_NOT_ENOUGH_TOKENS.getProperty()));
                    user.setCurrentStatus(UserStatus.MAIN_MENU);
                    return;
                }
                user.setCurrentStatus(UserStatus.GPT_4_CHAT);
                sendMessage(chatId, ReplyMarkups.getReplyChatMenu(language), user.getMsgProperties().getProperty(PropertiesKeys.MENU_STARTED_GPT4_CHAT.getProperty()));
            }
            else
            {
                sendMessage(chatId, ReplyMarkups.getPREVIOUS(), user.getMsgProperties().getProperty(PropertiesKeys.ERROR_INCORRECT_MODEL.getProperty()));
            }
        }

        else if (user.getCurrentStatus().equals(UserStatus.ADMIN_ADD_TOKENS))
        {
            try
            {
                String[] parsedStr = msg.split(" ");
                if (parsedStr.length > 2)
                {
                    throw new NumberFormatException();
                }

                Long id = Long.parseLong(parsedStr[0]);
                int tokensToAdd = Integer.parseInt(parsedStr[1]);
                TelegramBotUser u = null;

                if ((u = getUserFromList(id)) == null)
                {
                    sendMessage(chatId, ReplyMarkups.getEMPTY(), user.getMsgProperties().getProperty(PropertiesKeys.ADMIN_USER_NOT_FOUND.getProperty()));
                    return;
                }

                u.setTokensBalance(u.getTokensBalance() + tokensToAdd);

                //LogFiles.writeUsersToFile();
                sendMessage(chatId, ReplyMarkups.getEMPTY(), user.getMsgProperties().getProperty(PropertiesKeys.ADMIN_TOKENS_ADDED.getProperty()));
                user.setCurrentStatus(UserStatus.MAIN_MENU);
            }
            catch (NumberFormatException e)
            {
                sendMessage(chatId, ReplyMarkups.getEMPTY(), user.getMsgProperties().getProperty(PropertiesKeys.ERROR_ADMIN_MODE_PARSE.getProperty()));
                user.setCurrentStatus(UserStatus.MAIN_MENU);
            }
        }

        else if (user.getCurrentStatus().equals(UserStatus.ADMIN_SEND_MESSAGE))
        {
            sendMessageToAllUsers(msg);
        }

        else if (user.getCurrentStatus().equals(UserStatus.GPT_3_CHAT) || user.getCurrentStatus().equals(UserStatus.GPT_4_CHAT))
        {
            if (msg.equals(user.getMsgProperties().getProperty(PropertiesKeys.CHAT_END_CHAT.getProperty())))
            {
                user.getMessageList().clear();
                user.setCurrentStatus(UserStatus.MAIN_MENU);
                sendMessage(chatId, ReplyMarkups.getEMPTY(), user.getMsgProperties().getProperty(PropertiesKeys.MENU_RETURNED_TO_MAIN_MENU.getProperty()));
                return;
            }

            else if (msg.equals(user.getMsgProperties().getProperty(PropertiesKeys.CHAT_START_NEW_CHAT.getProperty())))
            {
                user.getMessageList().clear();
                user.setCurrentStatus(UserStatus.MODEL_CHOOSE);
                sendMessage(chatId, ReplyMarkups.getReplyModelChoose(language), user.getMsgProperties().getProperty(PropertiesKeys.MENU_MODEL_CHOOSE.getProperty()));
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
            sendMessage(chatId, ReplyMarkups.getEMPTY(), user.getMsgProperties().getProperty(PropertiesKeys.ERROR_NOT_IN_CHAT.getProperty()));
        }
    }

    private void handlePhotos(TelegramBotUser user, Message message)
    {
        for (Long id : adminsId)
        {
            sendMessage(id, ReplyMarkups.getPREVIOUS(), String.format(user.getMsgProperties().getProperty(PropertiesKeys.ADMIN_USER_SEND_PHOTO.getProperty()), user.getUserName()));
            sendPhoto(id, new InputFile(message.getPhoto().get(0).getFileId()));
        }
    }

    private void handleCallbackData(TelegramBotUser user, String data, Long chatId, Integer messageId)
    {
        LanguageCodes language = user.getLanguage();
        if (data.equals(CallbackData.PRESSED_BUY_BUTTON.getData()))
        {
            editMessageText(chatId, messageId, user.getMsgProperties().getProperty(PropertiesKeys.MENU_PRODUCT_LIST.getProperty()));
            editMessageInlineKeyboard(chatId, messageId, ReplyMarkups.getInlineProductList(language));
        }

        else if (data.equals(CallbackData.PRESSED_MINIMAL_PURCHASE_BUTTON.getData()))
        {
            sendMessage(chatId, ReplyMarkups.getEMPTY(), String.format(user.getMsgProperties().getProperty(PropertiesKeys.PURCHASE_FINAL_MSG.getProperty()), 119));
        }

        else if (data.equals(CallbackData.PRESSED_MEDIUM_PURCHASE_BUTTON.getData()))
        {
            sendMessage(chatId, ReplyMarkups.getEMPTY(), String.format(user.getMsgProperties().getProperty(PropertiesKeys.PURCHASE_FINAL_MSG.getProperty()), 199));
        }

        else if (data.equals(CallbackData.PRESSED_MAXIMUM_PURCHASE_BUTTON.getData()))
        {
            sendMessage(chatId, ReplyMarkups.getEMPTY(), String.format(user.getMsgProperties().getProperty(PropertiesKeys.PURCHASE_FINAL_MSG.getProperty()), 319));
        }
    }

    private void sendMessageToAllUsers(String msg)
    {
        for (TelegramBotUser user : usersList)
        {
            sendMessage(user.getChatId(), ReplyMarkups.getPREVIOUS(), msg);
        }
    }

    private String getMsgFromChat(Message message)
    {
        return message.getText();
    }

    private String getMsgFromTxtFile(TelegramBotUser user, Document document) throws NotTxtFormatException
    {
        GetFile getFile = new GetFile();
        getFile.setFileId(document.getFileId());
        String msg = "";

        try
        {
            File file = execute(getFile);
            if (!file.getFilePath().endsWith(".txt"))
            {
                throw new NotTxtFormatException(user.getMsgProperties().getProperty(PropertiesKeys.ERROR_NOT_A_TXT_FILE.getProperty()));
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
        for (TelegramBotUser u : usersList)
        {
            if (u.getUserName().equals(userName))
            {
                return true;
            }
        }
        return false;
    }

    public TelegramBotUser getUserFromList(Long id)
    {
        for (TelegramBotUser u : usersList)
        {
            if (u.getId().equals(id))
            {
                return u;
            }
        }
        return null;
    }

    public String getMsg(TelegramBotUser user, Update update) throws NotTxtFormatException
    {
        String msg = null;
        if (update.getMessage().hasText())
        {
            msg = getMsgFromChat(update.getMessage());
        }

        if (update.getMessage().hasDocument())
        {
            msg = getMsgFromTxtFile(user, update.getMessage().getDocument());
        }
        return msg;
    }
}
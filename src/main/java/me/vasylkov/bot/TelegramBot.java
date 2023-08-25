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
    private static final List<Long> adminsId = Arrays.stream(configProperties.getProperty(PropertiesKeys.CONFIG_ADMINS_ID.getProperty()).split(",")).map(str -> Long.parseLong(str.trim())).toList();

    public static List<TelegramBotUser> getUsersList()
    {
        return usersList;
    }

    public static void setUsersList(List<TelegramBotUser> usersList)
    {
        TelegramBot.usersList = usersList;
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
            Message message = update.getMessage();
            User tempUser = message.getFrom();
            Long chatId = message.getChatId();
            String msg = "";
            TelegramBotUser user = null;

            if ((user = getUserFromList(tempUser.getId())) == null)
            {
                user = new TelegramBotUser(chatId, tempUser.getId(), tempUser.getFirstName(), tempUser.getIsBot(), tempUser.getLastName(), tempUser.getUserName(), tempUser.getLanguageCode(), tempUser.getCanJoinGroups(), tempUser.getCanReadAllGroupMessages(), tempUser.getSupportInlineQueries(), tempUser.getIsPremium(), tempUser.getAddedToAttachmentMenu());
                usersList.add(user);
                DataSerializer.serializeUsersList();
            }


            Properties msgProperties = user.getMsgProperties();
            if (update.getMessage().hasText() || update.getMessage().hasDocument())
            {
                try
                {
                    msg = getMsg(user, update);
                    if (msg == null)
                    {
                        sendMessage(chatId, ReplyMarkups.getPrevious(), msgProperties.getProperty(PropertiesKeys.ERROR_INCORRECT_INPUT.getProperty()));
                        return;
                    }
                }
                catch (NotTxtFormatException e)
                {
                    sendMessage(chatId, ReplyMarkups.getPrevious(), e.getMessage());
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
            if (!markup.equals(ReplyMarkups.getPrevious()))
            {
                if (!markup.equals(ReplyMarkups.getEmpty()))
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
        Properties msgProperties = user.getMsgProperties();
        Languages language = user.getLanguage();
        Long chatId = user.getChatId();
        if (!user.getCurrentStatus().equals(UserStatus.CHAT_WITH_GPT))
        {
            if (cmd.equals("/start"))
            {
                sendMessage(chatId, ReplyMarkups.getInlineChooseLanguage(language), PropertiesManager.getEnMsgProperties().getProperty(PropertiesKeys.SETTINGS_CHOOSE_LANGUAGE.getProperty()) + " / " + PropertiesManager.getRuMsgProperties().getProperty(PropertiesKeys.SETTINGS_CHOOSE_LANGUAGE.getProperty()));
            }
            else if (cmd.equals("/menu"))
            {
                sendMessage(chatId, ReplyMarkups.getInlineMainMenu(language), msgProperties.getProperty(PropertiesKeys.MENU_TITLE.getProperty()));
            }
            else if (cmd.equals("/help"))
            {
                sendMessage(chatId, ReplyMarkups.getEmpty(), msgProperties.getProperty(PropertiesKeys.MENU_HELP.getProperty()));
                sendMessage(chatId, ReplyMarkups.getEmpty(), msgProperties.getProperty(PropertiesKeys.MENU_HELP.getProperty()));
            }
            else if (cmd.equals("/startchat"))
            {
                sendMessage(chatId, ReplyMarkups.getReplyChatMenu(language), msgProperties.getProperty(PropertiesKeys.MENU_START_GPT_CHAT.getProperty()));
                user.setCurrentStatus(UserStatus.CHAT_WITH_GPT);
            }
            else if (cmd.equals("/balance"))
            {
               // sendMessage(chatId, ReplyMarkups.getInlineBuyTokens(language), String.format(msgProperties.getProperty(PropertiesKeys.MENU_BALANCE.getProperty()), user.getTokensBalance()));
            }

            if (adminsId.contains(user.getId()))
            {
                if (cmd.equals("/addtokens"))
                {
                    sendMessage(chatId, ReplyMarkups.getEmpty(), msgProperties.getProperty(PropertiesKeys.ADMIN_PRINT_USER_DATA.getProperty()));
                    user.setCurrentStatus(UserStatus.ADMIN_ADD_TOKENS);
                }
                else if (cmd.equals("/sendmessage"))
                {
                    sendMessage(chatId, ReplyMarkups.getEmpty(), msgProperties.getProperty(PropertiesKeys.ADMIN_SEND_MESSAGE_TO_ALL.getProperty()));
                    user.setCurrentStatus(UserStatus.ADMIN_SEND_MESSAGE);
                }
            }
        }
        else
        {
            sendMessage(chatId, ReplyMarkups.getPrevious(), msgProperties.getProperty(PropertiesKeys.ERROR_NOT_IN_MAIN_MENU.getProperty()));
        }
    }

    private void handleMessages(TelegramBotUser user, String msg)
    {
        Properties msgProperties = user.getMsgProperties();
        Languages language = user.getLanguage();
        Long chatId = user.getChatId();

        if (user.getCurrentStatus().equals(UserStatus.ADMIN_ADD_TOKENS))
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
                    sendMessage(chatId, ReplyMarkups.getEmpty(), msgProperties.getProperty(PropertiesKeys.ADMIN_USER_NOT_FOUND.getProperty()));
                    return;
                }

                u.setTokensBalance(u.getTokensBalance() + tokensToAdd);

                DataSerializer.serializeUsersList();
                sendMessage(chatId, ReplyMarkups.getEmpty(), msgProperties.getProperty(PropertiesKeys.ADMIN_TOKENS_ADDED.getProperty()));
                user.setCurrentStatus(UserStatus.MAIN_MENU);
            }
            catch (NumberFormatException e)
            {
                sendMessage(chatId, ReplyMarkups.getEmpty(), msgProperties.getProperty(PropertiesKeys.ERROR_ADMIN_MODE_PARSE.getProperty()));
                user.setCurrentStatus(UserStatus.MAIN_MENU);
            }
        }

        else if (user.getCurrentStatus().equals(UserStatus.ADMIN_SEND_MESSAGE))
        {
            sendMessageToAllUsers(msg);
        }

        else if (user.getCurrentStatus().equals(UserStatus.CHAT_WITH_GPT))
        {
            if (msg.equals(msgProperties.getProperty(PropertiesKeys.CHAT_END_CHAT_BUTTON_TITLE.getProperty())))
            {
                user.getMessageList().clear();
                user.setCurrentStatus(UserStatus.MAIN_MENU);
                sendMessage(chatId, ReplyMarkups.getEmpty(), msgProperties.getProperty(PropertiesKeys.MENU_RETURNED_TO_MAIN_MENU.getProperty()));
                return;
            }

            else if (msg.equals(msgProperties.getProperty(PropertiesKeys.CHAT_START_NEW_BUTTON_TITLE.getProperty())))
            {
                user.getMessageList().clear();
                sendMessage(chatId, ReplyMarkups.getPrevious(), msgProperties.getProperty(PropertiesKeys.MENU_START_GPT_CHAT.getProperty()));
                return;
            }

            ChatRequest chatRequest = new ChatRequest(user, msg, user.getGptModel().equals(GptModels.GPT3) ? GptModels.GPT3 : GptModels.GPT4);
            chatRequest.sendNewChatRequest(this, user.getGptModel().equals(GptModels.GPT3) ? ChatRequest.GPT3_MAX_TOKENS : ChatRequest.GPT4_MAX_TOKENS);
        }

        else
        {
            sendMessage(chatId, ReplyMarkups.getEmpty(), msgProperties.getProperty(PropertiesKeys.ERROR_NOT_IN_CHAT.getProperty()));
        }
    }

    private void handlePhotos(TelegramBotUser user, Message message)
    {
        Properties msgProperties = user.getMsgProperties();
        for (Long id : adminsId)
        {
            sendMessage(id, ReplyMarkups.getPrevious(), String.format(msgProperties.getProperty(PropertiesKeys.ADMIN_USER_SEND_PHOTO.getProperty()), user.getUserName()));
            sendPhoto(id, new InputFile(message.getPhoto().get(0).getFileId()));
        }
    }

    private void handleCallbackData(TelegramBotUser user, String data, Long chatId, Integer messageId)
    {
        Languages language = user.getLanguage();
        Properties msgProperties = user.getMsgProperties();

        if (!user.getCurrentStatus().equals(UserStatus.CHAT_WITH_GPT))
        {
            if (data.equals(CallbackData.PRESSED_MENU_BALANCE_BUTTON.getData()))
            {
                editMessageText(chatId, messageId, String.format(msgProperties.getProperty(user.isPremium() ? PropertiesKeys.MENU_PREMIUM_BALANCE.getProperty() : PropertiesKeys.MENU_DEFAULT_BALANCE.getProperty()), user.getTokensBalance()));
                editMessageInlineKeyboard(chatId, messageId, ReplyMarkups.getInlineBuyTokens(language));
            }

            else if (data.equals(CallbackData.PRESSED_MENU_SETTINGS_BUTTON.getData()))
            {
                editMessageText(chatId, messageId, msgProperties.getProperty(PropertiesKeys.SETTINGS_TITLE.getProperty()));
                editMessageInlineKeyboard(chatId, messageId, ReplyMarkups.getInlineSettings(language));
            }

            else if (data.equals(CallbackData.PRESSED_SETTINGS_AI_MODEL_BUTTON.getData()))
            {
                editMessageText(chatId, messageId, msgProperties.getProperty(PropertiesKeys.SETTINGS_AI_MODELS.getProperty()));
                editMessageInlineKeyboard(chatId, messageId, ReplyMarkups.getInlineAIModels(language));
            }

            else if (data.equals(CallbackData.PRESSED_SETTINGS_LANGUAGE_BUTTON.getData()))
            {
                editMessageText(chatId, messageId, msgProperties.getProperty(PropertiesKeys.SETTINGS_CHOOSE_LANGUAGE.getProperty()));
                editMessageInlineKeyboard(chatId, messageId, ReplyMarkups.getInlineChooseLanguage(language));
            }

            else if (data.equals(CallbackData.PRESSED_SETTINGS_GPT3_BUTTON.getData()))
            {
                user.setGptModel(GptModels.GPT3);
                editMessageText(chatId, messageId, msgProperties.getProperty(PropertiesKeys.SETTINGS_TITLE.getProperty()));
                editMessageInlineKeyboard(chatId, messageId, ReplyMarkups.getInlineSettings(language));
                sendMessage(chatId, ReplyMarkups.getEmpty(), String.format(msgProperties.getProperty(PropertiesKeys.SETTINGS_MODEL_CHANGED.getProperty()), msgProperties.getProperty(PropertiesKeys.SETTINGS_GPT3_BUTTON_TITLE.getProperty())));
            }

            else if (data.equals(CallbackData.PRESSED_SETTINGS_GPT4_BUTTON.getData()))
            {
                if (user.isPremium())
                {
                    user.setGptModel(GptModels.GPT4);
                    editMessageText(chatId, messageId, msgProperties.getProperty(PropertiesKeys.SETTINGS_TITLE.getProperty()));
                    editMessageInlineKeyboard(chatId, messageId, ReplyMarkups.getInlineSettings(language));
                    sendMessage(chatId, ReplyMarkups.getEmpty(), String.format(msgProperties.getProperty(PropertiesKeys.SETTINGS_MODEL_CHANGED.getProperty()), msgProperties.getProperty(PropertiesKeys.SETTINGS_GPT4_BUTTON_TITLE.getProperty())));
                }
                else
                {
                    sendMessage(chatId, ReplyMarkups.getEmpty(), msgProperties.getProperty(PropertiesKeys.ERROR_NOT_PREMIUM_ACC.getProperty()));
                }
            }

            else if (data.equals(CallbackData.PRESSED_MENU_HELP_BUTTON.getData()))
            {
                editMessageText(chatId, messageId, msgProperties.getProperty(PropertiesKeys.MENU_HELP.getProperty()));
                editMessageInlineKeyboard(chatId, messageId, ReplyMarkups.getInlineHelp(language));
            }

            else if (data.equals(CallbackData.PRESSED_MENU_BUY_TOKENS_BUTTON.getData()))
            {
                editMessageText(chatId, messageId, msgProperties.getProperty(PropertiesKeys.MENU_PRODUCT_LIST.getProperty()));
                editMessageInlineKeyboard(chatId, messageId, ReplyMarkups.getInlineProductList(language));
            }

            else if (data.equals(CallbackData.PRESSED_PURCHASE_MINIMAL_BUTTON.getData()))
            {
                sendMessage(chatId, ReplyMarkups.getEmpty(), String.format(msgProperties.getProperty(PropertiesKeys.PURCHASE_FINAL_MSG.getProperty()), 119));
            }

            else if (data.equals(CallbackData.PRESSED_PURCHASE_MEDIUM_BUTTON.getData()))
            {
                sendMessage(chatId, ReplyMarkups.getEmpty(), String.format(msgProperties.getProperty(PropertiesKeys.PURCHASE_FINAL_MSG.getProperty()), 199));
            }

            else if (data.equals(CallbackData.PRESSED_PURCHASE_MAXIMUM_BUTTON.getData()))
            {
                sendMessage(chatId, ReplyMarkups.getEmpty(), String.format(msgProperties.getProperty(PropertiesKeys.PURCHASE_FINAL_MSG.getProperty()), 319));
            }

            else if (data.equals(CallbackData.PRESSED_SETTINGS_RU_LANGUAGE_BUTTON.getData()))
            {
                user.setLanguage(language = Languages.RU);
                user.setMsgProperties(msgProperties = PropertiesManager.getRuMsgProperties());
                editMessageText(chatId, messageId, msgProperties.getProperty(PropertiesKeys.SETTINGS_TITLE.getProperty()));
                editMessageInlineKeyboard(chatId, messageId, ReplyMarkups.getInlineSettings(language));
                sendMessage(chatId, ReplyMarkups.getPrevious(), String.format(msgProperties.getProperty(PropertiesKeys.SETTINGS_LANGUAGE_CHANGED.getProperty()), PropertiesManager.getRuMsgProperties().getProperty(PropertiesKeys.SETTINGS_LANGUAGE_TITLE.getProperty())));
            }

            else if (data.equals(CallbackData.PRESSED_SETTINGS_EN_LANGUAGE_BUTTON.getData()))
            {
                user.setLanguage(language = Languages.EN);
                user.setMsgProperties(msgProperties = PropertiesManager.getEnMsgProperties());
                editMessageText(chatId, messageId, msgProperties.getProperty(PropertiesKeys.SETTINGS_TITLE.getProperty()));
                editMessageInlineKeyboard(chatId, messageId, ReplyMarkups.getInlineSettings(language));
                sendMessage(chatId, ReplyMarkups.getPrevious(), String.format(msgProperties.getProperty(PropertiesKeys.SETTINGS_LANGUAGE_CHANGED.getProperty()), PropertiesManager.getEnMsgProperties().getProperty(PropertiesKeys.SETTINGS_LANGUAGE_TITLE.getProperty())));
            }

            else if (data.equals(CallbackData.PRESSED_MENU_RETURN_BUTTON.getData()))
            {
                editMessageText(chatId, messageId, msgProperties.getProperty(PropertiesKeys.MENU_TITLE.getProperty()));
                editMessageInlineKeyboard(chatId, messageId, ReplyMarkups.getInlineMainMenu(language));
            }
        }
        else
        {
            editMessageText(chatId, messageId, msgProperties.getProperty(PropertiesKeys.ERROR_NOT_IN_MAIN_MENU.getProperty()));
            editMessageInlineKeyboard(chatId, messageId, null);
        }
    }

    private void sendMessageToAllUsers(String msg)
    {
        for (TelegramBotUser user : usersList)
        {
            sendMessage(user.getChatId(), ReplyMarkups.getPrevious(), msg);
        }
    }

    private String getMsgFromChat(Message message)
    {
        return message.getText();
    }

    private String getMsgFromTxtFile(TelegramBotUser user, Document document) throws NotTxtFormatException
    {
        Properties msgProperties = user.getMsgProperties();
        GetFile getFile = new GetFile();
        getFile.setFileId(document.getFileId());
        String msg = "";

        try
        {
            File file = execute(getFile);
            if (!file.getFilePath().endsWith(".txt"))
            {
                throw new NotTxtFormatException(msgProperties.getProperty(PropertiesKeys.ERROR_NOT_A_TXT_FILE.getProperty()));
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

    public static String getVersion()
    {
        return "";
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
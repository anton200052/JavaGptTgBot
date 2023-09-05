package me.vasylkov.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ReplyMarkups
{
    private static final Properties ruMsgProperties = PropertiesManager.getRuMsgProperties();
    private static final Properties enMsgProperties = PropertiesManager.getEnMsgProperties();


    private static InlineKeyboardMarkup createInlineMarkup(List<List<InlineKeyboardButton>> rows)
    {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    private static List<InlineKeyboardButton> createInlineRow(InlineKeyboardButton... buttons)
    {
        return new ArrayList<>(Arrays.asList(buttons));
    }

    private static InlineKeyboardButton createInlineButton(String text, String callbackData)
    {
        InlineKeyboardButton button = new InlineKeyboardButton(text);
        button.setCallbackData(callbackData);
        return button;
    }

    private static ReplyKeyboardMarkup createOneRowReplyMarkup(String... args)
    {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        for (String str : args)
        {
            row.add(str);
        }
        keyboardRowList.add(row);

        keyboardMarkup.setKeyboard(keyboardRowList);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public static ReplyKeyboardMarkup getEmpty()
    {
        return createOneRowReplyMarkup("empty");
    }

    public static ReplyKeyboardMarkup getPrevious()
    {
        return createOneRowReplyMarkup("previous");
    }

    private static InlineKeyboardButton getReturnMenuButton(Languages language)
    {
        String returnMenuTitle = language.equals(Languages.RU) ? ruMsgProperties.getProperty(PropertiesKeys.MENU_RETURN_TO_MENU_BUTTON_TITLE.getProperty()) : enMsgProperties.getProperty(PropertiesKeys.MENU_RETURN_TO_MENU_BUTTON_TITLE.getProperty());
        return createInlineButton(returnMenuTitle, CallbackData.PRESSED_MENU_RETURN_BUTTON.getData());
    }



    public static InlineKeyboardMarkup getInlineBuyTokens(Languages language)
    {
        String buyTokensTitle = language.equals(Languages.RU) ? ruMsgProperties.getProperty(PropertiesKeys.PURCHASE_BUY_TOKENS_BUTTON_TITLE.getProperty()) : enMsgProperties.getProperty(PropertiesKeys.PURCHASE_BUY_TOKENS_BUTTON_TITLE.getProperty());

        return createInlineMarkup(List.of(createInlineRow(createInlineButton(buyTokensTitle, CallbackData.PRESSED_MENU_BUY_TOKENS_BUTTON.getData())),
                createInlineRow(getReturnMenuButton(language))));
    }

    public static InlineKeyboardMarkup getInlineProductList(Languages language)
    {
        String minimalVal = language.equals(Languages.RU) ? ruMsgProperties.getProperty(PropertiesKeys.PURCHASE_MINIMAL_VALUE_BUTTON_TITLE.getProperty()) : enMsgProperties.getProperty(PropertiesKeys.PURCHASE_MINIMAL_VALUE_BUTTON_TITLE.getProperty());
        String mediumVal = language.equals(Languages.RU) ? ruMsgProperties.getProperty(PropertiesKeys.PURCHASE_MEDIUM_VALUE_BUTTON_TITLE.getProperty()) : enMsgProperties.getProperty(PropertiesKeys.PURCHASE_MEDIUM_VALUE_BUTTON_TITLE.getProperty());
        String maximumVal = language.equals(Languages.RU) ? ruMsgProperties.getProperty(PropertiesKeys.PURCHASE_MAXIMUM_VALUE_BUTTON_TITLE.getProperty()) : enMsgProperties.getProperty(PropertiesKeys.PURCHASE_MAXIMUM_VALUE_BUTTON_TITLE.getProperty());

        return createInlineMarkup(List.of(createInlineRow(createInlineButton(minimalVal, CallbackData.PRESSED_PURCHASE_MINIMAL_BUTTON.getData())),
                createInlineRow(createInlineButton(mediumVal, CallbackData.PRESSED_PURCHASE_MEDIUM_BUTTON.getData())),
                createInlineRow(createInlineButton(maximumVal, CallbackData.PRESSED_PURCHASE_MAXIMUM_BUTTON.getData())),
                createInlineRow(getReturnMenuButton(language))));
    }

    public static InlineKeyboardMarkup getInlineChooseLanguage(Languages language)
    {
        String ruLanguageTitle = ruMsgProperties.getProperty(PropertiesKeys.SETTINGS_LANGUAGE_TITLE.getProperty());
        String enLanguageTitle = enMsgProperties.getProperty(PropertiesKeys.SETTINGS_LANGUAGE_TITLE.getProperty());

        return createInlineMarkup(List.of(createInlineRow(createInlineButton(ruLanguageTitle, CallbackData.PRESSED_SETTINGS_RU_LANGUAGE_BUTTON.getData()),
                createInlineButton(enLanguageTitle, CallbackData.PRESSED_SETTINGS_EN_LANGUAGE_BUTTON.getData())),
                createInlineRow(getReturnMenuButton(language))));
    }


    public static InlineKeyboardMarkup getInlineMainMenu(Languages language)
    {
        String balanceTitle = language.equals(Languages.RU) ? ruMsgProperties.getProperty(PropertiesKeys.MENU_BALANCE_BUTTON_TITLE.getProperty()) : enMsgProperties.getProperty(PropertiesKeys.MENU_BALANCE_BUTTON_TITLE.getProperty());
        String settingsTitle = language.equals(Languages.RU) ? ruMsgProperties.getProperty(PropertiesKeys.MENU_SETTINGS_BUTTON_TITLE.getProperty()) : enMsgProperties.getProperty(PropertiesKeys.MENU_SETTINGS_BUTTON_TITLE.getProperty());
        String helpTitle = language.equals(Languages.RU) ? ruMsgProperties.getProperty(PropertiesKeys.MENU_HELP_BUTTON_TITLE.getProperty()) : enMsgProperties.getProperty(PropertiesKeys.MENU_HELP_BUTTON_TITLE.getProperty());

       return createInlineMarkup(List.of(createInlineRow(createInlineButton(balanceTitle, CallbackData.PRESSED_MENU_BALANCE_BUTTON.getData())),
               createInlineRow(createInlineButton(settingsTitle, CallbackData.PRESSED_MENU_SETTINGS_BUTTON.getData()),
                       createInlineButton(helpTitle, CallbackData.PRESSED_MENU_HELP_BUTTON.getData()))));
    }

    public static InlineKeyboardMarkup getInlineSettings(Languages language)
    {
        String languageTitle = language.equals(Languages.RU) ? ruMsgProperties.getProperty(PropertiesKeys.SETTINGS_LANGUAGE_BUTTON_TITLE.getProperty()) : enMsgProperties.getProperty(PropertiesKeys.SETTINGS_LANGUAGE_BUTTON_TITLE.getProperty());
        String aiModelTitle = language.equals(Languages.RU) ? ruMsgProperties.getProperty(PropertiesKeys.SETTINGS_AI_MODEL_BUTTON_TITLE.getProperty()) : enMsgProperties.getProperty(PropertiesKeys.SETTINGS_AI_MODEL_BUTTON_TITLE.getProperty());

        return createInlineMarkup(List.of(createInlineRow(createInlineButton(aiModelTitle, CallbackData.PRESSED_SETTINGS_AI_MODEL_BUTTON.getData())),
                createInlineRow(createInlineButton(languageTitle, CallbackData.PRESSED_SETTINGS_LANGUAGE_BUTTON.getData())),
                createInlineRow(getReturnMenuButton(language))));
    }

    public static InlineKeyboardMarkup getInlineAIModels(Languages language)
    {
        String gpt3Title = language.equals(Languages.RU) ? ruMsgProperties.getProperty(PropertiesKeys.SETTINGS_GPT3_BUTTON_TITLE.getProperty()) : enMsgProperties.getProperty(PropertiesKeys.SETTINGS_GPT3_BUTTON_TITLE.getProperty());
        String gpt4Title = language.equals(Languages.RU) ? ruMsgProperties.getProperty(PropertiesKeys.SETTINGS_GPT4_BUTTON_TITLE.getProperty()) : enMsgProperties.getProperty(PropertiesKeys.SETTINGS_GPT4_BUTTON_TITLE.getProperty());

        return createInlineMarkup(List.of(createInlineRow(createInlineButton(gpt3Title, CallbackData.PRESSED_SETTINGS_GPT3_BUTTON.getData()),
                createInlineButton(gpt4Title, CallbackData.PRESSED_SETTINGS_GPT4_BUTTON.getData())),
                createInlineRow(getReturnMenuButton(language))));
    }

    public static InlineKeyboardMarkup getInlineHelp(Languages language)
    {
        return createInlineMarkup(List.of(createInlineRow(getReturnMenuButton(language))));
    }

    public static InlineKeyboardMarkup getInlineCancelAction(Languages language)
    {
        String cancelButton = language.equals(Languages.RU) ? ruMsgProperties.getProperty(PropertiesKeys.APANEL_CANCEL_ACTION_BUTTON_TITLE.getProperty()) : enMsgProperties.getProperty(PropertiesKeys.APANEL_CANCEL_ACTION_BUTTON_TITLE.getProperty());

        return createInlineMarkup(List.of(createInlineRow(createInlineButton(cancelButton, CallbackData.PRESSED_APANEL_CANCEL_ACTION_BUTTON.getData()))));
    }

    public static InlineKeyboardMarkup getInlineApanel(Languages language)
    {
        String msgToAllButton = language.equals(Languages.RU) ? ruMsgProperties.getProperty(PropertiesKeys.APANEL_MSG_TO_ALL_BUTTON_TITLE.getProperty()) : enMsgProperties.getProperty(PropertiesKeys.APANEL_MSG_TO_ALL_BUTTON_TITLE.getProperty());
        String addTokensButton = language.equals(Languages.RU) ? ruMsgProperties.getProperty(PropertiesKeys.APANEL_ADD_TOKENS_BUTTON_TITLE.getProperty()) : enMsgProperties.getProperty(PropertiesKeys.APANEL_ADD_TOKENS_BUTTON_TITLE.getProperty());
        String userInfoButton = language.equals(Languages.RU) ? ruMsgProperties.getProperty(PropertiesKeys.APANEL_USER_INFO_BUTTON_TITLE.getProperty()) : enMsgProperties.getProperty(PropertiesKeys.APANEL_USER_INFO_BUTTON_TITLE.getProperty());

        return createInlineMarkup(List.of(createInlineRow(createInlineButton(msgToAllButton, CallbackData.PRESSED_APANEL_MSG_TO_ALL_BUTTON.getData())), createInlineRow(createInlineButton(addTokensButton, CallbackData.PRESSED_APANEL_ADD_TOKENS_BUTTON.getData()), createInlineButton(userInfoButton, CallbackData.PRESSED_APANEL_USER_INFO_BUTTON.getData()))));
    }

    public static ReplyKeyboardMarkup getReplyChatMenu(Languages language)
    {
        String endChat = language.equals(Languages.RU) ? ruMsgProperties.getProperty(PropertiesKeys.CHAT_END_CHAT_BUTTON_TITLE.getProperty()) : enMsgProperties.getProperty(PropertiesKeys.CHAT_END_CHAT_BUTTON_TITLE.getProperty());
        String startNewChat = language.equals(Languages.RU) ? ruMsgProperties.getProperty(PropertiesKeys.CHAT_START_NEW_BUTTON_TITLE.getProperty()) : enMsgProperties.getProperty(PropertiesKeys.CHAT_START_NEW_BUTTON_TITLE.getProperty());

        return createOneRowReplyMarkup(endChat, startNewChat);
    }

}

package me.vasylkov.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ReplyMarkups
{
    private static final Properties ruMsgProperties = PropertiesManager.getRuMsgProperties();
    private static final Properties enMsgProperties = PropertiesManager.getEnMsgProperties();

    public static final ReplyKeyboardMarkup PREVIOUS;
    public static final ReplyKeyboardMarkup EMPTY;

    public static final InlineKeyboardMarkup INLINE_BUY_TOKENS_RU;
    public static final InlineKeyboardMarkup INLINE_PRODUCT_LIST_RU;
    public static final InlineKeyboardMarkup INLINE_BUY_TOKENS_EN;
    public static final InlineKeyboardMarkup INLINE_PRODUCT_LIST_EN;


    public static final ReplyKeyboardMarkup REPLY_MODEL_CHOSE_RU;
    public static final ReplyKeyboardMarkup REPLY_CHAT_MENU_RU;
    public static final ReplyKeyboardMarkup REPLY_MODEL_CHOSE_EN;
    public static final ReplyKeyboardMarkup REPLY_CHAT_MENU_EN;


    static
    {
        PREVIOUS = createOneRowReplyMarkup("previous");
        EMPTY = createOneRowReplyMarkup("nullMarkup");

        INLINE_BUY_TOKENS_RU = createInlineMarkup(Arrays.asList(createInlineButton(ruMsgProperties.getProperty(PropertiesKeys.PURCHASE_BUY_TOKENS.getProperty()), CallbackData.PRESSED_BUY_BUTTON.getData())));
        INLINE_BUY_TOKENS_EN = createInlineMarkup(Arrays.asList(createInlineButton(enMsgProperties.getProperty(PropertiesKeys.PURCHASE_BUY_TOKENS.getProperty()), CallbackData.PRESSED_BUY_BUTTON.getData())));
        INLINE_PRODUCT_LIST_RU = createInlineMarkup(Arrays.asList(createInlineButton(ruMsgProperties.getProperty(PropertiesKeys.PURCHASE_MINIMAL_VALUE.getProperty()), CallbackData.PRESSED_MINIMAL_PURCHASE_BUTTON.getData())), Arrays.asList(createInlineButton(ruMsgProperties.getProperty(PropertiesKeys.PURCHASE_MEDIUM_VALUE.getProperty()), CallbackData.PRESSED_MEDIUM_PURCHASE_BUTTON.getData())), Arrays.asList(createInlineButton(ruMsgProperties.getProperty(PropertiesKeys.PURCHASE_MAXIMUM_VALUE.getProperty()), CallbackData.PRESSED_MAXIMUM_PURCHASE_BUTTON.getData())));
        INLINE_PRODUCT_LIST_EN = createInlineMarkup(Arrays.asList(createInlineButton(enMsgProperties.getProperty(PropertiesKeys.PURCHASE_MINIMAL_VALUE.getProperty()), CallbackData.PRESSED_MINIMAL_PURCHASE_BUTTON.getData())), Arrays.asList(createInlineButton(enMsgProperties.getProperty(PropertiesKeys.PURCHASE_MEDIUM_VALUE.getProperty()), CallbackData.PRESSED_MEDIUM_PURCHASE_BUTTON.getData())), Arrays.asList(createInlineButton(enMsgProperties.getProperty(PropertiesKeys.PURCHASE_MAXIMUM_VALUE.getProperty()), CallbackData.PRESSED_MAXIMUM_PURCHASE_BUTTON.getData())));

        REPLY_MODEL_CHOSE_RU = createOneRowReplyMarkup(ruMsgProperties.getProperty(PropertiesKeys.CHAT_GPT3_TITLE.getProperty()), ruMsgProperties.getProperty(PropertiesKeys.CHAT_GPT4_TITLE.getProperty()));
        REPLY_CHAT_MENU_RU = createOneRowReplyMarkup(ruMsgProperties.getProperty(PropertiesKeys.CHAT_END_CHAT.getProperty()), ruMsgProperties.getProperty(PropertiesKeys.CHAT_START_NEW_CHAT.getProperty()));
        REPLY_MODEL_CHOSE_EN = createOneRowReplyMarkup(ruMsgProperties.getProperty(PropertiesKeys.CHAT_GPT3_TITLE.getProperty()), ruMsgProperties.getProperty(PropertiesKeys.CHAT_GPT4_TITLE.getProperty()));
        REPLY_CHAT_MENU_EN = createOneRowReplyMarkup(enMsgProperties.getProperty(PropertiesKeys.CHAT_END_CHAT.getProperty()), enMsgProperties.getProperty(PropertiesKeys.CHAT_START_NEW_CHAT.getProperty()));
    }

    private static InlineKeyboardMarkup createInlineMarkup(List<InlineKeyboardButton>... lines)
    {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (List<InlineKeyboardButton> line : lines)
        {
            rows.add(line);
        }
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
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
}

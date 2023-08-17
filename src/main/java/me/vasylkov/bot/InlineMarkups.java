package me.vasylkov.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class InlineMarkups
{
    private static final Properties ruMsgProperties = PropertiesManager.getRuMsgProperties();
    private static final Properties enMsgProperties = PropertiesManager.getEnMsgProperties();
    public static final InlineKeyboardMarkup BUY_TOKENS_RU;
    public static final InlineKeyboardMarkup PRODUCT_LIST_RU;

    public static final InlineKeyboardMarkup BUY_TOKENS_EN;
    public static final InlineKeyboardMarkup PRODUCT_LIST_EN;

    static
    {
        BUY_TOKENS_RU = createInlineMarkup(Arrays.asList(createInlineButton(ruMsgProperties.getProperty(PropertyKeys.PURCHASE_BUY_TOKENS.getProperty()), CallbackData.PRESSED_BUY_BUTTON.getData())));
        BUY_TOKENS_EN = createInlineMarkup(Arrays.asList(createInlineButton(enMsgProperties.getProperty(PropertyKeys.PURCHASE_BUY_TOKENS.getProperty()), CallbackData.PRESSED_BUY_BUTTON.getData())));
        PRODUCT_LIST_RU = createInlineMarkup(Arrays.asList(createInlineButton(ruMsgProperties.getProperty(PropertyKeys.PURCHASE_MINIMAL_VALUE.getProperty()), CallbackData.PRESSED_MINIMAL_PURCHASE_BUTTON.getData())), Arrays.asList(createInlineButton(ruMsgProperties.getProperty(PropertyKeys.PURCHASE_MEDIUM_VALUE.getProperty()), CallbackData.PRESSED_MEDIUM_PURCHASE_BUTTON.getData())), Arrays.asList(createInlineButton(ruMsgProperties.getProperty(PropertyKeys.PURCHASE_MAXIMUM_VALUE.getProperty()), CallbackData.PRESSED_MAXIMUM_PURCHASE_BUTTON.getData())));
        PRODUCT_LIST_EN = createInlineMarkup(Arrays.asList(createInlineButton(enMsgProperties.getProperty(PropertyKeys.PURCHASE_MINIMAL_VALUE.getProperty()), CallbackData.PRESSED_MINIMAL_PURCHASE_BUTTON.getData())), Arrays.asList(createInlineButton(enMsgProperties.getProperty(PropertyKeys.PURCHASE_MEDIUM_VALUE.getProperty()), CallbackData.PRESSED_MEDIUM_PURCHASE_BUTTON.getData())), Arrays.asList(createInlineButton(enMsgProperties.getProperty(PropertyKeys.PURCHASE_MAXIMUM_VALUE.getProperty()), CallbackData.PRESSED_MAXIMUM_PURCHASE_BUTTON.getData())));
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
        // null - новый ряд
        //InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        //List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        //List<InlineKeyboardButton> colsInLine = new ArrayList<>();
        //for (InlineKeyboardButton button : buttons)
        //{
        //    if (button == null)
        //    {
         //       rows.add(List.copyOf(colsInLine));
        //        colsInLine.clear();
         //   }
         //   else
          //  {
         //       colsInLine.add(button);
        //    }
        //}
       // inlineKeyboardMarkup.setKeyboard(rows);
       // return inlineKeyboardMarkup;
    }

    private static InlineKeyboardButton createInlineButton(String text, String callbackData)
    {
        InlineKeyboardButton button = new InlineKeyboardButton(text);
        button.setCallbackData(callbackData);
        return button;
    }
}

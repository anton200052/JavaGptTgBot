package me.vasylkov.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineMarkups
{
    public static final InlineKeyboardMarkup BUY_TOKENS;
    public static final InlineKeyboardMarkup PRODUCT_LIST;

    static
    {
        BUY_TOKENS = createInlineMarkup(createInlineButton(Messages.PURCHASE_TOKENS, CallbackDatas.PRESSED_BUY_BUTTON), null);
        PRODUCT_LIST = createInlineMarkup(createInlineButton(Messages.MINIMAL_TOKENS_PURCHASE, CallbackDatas.PRESSED_MINIMAL_PURCHASE_BUTTON), null, createInlineButton(Messages.MEDIUM_TOKENS_PURCHASE, CallbackDatas.PRESSED_MEDIUM_PURCHASE_BUTTON), null, createInlineButton(Messages.MAXIMUM_TOKENS_PURCHASE, CallbackDatas.PRESSED_MAXIMUM_PURCHASE_BUTTON), null);
    }

    private static InlineKeyboardMarkup createInlineMarkup(InlineKeyboardButton... buttons)
    {
        // null - новый ряд
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> colsInLine = new ArrayList<>();
        for (InlineKeyboardButton button : buttons)
        {
            if (button == null)
            {
                rows.add(List.copyOf(colsInLine));
                colsInLine.clear();
            }
            else
            {
                colsInLine.add(button);
            }
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
}

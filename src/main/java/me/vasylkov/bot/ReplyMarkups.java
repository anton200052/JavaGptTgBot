package me.vasylkov.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ReplyMarkups
{
    public static final ReplyKeyboardMarkup PREVIOUS;
    public static final ReplyKeyboardMarkup NULL;
    public static final ReplyKeyboardMarkup MODEL_CHOSE;
    public static final ReplyKeyboardMarkup CHAT_MENU;

    static
    {
        PREVIOUS = createOneRowMarkup("previous");
        NULL = createOneRowMarkup("nullMarkup");
        MODEL_CHOSE = createOneRowMarkup("🌟GPT-3.5🌟", "⚡GPT-4⚡");
        CHAT_MENU = createOneRowMarkup("Завершить чат \uD83D\uDCA7", "Начать новый чат \uD83D\uDD25");
    }

    private static ReplyKeyboardMarkup createOneRowMarkup(String... args)
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

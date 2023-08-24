package me.vasylkov.bot;

public enum CallbackData
{
    PRESSED_BUY_BUTTON("pressed_buy_button"),
    PRESSED_MINIMAL_PURCHASE_BUTTON("pressed_minimal_purchase_button"),
    PRESSED_MEDIUM_PURCHASE_BUTTON("pressed_medium_purchase_button"),
    PRESSED_MAXIMUM_PURCHASE_BUTTON("pressed_maximum_purchase_button"),
    PRESSED_RU_LANGUAGE_BUTTON("pressed_ru_language_button"),
    PRESSED_EN_LANGUAGE_BUTTON("pressed_en_language_button");

    private String data;

    CallbackData(String data)
    {
        this.data = data;
    }

    public String getData()
    {
        return data;
    }
}

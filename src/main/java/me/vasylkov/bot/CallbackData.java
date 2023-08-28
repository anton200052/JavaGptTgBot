package me.vasylkov.bot;

public enum CallbackData
{
    PRESSED_PURCHASE_MINIMAL_BUTTON("pressed_purchase_minimal_button"),
    PRESSED_PURCHASE_MEDIUM_BUTTON("pressed_purchase_medium_button"),
    PRESSED_PURCHASE_MAXIMUM_BUTTON("pressed_purchase_maximum_button"),
    PRESSED_MENU_BUY_TOKENS_BUTTON("pressed_buy_button"),
    PRESSED_MENU_BALANCE_BUTTON("pressed_menu_balance_button"),
    PRESSED_MENU_SETTINGS_BUTTON("pressed_menu_settings_button"),
    PRESSED_MENU_HELP_BUTTON("pressed_menu_help_button"),
    PRESSED_MENU_RETURN_BUTTON("pressed_menu_return_menu_button"),
    PRESSED_SETTINGS_RU_LANGUAGE_BUTTON("pressed_settings_ru_language_button"),
    PRESSED_SETTINGS_EN_LANGUAGE_BUTTON("pressed_settings_en_language_button"),
    PRESSED_SETTINGS_AI_MODEL_BUTTON("pressed_settings_ai_model_button"),
    PRESSED_SETTINGS_LANGUAGE_BUTTON("pressed_settings_language_button"),
    PRESSED_SETTINGS_GPT3_BUTTON("pressed_settings_gpt3_button"),
    PRESSED_SETTINGS_GPT4_BUTTON("pressed_settings_gpt4_button"),
    PRESSED_APANEL_MSG_TO_ALL_BUTTON("pressed_apanel_msg_to_all_button"),
    PRESSED_APANEL_ADD_TOKENS_BUTTON("pressed_apanel_add_tokens_button"),
    PRESSED_APANEL_USER_INFO_BUTTON("pressed_apanel_user_info_button"),
    PRESSED_APANEL_CANCEL_ACTION_BUTTON("pressed_apanel_cancel_action_button");

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

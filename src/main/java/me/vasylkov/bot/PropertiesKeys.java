package me.vasylkov.bot;

public enum PropertiesKeys
{
    CONFIG_BOT_USERNAME("config.botUsername"),
    CONFIG_BOT_TOKEN("config.botToken"),
    CONFIG_GPT3_TOKEN("config.gpt3Token"),
    CONFIG_GPT4_TOKEN("config.gpt4Token"),
    CONFIG_ADMINS_ID("config.adminsId"),
    CONFIG_GPT3_MAX_TOKENS("config.gpt3MaxTokens"),
    CONFIG_GPT4_MAX_TOKENS("config.gpt4MaxTokens"),
    CONFIG_GPT3_VERSION("config.gpt3Version"),
    CONFIG_GPT4_VERSION("config.gpt4Version"),
    APANEL_TITLE("apanel.title"),
    APANEL_MSG_TO_ALL_BUTTON_TITLE("apanel.msgToAllButtonTitle"),
    APANEL_ADD_TOKENS_BUTTON_TITLE("apanel.addTokensButtonTitle"),
    APANEL_MSG_TO_ALL_INSTRUCTIONS("apanel.msgToAllInstructions"),
    APANEL_CANCEL_ACTION_BUTTON_TITLE("apanel.cancelActionButtonTitle"),
    APANEL_ADD_TOKENS_INSTRUCTIONS("apanel.addTokensInstruction"),
    APANEL_TOKENS_ADDED("apanel.tokensHasBeAdded"),
    APANEL_USER_NOT_FOUND("apanel.userNotFound"),
    APANEL_USER_SEND_PHOTO("apanel.userSendPhoto"),
    APANEL_USER_INFO_BUTTON_TITLE("apanel.userInfoButtonTitle"),
    APANEL_USER_INFO_INSTRUCTIONS("apanel.userInfoInstructions"),
    APANEL_USER_INFO("apanel.userInfo"),
    CHAT_START_GPT_CHAT("chat.startGptChat"),
    CHAT_REQUEST_WAITING("chat.requestWaiting"),
    CHAT_END_CHAT_BUTTON_TITLE("chat.endChatButtonTitle"),
    CHAT_START_NEW_BUTTON_TITLE("chat.startNewButtonTitle"),
    ERROR_ADMIN_MODE_PARSE("error.adminModeParse"),
    ERROR_INCORRECT_INPUT("error.incorrectInput"),
    ERROR_NOT_A_TXT_FILE("error.notATxtFile"),
    ERROR_NOT_ENOUGH_TOKENS("error.notEnoughTokens"),
    ERROR_NOT_IN_CHAT("error.notInChat"),
    ERROR_NULL_BALANCE("error.nullBalance"),
    ERROR_NOT_IN_MAIN_MENU("error.notInMainMenu"),
    ERROR_REQUEST_ERROR("error.requestError"),
    ERROR_NOT_PREMIUM_ACC("error.notPremiumAcc"),
    ERROR_USERNAME_NOT_AVAILABLE("error.usernameNotAval"),
    MENU_TITLE("menu.title"),
    MENU_BALANCE_BUTTON_TITLE("menu.balanceButtonTitle"),
    MENU_SETTINGS_BUTTON_TITLE("menu.settingsButtonTitle"),
    MENU_HELP_BUTTON_TITLE("menu.helpButtonTitle"),
    MENU_VIP_BALANCE("menu.proBalance"),
    MENU_DEFAULT_BALANCE("menu.defaultBalance"),
    MENU_RETURN_TO_MENU_BUTTON_TITLE("menu.returnToMenuButtonTitle"),
    MENU_HELP("menu.help"),
    MENU_PRODUCT_LIST("menu.productList"),
    MENU_RETURNED_TO_MAIN_MENU("menu.returnedToMainMenu"),
    MENU_START_1("menu.start1"),
    MENU_START_2("menu.start2"),
    SETTINGS_TITLE("settings.title"),
    SETTINGS_GPT3_BUTTON_TITLE("settings.gpt3Title"),
    SETTINGS_GPT4_BUTTON_TITLE("settings.gpt4Title"),
    SETTINGS_AI_MODELS("settings.gptModels"),
    SETTINGS_CHOOSE_LANGUAGE("settings.chooseLanguage"),
    SETTINGS_LANGUAGE_CHANGED("settings.languageChanged"),
    SETTINGS_LANGUAGE_TITLE("settings.languageTitle"),
    SETTINGS_MODEL_CHANGED("settings.modelChanged"),
    SETTINGS_AI_MODEL_BUTTON_TITLE("settings.aiModelButtonTitle"),
    SETTINGS_LANGUAGE_BUTTON_TITLE("settings.languageButtonTitle"),
    PURCHASE_BUY_TOKENS_BUTTON_TITLE("purchase.buyTokensButtonTitle"),
    PURCHASE_FINAL_MSG("purchase.finalMsg"),
    PURCHASE_MAXIMUM_VALUE_BUTTON_TITLE("purchase.maximumValueButtonTitle"),
    PURCHASE_MEDIUM_VALUE_BUTTON_TITLE("purchase.mediumValueButtonTitle"),
    PURCHASE_MINIMAL_VALUE_BUTTON_TITLE("purchase.minimalValueButtonTitle");

    private final String property;

    PropertiesKeys(String property)
    {
        this.property = property;
    }

    public String getProperty()
    {
        return property;
    }
}

package me.vasylkov.bot;

public enum PropertiesKeys
{
    CONFIG_BOT_USERNAME("config.botUsername"),
    CONFIG_BOT_TOKEN("config.botToken"),
    CONFIG_GPT3_TOKEN("config.gpt3Token"),
    CONFIG_GPT4_TOKEN("config.gpt4Token"),
    CONFIG_ADMINS_ID("config.adminsId"),
    ADMIN_PRINT_USER_DATA("admin.printUserData"),
    ADMIN_SEND_MESSAGE_TO_ALL("admin.sendMessageToAll"),
    ADMIN_TOKENS_ADDED("admin.tokensHasBeAdded"),
    ADMIN_USER_NOT_FOUND("admin.userNotFound"),
    ADMIN_USER_SEND_PHOTO("admin.userSendPhoto"),
    CHAT_REQUEST_WAITING("chat.requestWaiting"),
    CHAT_GPT3_TITLE("chat.gpt3Title"),
    CHAT_GPT4_TITLE("chat.gpt4Title"),
    CHAT_END_CHAT("chat.endChat"),
    CHAT_START_NEW_CHAT("chat.startNewChat"),
    ERROR_ADMIN_MODE_PARSE("error.adminModeParse"),
    ERROR_INCORRECT_INPUT("error.incorrectInput"),
    ERROR_INCORRECT_MODEL("error.incorrectModel"),
    ERROR_NOT_A_TXT_FILE("error.notATxtFile"),
    ERROR_NOT_ENOUGH_TOKENS("error.notEnoughTokens"),
    ERROR_NOT_IN_CHAT("error.notInChat"),
    ERROR_NOT_IN_MAIN_MENU("error.notInMainMenu"),
    ERROR_REQUEST_ERROR("error.requestError"),
    ERROR_USERNAME_NOT_AVAILABLE("error.usernameNotAval"),
    MENU_BALANCE("menu.balance"),
    MENU_HELP_1("menu.help1"),
    MENU_HELP_2("menu.help2"),
    MENU_MODEL_CHOOSE("menu.modelChoose"),
    MENU_PRODUCT_LIST("menu.productList"),
    MENU_RETURNED_TO_MAIN_MENU("menu.returnedToMainMenu"),
    MENU_START_1("menu.start1"),
    MENU_START_2("menu.start2"),
    MENU_STARTED_GPT3_CHAT("menu.startedGpt3Chat"),
    MENU_STARTED_GPT4_CHAT("menu.startedGpt4Chat"),
    PURCHASE_BUY_TOKENS("purchase.buyTokens"),
    PURCHASE_FINAL_MSG("purchase.finalMsg"),
    PURCHASE_MAXIMUM_VALUE("purchase.maximumValue"),
    PURCHASE_MEDIUM_VALUE("purchase.mediumValue"),
    PURCHASE_MINIMAL_VALUE("purchase.minimalValue");

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

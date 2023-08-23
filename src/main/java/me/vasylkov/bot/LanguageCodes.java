package me.vasylkov.bot;

public enum LanguageCodes
{

    RU(PropertiesManager.getRuMsgProperties().getProperty(PropertiesKeys.MENU_LANGUAGE_TITLE.getProperty())),
    EN(PropertiesManager.getEnMsgProperties().getProperty(PropertiesKeys.MENU_LANGUAGE_TITLE.getProperty()));

    private final String languageTitle;

    LanguageCodes(String languageTitle)
    {
        this.languageTitle = languageTitle;
    }

    public String getLanguageTitle()
    {
        return languageTitle;
    }
}

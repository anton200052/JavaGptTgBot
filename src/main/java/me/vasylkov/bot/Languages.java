package me.vasylkov.bot;

public enum Languages
{

    RU(PropertiesManager.getRuMsgProperties().getProperty(PropertiesKeys.SETTINGS_LANGUAGE_TITLE.getProperty())),
    EN(PropertiesManager.getEnMsgProperties().getProperty(PropertiesKeys.SETTINGS_LANGUAGE_TITLE.getProperty()));

    private final String languageTitle;

    Languages(String languageTitle)
    {
        this.languageTitle = languageTitle;
    }

    public String getLanguageTitle()
    {
        return languageTitle;
    }
}

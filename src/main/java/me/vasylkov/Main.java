package me.vasylkov;

import me.vasylkov.bot.NotValidConfigDataException;
import me.vasylkov.bot.PropertiesManager;
import me.vasylkov.bot.TelegramBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            PropertiesManager.loadConfigProperties();
        }
        catch (NotValidConfigDataException e)
        {
            System.out.println(e.getMessage());
            return;
        }

        PropertiesManager.loadMsgBundles();
        setupBot();
    }

    private static void setupBot()
    {
        TelegramBotsApi api = null;
        try
        {
            api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(new TelegramBot());
        }
        catch (TelegramApiException e)
        {
            throw new RuntimeException(e);
        }
    }
}
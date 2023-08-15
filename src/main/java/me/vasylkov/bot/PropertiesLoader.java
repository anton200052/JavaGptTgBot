package me.vasylkov.bot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class PropertiesLoader
{
    private static final Path configPropertiesPath = Path.of("config.properties");
    private static Properties configProperties;

    public static void loadConfigProperties() throws NotValidConfigDataException
    {
        if (!Files.exists(configPropertiesPath))
        {
            configProperties = initConfigDefaultProperties();

            try
            {
                Files.createFile(configPropertiesPath);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            storeProperties(configPropertiesPath, configProperties);
        }
        else
        {
            configProperties = loadExistProperties(configPropertiesPath);
        }

        if (!isConfigPropertiesDataValid())
        {
            throw new NotValidConfigDataException("Откройте файл config.properties и замените слова empty соответствующими данными для каждого поля. Если вы повторно видите это сообщение тогда удалите файл и заполните его заново");
        }
    }

    private static Properties initConfigDefaultProperties()
    {
        Properties properties = new Properties();
        properties.setProperty("tg_bot_username", "empty");
        properties.setProperty("tg_bot_token", "empty");
        properties.setProperty("openai_gpt3_token", "empty");
        properties.setProperty("openai_gpt4_token", "empty");
        return properties;
    }

    private static void storeProperties(Path path, Properties properties)
    {
        try (FileOutputStream fileOutputStream = new FileOutputStream(path.toString()))
        {
            properties.store(fileOutputStream, "Config file");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static Properties loadExistProperties(Path path)
    {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(path.toString()))
        {
            properties.load(fileInputStream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return properties;
    }

    private static boolean isConfigPropertiesDataValid()
    {
        if (configProperties.getProperty("tg_bot_username") == null || configProperties.getProperty("tg_bot_username").equals("empty")
        || configProperties.getProperty("tg_bot_token") == null || configProperties.getProperty("tg_bot_token").equals("empty")
        || configProperties.getProperty("openai_gpt3_token") == null || configProperties.getProperty("openai_gpt3_token").equals("empty")
        || configProperties.getProperty("openai_gpt4_token") == null || configProperties.getProperty("openai_gpt4_token").equals("empty"))
        {
            return false;
        }
        return true;
    }

    public static Properties getConfigProperties()
    {
        return configProperties;
    }
}

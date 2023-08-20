package me.vasylkov.bot;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class PropertiesManager
{
    private static final String ruLocaleCode = "ru";
    private static final String enLocaleCode = "en";
    private static final Path pathToMessagesBaseName = Path.of("messages");
    private static final Path configPropertiesPath = Path.of("config.properties");
    private static final Path ruMsgPropertiesPath = Path.of(pathToMessagesBaseName.toString() + "_" + ruLocaleCode + ".properties");
    private static final Path enMsgPropertiesPath = Path.of(pathToMessagesBaseName.toString() + "_" + enLocaleCode + ".properties");


    private static Properties configProperties;
    private static Properties ruMsgProperties;
    private static Properties enMsgProperties;

    public static void loadMsgProperties()
    {
        if (!Files.exists(ruMsgPropertiesPath))
        {
            ruMsgProperties = initRuMsgDefaultProperties();

            try
            {
                Files.createFile(ruMsgPropertiesPath);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            storeProperties(ruMsgPropertiesPath, ruMsgProperties);
        }
        else
        {
            ruMsgProperties = loadExistProperties(ruMsgPropertiesPath);
        }

        if (!Files.exists(enMsgPropertiesPath))
        {
            enMsgProperties = initEnMsgDefaultProperties();

            try
            {
                Files.createFile(enMsgPropertiesPath);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            storeProperties(enMsgPropertiesPath, enMsgProperties);
        }
        else
        {
            enMsgProperties = loadExistProperties(enMsgPropertiesPath);
        }
    }

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
        properties.setProperty(PropertiesKeys.CONFIG_BOT_USERNAME.getProperty(), "empty");
        properties.setProperty(PropertiesKeys.CONFIG_BOT_TOKEN.getProperty(), "empty");
        properties.setProperty(PropertiesKeys.CONFIG_GPT3_TOKEN.getProperty(), "empty");
        properties.setProperty(PropertiesKeys.CONFIG_GPT4_TOKEN.getProperty(), "empty");
        properties.setProperty(PropertiesKeys.CONFIG_ADMINS_ID.getProperty(), "empty");
        return properties;
    }

    private static Properties initRuMsgDefaultProperties() {
        Properties properties = new Properties();

        // Errors
        properties.setProperty(PropertiesKeys.ERROR_NOT_A_TXT_FILE.getProperty(), "Ошибка! Файл должен быть формата .txt");
        properties.setProperty(PropertiesKeys.ERROR_NOT_IN_MAIN_MENU.getProperty(), "Вы должны завершить чат чтобы использовать данную команду.");
        properties.setProperty(PropertiesKeys.ERROR_INCORRECT_MODEL.getProperty(), "Выбрана неправильная модель, пожалуйста выберите одну из моделей предложенных в навигационном меню");
        properties.setProperty(PropertiesKeys.ERROR_NOT_IN_CHAT.getProperty(), "Чтобы задать вопрос боту используйте команду: /startchat");
        properties.setProperty(PropertiesKeys.ERROR_INCORRECT_INPUT.getProperty(), "Ошибка! Неправильный формат ввода. Вы можете либо написать сообщение боту, либо отправить ему txt файл и он прочитает из него.");
        properties.setProperty(PropertiesKeys.ERROR_ADMIN_MODE_PARSE.getProperty(), "Ошибка при парсинге");
        properties.setProperty(PropertiesKeys.ERROR_NOT_ENOUGH_TOKENS.getProperty(), """
        На вашем балансе должно быть больше чем 300 токенов для того чтобы использовать GPT-4 🙁
        Пожалуйста выберите другую модель либо пополните свой баланс.
        ⚪ Пополнение токенов - /balance
        """);
        properties.setProperty(PropertiesKeys.ERROR_REQUEST_ERROR.getProperty(), """
        Ошибка при попытке получить ответ от OpenAI.
        Возможные причины:
        - Слишком большое количество одновременных запросов.
        - Перегрузка серверов OpenAI.
        Решение:
        - Используйте команду /startchat и повторите свой запрос.
   
        """);
        properties.setProperty(PropertiesKeys.ERROR_USERNAME_NOT_AVAILABLE.getProperty(), """
        Для того, чтобы использовать Telegram-бота, добавьте его юзернейм или сделайте его публичным в настройках Telegram.
        """);

        // Menu
        properties.setProperty(PropertiesKeys.MENU_MODEL_CHOOSE.getProperty(), "Выберите модель чата в навигационном меню.\nGPT-3.5: Бесплатно");
        properties.setProperty(PropertiesKeys.MENU_STARTED_GPT3_CHAT.getProperty(), "Вы успешно начали чат с моделью GPT-3.5");
        properties.setProperty(PropertiesKeys.MENU_STARTED_GPT4_CHAT.getProperty(), "Вы успешно начали чат с моделью GPT-4. Генерация ответов до 2-х минут");
        properties.setProperty(PropertiesKeys.MENU_RETURNED_TO_MAIN_MENU.getProperty(), "Вы были перемещены в главное меню.");
        properties.setProperty(PropertiesKeys.MENU_PRODUCT_LIST.getProperty(), """
        💌 Сколько токенов вы хотите приобрести?
    
        ⚡ Подсказка:
        ⚪ 50К токенов ≈ 300 ответов от GPT-4
    
        ‼️ Оплата только через укр. карты, для оплаты другой валютой свяжитесь с нашей поддержкой - /help ‼️
        """);
        properties.setProperty(PropertiesKeys.MENU_BALANCE.getProperty(), """
        GPT-4:
        ⚪ У вас %d💰токенов на балансе
    
        GPT-3.5:
        ⚪ Безграничное использование 🌟
    
        Покупка токенов:
        """);

        // Chat
        properties.setProperty(PropertiesKeys.CHAT_REQUEST_WAITING.getProperty(), "\uD83E\uDDE0ChatGPT генерирует ответ...");
        properties.setProperty(PropertiesKeys.CHAT_GPT3_TITLE.getProperty(), "🌟GPT-3.5🌟");
        properties.setProperty(PropertiesKeys.CHAT_GPT4_TITLE.getProperty(), "⚡GPT-4⚡");
        properties.setProperty(PropertiesKeys.CHAT_END_CHAT.getProperty(), "Завершить чат \uD83D\uDCA7");
        properties.setProperty(PropertiesKeys.CHAT_START_NEW_CHAT.getProperty(), "Начать новый чат \uD83D\uDD25");


        // Purchase
        properties.setProperty(PropertiesKeys.PURCHASE_MINIMAL_VALUE.getProperty(), "\uD83D\uDFE3+25К токенов - 119 UAH (-40%)");
        properties.setProperty(PropertiesKeys.PURCHASE_MEDIUM_VALUE.getProperty(), "\uD83D\uDFE3+50К токенов - 199 UAH (-50%)");
        properties.setProperty(PropertiesKeys.PURCHASE_MAXIMUM_VALUE.getProperty(), "\uD83D\uDFE3+100К токенов - 319 UAH (-80%)");
        properties.setProperty(PropertiesKeys.PURCHASE_BUY_TOKENS.getProperty(), "\uD83E\uDD51Купить токены");
        properties.setProperty(PropertiesKeys.PURCHASE_FINAL_MSG.getProperty(), """
        ⚪ Отправьте %d UAH на данную карту:
        ‼️MonoBank‼️→ (CARD NUMBER)
    
        ⚪ После оплаты отправьте скриншот успешного перевода в чат и ожидайте начисления в течение 10 минут.
    
        🔴 Если будет произведена оплата, но скриншот не будет отправлен, токены не будут зачислены.
        """);

        // Admin
        properties.setProperty(PropertiesKeys.ADMIN_PRINT_USER_DATA.getProperty(), "Введите юзернейм пользователя и количество токенов в формате: (юзернейм количество)");
        properties.setProperty(PropertiesKeys.ADMIN_USER_NOT_FOUND.getProperty(), "Пользователь не найден");
        properties.setProperty(PropertiesKeys.ADMIN_TOKENS_ADDED.getProperty(), "Токены были успешно зачислены на баланс указанного пользователя");
        properties.setProperty(PropertiesKeys.ADMIN_USER_SEND_PHOTO.getProperty(), "Пользователь @%s отправил фото:");
        properties.setProperty(PropertiesKeys.ADMIN_SEND_MESSAGE_TO_ALL.getProperty(), "Введите сообщение которое будет отправлено всем пользователям.");

        return properties;
    }

    private static Properties initEnMsgDefaultProperties() {
        Properties properties = new Properties();

        // Errors
        properties.setProperty(PropertiesKeys.ERROR_NOT_A_TXT_FILE.getProperty(), "Error! The file must be in .txt format.");
        properties.setProperty(PropertiesKeys.ERROR_NOT_IN_MAIN_MENU.getProperty(), "You must exit the chat to use this command.");
        properties.setProperty(PropertiesKeys.ERROR_INCORRECT_MODEL.getProperty(), "Incorrect model selected. Please choose one of the models provided in the navigation menu.");
        properties.setProperty(PropertiesKeys.ERROR_NOT_IN_CHAT.getProperty(), "To ask a question to the bot, use the command: /startchat");
        properties.setProperty(PropertiesKeys.ERROR_INCORRECT_INPUT.getProperty(), "Error! Incorrect input format. You can either send a message to the bot or send a txt file for it to read.");
        properties.setProperty(PropertiesKeys.ERROR_ADMIN_MODE_PARSE.getProperty(), "Parsing error");
        properties.setProperty(PropertiesKeys.ERROR_NOT_ENOUGH_TOKENS.getProperty(), """
        You need to have more than 300 tokens on your balance to use GPT-4 🙁
        Please choose another model or top up your balance.
        ⚪ Top up tokens - /balance
        """);
        properties.setProperty(PropertiesKeys.ERROR_REQUEST_ERROR.getProperty(), """
        Error while trying to receive a response from OpenAI.
        Possible reasons:
        - Too many concurrent requests.
        - OpenAI server overload.
        Solution:
        - Use the /startchat command and retry your request.
        """);
        properties.setProperty(PropertiesKeys.ERROR_USERNAME_NOT_AVAILABLE.getProperty(), """
        To use the Telegram bot, add its username or make it public in your Telegram settings.
        """);

        // Menu
        properties.setProperty(PropertiesKeys.MENU_MODEL_CHOOSE.getProperty(), "Choose a chat model in the navigation menu.\nGPT-3.5: Free");
        properties.setProperty(PropertiesKeys.MENU_STARTED_GPT3_CHAT.getProperty(), "You have successfully started a chat with the GPT-3.5 model.");
        properties.setProperty(PropertiesKeys.MENU_STARTED_GPT4_CHAT.getProperty(), "You have successfully started a chat with the GPT-4 model. Response generation takes up to 2 minutes");
        properties.setProperty(PropertiesKeys.MENU_RETURNED_TO_MAIN_MENU.getProperty(), "You have been returned to the main menu.");
        properties.setProperty(PropertiesKeys.MENU_PRODUCT_LIST.getProperty(), """
        💌 How many tokens would you like to purchase?
    
        ⚡ Hint:
        ⚪ 50K tokens ≈ 300 responses from GPT-4
    
        ‼️ Payment only via Ukrainian cards, for payment in other currency contact our support - /help ‼️
        """);
        properties.setProperty(PropertiesKeys.MENU_BALANCE.getProperty(), """
        GPT-4:
        ⚪ You have %d💰tokens on your balance
    
        GPT-3.5:
        ⚪ Unlimited usage 🌟
    
        Token purchase:
        """);

        // Chat
        properties.setProperty(PropertiesKeys.CHAT_REQUEST_WAITING.getProperty(), "\uD83E\uDDE0ChatGPT is generating a response...");
        properties.setProperty(PropertiesKeys.CHAT_GPT3_TITLE.getProperty(), "🌟GPT-3.5🌟");
        properties.setProperty(PropertiesKeys.CHAT_GPT4_TITLE.getProperty(), "⚡GPT-4⚡");
        properties.setProperty(PropertiesKeys.CHAT_END_CHAT.getProperty(), "End chat \uD83D\uDCA7");
        properties.setProperty(PropertiesKeys.CHAT_START_NEW_CHAT.getProperty(), "Start new chat \uD83D\uDD25");


        // Purchase
        properties.setProperty(PropertiesKeys.PURCHASE_MINIMAL_VALUE.getProperty(), "\uD83D\uDFE3+25K tokens - 119 UAH (-40%)");
        properties.setProperty(PropertiesKeys.PURCHASE_MEDIUM_VALUE.getProperty(), "\uD83D\uDFE3+50K tokens - 199 UAH (-50%)");
        properties.setProperty(PropertiesKeys.PURCHASE_MAXIMUM_VALUE.getProperty(), "\uD83D\uDFE3+100K tokens - 319 UAH (-80%)");
        properties.setProperty(PropertiesKeys.PURCHASE_BUY_TOKENS.getProperty(), "\uD83E\uDD51Buy Tokens");
        properties.setProperty(PropertiesKeys.PURCHASE_FINAL_MSG.getProperty(), """
        ⚪ Send %d UAH to this card:
        ‼️MonoBank‼️→ (CARD NUMBER)
    
        ⚪ After payment, send a screenshot of the successful transfer to the chat and wait for the tokens to be credited within 10 minutes.
    
        🔴 If payment is made but no screenshot is sent, the tokens will not be credited.
        """);

        // Admin
        properties.setProperty(PropertiesKeys.ADMIN_PRINT_USER_DATA.getProperty(), "Enter the username of the user and the number of tokens in the format: (username amount)");
        properties.setProperty(PropertiesKeys.ADMIN_USER_NOT_FOUND.getProperty(), "User not found");
        properties.setProperty(PropertiesKeys.ADMIN_TOKENS_ADDED.getProperty(), "Tokens have been successfully added to the balance of the specified user");
        properties.setProperty(PropertiesKeys.ADMIN_USER_SEND_PHOTO.getProperty(), "User @%s sent a photo:");
        properties.setProperty(PropertiesKeys.ADMIN_SEND_MESSAGE_TO_ALL.getProperty(), "Enter the message that will be sent to all users.");

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
        if (configProperties.getProperty("tgBot.username") == null || configProperties.getProperty("tgBot.username").equals("empty")
        || configProperties.getProperty("tgBot.token") == null || configProperties.getProperty("tgBot.token").equals("empty")
        || configProperties.getProperty("openAiGpt3.token") == null || configProperties.getProperty("openAiGpt3.token").equals("empty")
        || configProperties.getProperty("openAiGpt4.token") == null || configProperties.getProperty("openAiGpt4.token").equals("empty"))
        {
            return false;
        }
        return true;
    }

    public static Properties getConfigProperties()
    {
        return configProperties;
    }

    public static Properties getRuMsgProperties()
    {
        return ruMsgProperties;
    }

    public static Properties getEnMsgProperties()
    {
        return enMsgProperties;
    }
}

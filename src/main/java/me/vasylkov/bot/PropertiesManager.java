package me.vasylkov.bot;

import java.io.*;
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

    public static void loadConfigProperties()
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
            configProperties = initConfigProperties();
            storeProperties(configPropertiesPath, configProperties);
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
        properties.setProperty(PropertiesKeys.CONFIG_GPT3_VERSION.getProperty(), "empty");
        properties.setProperty(PropertiesKeys.CONFIG_GPT4_VERSION.getProperty(), "empty");
        properties.setProperty(PropertiesKeys.CONFIG_GPT3_MAX_TOKENS.getProperty(), "empty");
        properties.setProperty(PropertiesKeys.CONFIG_GPT4_MAX_TOKENS.getProperty(), "empty");
        return properties;
    }

    private static boolean isConfigPropertiesDataValid()
    {
        if (configProperties.getProperty(PropertiesKeys.CONFIG_BOT_USERNAME.getProperty()) == null
                || configProperties.getProperty(PropertiesKeys.CONFIG_BOT_USERNAME.getProperty()).equals("empty")
                || configProperties.getProperty(PropertiesKeys.CONFIG_BOT_TOKEN.getProperty()) == null
                || configProperties.getProperty(PropertiesKeys.CONFIG_BOT_TOKEN.getProperty()).equals("empty")
                || configProperties.getProperty(PropertiesKeys.CONFIG_GPT3_TOKEN.getProperty()) == null
                || configProperties.getProperty(PropertiesKeys.CONFIG_GPT3_TOKEN.getProperty()).equals("empty")
                || configProperties.getProperty(PropertiesKeys.CONFIG_GPT4_TOKEN.getProperty()) == null
                || configProperties.getProperty(PropertiesKeys.CONFIG_GPT4_TOKEN.getProperty()).equals("empty")
                || configProperties.getProperty(PropertiesKeys.CONFIG_ADMINS_ID.getProperty()) == null
                || configProperties.getProperty(PropertiesKeys.CONFIG_ADMINS_ID.getProperty()).equals("empty")
                || configProperties.getProperty(PropertiesKeys.CONFIG_GPT3_VERSION.getProperty()) == null
                || configProperties.getProperty(PropertiesKeys.CONFIG_GPT3_VERSION.getProperty()).equals("empty")
                || configProperties.getProperty(PropertiesKeys.CONFIG_GPT4_VERSION.getProperty()) == null
                || configProperties.getProperty(PropertiesKeys.CONFIG_GPT4_VERSION.getProperty()).equals("empty")
                || configProperties.getProperty(PropertiesKeys.CONFIG_GPT3_MAX_TOKENS.getProperty()) == null
                || configProperties.getProperty(PropertiesKeys.CONFIG_GPT3_MAX_TOKENS.getProperty()).equals("empty")
                || configProperties.getProperty(PropertiesKeys.CONFIG_GPT4_MAX_TOKENS.getProperty()) == null
                || configProperties.getProperty(PropertiesKeys.CONFIG_GPT4_MAX_TOKENS.getProperty()).equals("empty"))
        {
            return false;
        }
        return true;
    }


    public static Properties initConfigProperties()
    {
        Properties properties = new Properties();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in)))
        {
            System.out.println("Enter telegram bot username:\n");
            properties.setProperty(PropertiesKeys.CONFIG_BOT_USERNAME.getProperty(), bufferedReader.readLine());

            System.out.println("Enter telegram bot token:\n");
            properties.setProperty(PropertiesKeys.CONFIG_BOT_TOKEN.getProperty(), bufferedReader.readLine());

            System.out.println("Enter OpenAI GPT-3.5 token:\n");
            properties.setProperty(PropertiesKeys.CONFIG_GPT3_TOKEN.getProperty(), bufferedReader.readLine());

            System.out.println("Enter OpenAI GPT-4 token(it can be same as GPT-3.5 token):\n");
            properties.setProperty(PropertiesKeys.CONFIG_GPT4_TOKEN.getProperty(), bufferedReader.readLine());

            System.out.println("Enter admins telegram id`s. Format: id1,id2,id3,id4\n");
            properties.setProperty(PropertiesKeys.CONFIG_ADMINS_ID.getProperty(), bufferedReader.readLine());

            properties.setProperty(PropertiesKeys.CONFIG_GPT3_VERSION.getProperty(), "gpt-3.5-turbo-16k");
            properties.setProperty(PropertiesKeys.CONFIG_GPT4_VERSION.getProperty(), "gpt-4");
            properties.setProperty(PropertiesKeys.CONFIG_GPT3_MAX_TOKENS.getProperty(), "9000");
            properties.setProperty(PropertiesKeys.CONFIG_GPT4_MAX_TOKENS.getProperty(), "1000");

            System.out.println("If you want to change or add anything. For example, to change the username or token of the bot,\njust open the config.properties file through any text editor in the root directory of the jar file.\nAlso you can change gpt version and max tokens");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return properties;
    }

    private static Properties initRuMsgDefaultProperties()
    {
        Properties properties = new Properties();

        // Errors
        properties.setProperty(PropertiesKeys.ERROR_NOT_A_TXT_FILE.getProperty(), "Ошибка! Файл должен быть формата .txt");
        properties.setProperty(PropertiesKeys.ERROR_NOT_IN_MAIN_MENU.getProperty(), "Вы должны завершить чат с GPT чтобы совершить это действие");
        properties.setProperty(PropertiesKeys.ERROR_NOT_IN_CHAT.getProperty(), "Чтобы задать вопрос боту используйте команду: /startchat");
        properties.setProperty(PropertiesKeys.ERROR_INCORRECT_INPUT.getProperty(), "Ошибка! Неправильный формат ввода. Вы можете либо написать сообщение боту, либо отправить ему txt файл и он прочитает из него.");
        properties.setProperty(PropertiesKeys.ERROR_ADMIN_MODE_PARSE.getProperty(), "Ошибка при парсинге");
        properties.setProperty(PropertiesKeys.ERROR_NOT_PREMIUM_ACC.getProperty(), """
                ❌ GPT-4 - это модель премиум-класса!
                                
                ⚪ Для её использования вам необходим статус Premium
                ⤷ /menu -> Баланс токенов
                """);
        properties.setProperty(PropertiesKeys.ERROR_NOT_ENOUGH_TOKENS.getProperty(), """
                На вашем балансе должно быть больше чем 300 токенов для того чтобы использовать GPT-4 🙁
                Пожалуйста выберите другую модель либо пополните свой баланс.
                ⚪ Пополнение токенов - /balance
                """);
        properties.setProperty(PropertiesKeys.ERROR_REQUEST_ERROR.getProperty(), """
                Ошибка при попытке получить ответ от OpenAI.
                Возможные причины:
                ⤷ Слишком большое количество одновременных запросов.
                ⤷ Перегрузка серверов OpenAI.
                Решение:
                ⤷ Используйте команду /startchat и повторите свой запрос.
                   
                """);
        properties.setProperty(PropertiesKeys.ERROR_NULL_BALANCE.getProperty(), """
                На вашем балансе недостаточно токенов для чата с GPT-4!
                Пополнение токенов:
                ⤷ /menu - Баланс
                """);

        // menu
        properties.setProperty(PropertiesKeys.MENU_RETURN_TO_MENU_BUTTON_TITLE.getProperty(), "Назад в меню↩️");
        properties.setProperty(PropertiesKeys.MENU_HELP_BUTTON_TITLE.getProperty(), "\uD83D\uDCA1 Помощь");
        properties.setProperty(PropertiesKeys.MENU_BALANCE_BUTTON_TITLE.getProperty(), "\uD83C\uDF49 Баланс токенов");
        properties.setProperty(PropertiesKeys.MENU_SETTINGS_BUTTON_TITLE.getProperty(), "⚙️ Настройки");
        properties.setProperty(PropertiesKeys.MENU_TITLE.getProperty(), "\uD83C\uDFE0 Меню:");
        properties.setProperty(PropertiesKeys.MENU_RETURNED_TO_MAIN_MENU.getProperty(), "Вы вернулись в главное меню.");
        properties.setProperty(PropertiesKeys.MENU_PRODUCT_LIST.getProperty(), """
                💌 Сколько токенов вы хотите приобрести?

                ⚡ Подсказка:
                ⚪ 50 тыс. токенов ≈ 300 ответов от GPT-4

                ‼️ Оплата только через украинские карты, для оплаты в другой валюте обратитесь в нашу поддержку - /help ‼️
                """);
        properties.setProperty(PropertiesKeys.MENU_VIP_BALANCE.getProperty(), """
                GPT-4:
                ⚪ВИП статус:
                ⤷ ✅Активирован
                                
                🟤Баланс
                ⤷ У вас %d💰 GPT-4 токенов
                                
                🍉ВИП статус вам даёт
                ⤷ Доступ к GPT-4
                ⤷ Ввод с помощью голосовых сообщений
                ⤷ Ввод с помощью текстовых файлов
                                
                               
                Покупка токенов:
                """);

        properties.setProperty(PropertiesKeys.MENU_DEFAULT_BALANCE.getProperty(), """
                GPT-4:
                ⚪ВИП статус:
                ⤷ ❌Дективирован
                                
                🟤Баланс
                ⤷ У вас %d💰 GPT-4 токенов
                                
                💡 Примечание: ВИП статус можно получить посредством покупки
                любого количества токенов (нажмите кнопку снизу)
                                
                🍉ВИП статус вам даёт
                ⤷ Доступ к GPT-4
                ⤷ Ввод с помощью голосовых сообщений
                ⤷ Ввод с помощью текстовых файлов
                                
                               
                Покупка токенов:
                """);
        properties.setProperty(PropertiesKeys.MENU_START_1.getProperty(), """
                Привет! 😄 Я самый передовой искусственный интеллект в мире (созданный OpenAI)! 🌟
                               
                Я рад помочь вам с любой задачей, будь то 💻 программирование, отладка или объяснение кода! 💡 Я могу сочинять электронные письма, 📧 писать блоговые статьи на любую тему, или даже помогать с домашними заданиями! 📚
                               
                Нужны красочные стихи или запоминающиеся песни? 🎵 Нет проблем, я могу создать их для вас! И если вам нужен персональный репетитор, я готов помочь вам в любом предмете. 🎓
                               
                А если вы просто хотите поболтать, я всегда готов вести интересные и приятные разговоры! 🗣️ Что бы вы ни хотели, я здесь, чтобы это исполнить! 🌟
                """);
        properties.setProperty(PropertiesKeys.MENU_START_2.getProperty(), """
                Прежде чем мы начнем, есть 2 важные вещи, которые стоит знать: ✌️
                                
                1. Вы можете изменить язык интерфейса в настройках!
                   ⤷ /menu - Настройки
                                
                2. Если вам нужна дополнительная информация, просто отправьте мне команду:
                   ⤷ /menu - Помощь
                """);

        properties.setProperty(PropertiesKeys.MENU_HELP.getProperty(), """
                💬 Свяжитесь с нами здесь по всем важным вопросам 💬
                💻 Поддержка: @lavviku 🌟
                                
                                
                Краткое руководство по использованию бота:
                                
                • Список команд расположен на левой стороне поля ввода сообщения. Чтобы ввести команду, просто нажмите на интересующую вас команду.
                • "Навигационное меню" - это выбор действия, аналогичный выбору команды, но это динамический список, который появляется, когда вам нужно что-то выбрать. Обычно список появляется прямо под полем ввода, но если этого не происходит, вы можете активировать его, нажав кнопку справа от поля ввода.
                                
                Шаги взаимодействия с ботом:
                                
                1. Чтобы задать вопрос боту, используйте команду /startchat. По умолчанию стоит бесплатная модель - GPT3.5, изменить её можно в настройках:
                   ⤷ /menu - Настройки
                   
                3. Далее отправьте сообщение боту с вашим интересующим вопросом.
                
                4. Дождитесь ответа на ваш запрос (генерация занимает до 3 минут).
                
                5. Если вы хотите переключиться на другую модель чата, выберите опцию "Начать новый чат" в навигационном меню. Затем следуйте шагам, начиная с пункта номер 3.
                                
                Важное примечание 1:
                • Доступ к модели GPT-3.5 неограничен, и вы можете использовать этот режим без расходования токенов(бесплатно). Для использования GPT-4 вам нужно иметь VIP статус и токены на балансе. Покупка токенов:
                  ⤷ /menu - Баланс
                                
                Важное примечание 2:
                • Чем длиннее диалог, тем больше токенов используется сразу. Так что будьте экономны и начните новый чат после каждого вопроса, если вы не планируете вести диалог с ботом.
                """);

        //apanel
        properties.setProperty(PropertiesKeys.APANEL_TITLE.getProperty(), "\uD83D\uDEE1️ Админ панель");
        properties.setProperty(PropertiesKeys.APANEL_MSG_TO_ALL_BUTTON_TITLE.getProperty(), "\uD83D\uDCE3 Сообщение всем");
        properties.setProperty(PropertiesKeys.APANEL_ADD_TOKENS_BUTTON_TITLE.getProperty(), "\uD83D\uDCBC Добавить токены");
        properties.setProperty(PropertiesKeys.APANEL_USER_INFO_BUTTON_TITLE.getProperty(), "💡 Инфо. о польз.");
        properties.setProperty(PropertiesKeys.APANEL_CANCEL_ACTION_BUTTON_TITLE.getProperty(), "❌ Отменить действие");
        properties.setProperty(PropertiesKeys.APANEL_USER_NOT_FOUND.getProperty(), "Пользователь не найден");
        properties.setProperty(PropertiesKeys.APANEL_TOKENS_ADDED.getProperty(), "Токены были успешно зачислены на баланс указанного пользователя");
        properties.setProperty(PropertiesKeys.APANEL_USER_SEND_PHOTO.getProperty(), "Пользователь %d отправил фото:");
        properties.setProperty(PropertiesKeys.APANEL_MSG_TO_ALL_INSTRUCTIONS.getProperty(), """
                🔴ВНИМАНИЕ🔴
                ⤷ Ваше следующее сообщение, которое вы напишите в этот чат,
                  будет отправлено всем пользователям бота.
                                
                ⤷ Это так же может быть фотография с или без подписи.
                                
                ⤷ Если вы хотите отменить это действие, нажмите кнопку ниже.
                """);
        properties.setProperty(PropertiesKeys.APANEL_ADD_TOKENS_INSTRUCTIONS.getProperty(), """
                🔴ВНИМАНИЕ🔴
                ⤷ Ваше следующее сообщение, которое вы напишите в этот чат,
                  будет использовано для добавления токенов пользователю.
                  
                ⤷ Введите айди пользователя количество токенов, индикатор(true/false) будут ли токены добавлены к текущему значению, или установлены от нуля, индикатор(true/false) будет ли пользователь VIP после добавления.
                
                ⤷ Пример ввода: 123123 15000 true true
                
                ⤷ Если вы хотите отменить это действие, нажмите кнопку ниже.
                """);
        properties.setProperty(PropertiesKeys.APANEL_USER_INFO_INSTRUCTIONS.getProperty(), """
                🔴ВНИМАНИЕ🔴
                ⤷ Ваше следующее сообщение, которое вы напишите в этот чат,
                  будет использовано для получения информации про пользователя.
                  
                ⤷ Введите айди пользователя.
                
                ⤷ Пример ввода: 123123
                
                ⤷ Если вы хотите отменить это действие, нажмите кнопку ниже.
                """);
        properties.setProperty(PropertiesKeys.APANEL_USER_INFO.getProperty(), """
                Информация про %d:
                ⤷ Айди: %d
                ⤷ Чат айди: %d
                ⤷ Юзернейм: %s
                ⤷ Количество токенов: %d
                ⤷ Вип: %b
                """);

        // settings
        properties.setProperty(PropertiesKeys.SETTINGS_CHOOSE_LANGUAGE.getProperty(), "Выберите язык: ");
        properties.setProperty(PropertiesKeys.SETTINGS_LANGUAGE_CHANGED.getProperty(), "Язык изменён на %s");
        properties.setProperty(PropertiesKeys.SETTINGS_MODEL_CHANGED.getProperty(), "Модель изменена на %s");
        properties.setProperty(PropertiesKeys.SETTINGS_LANGUAGE_TITLE.getProperty(), "RU");
        properties.setProperty(PropertiesKeys.SETTINGS_GPT3_BUTTON_TITLE.getProperty(), "🌟GPT-3.5🌟");
        properties.setProperty(PropertiesKeys.SETTINGS_GPT4_BUTTON_TITLE.getProperty(), "⚡GPT-4⚡");
        properties.setProperty(PropertiesKeys.SETTINGS_TITLE.getProperty(), "⚙️ Настройки");
        properties.setProperty(PropertiesKeys.SETTINGS_AI_MODEL_BUTTON_TITLE.getProperty(), "\uD83E\uDDE0 Модель GPT");
        properties.setProperty(PropertiesKeys.SETTINGS_LANGUAGE_BUTTON_TITLE.getProperty(), "\uD83C\uDF10️ Язык");
        properties.setProperty(PropertiesKeys.SETTINGS_AI_MODELS.getProperty(), """
                GPT-3.5 - это хорошо известная модель, которая в 5 раз лучше, чем модель, используемая на бесплатном веб-сайте ChatGPT. Она быстрая и бесплатная. Идеально подходит для повседневных задач. Если есть задачи, с которыми она не справляется, попробуйте GPT-4.
                                
                💡 Примечание: GPT-3.5 - это бесплатная модель, в отличие от GPT-4, за который вам потребуются токены с вашего баланса.
                                
                🟣 Модель ChatGPT по умолчанию:
                ⤷
                🟢⚪⚪⚪⚪️ - Умная
                                
                🟢🟢⚪⚪⚪️ - Быстрая
                                
                🟢🟢🟢🟢🟢 - Дешевая
                                
                🟣 GPT-3.5:
                ⤷
                🟢🟢🟢⚪⚪️ - Умная
                                
                🟢🟢🟢⚪⚪ - Быстрая
                                
                🟢🟢🟢🟢🟢 - Дешевая
                                
                🟣 GPT-4:
                ⤷
                🟢🟢🟢🟢🟢 - Умная
                                
                🟢🟢🟢🟢🟢 - Быстрая
                                
                🟢🟢⚪️⚪️⚪️ - Дешевая
                                
                Выберите модель:
                """);


        // Chat
        properties.setProperty(PropertiesKeys.CHAT_REQUEST_WAITING.getProperty(), "\uD83E\uDDE0ChatGPT генерирует ответ...");
        properties.setProperty(PropertiesKeys.CHAT_END_CHAT_BUTTON_TITLE.getProperty(), "Завершить чат \uD83D\uDCA7");
        properties.setProperty(PropertiesKeys.CHAT_START_NEW_BUTTON_TITLE.getProperty(), "Начать новый чат \uD83D\uDD25");
        properties.setProperty(PropertiesKeys.CHAT_START_GPT_CHAT.getProperty(), """
                Вы начали чат с моделью:
                %s
                
                ⚪️ Изменить модель:
                   ⤷ /menu - Настройки
                """);

        // Purchase
        properties.setProperty(PropertiesKeys.PURCHASE_MINIMAL_VALUE_BUTTON_TITLE.getProperty(), "\uD83D\uDFE3+25К токенов - 119 UAH (-40%)");
        properties.setProperty(PropertiesKeys.PURCHASE_MEDIUM_VALUE_BUTTON_TITLE.getProperty(), "\uD83D\uDFE3+50К токенов - 199 UAH (-50%)");
        properties.setProperty(PropertiesKeys.PURCHASE_MAXIMUM_VALUE_BUTTON_TITLE.getProperty(), "\uD83D\uDFE3+100К токенов - 319 UAH (-80%)");
        properties.setProperty(PropertiesKeys.PURCHASE_BUY_TOKENS_BUTTON_TITLE.getProperty(), "\uD83E\uDD51Купить токены");
        properties.setProperty(PropertiesKeys.PURCHASE_FINAL_MSG.getProperty(), """
                ⚪ Отправьте %d UAH на данную карту:
                ‼️MonoBank‼️→ 4441 1144 6601 4014
                    
                ⚪ После оплаты отправьте скриншот успешного перевода в чат и ожидайте начисления в течение 10 минут.
                    
                🔴 Если будет произведена оплата, но скриншот не будет отправлен, токены не будут зачислены.
                """);

        return properties;
    }

    private static Properties initEnMsgDefaultProperties()
    {
        Properties properties = new Properties();

        // Errors
        properties.setProperty(PropertiesKeys.ERROR_NOT_A_TXT_FILE.getProperty(), "Error! The file must be in .txt format.");
        properties.setProperty(PropertiesKeys.ERROR_NOT_IN_MAIN_MENU.getProperty(), "You must end the chat with GPT in order to perform this action.");
        properties.setProperty(PropertiesKeys.ERROR_NOT_IN_CHAT.getProperty(), "To ask a question to the bot, use the command: /startchat");
        properties.setProperty(PropertiesKeys.ERROR_INCORRECT_INPUT.getProperty(), "Error! Incorrect input format. You can either send a message to the bot or send a txt file for it to read.");
        properties.setProperty(PropertiesKeys.ERROR_ADMIN_MODE_PARSE.getProperty(), "Parsing error");
        properties.setProperty(PropertiesKeys.ERROR_NOT_PREMIUM_ACC.getProperty(), """
                ❌ GPT-4 is a Premium model!
                                
                ⚪ To use it, you need to have Premium status
                ⤷ /menu -> Tokens balance
                """);
        properties.setProperty(PropertiesKeys.ERROR_NOT_ENOUGH_TOKENS.getProperty(), """
                You need to have more than 300 tokens on your balance to use GPT-4 🙁
                Please choose another model or top up your balance.
                ⚪ Top up tokens - /balance
                """);
        properties.setProperty(PropertiesKeys.ERROR_REQUEST_ERROR.getProperty(), """
                Error while trying to receive a response from OpenAI.
                Possible reasons:
                ⤷ Too many concurrent requests.
                ⤷ OpenAI server overload.
                Solution:
                ⤷ Use the /startchat command and retry your request.
                """);
        properties.setProperty(PropertiesKeys.ERROR_NULL_BALANCE.getProperty(), """
                Your balance doesn't have enough tokens for chatting with GPT-4!
                To replenish your tokens:
                ⤷ /menu - Balance
                """);

        //menu
        properties.setProperty(PropertiesKeys.MENU_RETURN_TO_MENU_BUTTON_TITLE.getProperty(), "Back to menu↩️");
        properties.setProperty(PropertiesKeys.MENU_HELP_BUTTON_TITLE.getProperty(), "\uD83D\uDCA1 Help");
        properties.setProperty(PropertiesKeys.MENU_SETTINGS_BUTTON_TITLE.getProperty(), "⚙️ Settings");
        properties.setProperty(PropertiesKeys.MENU_BALANCE_BUTTON_TITLE.getProperty(), "\uD83C\uDF49 Tokens balance");
        properties.setProperty(PropertiesKeys.MENU_TITLE.getProperty(), "\uD83C\uDFE0 Menu:");
        properties.setProperty(PropertiesKeys.MENU_RETURNED_TO_MAIN_MENU.getProperty(), "You have been returned to the main menu.");
        properties.setProperty(PropertiesKeys.MENU_PRODUCT_LIST.getProperty(), """
                💌 How many tokens would you like to purchase?
                    
                ⚡ Hint:
                ⚪ 50K tokens ≈ 300 responses from GPT-4
                    
                ‼️ Payment only via Ukrainian cards, for payment in other currency contact our support - /help ‼️
                """);
        properties.setProperty(PropertiesKeys.MENU_VIP_BALANCE.getProperty(), """
                GPT-4:
                ⚪VIP Status:
                ⤷ ✅Activated
                                
                🟤Balance
                ⤷ You have %d💰 GPT-4 tokens
                                
                🍉VIP Status grants you
                ⤷ Access to GPT-4
                ⤷ Input using voice messages
                ⤷ Input using text files
                                
                               
                Token Purchase:
                """);

        properties.setProperty(PropertiesKeys.MENU_DEFAULT_BALANCE.getProperty(), """
                GPT-4:
                ⚪VIP Status:
                ⤷ ❌Deactivated
                                
                🟤Balance
                ⤷ You have %d💰 GPT-4 tokens
                                
                💡 Note: VIP Status can be obtained by purchasing
                any amount of tokens (press the button below)
                                
                🍉VIP Status grants you
                ⤷ Access to GPT-4
                ⤷ Input using voice messages
                ⤷ Input using text files
                                
                               
                Token Purchase:
                """);
        properties.setProperty(PropertiesKeys.MENU_START_1.getProperty(), """
                Hello! 😄 I'm the most advanced Artificial Intelligence in the world (created by OpenAI)! 🌟
                       
                I'm happy to assist you with any task, whether it's 💻 coding, debugging, or explaining code! 💡 I can compose emails, 📧 write blog posts on any topic, or even help with homework! 📚
                       
                Need colorful poems or catchy songs? 🎵 No problem, I can create them for you! And if you need a personal tutor, I'm ready to help you with any subject. 🎓
                       
                And if you just want to chat, I'm always available for interesting and pleasant conversations! 🗣️ Whatever you wish for, I'm here to fulfill it! 🌟
                """);
        properties.setProperty(PropertiesKeys.MENU_START_2.getProperty(), """
                Before we begin, there are 2 important things to know: ✌️
                
                1. You can change the interface language in the settings!
                   ⤷ /menu - Settings
                
                2. If you need additional information, simply send me the command:
                   ⤷ /menu - Help
                """);
        properties.setProperty(PropertiesKeys.MENU_HELP.getProperty(), """
                💬 Contact us here for all important inquiries 💬
                💻 Support: @lavviku 🌟
                
                
                Quick guide to using the bot:
                
                • The list of commands is located on the left side of the message input field. To input a command, simply click on the command you're interested in.
                • "Navigation menu" is a selection of actions similar to choosing a command, but it's a dynamic list that appears when you need to make a choice. Usually, the list appears right below the input field, but if it doesn't, you can activate it by clicking the button to the right of the input field.
                
                Steps to interact with the bot:
                
                1. To ask the bot a question, use the command /startchat. By default, the free model GPT3.5 is used; you can change it in the settings:
                   ⤷ /menu - Settings
                   
                3. Then send a message to the bot with your question of interest.
                
                4. Wait for a response to your request (generation takes up to 3 minutes).
                
                5. If you want to switch to another chat model, select the option "Start a new chat" in the navigation menu. Then follow the steps, starting from step number 3.
                
                Important note 1:
                • Access to the GPT-3.5 model is unlimited, and you can use this mode without spending tokens (for free). To use GPT-4, you need to have VIP status and tokens in your balance. Token purchase:
                   ⤷ /menu - Balance
                
                Important note 2:
                • The longer the dialogue, the more tokens are used at once. So be economical and start a new chat after each question if you don't plan to have an extended conversation with the bot.
                """);



        //apanel
        properties.setProperty(PropertiesKeys.APANEL_TITLE.getProperty(), "\uD83D\uDEE1️ Admin panel");
        properties.setProperty(PropertiesKeys.APANEL_MSG_TO_ALL_BUTTON_TITLE.getProperty(), "\uD83D\uDCE3 Msg to all");
        properties.setProperty(PropertiesKeys.APANEL_CANCEL_ACTION_BUTTON_TITLE.getProperty(), "❌ Cancel action");
        properties.setProperty(PropertiesKeys.APANEL_ADD_TOKENS_BUTTON_TITLE.getProperty(), "\uD83D\uDCBC Add tokens");
        properties.setProperty(PropertiesKeys.APANEL_USER_INFO_BUTTON_TITLE.getProperty(), "💡 Info about user");
        properties.setProperty(PropertiesKeys.APANEL_ADD_TOKENS_INSTRUCTIONS.getProperty(), "Enter the username of the user and the number of tokens in the format: (username amount)");
        properties.setProperty(PropertiesKeys.APANEL_USER_NOT_FOUND.getProperty(), "User not found");
        properties.setProperty(PropertiesKeys.APANEL_TOKENS_ADDED.getProperty(), "Tokens have been successfully added to the balance of the specified user");
        properties.setProperty(PropertiesKeys.APANEL_USER_SEND_PHOTO.getProperty(), "User %d sent a photo:");
        properties.setProperty(PropertiesKeys.APANEL_MSG_TO_ALL_INSTRUCTIONS.getProperty(), """
                🔴ATTENTION🔴
                ⤷ Your next message that you write in this chat
                  will be sent to all users of the bot.
                                
                ⤷ This can also be a photo with or without a caption.
                                
                ⤷ If you want to cancel this action, press the button below.
                """);
        properties.setProperty(PropertiesKeys.APANEL_ADD_TOKENS_INSTRUCTIONS.getProperty(), """
                🔴ATTENTION🔴
                ⤷ Your next message that you write in this chat will be used for adding tokens to the user.
                
                ⤷ Enter the user's ID, the number of tokens, an indicator (true/false) whether the tokens will be added to the current value or set from zero, an indicator (true/false) whether the user will be VIP after the addition.
                
                ⤷ Input example: 123123 15000 true true
                
                ⤷ If you want to cancel this action, press the button below.
                """);
        properties.setProperty(PropertiesKeys.APANEL_USER_INFO_INSTRUCTIONS.getProperty(), """
                🔴ATTENTION🔴
                ⤷ Your next message that you write in this chat
                  will be used to obtain information about the user.
                  
                ⤷ Enter the user ID.
                
                ⤷ Input example: 123123
                
                ⤷ If you want to cancel this action, press the button below.
                """);
        properties.setProperty(PropertiesKeys.APANEL_USER_INFO.getProperty(), """
                Information about %d:
                ⤷ ID: %d
                ⤷ Chat ID: %d
                ⤷ Username: %s
                ⤷ Number of tokens: %d
                ⤷ VIP: %b
                """);



        // settings
        properties.setProperty(PropertiesKeys.SETTINGS_CHOOSE_LANGUAGE.getProperty(), "Select language: ");
        properties.setProperty(PropertiesKeys.SETTINGS_LANGUAGE_CHANGED.getProperty(), "Language changed to %s");
        properties.setProperty(PropertiesKeys.SETTINGS_MODEL_CHANGED.getProperty(), "Model changed to %s");
        properties.setProperty(PropertiesKeys.SETTINGS_LANGUAGE_TITLE.getProperty(), "EN");
        properties.setProperty(PropertiesKeys.SETTINGS_GPT3_BUTTON_TITLE.getProperty(), "🌟GPT-3.5🌟");
        properties.setProperty(PropertiesKeys.SETTINGS_GPT4_BUTTON_TITLE.getProperty(), "⚡GPT-4⚡");
        properties.setProperty(PropertiesKeys.SETTINGS_TITLE.getProperty(), "⚙️ Settings");
        properties.setProperty(PropertiesKeys.SETTINGS_AI_MODEL_BUTTON_TITLE.getProperty(), "\uD83E\uDDE0 GPT Model");
        properties.setProperty(PropertiesKeys.SETTINGS_LANGUAGE_BUTTON_TITLE.getProperty(), "\uD83C\uDF10️ Language");
        properties.setProperty(PropertiesKeys.SETTINGS_AI_MODELS.getProperty(), """
                GPT-3.5 is that well-known model, 5 times better than the model used on the free ChatGPT website. It's fast and free. Ideal for everyday tasks. If there are some tasks it can't handle, try the GPT-4
                                
                💡 Note: GPT-3.5 is a free model, unlike GPT-4, for which you'll need to pay tokens from your balance.
                    
                🟣Default ChatGPT:
                 ⤷
                🟢⚪⚪⚪⚪️ – Smart
                                
                🟢🟢⚪⚪⚪️ – Fast
                                
                🟢🟢🟢🟢🟢 – Cheap
                                
                         
                🟣GPT-3.5:
                 ⤷
                🟢🟢🟢⚪⚪️ – Smart
                                
                🟢🟢🟢⚪⚪ – Fast
                                
                🟢🟢🟢🟢🟢 – Cheap
                                
                           
                🟣GPT-4:
                 ⤷
                🟢🟢🟢🟢🟢 – Smart
                                
                🟢🟢🟢🟢🟢 – Fast
                                
                🟢🟢⚪️⚪️⚪️ – Cheap
                                
                Select model:
                """);

        // Chat
        properties.setProperty(PropertiesKeys.CHAT_REQUEST_WAITING.getProperty(), "\uD83E\uDDE0ChatGPT is generating a response...");
        properties.setProperty(PropertiesKeys.CHAT_END_CHAT_BUTTON_TITLE.getProperty(), "End chat \uD83D\uDCA7");
        properties.setProperty(PropertiesKeys.CHAT_START_NEW_BUTTON_TITLE.getProperty(), "Start new chat \uD83D\uDD25");
        properties.setProperty(PropertiesKeys.CHAT_START_GPT_CHAT.getProperty(), """
                You've started a chat with model:
                %s
                
                ⚪️ Change the model:
                   ⤷ /menu - Settings
                """);


        // Purchase
        properties.setProperty(PropertiesKeys.PURCHASE_MINIMAL_VALUE_BUTTON_TITLE.getProperty(), "\uD83D\uDFE3+25K tokens - 119 UAH (-40%)");
        properties.setProperty(PropertiesKeys.PURCHASE_MEDIUM_VALUE_BUTTON_TITLE.getProperty(), "\uD83D\uDFE3+50K tokens - 199 UAH (-50%)");
        properties.setProperty(PropertiesKeys.PURCHASE_MAXIMUM_VALUE_BUTTON_TITLE.getProperty(), "\uD83D\uDFE3+100K tokens - 319 UAH (-80%)");
        properties.setProperty(PropertiesKeys.PURCHASE_BUY_TOKENS_BUTTON_TITLE.getProperty(), "\uD83E\uDD51Buy Tokens");
        properties.setProperty(PropertiesKeys.PURCHASE_FINAL_MSG.getProperty(), """
                ⚪ Send %d UAH to this card:
                ‼️MonoBank‼️→ 4441 1144 6601 4014
                    
                ⚪ After payment, send a screenshot of the successful transfer to the chat and wait for the tokens to be credited within 10 minutes.
                    
                🔴 If payment is made but no screenshot is sent, the tokens will not be credited.
                """);

        return properties;
    }

    private static void storeProperties(Path path, Properties properties)
    {
        try (FileWriter fileWriter = new FileWriter(path.toString()))
        {
            properties.store(fileWriter, "Config file");
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

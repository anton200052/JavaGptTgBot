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
        return properties;
    }

    public static Properties initConfigProperties() throws NotValidConfigDataException
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


            System.out.println("If you want to change or add anything. For example, to change the username or token of the bot,\njust open the config.properties file through any text editor in the root directory of the jar file");
        }
        catch (IOException e)
        {
            throw new NotValidConfigDataException("Откройте файл config.properties и замените слова empty соответствующими данными для каждого поля. Если вы повторно видите это сообщение тогда удалите файл и заполните его заново");
        }
        return properties;
    }

    private static Properties initRuMsgDefaultProperties()
    {
        Properties properties = new Properties();

        // Errors
        properties.setProperty(PropertiesKeys.ERROR_NOT_A_TXT_FILE.getProperty(), "Ошибка! Файл должен быть формата .txt");
        properties.setProperty(PropertiesKeys.ERROR_NOT_IN_MAIN_MENU.getProperty(), "Вы должны завершить чат с GPT чтобы совершить это действие");
        properties.setProperty(PropertiesKeys.ERROR_INCORRECT_MODEL.getProperty(), "Выбрана неправильная модель, пожалуйста выберите одну из моделей предложенных в навигационном меню");
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
                - Слишком большое количество одновременных запросов.
                - Перегрузка серверов OpenAI.
                Решение:
                - Используйте команду /startchat и повторите свой запрос.
                   
                """);
        properties.setProperty(PropertiesKeys.ERROR_USERNAME_NOT_AVAILABLE.getProperty(), """
                Для того, чтобы использовать Telegram-бота, добавьте его юзернейм или сделайте его публичным в настройках Telegram.
                """);

        // menu
        properties.setProperty(PropertiesKeys.MENU_RETURN_TO_MENU_BUTTON_TITLE.getProperty(), "Назад в меню↩️");
        properties.setProperty(PropertiesKeys.MENU_HELP_BUTTON_TITLE.getProperty(), "\uD83D\uDCA1 Помощь");
        properties.setProperty(PropertiesKeys.MENU_BALANCE_BUTTON_TITLE.getProperty(), "\uD83C\uDF49 Баланс токенов");
        properties.setProperty(PropertiesKeys.MENU_SETTINGS_BUTTON_TITLE.getProperty(), "⚙️ Настройки");
        properties.setProperty(PropertiesKeys.MENU_TITLE.getProperty(), "\uD83C\uDFE0 Меню:");
        properties.setProperty(PropertiesKeys.MENU_START_GPT_CHAT.getProperty(), "Вы успешно начали чат с моделью GPT");
        properties.setProperty(PropertiesKeys.MENU_RETURNED_TO_MAIN_MENU.getProperty(), "Вы вернулись в главное меню.");
        properties.setProperty(PropertiesKeys.MENU_PRODUCT_LIST.getProperty(), """
                💌 Сколько токенов вы хотите приобрести?

                ⚡ Подсказка:
                ⚪ 50 тыс. токенов ≈ 300 ответов от GPT-4

                ‼️ Оплата только через украинские карты, для оплаты в другой валюте обратитесь в нашу поддержку - /help ‼️
                """);
        properties.setProperty(PropertiesKeys.MENU_PREMIUM_BALANCE.getProperty(), """
                GPT-4:
                🔴Премиум статус:
                ⤷ ✅Активирован
                
                🟤Баланс
                ⤷ У вас %d💰 GPT-4 токенов
                
                🍉Премиум статус вам даёт
                ⤷ Доступ к GPT-4
                ⤷ Ввод с помощью голосовых сообщений
                ⤷ Ввод с помощью текстовых файлов
                
               
                Покупка токенов:
                """);

        properties.setProperty(PropertiesKeys.MENU_DEFAULT_BALANCE.getProperty(), """
                GPT-4:
                🔴Премиум статус:
                ⤷ ❌Дективирован
                
                🟤Баланс
                ⤷ У вас %d💰 GPT-4 токенов
                
                💡 Примечание: Премиум статус можно получить посредством покупки
                любого количества токенов (нажмите кнопку снизу)
                
                🍉Премиум статус вам даёт
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
                                
                1. Вы можете общаться со мной на любом языке. Но я наиболее эффективно работаю на 🇬🇧 английском языке! 💬💪
                                
                2. Если вам нужна дополнительная информация, просто отправьте мне команду /help, и я буду рад вам помочь! 🤗
                                
                👩🏼‍💻 Поддержка: @lavviku 🌟
                """);

        properties.setProperty(PropertiesKeys.MENU_HELP.getProperty(), """
                💬 Свяжитесь с нами здесь по всем важным вопросам 💬
                💻 Поддержка: (имя_пользователя) 🌟
                
                
                Краткое руководство по использованию бота:
                                
                • Список команд расположен на левой стороне поля ввода сообщения. Чтобы ввести команду, просто нажмите на интересующую вас команду.
                • "Навигационное меню" - это выбор действия, аналогичный выбору команды, но это динамический список, который появляется, когда вам нужно что-то выбрать. Обычно список появляется прямо под полем ввода, но если этого не происходит, вы можете активировать его, нажав кнопку справа от поля ввода.
                                
                Шаги взаимодействия с ботом:
                                
                1. Чтобы задать вопрос боту, используйте команду /startchat.
                2. Затем выберите желаемую модель чата из навигационного меню.
                3. После выбора модели просто отправьте сообщение боту с вашим интересующим вопросом.
                4. Дождитесь ответа на ваш запрос (генерация занимает до 3 минут).
                5. Если вы хотите переключиться на другую модель чата, выберите опцию "Начать новый чат" в навигационном меню. Затем следуйте шагам, начиная с пункта номер 3.
                                
                Важное примечание 1:
                • Доступ к модели GPT-3.5 неограничен, и вы можете использовать этот режим без расходования токенов. Для использования GPT-4 вам нужно иметь более 300 токенов на балансе. Проверьте баланс / пополните токены - /balance
                                
                Важное примечание 2:
                • Чем длиннее диалог, тем больше токенов используется сразу. Так что будьте экономны и начните новый чат после каждого вопроса, если вы не планируете вести диалог с ботом.
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


        // Purchase
        properties.setProperty(PropertiesKeys.PURCHASE_MINIMAL_VALUE_BUTTON_TITLE.getProperty(), "\uD83D\uDFE3+25К токенов - 119 UAH (-40%)");
        properties.setProperty(PropertiesKeys.PURCHASE_MEDIUM_VALUE_BUTTON_TITLE.getProperty(), "\uD83D\uDFE3+50К токенов - 199 UAH (-50%)");
        properties.setProperty(PropertiesKeys.PURCHASE_MAXIMUM_VALUE_BUTTON_TITLE.getProperty(), "\uD83D\uDFE3+100К токенов - 319 UAH (-80%)");
        properties.setProperty(PropertiesKeys.PURCHASE_BUY_TOKENS_BUTTON_TITLE.getProperty(), "\uD83E\uDD51Купить токены");
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

    private static Properties initEnMsgDefaultProperties()
    {
        Properties properties = new Properties();

        // Errors
        properties.setProperty(PropertiesKeys.ERROR_NOT_A_TXT_FILE.getProperty(), "Error! The file must be in .txt format.");
        properties.setProperty(PropertiesKeys.ERROR_NOT_IN_MAIN_MENU.getProperty(), "You must end the chat with GPT in order to perform this action.");
        properties.setProperty(PropertiesKeys.ERROR_INCORRECT_MODEL.getProperty(), "Incorrect model selected. Please choose one of the models provided in the navigation menu.");
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
                - Too many concurrent requests.
                - OpenAI server overload.
                Solution:
                - Use the /startchat command and retry your request.
                """);
        properties.setProperty(PropertiesKeys.ERROR_USERNAME_NOT_AVAILABLE.getProperty(), """
                To use the Telegram bot, add its username or make it public in your Telegram settings.
                """);

        //menu
        properties.setProperty(PropertiesKeys.MENU_RETURN_TO_MENU_BUTTON_TITLE.getProperty(), "Back to menu↩️");
        properties.setProperty(PropertiesKeys.MENU_HELP_BUTTON_TITLE.getProperty(), "\uD83D\uDCA1 Help");
        properties.setProperty(PropertiesKeys.MENU_SETTINGS_BUTTON_TITLE.getProperty(), "⚙️ Settings");
        properties.setProperty(PropertiesKeys.MENU_BALANCE_BUTTON_TITLE.getProperty(), "\uD83C\uDF49 Tokens balance");
        properties.setProperty(PropertiesKeys.MENU_TITLE.getProperty(), "\uD83C\uDFE0 Menu:");
        properties.setProperty(PropertiesKeys.MENU_START_GPT_CHAT.getProperty(), "You have successfully started a chat with the GPT model.");
        properties.setProperty(PropertiesKeys.MENU_RETURNED_TO_MAIN_MENU.getProperty(), "You have been returned to the main menu.");
        properties.setProperty(PropertiesKeys.MENU_PRODUCT_LIST.getProperty(), """
                💌 How many tokens would you like to purchase?
                    
                ⚡ Hint:
                ⚪ 50K tokens ≈ 300 responses from GPT-4
                    
                ‼️ Payment only via Ukrainian cards, for payment in other currency contact our support - /help ‼️
                """);
        properties.setProperty(PropertiesKeys.MENU_PREMIUM_BALANCE.getProperty(), """
                GPT-4:
                🔴Premium Status:
                ⤷ ✅Activated
                
                🟤Balance
                ⤷ You have %d💰 GPT-4 tokens
                
                🍉Premium Status grants you
                ⤷ Access to GPT-4
                ⤷ Input using voice messages
                ⤷ Input using text files
                
               
                Token Purchase:
                """);

        properties.setProperty(PropertiesKeys.MENU_DEFAULT_BALANCE.getProperty(), """
                GPT-4:
                🔴Premium Status:
                ⤷ ❌Deactivated
                
                🟤Balance
                ⤷ You have %d💰 GPT-4 tokens
                
                💡 Note: Premium Status can be obtained by purchasing
                any amount of tokens (press the button below)
                
                🍉Premium Status grants you
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
                        
                1. You can communicate with me in any language. But I work most effectively in 🇬🇧 English! 💬💪
                        
                2. If you need additional information, simply send me the /help command, and I'll be happy to assist you! 🤗
                        
                👩🏼‍💻 Support: @lavviku 🌟
                """);
        properties.setProperty(PropertiesKeys.MENU_HELP.getProperty(), """
                Contact us here for all important questions 💬
                💻 Support: (username) 🌟
                
                
                Quick guide to using the bot:
                        
                • The list of commands is located on the left side of the message input field. To enter a command, simply click on the command you are interested in.
                • "Navigation menu" is a choice of action, similar to selecting a command, but it's a dynamic list that appears when you need to press something. Usually, the list appears right under the input field, but if it doesn't, you can activate it by clicking the button to the right of the input field.
                        
                Steps to interact with the bot:
                        
                1. To ask a question to the bot, use the /startchat command.
                2. Next, choose the desired chat model from the navigation menu.
                3. After selecting the model, simply send a message to the bot with your question of interest.
                4. Wait for a response to your request (generation takes up to 3 minutes).
                5. If you want to switch chat models, choose the "Start a new chat" option in the navigation menu. Then follow the steps starting from point number 3.
                        
                Important Note 1:
                • Access to the GPT-3.5 model is unlimited, and you can use this mode without spending tokens. To use GPT-4, you need to have more than 300 tokens on your balance. Check balance/top up tokens - /balance
                        
                Important Note 2:
                • The longer the dialogue, the more tokens are used at once. So be economical and start a new chat after each question if you're not planning to have a dialogue with the bot.
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


        // Purchase
        properties.setProperty(PropertiesKeys.PURCHASE_MINIMAL_VALUE_BUTTON_TITLE.getProperty(), "\uD83D\uDFE3+25K tokens - 119 UAH (-40%)");
        properties.setProperty(PropertiesKeys.PURCHASE_MEDIUM_VALUE_BUTTON_TITLE.getProperty(), "\uD83D\uDFE3+50K tokens - 199 UAH (-50%)");
        properties.setProperty(PropertiesKeys.PURCHASE_MAXIMUM_VALUE_BUTTON_TITLE.getProperty(), "\uD83D\uDFE3+100K tokens - 319 UAH (-80%)");
        properties.setProperty(PropertiesKeys.PURCHASE_BUY_TOKENS_BUTTON_TITLE.getProperty(), "\uD83E\uDD51Buy Tokens");
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
        if (configProperties.getProperty(PropertiesKeys.CONFIG_BOT_USERNAME.getProperty()) == null || configProperties.getProperty(PropertiesKeys.CONFIG_BOT_USERNAME.getProperty()).equals("empty") || configProperties.getProperty(PropertiesKeys.CONFIG_BOT_TOKEN.getProperty()) == null || configProperties.getProperty(PropertiesKeys.CONFIG_BOT_TOKEN.getProperty()).equals("empty") || configProperties.getProperty(PropertiesKeys.CONFIG_GPT3_TOKEN.getProperty()) == null || configProperties.getProperty(PropertiesKeys.CONFIG_GPT3_TOKEN.getProperty()).equals("empty") || configProperties.getProperty(PropertiesKeys.CONFIG_GPT4_TOKEN.getProperty()) == null || configProperties.getProperty(PropertiesKeys.CONFIG_GPT4_TOKEN.getProperty()).equals("empty") || configProperties.getProperty(PropertiesKeys.CONFIG_ADMINS_ID.getProperty()) == null || configProperties.getProperty(PropertiesKeys.CONFIG_ADMINS_ID.getProperty()).equals("empty"))
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

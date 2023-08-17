package me.vasylkov.bot;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

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

    public static void loadMsgBundles()
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
        properties.setProperty("tgBot.username", "empty");
        properties.setProperty("tgBot.token", "empty");
        properties.setProperty("openAiGpt3.token", "empty");
        properties.setProperty("openAiGpt4.token", "empty");
        return properties;
    }

    private static Properties initRuMsgDefaultProperties()
    {
        Properties properties = new Properties();


        //errors
        properties.setProperty("error.notATxtFile", "Ошибка! Файл должен быть формата .txt");
        properties.setProperty("error.notInMainMenu", "Вы должны завершить чат чтобы использовать данную команду.");
        properties.setProperty("error.incorrectModel", "Выбрана неправильная модель, пожалуйста выберите одну из моделей предложенных в навигационном меню");
        properties.setProperty("error.notInChat", "Чтобы задать вопрос боту используйте команду: /startchat");
        properties.setProperty("error.incorrectInput", "Ошибка! Неправильный формат ввода. Вы можете либо написать сообщение боту, либо отправить ему txt файл и он прочитает из него.");
        properties.setProperty("error.adminModeParse", "Ошибка при парсинге");
        properties.setProperty("error.notEnoughTokens", """
                На вашем балансе должно быть больше чем 300 токенов для того чтобы использовать GPT-4 🙁
                Пожалуйста выберите другую модель либо пополните свой баланс.
                ⚪ Пополнение токенов - /balance
                """);
        properties.setProperty("error.requestError", """
                Ошибка при попытке получить ответ от OpenAI.
                Возможные причины:
                - Слишком большое количество одновременных запросов.
                - Перегрузка серверов OpenAI.
                Решение:
                - Используйте команду /startchat и повторите свой запрос.
           
                """);
        properties.setProperty("error.usernameNotAval", """
                Для того, чтобы использовать Telegram-бота, добавьте его юзернейм или сделайте его публичным в настройках Telegram.
                """);


        //menu
        properties.setProperty("menu.modelChoose", "Выберите модель чата в навигационном меню.\nGPT-3.5: Бесплатно");
        properties.setProperty("menu.startedGpt3Chat", "Вы успешно начали чат с моделью GPT-3.5");
        properties.setProperty("menu.startedGpt4Chat", "Вы успешно начали чат с моделью GPT-4. Генерация ответов до 2-х минут");
        properties.setProperty("menu.returnedToMainMenu", "Вы были перемещены в главное меню.");
        properties.setProperty("menu.productList", """
                💌 Сколько токенов вы хотите приобрести?
            
                ⚡ Подсказка:
                ⚪ 50К токенов ≈ 300 ответов от GPT-4
            
                ‼️ Оплата только через укр. карты, для оплаты другой валютой свяжитесь с нашей поддержкой - /help ‼️
                """);
        properties.setProperty("menu.balance", """
                GPT-4:
                ⚪ У вас %d💰токенов на балансе
            
                GPT-3.5:
                ⚪ Безграничное использование 🌟
            
                Покупка токенов:
                """);
        properties.setProperty("menu.start1",  """
                Привет! 😄 Я самый продвинутый Искусственный Интеллект в мире (созданный OpenAI)! 🌟
                       
                Я рад помочь вам с любой задачей, будь то 💻 написание кода, отладка или объяснение кода! 💡 Могу составить электронные письма, 📧 написать блог-посты на любую тему или даже помочь с домашним заданием! 📚
                       
                Нужны красочные стихи или заводные песни? 🎵 Не проблема, я могу создать их для вас! И если вам нужен личный репетитор, то я готов помочь вам в освоении любого предмета. 🎓
                       
                А если вам просто хочется поговорить, я всегда доступен для интересных и приятных бесед! 🗣️ Что бы вы ни хотели, я готов выполнить ваше желание! 🌟
                """);
        properties.setProperty("menu.start2", """
                Прежде чем начать, важно знать 2 вещи: ✌️
                        
                1. Вы можете общаться со мной на любом языке. Но, на 🇬🇧 английском, я работаю наиболее эффективно! 💬💪
                        
                2. Если вам нужна дополнительная информация, просто отправьте мне команду /help, и я с радостью помогу вам! 🤗
                        
                👩🏼‍💻 Support: @lavviku 🌟
                """);
        properties.setProperty("menu.help1", """
                💬 По всем важным вопросам писать сюда 💬
                💻 Support: (username) 🌟
                """);
        properties.setProperty("menu.help2", """
                Краткое руководство по использованию бота:
                        
                • Список команд расположен слева от поля ввода сообщения. Чтобы ввести команду, просто нажмите на интересующую вас команду.
                • “Навигационное меню” - это выбор действия, похожий на выбор команды, но это динамический список и он появляется в моментах, когда вам нужно что-то нажать. Обычно список появляется сам под полем ввода, но если этого не произойдет, его можно активировать, нажав кнопку справа от поля ввода.
                        
                Алгоритм действий для работы с ботом:
                        
                1. Чтобы задать вопрос боту, используйте команду /startchat.
                2. Далее в навигационном меню выберите желаемую модель чата.
                3. После выбора модели просто отправьте сообщение боту с интересующим вас вопросом.
                4. Дождитесь ответа на ваш запрос (генерация занимает до 3-х минут).
                5. Если хотите поменять модель чата, выберите вариант “Начать новый чат” в навигационном меню. Далее действуйте по алгоритму с пункта номер 3.
                        
                Важная заметка 1:
                • Доступ к модели GPT-3.5 является безграничным и вы можете использовать данный режим без траты токенов. Чтобы использовать GPT-4 надо иметь на балансе более 300 токенов. Проверка баланса/пополнение счета - /balance
                
                Важная заметка 2:
                • Чем больше диалог, тем больше за раз используется токенов. Поэтому будь экономным и начинай новый чат после каждого вопроса если не собираешься вести диалог с ботом.
                """);

        //chat
        properties.setProperty("chat.requestWaiting", "\uD83E\uDDE0ChatGPT генерирует ответ...");

        //purchase
        properties.setProperty("purchase.minimalValue", "\uD83D\uDFE3+25К токенов - 119 UAH (-40%)");
        properties.setProperty("purchase.mediumValue", "\uD83D\uDFE3+50К токенов - 199 UAH (-50%)");
        properties.setProperty("purchase.maximumValue", "\uD83D\uDFE3+100К токенов - 319 UAH (-80%)");
        properties.setProperty("purchase.buyTokens", "\uD83E\uDD51Купить токены");
        properties.setProperty("purchase.finalMsg", """
                ⚪ Отправьте %d UAH на данную карту:
                ‼️MonoBank‼️→ (CARD NUMBER)
            
                ⚪ После оплаты отправьте скриншот успешного перевода в чат и ожидайте начисления в течение 10 минут.
            
                🔴 Если будет произведена оплата, но скриншот не будет отправлен, токены не будут зачислены.
                """);

        //admin
        properties.setProperty("admin.printUserData", "Введите юзернейм пользователя и количество токенов в формате: (юзернейм количество)");
        properties.setProperty("admin.userNotFound", "Пользователь не найден");
        properties.setProperty("admin.tokensHasBeAdded", "Токены были успешно зачислены на баланс указанного пользователя");
        properties.setProperty("admin.userSendPhoto", "Пользователь @%s отправил фото:");
        properties.setProperty("admin.sendMessageToAll", "Введите сообщение которое будет отправлено всем пользователям.");


        return properties;
    }

    private static Properties initEnMsgDefaultProperties()
    {
        Properties properties = new Properties();

        //errors
        properties.setProperty("error.notATxtFile", "Error! The file must be in .txt format.");
        properties.setProperty("error.notInMainMenu", "You must exit the chat to use this command.");
        properties.setProperty("error.incorrectModel", "Incorrect model selected. Please choose one of the models provided in the navigation menu.");
        properties.setProperty("error.notInChat", "To ask a question to the bot, use the command: /startchat");
        properties.setProperty("error.incorrectInput", "Error! Incorrect input format. You can either send a message to the bot or send a txt file for it to read.");
        properties.setProperty("error.adminModeParse", "Parsing error");
        properties.setProperty("error.notEnoughTokens", """
        You need to have more than 300 tokens on your balance to use GPT-4 🙁
        Please choose another model or top up your balance.
        ⚪ Top up tokens - /balance
        """);
        properties.setProperty("error.requestError", """
        Error while trying to receive a response from OpenAI.
        Possible reasons:
        - Too many concurrent requests.
        - OpenAI server overload.
        Solution:
        - Use the /startchat command and retry your request.
        """);
        properties.setProperty("error.usernameNotAval", """
        To use the Telegram bot, add its username or make it public in your Telegram settings.
        """);

        //menu
        properties.setProperty("menu.modelChoose", "Choose a chat model in the navigation menu.\nGPT-3.5: Free");
        properties.setProperty("menu.startedGpt3Chat", "You have successfully started a chat with the GPT-3.5 model.");
        properties.setProperty("menu.startedGpt4Chat", "You have successfully started a chat with the GPT-4 model. Response generation takes up to 2 minutes");
        properties.setProperty("menu.returnedToMainMenu", "You have been returned to the main menu.");
        properties.setProperty("menu.productList", """
        💌 How many tokens would you like to purchase?
    
        ⚡ Hint:
        ⚪ 50K tokens ≈ 300 responses from GPT-4
    
        ‼️ Payment only via Ukrainian cards, for payment in other currency contact our support - /help ‼️
        """);
        properties.setProperty("menu.balance", """
        GPT-4:
        ⚪ You have %d💰tokens on your balance
    
        GPT-3.5:
        ⚪ Unlimited usage 🌟
    
        Token purchase:
        """);
        properties.setProperty("menu.start1", """
        Hello! 😄 I'm the most advanced Artificial Intelligence in the world (created by OpenAI)! 🌟
               
        I'm happy to assist you with any task, whether it's 💻 coding, debugging, or explaining code! 💡 I can compose emails, 📧 write blog posts on any topic, or even help with homework! 📚
               
        Need colorful poems or catchy songs? 🎵 No problem, I can create them for you! And if you need a personal tutor, I'm ready to help you with any subject. 🎓
               
        And if you just want to chat, I'm always available for interesting and pleasant conversations! 🗣️ Whatever you wish for, I'm here to fulfill it! 🌟
        """);
        properties.setProperty("menu.start2", """
        Before we begin, there are 2 important things to know: ✌️
                
        1. You can communicate with me in any language. But I work most effectively in 🇬🇧 English! 💬💪
                
        2. If you need additional information, simply send me the /help command, and I'll be happy to assist you! 🤗
                
        👩🏼‍💻 Support: @lavviku 🌟
        """);
        properties.setProperty("menu.help1", """
        💬 Contact us here for all important questions 💬
        💻 Support: (username) 🌟
        """);
        properties.setProperty("menu.help2", """
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

        //chat
        properties.setProperty("chat.requestWaiting", "\uD83E\uDDE0ChatGPT is generating a response...");

        //purchase
        properties.setProperty("purchase.minimalValue", "\uD83D\uDFE3+25K tokens - 119 UAH (-40%)");
        properties.setProperty("purchase.mediumValue", "\uD83D\uDFE3+50K tokens - 199 UAH (-50%)");
        properties.setProperty("purchase.maximumValue", "\uD83D\uDFE3+100K tokens - 319 UAH (-80%)");
        properties.setProperty("purchase.buyTokens", "\uD83E\uDD51Buy Tokens");
        properties.setProperty("purchase.finalMsg", """
        ⚪ Send %d UAH to this card:
        ‼️MonoBank‼️→ (CARD NUMBER)
    
        ⚪ After payment, send a screenshot of the successful transfer to the chat and wait for the tokens to be credited within 10 minutes.
    
        🔴 If payment is made but no screenshot is sent, the tokens will not be credited.
        """);

        //admin
        properties.setProperty("admin.printUserData", "Enter the username of the user and the number of tokens in the format: (username amount)");
        properties.setProperty("admin.userNotFound", "User not found");
        properties.setProperty("admin.tokensHasBeAdded", "Tokens have been successfully added to the balance of the specified user");
        properties.setProperty("admin.userSendPhoto", "User @%s sent a photo:");
        properties.setProperty("admin.sendMessageToAll", "Enter the message that will be sent to all users.");


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

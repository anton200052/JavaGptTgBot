package me.vasylkov.bot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class LogManager
{
    private static final Path pathToLogDir = Path.of("UsersLog");
    public static void createLogDirIfNotExist()
    {
        if (!Files.exists(pathToLogDir))
        {
            try
            {
                Files.createDirectory(pathToLogDir);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public static void createLogFileIfNotExist(TelegramBotUser user)
    {
        Path pathToLogFile = Path.of(pathToLogDir + "/" + user.getId() + "=" + user.getUserName() + ".txt");

        if (!Files.exists(pathToLogFile))
        {
            try
            {
                Files.createFile(pathToLogFile);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public static void writeToLogFile(TelegramBotUser user, String msg)
    {
        Path pathToLogFile = Path.of(pathToLogDir + "/" + user.getId() + "=" + user.getUserName() + ".txt");
        createLogFileIfNotExist(user);

        LocalDateTime localDateTime = LocalDateTime.now();
        try
        {
            Files.writeString(pathToLogFile, localDateTime + " | " + user.getGptModel() + ": " + msg + "\n\n", StandardOpenOption.APPEND);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}

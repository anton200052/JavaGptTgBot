package me.vasylkov.bot;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DataSerializer
{
    private static final Path pathToStorageFile = Path.of("usersData.bin");

    public static void loadStorageFile()
    {
        if (!Files.exists(pathToStorageFile))
        {
            try
            {
                Files.createFile(pathToStorageFile);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public static synchronized void serializeUsersList()
    {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(pathToStorageFile.toString())))
        {
            objectOutputStream.writeObject(TelegramBot.getUsersList());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static synchronized void deserializeUsersList()
    {
        try
        {
            if (Files.size(pathToStorageFile) != 0)
            {
                try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(pathToStorageFile.toString())))
                {
                    TelegramBot.setUsersList((List<TelegramBotUser>) objectInputStream.readObject());
                }
                catch (ClassNotFoundException | IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}

package me.vasylkov.bot;

public class NotValidConfigDataException extends Exception
{
    public NotValidConfigDataException()
    {
        super();
    }

    public NotValidConfigDataException(String message)
    {
        super(message);
    }

    public NotValidConfigDataException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

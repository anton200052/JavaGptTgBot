package me.vasylkov.bot;

public class NotTxtFormatException extends Exception
{
    public NotTxtFormatException()
    {
        super();
    }

    public NotTxtFormatException(String message)
    {
        super(message);
    }

    public NotTxtFormatException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

package me.vasylkov.bot;

import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class TelegramBotUser
{
    private Long chatId;
    private Integer tokensBalance = 0;
    private UserStatus currentStatus = UserStatus.MAIN_MENU;
    private String userName;
    private List<ChatMessage> messageList = new ArrayList<>();

    public TelegramBotUser(String userName, Long chatId)
    {
        this.userName = userName;
        this.chatId = chatId;
    }

    public Integer getTokensBalance()
    {
        return tokensBalance;
    }

    public void setTokensBalance(Integer tokensBalance)
    {
        this.tokensBalance = tokensBalance;
    }

    public Long getChatId()
    {
        return chatId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setCurrentStatus(UserStatus currentStatus)
    {
        this.currentStatus = currentStatus;
    }

    public UserStatus getCurrentStatus()
    {
        return currentStatus;
    }

    public List<ChatMessage> getMessageList()
    {
        return messageList;
    }
}

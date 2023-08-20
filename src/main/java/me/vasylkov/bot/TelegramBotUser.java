package me.vasylkov.bot;

import com.theokanning.openai.completion.chat.ChatMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TelegramBotUser extends User
{
    private Properties msgProperties;
    private final Long chatId;
    private Integer tokensBalance = 0;
    private UserStatus currentStatus = UserStatus.MAIN_MENU;
    private final List<ChatMessage> messageList = new ArrayList<>();

    public TelegramBotUser(Properties msgProperties, Long chatId, Long id, String firstName, Boolean isBot, String lastName, String userName,
                           String languageCode, Boolean canJoinGroups, Boolean canReadAllGroupMessages, Boolean supportInlineQueries,
                           Boolean isPremium, Boolean addedToAttachmentMenu)
    {
        super(id, firstName, isBot, lastName, userName, languageCode, canJoinGroups, canReadAllGroupMessages, supportInlineQueries, isPremium, addedToAttachmentMenu);
        this.chatId = chatId;
        this.msgProperties = msgProperties;
    }


    public Integer getTokensBalance()
    {
        return tokensBalance;
    }

    public Properties getMsgProperties()
    {
        return msgProperties;
    }

    public void setMsgProperties(Properties msgProperties)
    {
        this.msgProperties = msgProperties;
    }

    public void setTokensBalance(Integer tokensBalance)
    {
        this.tokensBalance = tokensBalance;
    }

    public Long getChatId()
    {
        return chatId;
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

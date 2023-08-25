package me.vasylkov.bot;

import com.theokanning.openai.completion.chat.ChatMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TelegramBotUser extends User
{
    private transient Properties msgProperties;
    private final Long chatId;
    private Boolean isPremium;
    private Languages language;
    private Integer tokensBalance = 0;
    private GptModels gptModel;
    private transient UserStatus currentStatus;
    private transient List<ChatMessage> messageList;

    public TelegramBotUser(Long chatId, Long id, String firstName, Boolean isBot, String lastName, String userName, String languageCode, Boolean canJoinGroups, Boolean canReadAllGroupMessages, Boolean supportInlineQueries, Boolean isPremium, Boolean addedToAttachmentMenu)
    {
        super(id, firstName, isBot, lastName, userName, languageCode, canJoinGroups, canReadAllGroupMessages, supportInlineQueries, isPremium, addedToAttachmentMenu);
        this.currentStatus = UserStatus.MAIN_MENU;
        this.messageList = new ArrayList<>();
        gptModel = GptModels.GPT3;
        this.chatId = chatId;
        this.language = Languages.EN;
        this.msgProperties = PropertiesManager.getEnMsgProperties();
        this.isPremium = false;
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();

        this.language = Languages.EN;
        this.msgProperties = PropertiesManager.getEnMsgProperties();
        this.currentStatus = UserStatus.MAIN_MENU;
        this.messageList = new ArrayList<>();
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
    }

    public Integer getTokensBalance()
    {
        return tokensBalance;
    }

    public Properties getMsgProperties()
    {
        return msgProperties;
    }

    public Languages getLanguage()
    {
        return language;
    }

    public void setLanguage(Languages language)
    {
        this.language = language;
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

    public GptModels getGptModel()
    {
        return gptModel;
    }

    public void setGptModel(GptModels gptModel)
    {
        this.gptModel = gptModel;
    }

    public Boolean isPremium()
    {
        return isPremium;
    }
    public List<ChatMessage> getMessageList()
    {
        return messageList;
    }
}

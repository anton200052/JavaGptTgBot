package me.vasylkov.OpenAI;

import com.theokanning.openai.audio.CreateTranscriptionRequest;
import com.theokanning.openai.service.OpenAiService;
import me.vasylkov.bot.PropertiesKeys;
import me.vasylkov.bot.PropertiesManager;

import java.io.File;
import java.time.Duration;

public class SpeechRecognition
{
    private static final String openAIToken = PropertiesManager.getConfigProperties().getProperty(PropertiesKeys.CONFIG_SPEECH_TOKEN.getProperty());
    private static final OpenAiService openAiService = new OpenAiService(openAIToken, Duration.ofSeconds(30));

    public static String recognizeSpeechFromVoiceFile(File file)
    {
        CreateTranscriptionRequest createTranscriptionRequest = CreateTranscriptionRequest.builder()
                .model("whisper-1")
                .build();

        return openAiService.createTranscription(createTranscriptionRequest, file).getText();
    }
}

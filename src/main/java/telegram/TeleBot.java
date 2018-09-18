package telegram;

import logic.LogicModule;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

public class TeleBot extends TelegramLongPollingBot {
    private static TeleBot ourInstance;
    public static TeleBot getInstance() {
        return ourInstance;
    }

    private TeleBot() {

    }

    static {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        ourInstance = new TeleBot();
        try {
            telegramBotsApi.registerBot(ourInstance);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private static Map<Long, TeleConversation> conversations = new HashMap<>();
    private LogicModule logicModule;

    public void setLogicModule(LogicModule logicModule) {
        this.logicModule = logicModule;
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        if (!conversations.containsKey(chatId)) {
            TeleConversation conversation = new TeleConversation(chatId);
            conversations.put(chatId, conversation);
            logicModule.addConversation(conversation);
        }

        TeleConversation conversation = conversations.get(chatId);
        conversation.calculate(update.getMessage().getText());
    }

    @Override
    public String getBotToken() {
        return "681555237:AAETufR7vp6Ju4qEmaqVCPelpS9rZWsr2q4";
    }

    @Override
    public String getBotUsername() {
        return "BestAvitoBot";
    }

}

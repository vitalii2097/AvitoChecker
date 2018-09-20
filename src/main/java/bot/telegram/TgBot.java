package bot.telegram;

import bot.Bot;
import checker.AvitoChecker;
import me.veppev.avitodriver.ProxyList;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.HashMap;
import java.util.Map;

public class TgBot extends Bot {

    private final TeleBot teleBot;
    //TODO вынести прокси
    private static final String Host = "112.78.35.174";
    private static final Integer Port = 4145;
    static final Logger tgBotLogger = LogManager.getLogger(TgBot.class.getSimpleName());

    public TgBot(AvitoChecker avitoChecker) {
        super(avitoChecker);

        ProxyList list = new ProxyList();
        HttpHost proxyServer = new HttpHost("96.244.187.232", 40325);//list.getProxyServer();

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();


        DefaultBotOptions defaultBotOptions = proxyWithoutAuth(proxyServer);

        teleBot = new TeleBot(defaultBotOptions);
        teleBot.setTgBot(this);

        try {
            telegramBotsApi.registerBot(teleBot);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
        System.out.println("телега запустилась!!!!!!");
    }

    private static DefaultBotOptions proxyWithoutAuth(HttpHost host) {
        DefaultBotOptions defaultBotOptions = ApiContext.getInstance(DefaultBotOptions.class);
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setProxy(host)
                .setAuthenticationEnabled(false)
                .build();
        defaultBotOptions.setRequestConfig(requestConfig);

        defaultBotOptions.setProxyHost(host.getHostName());
        defaultBotOptions.setProxyPort(host.getPort());

        return defaultBotOptions;
    }

    void sendMessage(SendMessage sendMessage) {
        try {
            teleBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            tgBotLogger.error("Исключение при отправке {}", sendMessage);
            tgBotLogger.error(e);
            tgBotLogger.info("Попытка отправки сообщения без разметки");
            try {
                sendMessage.enableMarkdown(false);
                teleBot.execute(sendMessage);
            } catch (TelegramApiException ex) {
                tgBotLogger.fatal(ex);
            }
        }
    }

}


class TeleBot extends TelegramLongPollingBot {

    private TgBot tgBot;

    private static Map<Long, TgConversation> conversations = new HashMap<>();

    public TeleBot() {
    }


    public void setTgBot(TgBot tgBot) {
        this.tgBot = tgBot;
    }

    public TeleBot(DefaultBotOptions options) {
        super(options);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage().getText() == null) {
            return;
        }
        long chatId = update.getMessage().getChatId();
        if (!conversations.containsKey(chatId)) {
            TgConversation conversation = new TgConversation(tgBot, chatId);
            conversations.put(chatId, conversation);
            tgBot.addConversation(conversation);
        }

        TgConversation conversation = conversations.get(chatId);
        conversation.notifyAboutNewMessage(update.getMessage().getText());
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

package bot.telegram;

import bot.Bot;
import checker.AvitoChecker;
import me.veppev.avitodriver.ProxyList;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.util.HashMap;
import java.util.Map;

import static logic.Conversation.conversationLogger;

public class TgBot extends Bot {

    private final TeleBot teleBot;
    //TODO вынести прокси
    static final Logger tgBotLogger = LogManager.getLogger(TgBot.class.getSimpleName());

    public TgBot(AvitoChecker avitoChecker, HttpHost proxyServer) {
        super(avitoChecker);

        ProxyList list = new ProxyList();

        String log = "telegram";
        String pass = "telegram";

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();



        if (proxyServer != null) {
            DefaultBotOptions defaultBotOptions = proxyWithoutAuth(proxyServer);
            teleBot = new TeleBot(defaultBotOptions);
        } else {
            teleBot = new TeleBot();
        }
        teleBot.setTgBot(this);

        try {
            telegramBotsApi.registerBot(teleBot);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
        System.out.println("телега запустилась!!!!!!");
    }

    private static DefaultBotOptions proxyWithAuth(HttpHost host, String login, String password) {
        DefaultBotOptions defaultBotOptions = ApiContext.getInstance(DefaultBotOptions.class);
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(host.getHostName(), host.getPort()),
                new UsernamePasswordCredentials(login, password));
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setProxy(host)
                .setAuthenticationEnabled(true)
                .build();
        defaultBotOptions.setRequestConfig(requestConfig);
        defaultBotOptions.setCredentialsProvider(credentialsProvider);
        defaultBotOptions.setHttpProxy(host);

        return defaultBotOptions;
    }

    private static DefaultBotOptions proxyWithoutAuth(HttpHost host) {
        DefaultBotOptions defaultBotOptions = ApiContext.getInstance(DefaultBotOptions.class);
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setProxy(host)
                .setAuthenticationEnabled(false)
                .build();
        defaultBotOptions.setRequestConfig(requestConfig);

        defaultBotOptions.setHttpProxy(host);

        return defaultBotOptions;
    }

    void sendMessage(SendMessage sendMessage) {
        conversationLogger.info("Начало отправки {}", sendMessage);
        try {
            teleBot.execute(sendMessage);
            conversationLogger.info("Отправлено сообщение {}", sendMessage);
        } catch (TelegramApiException e) {
            tgBotLogger.error("Исключение при отправке {}", sendMessage);
            tgBotLogger.error(e);
            conversationLogger.warn("Не удалось отправить сообщение {}", sendMessage);
            tgBotLogger.info("Попытка отправки сообщения без разметки");
            try {
                sendMessage.enableMarkdown(false);
                teleBot.execute(sendMessage);
                conversationLogger.info("Отправлено сообщение без разметки {}", sendMessage);
            } catch (TelegramApiException ex) {
                tgBotLogger.fatal(ex);
                conversationLogger.error("Не удалось отправить сообщение без разметки {}", sendMessage);
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
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            if (!conversations.containsKey(chatId)) {
                TgConversation conversation = new TgConversation(tgBot, chatId);
                conversations.put(chatId, conversation);
                tgBot.addConversation(conversation);
            }

            TgConversation conversation = conversations.get(chatId);
            new Thread(() -> conversation.notifyAboutNewMessage(update.getMessage().getText())).start();
        }
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

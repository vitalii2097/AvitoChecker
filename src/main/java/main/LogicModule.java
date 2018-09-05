package main;

import avito.AvitoChecker;
import avito.AvitoUrl;
import avito.Url;
import core.DbListener;
import core.IListener;
import core.VkListener;
import javafx.util.Pair;
import storage.Storage;
import vk.Bot;
import vk.Conversation;

import java.util.Date;

public class LogicModule {

    private final AvitoChecker avitoChecker;
    private final Bot bot;
    private final Storage storage;

    public LogicModule(AvitoChecker avitoChecker, Bot bot, Storage storage) {
        this.avitoChecker = avitoChecker;
        this.bot = bot;
        this.storage = storage;
    }

    void handle(Conversation conversation, String message) {
        switch(message) {
            case "test" : {
                conversation.send(new Date().toString());
                break;
            }
            case "clear" : {
                avitoChecker.clearListener(new VkListener(Bot.getInstance(), conversation));
                conversation.send("Все ссылки были удалены");
                break;
            }
            default: {
                Url url = new AvitoUrl(message);
                addRequest(conversation, url);

                storage.add(new Pair<>(message, (int)conversation.getId()));
            }
        }

    }

    void addRequest(Conversation conversation, Url url) {
        IListener listener = new VkListener(bot, conversation);
        avitoChecker.addListener(listener, url);

        IListener dbListener = new DbListener(url);
        avitoChecker.addListener(dbListener, url);
    }

}

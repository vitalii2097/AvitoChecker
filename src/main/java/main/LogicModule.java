package main;

import avito.Activity;
import avito.AvitoChecker;
import avito.AvitoUrl;
import avito.Url;
import core.IListener;
import core.VkListener;
import javafx.util.Pair;
import storage.Storage;
import vk.Bot;
import vk.Conversation;

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
        Url url = new AvitoUrl(message);

        IListener listener = new VkListener(bot, conversation);
        avitoChecker.addListener(listener, url, Activity.MEDIUM);
        storage.add(new Pair<>(message, (int)conversation.getId()));
    }

}

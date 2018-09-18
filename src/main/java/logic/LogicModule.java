package logic;

import checker.AvitoChecker;
import me.veppev.avitodriver.AvitoUrl;
import observers.DbObserver;
import observers.Observer;
import observers.VkObserver;
import javafx.util.Pair;
import storage.Storage;
import vk.VkBot;
import vk.Conversation;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class LogicModule {

    private final AvitoChecker avitoChecker;
    private final Class<? extends Observer> observerClass;
    //private final Conversation conversation;
    //private final Storage storage;

    public LogicModule(AvitoChecker avitoChecker, Class<? extends Observer> observerClass/*, Conversation conversation, Storage storage*/) {
        this.avitoChecker = avitoChecker;
        this.observerClass = observerClass;
        //this.conversation = conversation;
        //this.storage = storage;
    }

    public void addConversation(Conversation conversation) {
        new LogicHandler(conversation, observerClass, avitoChecker);
    }

}

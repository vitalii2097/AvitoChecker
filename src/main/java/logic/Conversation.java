package logic;

import logic.appreciation.CheckedAnnouncement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public abstract class Conversation {

    private LogicModule logicModule;
    public static final Logger conversationLogger = LogManager.getLogger(Conversation.class.getSimpleName());

    public Conversation() {
        conversationLogger.info("Создан диалог {}", this);
    }

    void setLogicModule(LogicModule logicModule) {
        conversationLogger.info("К диалогу {} подключился {}", this, logicModule);
        this.logicModule = logicModule;
    }

    public abstract void send(String message);

    public abstract void send(String message, List<String> photos);

    public void send(CheckedAnnouncement announcement) {
        send(announcement.toString(), announcement.getAnnouncement().getImageUrls());
    }

    public void notifyAboutNewMessage(String message) {
        conversationLogger.info("Получено новое сообщение [{}] в {}", message, this);
        logicModule.notifyAboutNewMessage(message);
    }

}

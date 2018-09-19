package logic;

import java.io.File;
import java.util.List;

public abstract class Conversation {

    private LogicModule logicModule;

    void setLogicModule(LogicModule logicModule) {
        this.logicModule = logicModule;
    }

    public abstract void send(String message);

    public abstract void send(String message, List<File> photos);

    public void notifyAboutNewMessage(String message) {
        logicModule.notifyAboutNewMessage(message);
    }

}

package bot;

import checker.AvitoChecker;
import logic.Conversation;
import logic.LogicModule;

public abstract class Bot {

    protected AvitoChecker avitoChecker;

    public Bot(AvitoChecker avitoChecker) {
        this.avitoChecker = avitoChecker;
    }

    public void addConversation(Conversation conversation) {
        new LogicModule(conversation, avitoChecker);
    }

}

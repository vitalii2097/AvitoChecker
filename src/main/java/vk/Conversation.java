package vk;

import logic.LogicHandler;
import logic.LogicModule;

import java.util.List;
import java.util.Objects;

public abstract class Conversation {

    private long id;
    private LogicHandler logicHandler;

    public Conversation(long id) {
        this.id = id;
    }

    public void setLogicHandler(LogicHandler logicHandler) {
        this.logicHandler = logicHandler;
    }

    public long getId() {
        return id;
    }

    public void calculate(String message) {
        logicHandler.handle(message);
    }

    public abstract void send(String message);

    public abstract void send(String message, List<String> images);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

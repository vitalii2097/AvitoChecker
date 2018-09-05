package vk;

import java.util.List;
import java.util.Objects;

public abstract class Conversation {

    private long id;

    public Conversation(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
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

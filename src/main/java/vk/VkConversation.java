package vk;

import java.util.Collections;
import java.util.List;

public class VkConversation extends Conversation {

    public VkConversation(long id) {
        super(id);
    }

    @Override
    public void send(String message) {
        Bot.getInstance().sendMessage(this, message, Collections.emptyList());
    }

    @Override
    public void send(String message, List<String> images) {
        Bot.getInstance().sendMessage(this, message, images);
    }
}

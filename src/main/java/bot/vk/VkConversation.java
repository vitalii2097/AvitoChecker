package bot.vk;

import logic.Conversation;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class VkConversation extends Conversation {

    private final VkBot bot;
    private final long id;

    public VkConversation(VkBot bot, long id) {
        this.bot = bot;
        this.id = id;
    }

    @Override
    public void send(String message) {
        bot.sendMessage(this, message, Collections.emptyList());
    }

    @Override
    public void send(String message, List<File> images) {
        bot.sendMessage(this, message, images);
    }

    public long getId() {
        return id;
    }
}


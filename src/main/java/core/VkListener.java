package core;

import avito.net.Announcement;
import vk.Bot;
import vk.Conversation;

import java.util.Objects;

public class VkListener extends IListener {

    private final Bot bot;
    private final Conversation conversation;

    public VkListener(Bot bot, Conversation conversation) {
        this.bot = bot;
        this.conversation = conversation;
    }

    @Override
    void action(IListener listener, Announcement announcement) {
        StringBuilder builder = new StringBuilder();
        builder.append('•')
                .append(announcement.getName())
                .append("\n•")
                .append(announcement.getPrice())
                .append("\n•");
        if (announcement.getDescription().length() > 700) {
            builder.append(announcement.getDescription(), 0, 700).append("...");
        } else {
            builder.append(announcement.getDescription());
        }
        builder.append('\n').append(announcement.getUrl());

        bot.sendMessage(conversation, builder.toString(), announcement.getImageUrls());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VkListener that = (VkListener) o;
        return Objects.equals(conversation, that.conversation);
    }

    @Override
    public int hashCode() {

        return Objects.hash(conversation);
    }
}

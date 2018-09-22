package bot.vk;

import logic.Conversation;
import logic.appreciation.CheckedAnnouncement;
import me.veppev.avitodriver.Announcement;

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
        send(message, Collections.emptyList());
    }

    @Override
    public void send(String message, List<String> images) {
        bot.sendMessage(this, message, images);
    }

    @Override
    public void send(CheckedAnnouncement checkedAnnouncement) {
        StringBuilder builder = new StringBuilder();
        Announcement announcement = checkedAnnouncement.getAnnouncement();

        builder.append(checkedAnnouncement.getMark().name())
                .append(" profit (")
                .append(checkedAnnouncement.getMinProfit() > 0 ? "+" : "")
                .append(checkedAnnouncement.getMinProfit());

        if (checkedAnnouncement.getMinProfit() != checkedAnnouncement.getMaxProfit()) {
            builder.append(" - ")
                    .append(checkedAnnouncement.getMaxProfit() > 0 ? "+" : "")
                    .append(checkedAnnouncement.getMaxProfit());
        }

        builder.append(')');
        builder.append("\n").append(checkedAnnouncement.getModel());
        builder.append("\n(").append(announcement.getName()).append(")");
        builder.append("\n(").append(announcement.getPrice()).append('\u20BD').append(')');
        builder.append("\n").append(announcement.getMetro());
        builder.append("\n\n").append(announcement.getDescription().length() < 700
                        ? announcement.getDescription()
                        : (announcement.getDescription().substring(0, 700) + "...")
        );

        builder.append('\n').append(announcement.getUrl());

        send(builder.toString(), announcement.getImageUrls());
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "VkConversation{" +
                "bot=" + bot +
                ", id=" + id +
                '}';
    }
}


package observers;

import me.veppev.avitodriver.Announcement;
import vk.Conversation;

import java.util.Objects;

public class VkObserver extends Observer {

    private Conversation conversation;

    @Override
    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    @Override
    void action(Announcement announcement) {
        StringBuilder builder = new StringBuilder();
        builder.append('•')
                .append(announcement.getName())
                .append("\n•")
                .append(announcement.getPrice())
                .append("\n\n")
                .append(announcement.getMetro())
                .append("\n")
                .append(announcement.getOwnerName())
                .append("\n\n");
        if (announcement.getDescription().length() > 700) {
            builder.append(announcement.getDescription(), 0, 700).append("...");
        } else {
            builder.append(announcement.getDescription());
        }
        builder.append('\n').append(announcement.getUrl());

        conversation.send(builder.toString(), announcement.getImageUrls());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VkObserver that = (VkObserver) o;
        return Objects.equals(conversation, that.conversation);
    }

    @Override
    public int hashCode() {

        return Objects.hash(conversation);
    }
}

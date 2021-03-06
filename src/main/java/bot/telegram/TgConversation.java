package bot.telegram;

import logic.Conversation;
import logic.appreciation.CheckedAnnouncement;
import me.veppev.avitodriver.Announcement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.List;

public class TgConversation extends Conversation {

    private final TgBot bot;
    private long id;
    static final Logger tgLogger = LogManager.getLogger(TgConversation.class.getSimpleName());

    public TgConversation(TgBot bot, long id) {
        this.bot = bot;
        this.id = id;
    }

    @Override
    public void send(String text) {
        tgLogger.debug("Вызов метода send объекта {} с текстом {}", this, text);
        SendMessage message = new SendMessage();
        message.enableMarkdown(true);
        message.setChatId(id);
        message.setText(text);
        message.disableWebPagePreview();
        tgLogger.debug("Сформирован объект message {}", message);
        conversationLogger.info("Отправка из диалога {} сообщения {}", this, message);
        bot.sendMessage(message);
    }

    @Override
    public void send(String message, List<String> photos) {
        StringBuilder messageBuilder = new StringBuilder(message);
        messageBuilder.append('\n');
        for (int i = 0; i < photos.size(); i++) {
            messageBuilder
                    .append(toHref("photo" + (i + 1), photos.get(i).trim()));

            if ((i + 1) % 3 == 0) {
                messageBuilder.append("\n\n");
            } else {
                messageBuilder.append("      ");
            }
        }
        message = messageBuilder.toString();
        send(message);
    }

    private String toBold(String text) {
        return '*' + text + '*';
    }

    private String toHref(String text, String href) {
        return "[" + text + "](" + href + ")";
    }

    private String removeMarkDown(String text) {

        return text
                .replaceAll("\\*", "")
                .replaceAll("_", "__")
                .replaceAll(":", "");
    }

    @Override
    public void send(CheckedAnnouncement checkedAnnouncement) {
        StringBuilder builder = new StringBuilder();
        Announcement announcement = checkedAnnouncement.getAnnouncement();
        if (checkedAnnouncement.getMinProfit() != checkedAnnouncement.getMaxProfit()) {
            builder.append(
                    toBold(
                            checkedAnnouncement.getMark().name()
                                    + " profit ("
                                    + (checkedAnnouncement.getMinProfit() > 0 ? "+" : "")
                                    + checkedAnnouncement.getMinProfit()
                                    + " - "
                                    + (checkedAnnouncement.getMaxProfit() > 0 ? "+" : "")
                                    + checkedAnnouncement.getMaxProfit()
                                    + ")"
                    )
            );
        } else {
            builder.append(
                    toBold(
                            checkedAnnouncement.getMark().name()
                                    + " profit ("
                                    + (checkedAnnouncement.getMinProfit() > 0 ? "+" : "")
                                    + checkedAnnouncement.getMinProfit()
                                    + ")"
                    )
            );
        }
        builder.append("\n").append(toBold(checkedAnnouncement.getModel()));
        builder.append("\n(").append(toHref(announcement.getName(), announcement.getUrl())).append(")");
        builder.append("\n(").append(announcement.getPrice()).append('\u20BD').append(')');
        builder.append("\n(").append(announcement.getMetro()).append(')');
        builder.append("\n\n").append(
                removeMarkDown(announcement.getDescription().length() < 700
                        ? announcement.getDescription()
                        : announcement.getDescription().substring(0, 700) + "...")
        );

        send(builder.toString(), announcement.getImageUrls());
    }
}

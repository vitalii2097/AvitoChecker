package bot.telegram;

import logic.Conversation;
import logic.appreciation.CheckedAnnouncement;
import me.veppev.avitodriver.Announcement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.File;
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
        bot.sendMessage(message);
    }

    @Override
    public void send(String message, List<File> photos) {
        //TODO
        send(message);
    }

    private String toBold(String text) {
        return '*' + text + '*';
    }

    private String toHref(String text, String href) {
        return "[" + text + "](" + href + ")";
    }

    private String removeMarkDown(String text) {
        return text.replaceAll("\\*", "").replaceAll("_", "__");
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
                                    +checkedAnnouncement.getMaxProfit()
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
        builder.append("\n\n").append(removeMarkDown(announcement.getDescription()));

        send(builder.toString(), announcement.getImageUrls());
    }
}

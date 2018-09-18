package telegram;

import logic.LogicModule;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import vk.Conversation;

import java.util.List;

public class TeleConversation extends Conversation {

    public TeleConversation(long chatId) {
        super(chatId);
    }

    public void send(String text) {
        SendMessage message = new SendMessage();
        message.enableMarkdown(true);
        message.setChatId(getId());
        message.setText(text);
        try {
            TeleBot.getInstance().execute(message);
        } catch (TelegramApiException e) {
        //    log.log(Level.SEVERE, "Exception: ", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void send(String message, List<String> images) {
        //TODO
        send(message);
    }
}

package main;

import avito.AvitoChecker;
import avito.AvitoUrl;
import avito.Url;
import core.DbListener;
import core.IListener;
import core.VkListener;
import javafx.util.Pair;
import storage.Storage;
import vk.Bot;
import vk.Conversation;

import java.util.Date;
import java.util.List;

public class LogicModule {

    private final AvitoChecker avitoChecker;
    private final Bot bot;
    private final Storage storage;

    public LogicModule(AvitoChecker avitoChecker, Bot bot, Storage storage) {
        this.avitoChecker = avitoChecker;
        this.bot = bot;
        this.storage = storage;
    }

    void handle(Conversation conversation, String message) {
        switch(message) {
            case "test" : {
                conversation.send(new Date().toString());
                break;
            }
            case "clear" : {
                avitoChecker.clearListener(new VkListener(Bot.getInstance(), conversation));
                conversation.send("Все ссылки были удалены");
                break;
            }
            case "urls" : {
                List<Url> urls = avitoChecker.getUrls(new VkListener(Bot.getInstance(), conversation));
                StringBuilder result = new StringBuilder();
                for (Url url : urls) {
                    result.append(url.getUrl()).append('\n');
                }
                conversation.send(result.length() == 0 ? "Запросов нет" : result.toString());
                break;
            }
            case "" : {
                conversation.send("Было прислано пустое сообщение. Возможно vk заменил текст на ссылку");
                break;
            }
            case "help" : {
                conversation.send("Avito Notifier by VEP\n" +
                        "Команды:\n" +
                        "help - даёт справку\n" +
                        "urls - возвращает активные запросы в этом диалоге\n" +
                        "clear - удаляет все запросы\n" +
                        "test - проверяет работает ли бот вообще\n" +
                        "Все остальные сообщения расцениваются, как ссылки.");
                break;
            }
            default: {
                if (message.length() < 26) {
                    conversation.send("Слишком короткая ссылка");
                } else {
                    Url url = new AvitoUrl(message);
                    addRequest(conversation, url);

                    storage.add(new Pair<>(message, (int) conversation.getId()));
                    conversation.send("Добавлен запрос с адресом " + url.getUrl());
                }
            }
        }

    }

    void addRequest(Conversation conversation, Url url) {
        IListener listener = new VkListener(bot, conversation);
        avitoChecker.addListener(listener, url);

        IListener dbListener = new DbListener(url);
        avitoChecker.addListener(dbListener, url);
    }

}

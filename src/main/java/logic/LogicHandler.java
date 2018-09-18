package logic;

import checker.AvitoChecker;
import me.veppev.avitodriver.AvitoUrl;
import observers.Observer;
import vk.Conversation;

import java.io.IOException;
import java.util.Date;

public class LogicHandler {

    private Conversation conversation;
    private final Class<? extends Observer> observerClass;
    private final AvitoChecker avitoChecker;

    public LogicHandler(Conversation conversation, Class<? extends Observer> observerClass, AvitoChecker avitoChecker) {
        this.conversation = conversation;
        this.observerClass = observerClass;
        this.avitoChecker = avitoChecker;
    }

    public void handle(String message) {
        switch(message) {
            case "test" : {
                conversation.send(new Date().toString());
                break;
            }
            case "clear" : {
                //avitoChecker.clearUrlsOfObserver(new VkObserver(VkBot.getInstance(), conversation));
                conversation.send("Все ссылки были удалены.");
                break;
            }
            case "urls" : {
                /*List<AvitoUrl> urls = avitoChecker.getUrlsOfObserver(new VkObserver(VkBot.getInstance(), conversation));
                StringBuilder result = new StringBuilder();
                for (AvitoUrl url : urls) {
                    result.append(url.toString()).append('\n');
                }
                conversation.send(result.length() == 0 ? "Запросов нет" : result.toString());*/
                break;
            }
            case "" : {
                conversation.send("Было прислано пустое сообщение. Возможно, vk заменил текст на ссылку.");
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
                try {
                    AvitoUrl url = new AvitoUrl(message);
                    addRequest(conversation, url);

                    //storage.add(new Pair<>(message, (int) conversation.getId()));
                    conversation.send("Добавлен запрос с адресом " + url.toString());
                } catch (IOException | IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    void addRequest(Conversation conversation, AvitoUrl url) {
        try {
            Observer listener = observerClass.newInstance();
            listener.setConversation(conversation);
            avitoChecker.addObserver(listener, url);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }


/*
        Observer dbListener = new DbObserver(url);
        avitoChecker.addObserver(dbListener, url);*/
    }

}

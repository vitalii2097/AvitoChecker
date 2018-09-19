package logic;

import checker.AvitoChecker;
import logic.appreciation.CheckedAnnouncement;
import logic.appreciation.GlobalAppraiser;
import logic.appreciation.Mark;
import me.veppev.avitodriver.Announcement;
import me.veppev.avitodriver.AvitoUrl;
import observers.Observer;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class LogicModule extends Observer {

    private final Conversation conversation;
    private final AvitoChecker avitoChecker;
    private final static GlobalAppraiser appraiser = new GlobalAppraiser();
    private Mark minMark = Mark.F;

    public LogicModule(Conversation conversation, AvitoChecker avitoChecker) {
        this.conversation = conversation;
        this.avitoChecker = avitoChecker;
        conversation.setLogicModule(this);

        conversation.send("Приветствую. К данному диалогу подключился логический модуль " + this);
        conversation.send("Введите help для справки");
    }

    @Override
    protected void action(Announcement announcement) {
        CheckedAnnouncement checkedAnnouncement = appraiser.appreciate(announcement);
        Mark mark = checkedAnnouncement.getMark();
        if (mark.getValue() >= minMark.getValue()) {
            conversation.send(checkedAnnouncement.toString(), announcement.getImageUrls());
        }
        //Оценить объявление
        //В зависимости от ценности что то сделать
        //Например передать в диалог
    }

    void notifyAboutNewMessage(String message) {
        //Разобрать сообщение и проделать некие манипуляции
        System.out.println(message);
        switch(message.toLowerCase()) {
            case "time" : {
                conversation.send(new Date().toString());
                break;
            }
            case "clear" : {
                avitoChecker.clearUrlsOfObserver(this);
                conversation.send("Все ссылки были удалены.");
                break;
            }
            case "urls" : {
                List<AvitoUrl> urls = avitoChecker.getUrlsOfObserver(this);
                StringBuilder result = new StringBuilder();
                for (AvitoUrl url : urls) {
                    result.append(url.toString()).append('\n');
                }
                conversation.send(result.length() == 0 ? "Запросов нет" : result.toString());
                break;
            }
            /*case "" : {
                conversation.send("Было прислано пустое сообщение. Возможно, vk заменил текст на ссылку.");
                break;
            }*/
            case "a": {
                minMark = Mark.A;
                conversation.send("Установлен уровень " + minMark.name()
                        + ". Профит от " + minMark.getMin() + "р.");
                break;
            }
            case "b": {
                minMark = Mark.B;
                conversation.send("Установлен уровень " + minMark.name()
                        + ". Профит от " + minMark.getMin() + "р.");
                break;
            }
            case "c": {
                minMark = Mark.C;
                conversation.send("Установлен уровень " + minMark.name()
                        + ". Профит от " + minMark.getMin() + "р.");
                break;
            }
            case "d": {
                minMark = Mark.D;
                conversation.send("Установлен уровень " + minMark.name()
                        + ". Профит от " + minMark.getMin() + "р.");
                break;
            }
            case "e": {
                minMark = Mark.E;
                conversation.send("Установлен уровень " + minMark.name()
                        + ". Профит от " + minMark.getMin() + "р.");
                break;
            }
            case "f": {
                minMark = Mark.F;
                conversation.send("Установлен уровень " + minMark.name()
                        + ". Профит от " + minMark.getMin() + "р.");
                break;
            }
            case "help" : {
                conversation.send("Avito Notifier by VEP\n" +
                        "Команды:\n" +
                        "help - даёт справку\n" +
                        "urls - возвращает активные запросы в этом диалоге\n" +
                        "//clear - удаляет все запросы\n" +
                        "time - возвращает время на сервере\n" +
                        "Буква от A до F установит минимальную оценку для объявлений, которые будут показываются\n" +
                        "Все остальные сообщения расцениваются, как ссылки.");
                break;
            }
            case "hello":
            case "привет": {
                int hour = new Date().getHours();
                if (hour < 4) {
                    conversation.send("Доброй ночи");
                } else if (hour < 12) {
                    conversation.send("Доброе утро");
                } else if (hour < 18) {
                    conversation.send("Добрый день");
                } else if (hour < 24) {
                    conversation.send("Добрый вечер");
                }
                break;
            }
            default: {
                try {
                    AvitoUrl url = new AvitoUrl(message);

                    avitoChecker.addObserver(this, url);

                    //storage.add(new Pair<>(message, (int) conversation.getId()));
                    conversation.send("Добавлен запрос с адресом " + url.toString());
                } catch (IOException | IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

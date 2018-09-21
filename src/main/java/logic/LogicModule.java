package logic;

import checker.AvitoChecker;
import logic.appreciation.CheckedAnnouncement;
import logic.appreciation.GlobalAppraiser;
import logic.appreciation.Mark;
import me.veppev.avitodriver.Announcement;
import me.veppev.avitodriver.AvitoUrl;
import observers.Observer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class LogicModule extends Observer {

    private final Conversation conversation;
    private final AvitoChecker avitoChecker;
    private final static GlobalAppraiser appraiser = new GlobalAppraiser();
    private Mark minMark = Mark.Negative;
    private final Logger logicLogger = LogManager.getLogger(LogicModule.class.getSimpleName());
    private boolean isLogin = false;
    private final static String PASSWORD = "Валерон, лови патрон";

    public LogicModule(Conversation conversation, AvitoChecker avitoChecker) {
        this.conversation = conversation;
        this.avitoChecker = avitoChecker;
        conversation.setLogicModule(this);

        logicLogger.debug("Создан новый экземпляр LogicModule {} для диалога {}", this, conversation);
        conversation.send("Приветствую. К данному диалогу подключился логический модуль " + this);
        conversation.send("Введите пароль");
    }

    @Override
    protected void action(Announcement announcement) {

        CheckedAnnouncement checkedAnnouncement = appraiser.appreciate(announcement);
        Mark mark = checkedAnnouncement.getMark();
        logicLogger.info("Объявление {} оцененно на {}", announcement, mark.name());
        if (mark.getValue() >= minMark.getValue()) {
            conversation.send(checkedAnnouncement);
        }
        //Оценить объявление
        //В зависимости от ценности что то сделать
        //Например передать в диалог
    }

    private boolean login(String password) {
        return isLogin = password.equals(PASSWORD);
    }

    void notifyAboutNewMessage(String message) {

        if (!isLogin) {
            if (login(message)) {
                conversation.send(this + " успешно активирован");
            } else {
                conversation.send("Неверный пароль");
            }
            return;
        }

        //Разобрать сообщение и проделать некие манипуляции\
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
            case "high": {
                minMark = Mark.High;
                conversation.send("Установлен уровень " + minMark.name()
                        + ". Профит от " + minMark.getMin() + "р.");
                break;
            }
            case "ultra": {
                minMark = Mark.Ultra;
                conversation.send("Установлен уровень " + minMark.name()
                        + ". Профит от " + minMark.getMin() + "р.");
                break;
            }
            case "medium": {
                minMark = Mark.Medium;
                conversation.send("Установлен уровень " + minMark.name()
                        + ". Профит от " + minMark.getMin() + "р.");
                break;
            }
            case "low": {
                minMark = Mark.Low;
                conversation.send("Установлен уровень " + minMark.name()
                        + ". Профит от " + minMark.getMin() + "р.");
                break;
            }
            case "all": {
                minMark = Mark.Negative;
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
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    logicLogger.warn("Не удалось создать новую ссылку {}" + message);
                }
            }
        }
    }
}

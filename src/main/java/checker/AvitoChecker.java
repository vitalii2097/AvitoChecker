package checker;

import me.veppev.avitodriver.Announcement;
import me.veppev.avitodriver.AvitoDriver;
import me.veppev.avitodriver.AvitoUrl;
import observers.Observer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

class Checker implements Runnable {

    private Query query;
    private AvitoDriver driver;
    static final Logger checkerLogger = LogManager.getLogger(Checker.class.getSimpleName());

    private void updateQuery() {
        checkerLogger.info("Началось обновление запроса {}", query);
        List<Announcement> announcements = driver.getAnnouncements(query.getUrl());
        checkerLogger.info("Драйвер загрузил {} объявлений по запросу {}", announcements.size(), query);
        for (Announcement announcement : announcements) {
            query.addAnnouncement(announcement);
        }
        checkerLogger.info("Обновлён запрос ", query);
    }

    Checker(Query query, AvitoDriver driver) {
        this.query = query;
        this.driver = driver;

        checkerLogger.info("Первоначальная загрузка объявлений по запросу {} для нового {}", query, this);
        updateQuery();
    }

    @Override
    public void run() {
        updateQuery();
    }
}


public class AvitoChecker {

    private Map<AvitoUrl, Query> queries = new HashMap<>();
    private AvitoDriver driver = AvitoDriver.getInstance();
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
    static final Logger avitoLogger = LogManager.getLogger(AvitoChecker.class.getSimpleName());

    public AvitoChecker() {
        avitoLogger.debug("Создан экземпляр AvitoChecker");
    }

    public void addObserver(Observer observer, AvitoUrl url) {
        if (!queries.containsKey(url)) {
            Query query = new Query(url);
            queries.put(url, query);
            avitoLogger.info("Добавлен новый запрос для мониторинга; query={}", query);

            int seconds = new Date().getSeconds();
            scheduler.scheduleAtFixedRate(new Checker(query, driver), 65 - seconds, 60, TimeUnit.SECONDS);
        }

        Query query = queries.get(url);
        query.addObserver(observer);
        avitoLogger.info("Добавлен новый слушатель {} к запросу {}", observer, query);
    }

    public void clearUrlsOfObserver(Observer observer) {
        queries.values().forEach(query -> query.dropListener(observer));
        avitoLogger.info("Очищены все ссылки {}", observer);
    }

    public List<AvitoUrl> getUrlsOfObserver(Observer observer) {
        avitoLogger.info("Запрос на получение всех ссылок слушателя {}", observer);
        return queries.values()
                .stream()
                .filter(query -> query.contains(observer))
                .map(Query::getUrl)
                .collect(Collectors.toList());
    }

}

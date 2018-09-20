package checker;

import me.veppev.avitodriver.Announcement;
import me.veppev.avitodriver.AvitoUrl;
import observers.Observer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

class Query implements Iterable<Announcement> {

    private final AvitoUrl url;
    private List<Announcement> announcements;
    private Set<Observer> observers;
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(20);
    static final Logger queryLogger = LogManager.getLogger(Query.class.getSimpleName());

    Query(AvitoUrl url) {
        this.url = url;
        announcements = new ArrayList<>();
        observers = new HashSet<>();
        queryLogger.debug("Создано хранилище запросов Query с url={}", url);
    }

    @Override
    public String toString() {
        return url.toString();
    }

    void addAnnouncement(Announcement announcement) {
        queryLogger.debug("Попытка добавить новое объявление {} к запросу {}", announcement.getUrl(), this);
        if (!announcements.contains(announcement)) {
            queryLogger.info("К {} добавлено новое объявление {}", this, announcement.getUrl());
            announcements.add(announcement);
            for (Observer observer : observers) {
                scheduler.execute(observer::notifyNewAnnouncement);
            }
        }
    }

    void addObserver(Observer observer) {
        if (observers.contains(observer)) {
            return;
        }
        queryLogger.info("К запросу {} добавлен новый слушатель {}", this, observer);
        observers.add(observer);
        observer.setIterator(iterator());
    }

    void dropListener(Observer observer) {
        observers.remove(observer);
    }

    AvitoUrl getUrl() {
        return url;
    }

    boolean contains(Observer observer) {
        return observers.contains(observer);
    }

    @Override
    public Iterator<Announcement> iterator() {
        return new QueryIterator();
    }

    private class QueryIterator implements Iterator<Announcement> {

        private int index = 0;

        @Override
        public boolean hasNext() {
            return announcements.size() > index;
        }

        @Override
        public Announcement next() {
            return announcements.get(index++);
        }
    }
}

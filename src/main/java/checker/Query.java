package checker;

import me.veppev.avitodriver.Announcement;
import me.veppev.avitodriver.AvitoUrl;
import observers.Observer;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

class Query implements Iterable<Announcement> {

    private final AvitoUrl url;
    private List<Announcement> announcements;
    private Set<Observer> observers;
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(20);

    Query(AvitoUrl url) {
        this.url = url;
        announcements = new ArrayList<>();
        observers = new HashSet<>();
    }

    @Override
    public String toString() {
        return url.toString();
    }

    void addAnnouncement(Announcement announcement) {
        if (!announcements.contains(announcement)) {
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

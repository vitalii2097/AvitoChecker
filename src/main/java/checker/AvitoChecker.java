package checker;

import checker.appreciation.GlobalAppraiser;
import me.veppev.avitodriver.Announcement;
import me.veppev.avitodriver.AvitoDriver;
import me.veppev.avitodriver.AvitoUrl;
import observers.Observer;

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
    private static GlobalAppraiser appraiser = new GlobalAppraiser();

    private void updateQuery() {
        System.out.println("start load query " + query + " at " + new Date());
        List<Announcement> announcements = driver.getAnnouncements(query.getUrl());
        for (Announcement announcement : announcements) {
            CheckedAnnouncement checkedAnnouncement = appraiser.appreciate(announcement);
            query.addAnnouncement(checkedAnnouncement);
        }
        System.out.println("Updated query: " + query + " at " + new Date());
    }

    Checker(Query query, AvitoDriver driver) {
        this.query = query;
        this.driver = driver;

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

    public void addObserver(Observer observer, AvitoUrl url) {
        if (!queries.containsKey(url)) {
            Query query = new Query(url);
            queries.put(url, query);

            int seconds = new Date().getSeconds();
            scheduler.scheduleAtFixedRate(new Checker(query, driver), 65 - seconds, 60, TimeUnit.SECONDS);
            System.out.println("Added new query: " + query);
        }

        Query query = queries.get(url);
        query.addObserver(observer);
        System.out.println("Added new observer=" + observer + " to query=" + query);
    }

    public void clearUrlsOfObserver(Observer observer) {
        queries.values().forEach(query -> query.dropListener(observer));
    }

    public List<AvitoUrl> getUrlsOfObserver(Observer observer) {
        return queries.values()
                .stream()
                .filter(query -> query.contains(observer))
                .map(Query::getUrl)
                .collect(Collectors.toList());
    }

}

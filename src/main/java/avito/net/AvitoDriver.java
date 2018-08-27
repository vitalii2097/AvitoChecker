package avito.net;

import avito.Url;
import org.apache.http.HttpHost;

import java.io.IOException;
import java.util.*;

public class AvitoDriver {

    private Map<String, Announcement> announcementMap = new HashMap<>();
    private volatile HttpHost proxy;
    private final ProxyList proxyList = new ProxyList();

    private void changeIP() {
        proxy = proxyList.getProxyServer();
    }

    public List<Announcement> getAnnouncements(Url avitoUrl) {
        String url = avitoUrl.getUrl();

        String html;
        try {
            html = Network.loadPage(url, proxy);
        } catch (IOException e) {
            return Collections.emptyList();
        }
        if (html.contains("<title>Доступ временно заблокирован</title>") ) {
            System.out.println("Авито заблокрировало доступ, сменяем ip");
            changeIP();
            return getAnnouncements(avitoUrl);
        }

        int countNewAnnouncements = 0;

        List<Announcement> announcements = new ArrayList<>();
        for (String href : Parser.getAnnouncementUrls(html)) {
            if (announcementMap.containsKey(href)) {
                announcements.add(announcementMap.get(href));
            } else {
                Announcement announcement = new Announcement(href, this);
                announcements.add(announcement);
                announcementMap.put(href, announcement);
                countNewAnnouncements++;
            }
        }
        System.out.println("Loaded " + announcements.size() + " announcements by url=" + avitoUrl.getUrl()
                + "; new ann. =" + countNewAnnouncements);

        return announcements;
    }

    void loadAnnouncement(Announcement announcement) {
        String url = announcement.getUrl();
        Network network = new Network(url);
        try {
            String html = network.getPage();
            announcement.setName(Parser.getName(html));
            announcement.setDescription(Parser.getDescription(html));
            announcement.setPrice(Parser.getPrice(html));
            announcement.setImageUrl(Parser.getImageUrls(html));
        } catch (IOException | IllegalAccessException e) {
            announcement.setDescription("Не удалось загрузить описание");
            announcement.setName("Не удалось загрузить название");
            announcement.setPrice(0);
        }

    }

}

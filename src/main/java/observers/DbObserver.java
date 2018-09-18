package observers;

import db.AnnouncementDao;
import db.DbAnnouncement;
import me.veppev.avitodriver.Announcement;
import me.veppev.avitodriver.AvitoUrl;

public class DbObserver extends Observer {

    private AnnouncementDao announcementDao = new AnnouncementDao();
    private final AvitoUrl requestUrl;

    public DbObserver(AvitoUrl requestUrl) {
        this.requestUrl = requestUrl;
    }

    @Override
    void action(Announcement announcement) {
        DbAnnouncement dbAnnouncement = new DbAnnouncement();
        dbAnnouncement.setDescription(announcement.getDescription());
        dbAnnouncement.setName(announcement.getName());
        dbAnnouncement.setOwner(announcement.getOwnerName());
        dbAnnouncement.setPrice(announcement.getPrice());
        dbAnnouncement.setUrl(announcement.getUrl());

        dbAnnouncement.setRequestUrl(requestUrl.toString());

        announcementDao.save(dbAnnouncement);
    }
}

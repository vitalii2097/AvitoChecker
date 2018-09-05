package core;

import avito.Url;
import avito.net.Announcement;
import db.AnnouncementDao;
import db.DbAnnouncement;

public class DbListener extends IListener {

    private AnnouncementDao announcementDao = new AnnouncementDao();
    private final Url requestUrl;

    public DbListener(Url requestUrl) {
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

        dbAnnouncement.setRequestUrl(requestUrl.getUrl());

        announcementDao.save(dbAnnouncement);
    }
}

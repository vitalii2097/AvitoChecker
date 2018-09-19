package logic.appreciation;

import me.veppev.avitodriver.Announcement;

public class GlobalAppraiser implements Appraiser {

    @Override
    public CheckedAnnouncement appreciate(Announcement announcement) {
        if (announcement.getUrl().toLowerCase().contains("iphone")) {
            return new IPhoneAppraiser().appreciate(announcement);
        } else {
            return new CheckedAnnouncement(announcement);
        }
    }
}

package checker.appreciation;

import checker.CheckedAnnouncement;
import me.veppev.avitodriver.Announcement;

public class GlobalAppraiser implements Appraiser {

    @Override
    public CheckedAnnouncement appreciate(Announcement announcement) {
        if (announcement.getName().toLowerCase().contains("iphone")) {
            return new IPhoneAppraiser().appreciate(announcement);
        } else {
            return new CheckedAnnouncement(announcement);
        }
    }
}

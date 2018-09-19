package observers;

import me.veppev.avitodriver.Announcement;

import java.util.Iterator;

public abstract class Observer {

    private Iterator<Announcement> announcementIterator;

    public final void setIterator(Iterator<Announcement> announcementIterator) {
        this.announcementIterator = announcementIterator;
        while (announcementIterator.hasNext()) {
            announcementIterator.next();
        }
    }

    public final void notifyNewAnnouncement() {
        while (announcementIterator.hasNext()) {
            Announcement newAnn = announcementIterator.next();
            action(newAnn);
        }
    }

    protected abstract void action(Announcement announcement);


}

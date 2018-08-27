package core;

import avito.net.Announcement;

import java.util.Iterator;

public abstract class IListener {

    private Iterator<Announcement> announcementIterator;

    public final void setIterator(Iterator<Announcement> announcementIterator) {
        this.announcementIterator = announcementIterator;
        while (announcementIterator.hasNext()) {
            announcementIterator.next();
        }
    }

    public final void notifyNewAnnouncement() {
        while (announcementIterator.hasNext()) {
            action(this, announcementIterator.next());
        }
    }

    abstract void action(IListener listener, Announcement announcement);

}

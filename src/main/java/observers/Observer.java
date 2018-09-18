package observers;

import checker.CheckedAnnouncement;
import me.veppev.avitodriver.Announcement;
import vk.Conversation;

import java.util.Iterator;

public abstract class Observer {

    private Iterator<CheckedAnnouncement> announcementIterator;

    public final void setIterator(Iterator<CheckedAnnouncement> announcementIterator) {
        this.announcementIterator = announcementIterator;
        while (announcementIterator.hasNext()) {
            announcementIterator.next();
        }
    }

    public final void notifyNewAnnouncement() {
        while (announcementIterator.hasNext()) {
            action(announcementIterator.next());
        }
    }

    abstract void action(CheckedAnnouncement announcement);

    public void setConversation(Conversation conversation) {

    }

}

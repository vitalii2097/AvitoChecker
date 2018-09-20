package observers;

import me.veppev.avitodriver.Announcement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;

public abstract class Observer {

    private Iterator<Announcement> announcementIterator;
    static final Logger observerLogger = LogManager.getLogger(Observer.class.getSimpleName());

    public final void setIterator(Iterator<Announcement> announcementIterator) {
        this.announcementIterator = announcementIterator;
        while (announcementIterator.hasNext()) {
            announcementIterator.next();
        }
    }

    public final void notifyNewAnnouncement() {
        observerLogger.debug("Вызов метода notifyNewAnnouncemet у {}", this);
        while (announcementIterator.hasNext()) {
            Announcement newAnn = announcementIterator.next();
            observerLogger.info("{} уведомлен о новом объявлении {}", this, newAnn);
            action(newAnn);
        }
    }

    protected abstract void action(Announcement announcement);


}

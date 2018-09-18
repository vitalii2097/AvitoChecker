package observers;

import me.veppev.avitodriver.Announcement;

public class TestObserver extends Observer {

    @Override
    void action(Announcement announcement) {
        System.out.println(this + ": " + announcement);
    }
}

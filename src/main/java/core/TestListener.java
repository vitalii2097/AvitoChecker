package core;

import avito.net.Announcement;

public class TestListener extends IListener {

    @Override
    void action(Announcement announcement) {
        System.out.println(this + ": " + announcement);
    }
}

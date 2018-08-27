package core;

import avito.net.Announcement;

public class TestListener extends IListener {

    @Override
    void action(IListener listener, Announcement announcement) {
        System.out.println(this + ": " + announcement);
    }
}

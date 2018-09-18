package checker.appreciation;

import checker.CheckedAnnouncement;
import checker.Mark;
import me.veppev.avitodriver.Announcement;

import java.util.Arrays;

import static checker.appreciation.Memory.*;
import static checker.appreciation.Model.*;


enum Model {
    FiveS("iphone 5s"),
    Six("iphone 6"),
    SixPlus("iphone 6 plus");

    private final String engName;

    Model(String engName) {
        this.engName = engName;
    }

    public String getEngName() {
        return engName;
    }

    public boolean equalsByName(Announcement announcement) {
        return engName.replaceAll(" ", "")
                .equalsIgnoreCase(announcement.getName().replaceAll(" ", ""));
    }

    public static Model getModel(Announcement announcement) {
        for (Model model : Model.values()) {
            if (model.equalsByName(announcement)) {
                return model;
            }
        }
        return null;
    }

}

enum Memory {
    M16(16), M32(32), M64(64), M128(128), M256(256);

    private int size;

    Memory(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public boolean equalsBySize(Announcement announcement) {
        return announcement.getName().contains(Integer.toString(size));
    }

    public static Memory getMemory(Announcement announcement) {
        for (Memory memory : Memory.values()) {
            if (memory.equalsBySize(announcement)) {
                return memory;
            }
        }
        return null;
    }

}

enum IPhone {
    M6_16(FiveS, M16, 5000);

    private final Model model;
    private final Memory memory;
    private final int sellingPrice;

    IPhone(Model model, Memory memory, int sellingPrice) {
        this.model = model;
        this.memory = memory;
        this.sellingPrice = sellingPrice;
    }

    public Model getModel() {
        return model;
    }

    public Memory getMemory() {
        return memory;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public boolean equalsByParams(Announcement announcement) {
        return Model.getModel(announcement) == model && Memory.getMemory(announcement) == memory;
    }

    public static IPhone getIPhone(Announcement announcement) {
        for (IPhone iPhone : IPhone.values()) {
            if (iPhone.equalsByParams(announcement)) {
                return iPhone;
            }
        }
        return null;
    }

}

public class IPhoneAppraiser implements Appraiser {

    @Override
    public CheckedAnnouncement appreciate(Announcement announcement) {
        CheckedAnnouncement checkedAnnouncement = new CheckedAnnouncement(announcement);
        IPhone iPhone = IPhone.getIPhone(announcement);
        if (iPhone == null) {
            checkedAnnouncement.setMark(Mark.NOT_MARKED);
        } else {
            Mark mark = Mark.getMark(iPhone.getSellingPrice() - announcement.getPrice());
            checkedAnnouncement.setMark(mark);
        }
        return checkedAnnouncement;
    }
}

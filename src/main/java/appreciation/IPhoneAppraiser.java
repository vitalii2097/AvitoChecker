package appreciation;

import me.veppev.avitodriver.Announcement;

import static appreciation.Memory.*;
import static appreciation.Model.*;


enum Model {
    FiveS("5s"),
    Six("6"),
    SixP("6plus"),
    SixS("6s"),
    SixPS("6splus"),
    Seven("7"),
    SevenP("7plus"),
    Eight("8"),
    EightP("8plus"),
    X("X");

    private final String engName;

    Model(String engName) {
        this.engName = engName;
    }

    public String getEngName() {
        return engName;
    }

    public boolean equalsByName(Announcement announcement) {
        String annName = announcement.getName().replaceAll(" ", "").toLowerCase();
        return annName.contains("iphone" + engName) || annName.contains("айфон" + engName);
    }

    public static Model getModel(Announcement announcement) {
        for (int i = Model.values().length - 1; i >= 0; i--) {
            Model model = Model.values()[i];
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
    M5S_16(FiveS, M16, 5000),
    M5S_32(FiveS, M32, 6000),
    M5S_64(FiveS, M64, 6000),
    M6_16(Six, M16, 7000),
    M6_32(Six, M32, 8000),
    M6_64(Six, M64, 9000),
    M6_128(Six, M128, 10000),
    M6P_16(SixP, M16, 10000),
    M6P_32(SixP, M32, 12000),
    M6P_64(SixP, M64, 13000),
    M6P_128(SixP, M128, 14000),
    M6S_16(SixS, M16, 9000),
    M6S_32(SixS, M32, 10000),
    M6S_64(SixS, M64, 11000),
    M6S_128(SixS, M128, 12000),
    M6PS_16(SixPS, M16, 13000),
    M6PS_32(SixPS, M32, 14000),
    M6PS_64(SixPS, M64, 15000),
    M6PS_128(SixPS, M128, 16000),
    M7_32(Seven, M32, 17000),
    M7_128(Seven, M128, 20000),
    M7_256(Seven, M256, 23000),
    M7P_32(SevenP, M32, 25000),
    M7P_128(SevenP, M128, 27000),
    M7P_256(SevenP, M256, 32000),
    M8_64(Eight, M64, 30000),
    M8_256(Eight, M256, 35000),
    M8P_32(EightP, M32, 35000),
    M8P_256(EightP, M256, 40000),
    MX_64(X, M64, 40000),
    MX_256(X, M256, 45000);

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

package logic.appreciation;

import javafx.util.Pair;
import me.veppev.avitodriver.Announcement;

import java.util.Arrays;
import java.util.regex.Pattern;

import static logic.appreciation.Mark.*;
import static logic.appreciation.Memory.*;
import static logic.appreciation.Model.*;


enum Model {
    Two("2g", "2g"),
    Three("3g", "3g"),
    ThreeS("3gs", "3gs"),
    Four("4", "4"),
    FourS("4s[^i]?", "4s"),
    Five("5", "5"),
    FiveS("5(s|с)[^iep]?", "5s"),
    SE("5?se", "se"),
    Six("6", "6"),
    SixP("(6plus)|(6\\+)", "6Plus"),
    SixS("6s[^ip]?", "6s"),
    SixPS("(6splus)|(6s\\+)", "6s Plus"),
    Seven("7", "7"),
    SevenP("(7plus)|(7\\+)", "7Plus"),
    Eight("8", "8"),
    EightP("(8plus)|(8\\+)", "8Plus"),
    X("x", "X");

    private final String engName;
    private String ruName;

    Model(String engName, String ruName) {
        this.engName = engName;
        this.ruName = ruName;
    }

    public String getEngName() {
        return engName;
    }

    public boolean equalsByName(Announcement announcement) {
        String annName = announcement.getName().replaceAll(" ", "").toLowerCase();
        Pattern p = Pattern.compile("(i?phone(apple)?" + engName + ")|(айфон" + engName + ")");
        return p.matcher(annName).find();
    }

    public boolean equalsByDescription(Announcement announcement) {
        String annDesc = announcement.getDescription().replaceAll(" ", "").toLowerCase();
        Pattern p = Pattern.compile("(i?phone(apple)?" + engName + ")|(айфон" + engName + ")|(^" + engName + ")");
        return p.matcher(annDesc).find();
    }

    public static Model getModel(Announcement announcement) {
        for (int i = Model.values().length - 1; i >= 0; i--) {
            Model model = Model.values()[i];
            if (model.equalsByName(announcement)) {
                return model;
            }
        }

        for (int i = Model.values().length - 1; i >= 0; i--) {
            Model model = Model.values()[i];
            if (model.equalsByDescription(announcement)) {
                return model;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return ruName;
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

    public boolean equalsBySizeInDesc(Announcement announcement) {
        return announcement.getDescription().contains(Integer.toString(size));
    }

    public static Memory getMemory(Announcement announcement) {
        for (Memory memory : Memory.values()) {
            if (memory.equalsBySize(announcement)) {
                return memory;
            }
        }

        for (Memory memory : Memory.values()) {
            if (memory.equalsBySizeInDesc(announcement)) {
                return memory;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return size + "Gb";
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
    M7_32(Seven, M32, 20000),
    M7_128(Seven, M128, 25000),
    M7_256(Seven, M256, 27000),
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


    public static IPhone getIPhone(Model model, Memory memory) {
        for (IPhone iPhone : IPhone.values()) {
            if (iPhone.model == model && iPhone.memory == memory) {
                return iPhone;
            }
        }
        return null;
    }

    static int getMaxSellingPrice(Model model) {
        return Arrays.stream(IPhone.values())
                .filter(iphone -> iphone.model == model)
                .map(iPhone -> iPhone.sellingPrice)
                .max(Integer::compareTo).orElse(0);
    }

    static int getMinSellingPrice(Model model) {
        return Arrays.stream(IPhone.values())
                .filter(iphone -> iphone.model == model)
                .map(iPhone -> iPhone.sellingPrice)
                .min(Integer::compareTo).orElse(0);
    }

}

public class IPhoneAppraiser implements Appraiser {

    @Override
    public CheckedAnnouncement appreciate(Announcement announcement) {

        CheckedAnnouncement checkedAnnouncement = new CheckedAnnouncement(announcement);
        Model model = Model.getModel(announcement);
        Memory memory = Memory.getMemory(announcement);
        Pair<Integer, Integer> profit = new Pair<>(0, 0);

        if (model == null) {
            checkedAnnouncement.setMark(Unknown);
        } else {
            if (model == Two
                    || model == Three
                    || model == ThreeS
                    || model == Four
                    || model == FourS
                    || model == Five
                    || model == SE
                    || model == FiveS) {
                checkedAnnouncement.setMark(Negative);
            } else if (memory == null) {
                Mark mark = Mark.getMark(IPhone.getMaxSellingPrice(model)- announcement.getPrice());
                checkedAnnouncement.setMark(mark);
                profit = new Pair<>(IPhone.getMinSellingPrice(model) - announcement.getPrice(),
                        IPhone.getMaxSellingPrice(model)- announcement.getPrice());
            } else {
                IPhone iPhone = IPhone.getIPhone(model, memory);
                if (iPhone != null) {
                    int prof = iPhone.getSellingPrice() - announcement.getPrice();
                    Mark mark = getMark(prof);
                    checkedAnnouncement.setMark(mark);
                    profit = new Pair<>(prof, prof);
                }
            }
        }

        checkedAnnouncement.setModel("iPhone "
                + (model == null ? "?" : model.toString())
                + " "
                + (memory == null ? "?" : memory.toString()));
        checkedAnnouncement.setProfit(profit.getKey(), profit.getValue());

        return checkedAnnouncement;
    }
}

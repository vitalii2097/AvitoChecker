package logic.appreciation;

import me.veppev.avitodriver.Announcement;

import java.util.Objects;


public class CheckedAnnouncement {

    private Announcement announcement;
    private Mark mark;
    private String model;
    private int minProfit;
    private int maxProfit;

    public CheckedAnnouncement(Announcement announcement) {
        this.announcement = announcement;
        mark = Mark.Unknown;
    }

    public void setMark(Mark mark) {
        this.mark = mark;
    }

    public void setAnnouncement(Announcement announcement) {
        this.announcement = announcement;
    }

    public String getModel() {
        return model;
    }

    public int getMinProfit() {
        return minProfit;
    }

    public int getMaxProfit() {

        return maxProfit;
    }

    public void setProfit(int minProfit, int maxProfit) {
        this.minProfit = minProfit;
        this.maxProfit = maxProfit;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Announcement getAnnouncement() {
        return announcement;
    }

    public Mark getMark() {
        return mark;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(mark.name())
                .append(" profit (" + minProfit + " - " + maxProfit + ")")
                .append(model)
                .append('\n')
                .append('•')
                .append(announcement.getName())
                .append("\n•")
                .append(announcement.getPrice())
                .append("\n\n")
                .append(announcement.getMetro())
                .append("\n")
                .append(announcement.getOwnerName())
                .append("\n\n");
        if (announcement.getDescription().length() > 700) {
            builder.append(announcement.getDescription(), 0, 700).append("...");
        } else {
            builder.append(announcement.getDescription());
        }
        builder.append("\n=").append(announcement.getUrl());
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckedAnnouncement that = (CheckedAnnouncement) o;
        return Objects.equals(announcement, that.announcement);
    }

    @Override
    public int hashCode() {

        return Objects.hash(announcement);
    }
}
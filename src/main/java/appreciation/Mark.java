package appreciation;

public enum Mark {
    A(Integer.MAX_VALUE, 10_000),
    B(10_000, 5_000),
    C(5_000, 3_000),
    D(3_000, 1_000),
    E(1_000, 0),
    F(0, Integer.MIN_VALUE),
    NOT_MARKED(0, 0);

    private final int min;
    private final int max;

    Mark(int max, int min) {

        this.min = min;
        this.max = max;
    }

    public static Mark getMark(int profit) {
        for (Mark mark : Mark.values()) {
            if (mark.max > profit && mark.min <= profit) {
                return mark;
            }
        }
        return NOT_MARKED;
    }

    @Override
    public String toString() {
        return name() + " " + min + " - " + max;
    }
}

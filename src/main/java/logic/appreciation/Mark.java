package logic.appreciation;

public enum Mark {
    A(100_000, 10_000, 5),
    B(10_000, 5_000, 4),
    C(5_000, 3_000, 3),
    D(3_000, 1_000, 2),
    E(1_000, 0, 1),
    F(0, -100_000, 0),
    NM(0, 0, Integer.MAX_VALUE);

    private final int min;
    private final int max;
    private int value;

    public int getValue() {
        return value;
    }

    Mark(int max, int min, int value) {
        this.min = min;
        this.max = max;
        this.value = value;
    }

    public static Mark getMark(int profit) {
        for (Mark mark : Mark.values()) {
            if (mark.max > profit && mark.min <= profit) {
                return mark;
            }
        }
        return NM;
    }

    @Override
    public String toString() {
        return name() + " {"
                + (min == Integer.MIN_VALUE ? "-∞" : min)
                + "р. - "
                + (max == Integer.MAX_VALUE ? "∞" : max)
                + "р.}";
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}

package avito;

public enum Activity {
    EXTREME(1), FASTEST(10), FAST(30), MEDIUM(60), SLOW(180), SLOWEST(600);

    final long seconds;

    Activity(long seconds) {
        this.seconds = seconds;
    }
}

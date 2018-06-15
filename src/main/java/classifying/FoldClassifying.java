package classifying;

public class FoldClassifying {
    private final double accuracy;
    private final long time;

    public FoldClassifying(double accuracy, long time) {
        this.accuracy = accuracy;
        this.time = time;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "\t\tA: " + accuracy*100 + "% \t T: " + time + "ms\n";
    }
}

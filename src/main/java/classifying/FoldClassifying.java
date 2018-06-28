package classifying;

public class FoldClassifying {
    private final double accuracy;
    private final double reduction;
    private final long time;

    public FoldClassifying(double accuracy, double reduction, long time) {
        this.accuracy = accuracy;
        this.reduction = reduction;
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
        return "\t\tA: " + accuracy*100 + "% \t R: " + reduction + "% \t T: " + time + "ms\n";
    }

    public double getReduction() {
        return reduction;
    }
}

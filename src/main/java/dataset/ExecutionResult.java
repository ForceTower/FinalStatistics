package dataset;

public class ExecutionResult {
    private final double accuracy;
    private final double reduction;

    public ExecutionResult(double accuracy, double reduction) {
        this.accuracy = accuracy;
        this.reduction = reduction;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getReduction() {
        return reduction;
    }

    public static ExecutionResult createFromString(String string) {
        String[] parts = string.split(" ");
        double accuracy = Double.parseDouble(parts[0]) * -1;
        double reduction = Double.parseDouble(parts[1]) * -1;
        return new ExecutionResult(accuracy, reduction);
    }

    @Override
    public String toString() {
        return "{ Acc: " + accuracy + " <-> Red: " + reduction + " }";
    }
}

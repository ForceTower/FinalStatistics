package dataset;

public class Representative {
    private final String algorithm;
    private final String dataset;
    private final double accuracyMean;
    private final double accuracySd;
    private final double reductionMean;
    private final double reductionSd;
    private final double timeMean;
    private final double timeSd;

    public Representative(String algorithm, String dataset, double accuracyMean, double accuracySd, double reductionMean, double reductionSd, double timeMean, double timeSd) {
        this.algorithm = algorithm;
        this.dataset = dataset;
        this.accuracyMean = accuracyMean;
        this.accuracySd = accuracySd;
        this.reductionMean = reductionMean;
        this.reductionSd = reductionSd;
        this.timeMean = timeMean;
        this.timeSd = timeSd;

        if (accuracyMean < 0 || reductionMean < 0 || timeMean < 0)
            throw new RuntimeException("acc: " + accuracyMean + " __ red: " + reductionMean + " __ tim: " + timeMean);
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getDataset() {
        return dataset;
    }

    public double getAccuracyMean() {
        return accuracyMean;
    }

    public double getAccuracySd() {
        return accuracySd;
    }

    public double getReductionMean() {
        return reductionMean;
    }

    public double getReductionSd() {
        return reductionSd;
    }

    public double getTimeMean() {
        return timeMean;
    }

    public double getTimeSd() {
        return timeSd;
    }

    @Override
    public String toString() {
        return dataset + "\t" + accuracyMean + "\t" + accuracySd + "\t" + reductionMean + "\t" + reductionSd
                + "\t" + timeMean + "\t" + timeSd;
    }
}

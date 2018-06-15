package dataset;

import classifying.DatasetClassifying;

import java.util.HashMap;
import java.util.Map;

public class AlgorithmResult {
    private final String name;
    private final Map<String, DatasetClassifying> datasetResult;

    public AlgorithmResult(String name) {
        this.name = name;
        datasetResult = new HashMap<>();
    }

    public Map<String, DatasetClassifying> getDatasetResult() {
        return datasetResult;
    }

    @Override
    public String toString() {
        return name + ": \n" + datasetResult.values();
    }
}

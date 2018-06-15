package dataset;

import java.util.ArrayList;
import java.util.List;

public class DatasetResult {
    private List<FoldResult> results;

    public DatasetResult(List<FoldResult> results) {
        this.results = results;
    }

    public List<FoldResult> getResults() {
        return results;
    }
}

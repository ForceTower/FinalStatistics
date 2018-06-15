package classifying;

import java.util.ArrayList;
import java.util.List;

public class DatasetClassifying {
    private final String name;
    private final List<FoldClassifying> results;

    public DatasetClassifying(String name) {
        this.name = name;
        results = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<FoldClassifying> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return "\t" + name + ": \n" + results;
    }
}

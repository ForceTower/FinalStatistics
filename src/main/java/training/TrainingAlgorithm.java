package training;

import debug.Debug;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

//Into an Algorithm Path
//**BASE**/ALGORITHM
public class TrainingAlgorithm {
    private final String name;
    private final HashMap<String, TrainingDataset> results;

    TrainingAlgorithm(String name) {
        this.name = name;
        this.results = new HashMap<>();
        Debug.println(" -- Created Training Algorithm class with Name: " + name + " -- ");
    }

    void processAlgorithmFolder(Path path) {
        try {
            Files.list(path).forEach(dataset -> {
                String name = dataset.getFileName().toString();
                Debug.println("[Original]  Dataset Name: " + name);
                int index = name.lastIndexOf("-");
                name = name.substring(0, index);
                Debug.println("[Corrected] Dataset Name: " + name);

                TrainingDataset training = results.getOrDefault(name, new TrainingDataset(name));
                training.processResultFolder(dataset);
                results.put(name, training);
            });
            Debug.println("Datasets Size: " + results.entrySet().size());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public String getName() {
        return name;
    }

    public HashMap<String, TrainingDataset> getResults() {
        return results;
    }
}

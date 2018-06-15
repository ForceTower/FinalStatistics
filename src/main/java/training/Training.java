package training;

import debug.Debug;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class Training {
    private final HashMap<String, TrainingAlgorithm> algorithms;

    public Training() {
        algorithms = new HashMap<>();
    }

    public void execute(Path folder) {
        try {
            Files.list(folder).forEach(path -> {
                //for each subfolder in files - for each algorithm
                TrainingAlgorithm algorithm = algorithms.getOrDefault(
                        path.getFileName().toString(),
                        new TrainingAlgorithm(path.getFileName().toString())
                );

                algorithm.processAlgorithmFolder(path);
                algorithms.put(path.getFileName().toString(), algorithm);
            });
            Debug.println("Algorithms Size: " + algorithms.entrySet().size());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public HashMap<String, TrainingAlgorithm> getAlgorithms() {
        return algorithms;
    }
}

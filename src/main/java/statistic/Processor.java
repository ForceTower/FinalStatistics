package statistic;

import dataset.ExecutionResult;
import dataset.Representative;
import debug.Debug;
import training.Training;
import training.TrainingDataset;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Processor {
    private Training training;
    private List<Representative> representatives;

    private Processor(){}

    public static Processor create() {
        return new Processor();
    }

    public Processor with(Training training) {
        this.training = training;
        return this;
    }

    public void execute() {
        if (training != null) {
            trainingStats();
        }
    }

    private void trainingStats() {
        representatives = new ArrayList<>();
        this.training.getAlgorithms().forEach((algorithm, trainingAg) -> {
            Debug.println("Generating Statistics for " + algorithm);
            System.out.println("\n -------- " + algorithm + " -------- ");
            trainingAg.getResults().entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).forEach((entry) -> {
                String dataset = entry.getKey();
                TrainingDataset trainingDs = entry.getValue();
                Debug.println("Current Dataset: " + dataset);
                List<ExecutionResult> executions = trainingDs.getAllExecutions();
                List<Double> executionTimers     = trainingDs.getExecutionTimes();
                Representative representative = getRepresentative(algorithm, dataset, executions, executionTimers);
                System.out.println(representative);
                representatives.add(representative);
            });
        });

        Map<String, List<Representative>> map = representatives.stream().collect(Collectors.groupingBy(Representative::getAlgorithm));
        new Statistics().testDifferenceAcc(map);
        new Statistics().testDifferenceRed(map);
        new Statistics().testDifferenceTime(map);
    }

    private Representative getRepresentative(String algorithm, String dataset, List<ExecutionResult> executions, List<Double> executionTimers) {
        double accMean = executions.stream().mapToDouble(ExecutionResult::getAccuracy).average().orElse(-1);
        double accSd   = Math.sqrt(executions.stream().mapToDouble(value -> Math.pow(value.getAccuracy() - accMean, 2)).sum()/executions.size());

        double redMean = executions.stream().mapToDouble(ExecutionResult::getReduction).average().orElse(-1);
        double redSd   = Math.sqrt(executions.stream().mapToDouble(value -> Math.pow(value.getReduction() - redMean, 2)).sum()/executions.size());

        double timMean = executionTimers.stream().mapToDouble(Double::doubleValue).average().orElse(-1);
        double timSd   = Math.sqrt(executionTimers.stream().mapToDouble(value -> Math.pow(value - timMean, 2)).sum()/executionTimers.size());

        return new Representative(
                algorithm, dataset,
                accMean, accSd,
                redMean, redSd,
                timMean, timSd
        );
    }
}

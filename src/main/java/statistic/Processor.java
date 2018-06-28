package statistic;

import classifying.Classifying;
import classifying.DatasetClassifying;
import classifying.FoldClassifying;
import dataset.AlgorithmResult;
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
    private List<Representative> representativesTraining;
    private List<Representative> representativesClassifyingJ48;
    private List<Representative> representativesClassifyingIBk;
    private Classifying classifying;

    private Processor(){}

    public static Processor create() {
        return new Processor();
    }

    public Processor with(Training training) {
        this.training = training;
        return this;
    }

    public Processor with(Classifying classifying) {
        this.classifying = classifying;
        return this;
    }

    public void execute() {
        if (training != null) {
            trainingStats();
        }

        if (classifying != null) {
            classifyingStats();
        }
    }

    private void trainingStats() {
        representativesTraining = new ArrayList<>();
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
                representativesTraining.add(representative);
            });
        });

        Map<String, List<Representative>> map = representativesTraining.stream().collect(Collectors.groupingBy(Representative::getAlgorithm));
        new Statistics().testDifferenceAcc(map);
        new Statistics().testDifferenceRed(map);
        new Statistics().testDifferenceTime(map);
    }

    private void classifyingStats() {
        representativesClassifyingJ48 = new ArrayList<>();
        System.out.println("\n----------------------------------------------------------");
        System.out.println("                   --------  J48 -------- ");
        System.out.println("\n----------------------------------------------------------");
        this.classifying.getAlgorithmMapJ48().forEach((algorithm, agResult) ->
                generateRepresentatives(algorithm, agResult, representativesClassifyingJ48));

        Map<String, List<Representative>> mapJ48 = representativesClassifyingJ48.stream().collect(Collectors.groupingBy(Representative::getAlgorithm));
        new Statistics().testDifferenceAcc(mapJ48);
        new Statistics().testDifferenceRed(mapJ48);
        new Statistics().testDifferenceTime(mapJ48);

        representativesClassifyingIBk = new ArrayList<>();
        System.out.println("\n\n\n----------------------------------------------------------");
        System.out.println("                   --------  KNN -------- ");
        System.out.println("\n----------------------------------------------------------");
        this.classifying.getAlgorithmMapKNN().forEach((algorithm, agResult) ->
                generateRepresentatives(algorithm, agResult, representativesClassifyingIBk));

        Map<String, List<Representative>> mapKnn = representativesClassifyingIBk.stream().collect(Collectors.groupingBy(Representative::getAlgorithm));
        new Statistics().testDifferenceAcc(mapKnn);
        new Statistics().testDifferenceRed(mapKnn);
        new Statistics().testDifferenceTime(mapKnn);
    }

    private void generateRepresentatives(String algorithm, AlgorithmResult agResult, List<Representative> representativesClassifyingIBk) {
        Debug.println("Generating Statistics for " + algorithm);
        System.out.println("\n -------- " + algorithm + " -------- ");
        agResult.getDatasetResult().entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).forEach(entry -> {
            String dataset = entry.getKey();
            DatasetClassifying classifyingDs = entry.getValue();
            Debug.println("Current Dataset: " + dataset);
            List<FoldClassifying> results = classifyingDs.getResults();
            Representative representative = getRepresentative(algorithm, dataset, results);
            System.out.println(representative);
            representativesClassifyingIBk.add(representative);
        });
    }

    private Representative getRepresentative(String algorithm, String dataset, List<FoldClassifying> results) {
        double accMean = results.stream().mapToDouble(FoldClassifying::getAccuracy).average().orElse(-1);
        double accSd = Math.sqrt(results.stream().mapToDouble(value -> Math.pow(value.getAccuracy() - accMean, 2)).sum()/results.size());

        double redMean = results.stream().mapToDouble(FoldClassifying::getReduction).average().orElse(-1);
        double redSd = Math.sqrt(results.stream().mapToDouble(value -> Math.pow(value.getReduction() - redMean, 2)).sum()/results.size());

        double timMean = results.stream().mapToDouble(FoldClassifying::getTime).average().orElse(-1);
        double timSd = Math.sqrt(results.stream().mapToDouble(value -> Math.pow(value.getTime() - timMean, 2)).sum()/results.size());

        return new Representative(
                algorithm, dataset,
                accMean, accSd,
                redMean, redSd,
                timMean, timSd
        );
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

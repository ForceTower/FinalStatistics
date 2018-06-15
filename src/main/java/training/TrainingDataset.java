package training;

import dataset.ExecutionResult;
import dataset.FoldResult;
import dataset.Variable;
import debug.Debug;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//Into a fold folder
//**BASE**/ALGORITHM/DATASET-NUMBER
public class TrainingDataset {
    private final String name;
    private final List<FoldResult> results;

    public TrainingDataset(String name) {
        this.name = name;
        this.results = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<FoldResult> getResults() {
        return results;
    }

    public void addResult(FoldResult result) {
        this.results.add(result);
    }

    void processResultFolder(Path dataset) {
        try {
            Files.list(dataset).forEach(file -> {
                if (!file.getFileName().toString().startsWith("FUN")) {
                    Debug.println("Ignored file: " + file.getFileName().toString());
                    return;
                }

                runUsing(file);
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void runUsing(Path fun) {
        String funName = fun.getFileName().toString();
        String filename = funName.split("\\.")[0];
        int run = Integer.parseInt(filename.charAt(filename.length() - 1) + "");

        File varFile = new File(fun.toFile().getParent(), "VAR" + run + ".tsv");
        Path var = varFile.toPath();

        executeOperations(fun, var);
    }

    private void executeOperations(Path fun, Path var) {
        try {
            List<ExecutionResult> executions = Files.lines(fun)
                    .filter(s -> !s.isEmpty())
                    .filter(s -> !s.startsWith("Time:"))
                    .map(ExecutionResult::createFromString)
                    .collect(Collectors.toList());

            double time = Double.parseDouble(Files.lines(fun)
                    .filter(s -> !s.isEmpty())
                    .filter(s -> s.startsWith("Time:"))
                    .limit(1)
                    .collect(Collectors.toList())
                    .get(0)
                    .split(":")[1]);

            List<Variable> variables = Files.lines(var)
                    .filter(s -> !s.isEmpty())
                    .map(Variable::createFromLine)
                    .collect(Collectors.toList());

            FoldResult result = new FoldResult(time);
            result.setExecutions(executions);
            result.setVariables(variables);
            results.add(result);

            Debug.println("Executions: " + executions);
            Debug.println("Variables: " + variables);
            Debug.println("Time: " + time);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public List<ExecutionResult> getAllExecutions() {
        List<ExecutionResult> executions = new ArrayList<>();
        results.forEach(f -> executions.addAll(f.getExecutions()));
        return executions;
    }

    public List<Double> getExecutionTimes() {
        return results.stream().mapToDouble(FoldResult::getTime).boxed().collect(Collectors.toList());
    }
}

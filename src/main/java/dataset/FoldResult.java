package dataset;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a FUN[NUMBER].tsv file and a VAR[NUMBER].tsv file
 */
public class FoldResult {
    private final List<ExecutionResult> executions;
    private final List<Variable> variables;
    private final double time;


    public FoldResult(double time) {
        this.executions = new ArrayList<>();
        this.variables = new ArrayList<>();
        this.time = time;
    }

    public void addResult(ExecutionResult result) {
        this.executions.add(result);
    }

    public void addVariable(Variable variable) {
        this.variables.add(variable);
    }

    public List<ExecutionResult> getExecutions() {
        return executions;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public double getTime() {
        return time;
    }

    public void setVariables(List<Variable> variables) {
        this.variables.addAll(variables);
    }

    public void setExecutions(List<ExecutionResult> executions) {
        this.executions.addAll(executions);
    }
}

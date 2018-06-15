package dataset;

import java.util.BitSet;

public class Variable {
    private final String variable;
    private final BitSet bits;

    public Variable(String variable) {
        this.variable = variable;
        bits = new BitSet(variable.length());
        createBitSet();
    }

    private void createBitSet() {
        for (int i = 0; i < variable.length(); i++) bits.set(i, variable.charAt(i) == '1');
    }

    public String getVariable() {
        return variable;
    }

    public BitSet getBits() {
        return bits;
    }

    public static Variable createFromLine(String line) {
        String string = line.split(" ")[0];
        return new Variable(string);
    }

    @Override
    public String toString() {
        return "{ Var: " + variable + " }";
    }
}

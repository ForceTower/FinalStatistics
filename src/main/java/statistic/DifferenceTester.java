package statistic;

import debug.Debug;
import javanpst.data.structures.dataTable.DataTable;
import javanpst.tests.multiple.friedmanTest.FriedmanTest;
import javanpst.tests.oneSample.wilcoxonTest.WilcoxonTest;
import util.Pair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static statistic.ArrayUtils.transpose;

public class DifferenceTester {

    public static void executeFriedmanTest(double[][] samples, Map<Integer, String> algorithmNames) {
        DataTable data = new DataTable(samples);
        FriedmanTest friedman = new FriedmanTest(data);
        friedman.doTest();

        Debug.println("Results of Friedman test:\n"+friedman.printReport());
        System.out.println("Friedman P-Value: " + friedman.getPValue());

        if (friedman.getPValue() > 0.05) {
            System.out.println("No significant difference");
            return;
        }

        System.out.println("Results of the multiple comparisons procedure:\n" + friedman.printMultipleComparisonsProcedureReport());
        System.out.println(" -> There is significant difference <- ");

        findOutWhatIsDifferent(friedman, data, algorithmNames);
    }

    private static void findOutWhatIsDifferent(FriedmanTest friedman, DataTable data, Map<Integer, String> algorithmNames) {
        try {
            Field criticalZ95Field = FriedmanTest.class.getDeclaredField("criticalZ95");
            criticalZ95Field.setAccessible(true);
            double criticalZ95 = criticalZ95Field.getDouble(friedman);
            System.out.println("Critical Z(95%): " + criticalZ95);
            System.out.println("Anyone that is greater than or equal to it is considered different");

            Field avgRanksField = FriedmanTest.class.getDeclaredField("avgRanks");
            avgRanksField.setAccessible(true);
            double[] avgRanks = (double[]) avgRanksField.get(friedman);

            List<Pair<Integer, Integer>> differences = new ArrayList<>();

            for (int first = 0; first < data.getColumns(); first++) {
                for(int second = first + 1; second < data.getColumns(); second++) {
                    double Z = Math.abs(avgRanks[first] - avgRanks[second]);
                    if (Z >= criticalZ95) {
                        System.out.println("Found significant difference between " + algorithmNames.get(first) + " and " + algorithmNames.get(second) + " [Z = " + Z + "]");
                        differences.add(new Pair<>(first, second));
                    }
                }
            }

            if (differences.isEmpty()) {
                System.out.println("The difference where not found... Rest in peace");
                return;
            }

            betterCallWilcoxon(differences, data, algorithmNames);

        } catch (Exception e) {
            System.out.println("Reflection error");
            e.printStackTrace();
        }
    }

    private static void betterCallWilcoxon(List<Pair<Integer, Integer>> differences, DataTable data, Map<Integer, String> algorithmNames) {
        differences.forEach(pair -> {
            double[][] array = new double[2][];
            array[0] = data.getColumn(pair.first);
            array[1] = data.getColumn(pair.second);

            array = transpose(array);

            DataTable newData = new DataTable(array);
            WilcoxonTest wilcoxon = new WilcoxonTest(newData);
            wilcoxon.doTest();
            System.out.println("Printing Wilcoxon Test for " + algorithmNames.get(pair.first) + " and " + algorithmNames.get(pair.second));
            System.out.println(wilcoxon.printReport());
        });
    }
}

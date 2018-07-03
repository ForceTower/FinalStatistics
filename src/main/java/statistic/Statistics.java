package statistic;

import dataset.Representative;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static statistic.ArrayUtils.transpose;
import static statistic.DifferenceTester.executeFriedmanTest;

@SuppressWarnings("Duplicates")
public class Statistics {
    private int position = 0;

    void testDifferenceAcc(Map<String, List<Representative>> hash) {
        position = 0;
        Map<Integer, String> algPosition = new HashMap<>();
        int algorithms = hash.entrySet().size();
        Double[][] array = new Double[algorithms][];


        hash.forEach((algorithm, representatives) -> {
            algPosition.put(position, algorithm);
            Double[] blank = new Double[0];
            List<Double> list = representatives.stream()
                    .sorted(Comparator.comparing(Representative::getDataset))
                    .mapToDouble(Representative::getAccuracyMean)
                    .boxed()
                    .collect(Collectors.toList());
            array[position] = list.toArray(blank);
            position++;
        });

        System.out.println();
        System.out.println("---------------------------------------------------");
        System.out.println("           ---    Accuracy Array    ---");
        System.out.println("---------------------------------------------------");
        for (int i = 0; i < algPosition.entrySet().size(); i++) System.out.print("[" + (i + 1) + " is " + algPosition.get(i) + "] ");
        System.out.println();

        double[][] transposed = transpose(array);
        /*
        for (double[] dd : transposed) {
            System.out.println(Arrays.toString(dd));
        }
        */
        executeFriedmanTest(transposed, algPosition);
    }

    void testDifferenceRed(Map<String, List<Representative>> hash) {
        position = 0;
        //Each dataset has it's representatives in each algorithm
        Map<Integer, String> algPosition = new HashMap<>();
        int algorithms = hash.entrySet().size();
        Double[][] array = new Double[algorithms][];


        hash.forEach((algorithm, representatives) -> {
            algPosition.put(position, algorithm);
            Double[] blank = new Double[0];
            List<Double> list = representatives.stream()
                    .sorted(Comparator.comparing(Representative::getDataset))
                    .mapToDouble(Representative::getReductionMean)
                    .boxed()
                    .collect(Collectors.toList());
            array[position] = list.toArray(blank);
            position++;
        });

        System.out.println();
        System.out.println("---------------------------------------------------");
        System.out.println("           ---    Reduction Array    ---");
        System.out.println("---------------------------------------------------");
        for (int i = 0; i < algPosition.entrySet().size(); i++) System.out.print("[" + (i+1) + " is " + algPosition.get(i) + "] ");
        System.out.println();

        double[][] transposed = transpose(array);
        /*
        for (double[] dd : transposed) {
            System.out.println(Arrays.toString(dd));
        }
        */
        executeFriedmanTest(transposed, algPosition);
    }

    void testDifferenceTime(Map<String, List<Representative>> hash) {
        position = 0;
        //Each dataset has it's representatives in each algorithm
        Map<Integer, String> algPosition = new HashMap<>();
        int algorithms = hash.entrySet().size();
        Double[][] array = new Double[algorithms][];


        hash.forEach((algorithm, representatives) -> {
            algPosition.put(position, algorithm);
            Double[] blank = new Double[0];
            List<Double> list = representatives.stream()
                    .sorted(Comparator.comparing(Representative::getDataset))
                    .mapToDouble(Representative::getTimeMean)
                    .boxed()
                    .collect(Collectors.toList());
            array[position] = list.toArray(blank);
            position++;
        });

        System.out.println();
        System.out.println("---------------------------------------------------");
        System.out.println("              ---    Time Array    ---");
        System.out.println("---------------------------------------------------");
        for (int i = 0; i < algPosition.entrySet().size(); i++) System.out.print("[" + (i+1) + " is " + algPosition.get(i) + "] ");
        System.out.println();

        double[][] transposed = transpose(array);
        /*
        for (double[] dd : transposed) {
            System.out.println(Arrays.toString(dd));
        }
        */
        executeFriedmanTest(transposed, algPosition);
    }
}

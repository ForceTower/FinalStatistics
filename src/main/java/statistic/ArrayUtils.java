package statistic;

import java.util.Arrays;

@SuppressWarnings("Duplicates")
public class ArrayUtils {

    public static double[][] transpose(Double[][] array) {
        if (array == null || array.length == 0)
            throw new RuntimeException("An invalid array was caught " + Arrays.deepToString(array));

        int width = array.length;
        int height = array[0].length;

        double[][] newArray = new double[height][width];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                newArray[y][x] = array[x][y];
            }
        }
        return newArray;
    }

    public static double[][] transpose(double[][] array) {
        if (array == null || array.length == 0)
            throw new RuntimeException("An invalid array was caught " + Arrays.deepToString(array));

        int width = array.length;
        int height = array[0].length;

        double[][] newArray = new double[height][width];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                newArray[y][x] = array[x][y];
            }
        }
        return newArray;
    }
}

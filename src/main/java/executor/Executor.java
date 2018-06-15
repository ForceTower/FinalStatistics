package executor;

import classifying.Classifying;
import statistic.Processor;
import training.Training;

import java.io.IOException;
import java.nio.file.Paths;

public class Executor {
    private final Training training;
    private final Classifying classifying;

    private Executor() {
        training = new Training();
        classifying = new Classifying();
    }

    private void all() throws IOException {
        //trainingStats();
        classificationStats();
    }

    private void trainingStats() {
        //runs the training to fill with data
        training.execute(Paths.get("C:\\Outros\\IC_Stats\\small\\data"));
        //execute the statistics to display table
        Processor.create().with(training).execute();
    }

    private void classificationStats() throws IOException {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("---------------------------  Classification  -----------------------------");
        System.out.println("--------------------------------------------------------------------------");
        //prepare all the data to be used
        classifying.execute(Paths.get("C:\\Outros\\IC_Stats\\executor\\small"), Paths.get("C:\\Users\\joaop\\Desktop\\Small"));

        System.out.println("J48");
        System.out.println(classifying.getAlgorithmMapJ48().values());
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("KNN");
        System.out.println(classifying.getAlgorithmMapJ48().values());
    }

    public static void main(String[] args) throws IOException {
        new Executor().all();
    }
}

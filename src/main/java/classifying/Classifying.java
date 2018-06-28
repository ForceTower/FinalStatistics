package classifying;

import dataset.AlgorithmResult;
import dataset.DatasetResult;
import dataset.FoldResult;
import debug.Debug;
import util.Pair;
import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.core.Instances;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Classifying {
    private final Map<String, AlgorithmResult> algorithmMapJ48;
    private final Map<String, AlgorithmResult> algorithmMapKNN;
    private Path testFiles;

    public Classifying() {
        algorithmMapJ48 = new HashMap<>();
        algorithmMapKNN = new HashMap<>();
    }

    public void execute(Path underTest, Path testFiles, Path originalFiles) throws IOException {
        this.testFiles = testFiles;

        Files.list(underTest).forEach(algorithmDir -> {
            try {
                Files.list(algorithmDir).forEach(datasetDir -> {
                    try {
                        String datasetName = datasetDir.toFile().getName();
                        Files.list(datasetDir)
                                .filter(path -> !path.toFile().getName().contains("tst.arff"))
                                .filter(path -> path.toFile().getName().endsWith(".arff"))
                                .forEach(path -> {
                            Pair<Path, Path> both = findTestFileFor(path.toFile(), new File(testFiles.toFile(), datasetName), new File(originalFiles.toFile(), datasetName));
                            executeClassification(path, both.first, both.second, algorithmDir.toFile().getName(), datasetName);
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(0);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        });
    }

    private Pair<Path, Path> findTestFileFor(File subject, File testFolder, File originalFolder) {
        String subjectName = subject.getName();
        String testFolderName = testFolder.getName();
        String originalFolderName = originalFolder.getName();
        Debug.println("------- Fit subject --------");
        Debug.println("Test Folder: " + testFolderName);
        Debug.println("Subject Name: " + subjectName);
        String[] parts = subjectName.split("-");
        int length = parts.length;
        String value = parts[length - 1];
        String foldNumberStr;
        if (value.contains("red_")) {
            String[] important = value.split("red_");
            foldNumberStr = important[0];
            String executionStr = important[1];
        } else {
            String[] important = value.split("tra");
            foldNumberStr = important[0];
        }

        String testFileName = testFolderName + "-" + parts[length - 2] + "-" + foldNumberStr + "tst.arff";
        String originalFileName = originalFolderName + "-" + parts[length - 2] + "-" + foldNumberStr + "tra.arff";
        Debug.println("Fit Test File: " + testFileName);
        File test = new File(testFolder, testFileName);
        File orig = new File(originalFolder, originalFileName);
        return new Pair<>(test.toPath(), orig.toPath());
    }

    private void executeClassification(Path path, Path test, Path original, String algorithm, String dataset) {
        try {
            File under = path.toFile();
            File testing = test.toFile();
            File orig = original.toFile();

            Debug.println("Testing " + under.getName() + " with " + testing.getName());
            Instances instUnder = new Instances(new FileReader(under));
            Instances instTests = new Instances(new FileReader(testing));
            Instances instOrig  = new Instances(new FileReader(orig));
            if (instUnder.classIndex() == -1) instUnder.setClassIndex(instUnder.numAttributes() - 1);
            if (instTests.classIndex() == -1) instTests.setClassIndex(instTests.numAttributes() - 1);
            if (instOrig.classIndex() == -1) instOrig.setClassIndex(instOrig.numAttributes() - 1);

            //double reduction = (instUnder.numInstances()/(double)instOrig.numInstances())*100;
            double reduction = ((instOrig.numInstances() - instUnder.numInstances())/(double)instOrig.numInstances())*100;
            Debug.println("Reduction: " + reduction);

            J48 j48 = new J48();
            double j48acc;

            long j48start = System.currentTimeMillis();
            Evaluation j48evaluation = new Evaluation(instUnder);
            j48.buildClassifier(instUnder);
            //Classify the test into the reduced
            j48evaluation.evaluateModel(j48, instTests);

            //Gets the number of correct classifications of test
            j48acc = j48evaluation.correct();
            long j48end = System.currentTimeMillis();

            long j48time = j48end - j48start;
            j48acc = j48acc / instTests.numInstances();
            Debug.println("[On J48]");
            Debug.println("Time: " + j48time + "ms :: Accuracy: " + j48acc);

            AlgorithmResult algorithmResultJ48 = algorithmMapJ48.getOrDefault(algorithm, new AlgorithmResult(algorithm));
            DatasetClassifying datasetResultJ48 = algorithmResultJ48.getDatasetResult().getOrDefault(dataset, new DatasetClassifying(dataset));

            datasetResultJ48.getResults().add(new FoldClassifying(j48acc, reduction, j48time));

            algorithmResultJ48.getDatasetResult().put(dataset, datasetResultJ48);
            algorithmMapJ48.put(algorithm, algorithmResultJ48);

            IBk knn = new IBk(5);
            double knnAcc;

            long knnStart = System.currentTimeMillis();
            Evaluation knnEvaluation = new Evaluation(instUnder);
            knn.buildClassifier(instUnder);
            //Classify the test into the reduced
            knnEvaluation.evaluateModel(knn, instTests);

            //Gets the number of correct classifications of test
            knnAcc = knnEvaluation.correct();
            long knnEnd = System.currentTimeMillis();

            long knnTime = knnEnd - knnStart;
            knnAcc = knnAcc / instTests.numInstances();
            Debug.println("[On IBk(5)]");
            Debug.println("Time: " + knnTime + "ms :: Accuracy: " + knnAcc);

            AlgorithmResult algorithmResultKnn = algorithmMapKNN.getOrDefault(algorithm, new AlgorithmResult(algorithm));
            DatasetClassifying datasetResultKNN = algorithmResultKnn.getDatasetResult().getOrDefault(dataset, new DatasetClassifying(dataset));

            datasetResultKNN.getResults().add(new FoldClassifying(knnAcc, reduction, knnTime));

            algorithmResultKnn.getDatasetResult().put(dataset, datasetResultKNN);
            algorithmMapKNN.put(algorithm, algorithmResultKnn);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public Map<String, AlgorithmResult> getAlgorithmMapJ48() {
        return algorithmMapJ48;
    }

    public Map<String, AlgorithmResult> getAlgorithmMapKNN() {
        return algorithmMapKNN;
    }
}

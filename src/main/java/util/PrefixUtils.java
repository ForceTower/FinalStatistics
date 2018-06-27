package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class PrefixUtils {

    public static void removePrefix(String prefix, Path path, Path destination, boolean removeTest) {
        try {
            String name = path.toFile().getName();
            String oldName = name;
            name = name.replace(prefix, "");
            if (path.toFile().isDirectory()) {
                System.out.println("Dir: " + oldName + " -> " + name);
                File file = new File(destination.toFile(), name);
                boolean newFile = file.mkdirs();
                Files.list(path).forEach(sub -> removePrefix(prefix, sub, file.toPath(), removeTest));
            } else {
                if (!(name.contains("tst.dat") && removeTest)) {
                    Files.copy(path, new File(destination.toFile(), name).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("File: " + oldName + " -> " + name);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        removePrefix("RNG-TSS.", Paths.get("C:\\Users\\joaop\\Desktop\\datasets"), Paths.get("C:\\Users\\joaop\\Desktop\\fixed"), true);
        parseFile(Paths.get("C:\\Users\\joaop\\Desktop\\fixed"));
    }

    public static void parseFile(Path path) {
        try {
            if (path.toFile().isDirectory()) {
                Files.list(path).forEach(PrefixUtils::parseFile);
            } else {
                DatFixer.fixDatFormat(path.toFile());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.ga.banking.with.java.helpers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CommonUtil {
    public static void printSeparatorLine() {
        System.out.println("-".repeat(100));
    }

    public static void createDirectoriesAndWriteFile(String fileContent, String fileName, Path dataPath,
                                                     Path nestedDataPath) throws IOException {
        if (Files.notExists(dataPath)) {
            Files.createDirectory(dataPath);
        }

        if (nestedDataPath != null && Files.notExists(nestedDataPath)) {
            Files.createDirectory(nestedDataPath);
        }

        Path path = nestedDataPath == null ? dataPath.resolve(fileName) : nestedDataPath.resolve(fileName);


        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(fileContent);
        }
    }
}

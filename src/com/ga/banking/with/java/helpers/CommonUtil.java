package com.ga.banking.with.java.helpers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CommonUtil {
    public static void printSeparatorLine() {
        System.out.println("-".repeat(100));
    }

    public static void createDirectoriesAndWriteFile(String fileContent, String fileName, Path dataPath, Path bankersPath) throws IOException {
        if (Files.notExists(dataPath)) {
            Files.createDirectory(dataPath);
        }

        if (Files.notExists(bankersPath)) {
            Files.createDirectory(bankersPath);
        }

        Path path = bankersPath.resolve(fileName);


        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(fileContent);
        }
    }
}

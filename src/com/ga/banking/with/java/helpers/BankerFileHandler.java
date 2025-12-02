package com.ga.banking.with.java.helpers;

import com.ga.banking.with.java.interfaces.FileHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class BankerFileHandler implements FileHandler {

    @Override
    public String readFromFile(String filePath) {
        // Implementation for reading banker-specific data from file
        return "Banker data read from " + filePath;
    }

    @Override
    public boolean writeToFile(String bankerName, String bankerId, String fileContent) {
        try {
            String fileName = "Banker-" + bankerName + "-" + bankerId + ".json";
            Path path = Paths.get("Bankers", fileName);
            Path parent = path.getParent();
            if (parent != null && Files.notExists(parent)) {
                Files.createDirectories(parent);
            }

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write(fileContent);
            }
        } catch (Exception e) {
            System.out.println("An error occurred while writing to the banker file.");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

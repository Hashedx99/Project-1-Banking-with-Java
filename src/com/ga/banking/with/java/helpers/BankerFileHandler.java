package com.ga.banking.with.java.helpers;

import com.ga.banking.with.java.interfaces.FileHandler;

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
            String fileName = "Bankers/Banker-" + bankerName + "-" + bankerId + ".json";
            Path workingPath = Paths.get(System.getProperty("user.dir"), "Bankers", fileName);
            Files.createDirectories(workingPath);
            Files.writeString(workingPath, fileContent);
        } catch (Exception e) {
            System.out.println("An error occurred while writing to the banker file.");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

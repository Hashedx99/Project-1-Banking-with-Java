package com.ga.banking.with.java.helpers;

import com.ga.banking.with.java.entities.Banker;
import com.ga.banking.with.java.entities.User;
import com.ga.banking.with.java.interfaces.FileHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class BankerFileHandler implements FileHandler {

    @Override
    public User readFromFile(File file) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, User.class);
    }

    @Override
    public boolean writeToFile(String bankerName, String bankerId, String fileContent) {
        try {
            String fileName = "Banker-" + bankerName + "-" + bankerId + ".json";
            Path dataPath = Paths.get("Data");
            Path bankersPath = dataPath.resolve("Bankers");

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
        } catch (Exception e) {
            System.out.println("An error occurred while writing to the banker file.");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

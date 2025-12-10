package com.ga.banking.with.java.service;

import com.ga.banking.with.java.entities.Banker;
import com.ga.banking.with.java.entities.User;
import com.ga.banking.with.java.interfaces.FileHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.ga.banking.with.java.helpers.CommonUtil.createDirectoriesAndWriteFile;


public class BankerFileHandler implements FileHandler {

    @Override
    public User readFromFile(Object file) {
        if (!(file instanceof File)) {
            System.out.println("Invalid file content for banker. Expected a File.");
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue((File) file, Banker.class);
    }

    @Override
    public boolean writeToFile(String bankerName, String bankerId, Object fileContent) {

        if (!(fileContent instanceof String)) {
            System.out.println("Invalid file content for banker. Expected a JSON string.");
            return false;
        }
        try {
            String fileName = "Banker-" + bankerName + "-" + bankerId + ".json";
            Path dataPath = Paths.get("Data");
            Path bankersPath = dataPath.resolve("Bankers");

            createDirectoriesAndWriteFile((String) fileContent, fileName, dataPath, bankersPath, false);
        } catch (Exception e) {
            System.out.println("An error occurred while writing to the banker file.");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}

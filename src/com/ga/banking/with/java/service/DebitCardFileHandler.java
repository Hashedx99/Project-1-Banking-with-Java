package com.ga.banking.with.java.service;

import com.ga.banking.with.java.entities.DebitCard;
import com.ga.banking.with.java.interfaces.FileHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.ga.banking.with.java.helpers.CommonUtil.createDirectoriesAndWriteFile;

public class DebitCardFileHandler implements FileHandler {

    @Override
    public DebitCard readFromFile(Object accountId) {
        if (!(accountId instanceof String)) {
            System.out.println("Invalid accountId. Expected a String.");
            return null;
        }
        String fileName = accountId + ".json";
        Path dataPath = Paths.get("Data");
        Path debitCardsPath = dataPath.resolve("DebitCards");
        File file = debitCardsPath.resolve(fileName).toFile();
        ObjectMapper mapper = new ObjectMapper();
        return  mapper.readValue(file, DebitCard.class);
    }

    @Override
    public boolean writeToFile(String name, String accountId, Object fileContent) {
        if (!(fileContent instanceof String)) {
            System.out.println("Invalid file content for debitCard. Expected a JSON string.");
            return false;
        }
        try {
            String fileName = accountId + ".json";
            Path dataPath = Paths.get("Data");
            Path debitCardsPath = dataPath.resolve("DebitCards");

            createDirectoriesAndWriteFile((String) fileContent, fileName, dataPath, debitCardsPath, false);
        } catch (Exception e) {
            System.out.println("An error occurred while writing to the Debit Cards file.");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

}

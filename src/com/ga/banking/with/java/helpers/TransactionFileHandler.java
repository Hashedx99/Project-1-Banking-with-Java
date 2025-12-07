package com.ga.banking.with.java.helpers;

import com.ga.banking.with.java.entities.Transaction;
import com.ga.banking.with.java.interfaces.FileHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

import static com.ga.banking.with.java.helpers.CommonUtil.createDirectoriesAndWriteFile;

public class TransactionFileHandler implements FileHandler {
    @Override
    public Object readFromFile(Object userId) {
        if (!(userId instanceof String)) {
            System.out.println("Invalid userId. Expected a String.");
            return null;
        }
        String fileName = userId + ".json";
        Path dataPath = Paths.get("Data");
        Path transactionsDataPath = dataPath.resolve("Transactions");
        Path userTransactionsPath = transactionsDataPath.resolve((String) userId);
        File[] files = userTransactionsPath.resolve(fileName).toFile().listFiles();
        if (files == null || files.length == 0) {
            System.out.println("No transaction files found for userId: " + userId);
            return null;
        }
        Stream<File> fileStream = Stream.of(files);
        return fileStream.map(file -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(file, Transaction.class);
            } catch (Exception e) {
                System.out.println("Error reading transaction file: " + file.getName());
                return null;
            }
        }).filter(Objects::nonNull).toList();
    }

    @Override
    public boolean writeToFile(String id, String transactionId, Object fileContent) {
        if (!(fileContent instanceof String)) {
            System.out.println("Invalid file content for Transaction. Expected a JSON string.");
            return false;
        }
        try {
            String fileName = transactionId + ".json";
            Path dataPath = Paths.get("Data");
            Path transactionsDataPath = dataPath.resolve("Transactions");
            Path userTransactionsPath = transactionsDataPath.resolve(id);

            createDirectoriesAndWriteFile((String) fileContent, fileName, dataPath, userTransactionsPath);
        } catch (Exception e) {
            System.out.println("An error occurred while writing to the Transactions file.");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}

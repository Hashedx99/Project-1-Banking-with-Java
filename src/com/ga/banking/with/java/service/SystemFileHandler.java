package com.ga.banking.with.java.service;

import com.ga.banking.with.java.interfaces.FileHandler;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.ga.banking.with.java.helpers.CommonUtil.createDirectoriesAndWriteFile;

public class SystemFileHandler implements FileHandler {

    @Override
    public String readFromFile(Object filePath) {
        return null;
    }

    @Override
    public boolean writeToFile(String unused, String unused1, Object content) {
        if (!(content instanceof String)) {
            System.out.println("Invalid file content for system. Expected a String.");
            return false;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fileName = "SystemLog-" + LocalDate.now().format(formatter) + ".txt";
            Path dataPath = Paths.get("Data");
            Path systemPath = dataPath.resolve("SystemLogs");
            File file = systemPath.resolve(fileName).toFile();

            String existingContent = "";
            if (file.exists()) {
                existingContent = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                if (!existingContent.endsWith(System.lineSeparator()) && !existingContent.isEmpty()) {
                    existingContent += System.lineSeparator();
                }
            }

            String text = (String) content;
            String textWithNewline = text.endsWith(System.lineSeparator()) ? text : text + System.lineSeparator();

            String combined = existingContent + textWithNewline;

            createDirectoriesAndWriteFile(combined, fileName, dataPath, systemPath);
        } catch (Exception e) {
            System.out.println("An error occurred while writing to the system file.");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}

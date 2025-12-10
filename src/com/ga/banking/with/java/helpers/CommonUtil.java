package com.ga.banking.with.java.helpers;

import com.ga.banking.with.java.entities.Account;
import tools.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommonUtil {
    public static void printSeparatorLine() {
        System.out.println("-".repeat(100));
    }
    public static void printSeparatorLine(int length) {
        System.out.println("-".repeat(length));
    }

    public static void createDirectoriesAndWriteFile(String fileContent, String fileName, Path dataPath,
                                                     Path nestedDataPath, boolean shouldAppend) throws IOException {
        if (Files.notExists(dataPath)) {
            Files.createDirectory(dataPath);
        }

        if (nestedDataPath != null && Files.notExists(nestedDataPath)) {
            Files.createDirectory(nestedDataPath);
        }

        Path path = nestedDataPath == null ? dataPath.resolve(fileName) : nestedDataPath.resolve(fileName);


        try (BufferedWriter writer = Files.newBufferedWriter(path, shouldAppend ? StandardOpenOption.APPEND : StandardOpenOption.CREATE)) {
            writer.write(fileContent);
            writer.flush();
        }
    }

    public static List<Account> parseAccountsFromFile(File file, ObjectMapper mapper) {
        List<Account> accounts = new ArrayList<>();
        if (file == null || !file.exists() || !file.isFile()) {
            return accounts;
        }
        try {
            accounts = new ArrayList<>(Arrays.asList(mapper.readValue(file, Account[].class)));
        } catch (Exception e) {
            Account singleAccount = mapper.readValue(file, Account.class);
            accounts = new ArrayList<>();
            accounts.add(singleAccount);
        }
        return accounts;
    }

    public static void waitForUserInput() {
        System.out.println("Press Enter to continue...");
        try {
            System.in.read();
        } catch (IOException e) {
            // Ignore
        }
    }
}

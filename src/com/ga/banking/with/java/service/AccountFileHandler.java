package com.ga.banking.with.java.service;

import com.ga.banking.with.java.entities.Account;
import com.ga.banking.with.java.interfaces.FileHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.ga.banking.with.java.helpers.CommonUtil.createDirectoriesAndWriteFile;
import static com.ga.banking.with.java.helpers.CommonUtil.parseAccountsFromFile;

public class AccountFileHandler implements FileHandler {

    @Override
    public List<Account> readFromFile(Object userId) {
        if (!(userId instanceof String)) {
            System.out.println("Invalid customerId. Expected a String.");
            return null;
        }
        String fileName = userId + ".json";
        Path dataPath = Paths.get("Data");
        Path accountsPath = dataPath.resolve("Accounts");
        File file = accountsPath.resolve(fileName).toFile();
        ObjectMapper mapper = new ObjectMapper();
        return parseAccountsFromFile(file, mapper);
    }

    @Override
    public boolean writeToFile(String name, String userId, Object pAccount) {
        if (!(pAccount instanceof Account)) {
            System.out.println("Invalid object type. Expected Account.");
            return false;
        }
        try {
            String fileName = userId + ".json";
            Path dataPath = Paths.get("Data");
            Path accountsPath = dataPath.resolve("Accounts");
            File file = accountsPath.resolve(fileName).toFile();
            ObjectMapper mapper = new ObjectMapper();

            List<Account> accounts;
            if (file.exists()) {
                accounts = parseAccountsFromFile(file, mapper);
            } else {
                accounts = new ArrayList<>();
            }
            if (!accounts.isEmpty()) {
                accounts.stream().filter(account -> account.getAccountId().equals(((Account) pAccount).getAccountId())).findFirst().ifPresent(accounts::remove);
            }
            accounts.add((Account) pAccount);

            createDirectoriesAndWriteFile(mapper.writeValueAsString(accounts), fileName, dataPath, accountsPath);
        } catch (Exception e) {
            System.out.println("An error occurred while writing to the accounts file.");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

}

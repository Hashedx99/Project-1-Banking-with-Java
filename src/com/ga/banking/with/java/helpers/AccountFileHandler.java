package com.ga.banking.with.java.helpers;

import com.ga.banking.with.java.entities.Account;
import com.ga.banking.with.java.interfaces.FileHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ga.banking.with.java.helpers.CommonUtil.createDirectoriesAndWriteFile;

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
        List<Account> accounts;
        try {
            accounts = new ArrayList<>(Arrays.asList(mapper.readValue(file, Account[].class)));
        } catch (Exception e) {
            Account singleAccount = mapper.readValue(file, Account.class);
            accounts = new ArrayList<>();
            accounts.add(singleAccount);
        }
        return accounts;
    }

    @Override
    public boolean writeToFile(String name, String userId, Object account) {
        if (!(account instanceof Account)) {
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
                try {
                    accounts = new ArrayList<>(Arrays.asList(mapper.readValue(file, Account[].class)));
                } catch (Exception e) {
                    Account singleAccount = mapper.readValue(file, Account.class);
                    accounts = new ArrayList<>();
                    accounts.add(singleAccount);
                }
            } else {
                accounts = new ArrayList<>();
            }

            accounts.add((Account) account);

            createDirectoriesAndWriteFile(mapper.writeValueAsString(accounts), fileName, dataPath, accountsPath);
        } catch (Exception e) {
            System.out.println("An error occurred while writing to the accounts file.");
            System.out.println(e);
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

}

package com.ga.banking.with.java;

import com.ga.banking.with.java.entities.Banker;
import com.ga.banking.with.java.entities.Session;
import com.ga.banking.with.java.helpers.BankerFileHandler;
import com.ga.banking.with.java.helpers.CommonUtil;
import com.ga.banking.with.java.helpers.PasswordHasher;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Stream;

public class BankingApp {
    private static final BankerFileHandler bankerFileHandler = new BankerFileHandler();

    public static void main(String[] args) {
        Session session = new Session();
        Path dataPath = Paths.get("Data");
        Path bankersPath = dataPath.resolve("Bankers");
        Path customersPath = dataPath.resolve("Customers");

        boolean noBankersYet = !Files.exists(dataPath) && !Files.exists(bankersPath);
        boolean bankersPathExistsButNoBankersYet = Files.exists(dataPath) && Files.exists(bankersPath) &&
                (bankersPath.toFile().listFiles() == null || Objects.requireNonNull(bankersPath.toFile().listFiles()).length == 0);
        boolean shouldPromptForBankerCreation = noBankersYet || bankersPathExistsButNoBankersYet;

        CommonUtil.printSeparatorLine();
        System.out.println(" ".repeat(36) + "Welcome to Shadow Moses Bank" + " ".repeat(36));
        CommonUtil.printSeparatorLine();


        if (shouldPromptForBankerCreation) {
            session.initializeSession(acquireFirstEverSession());
        } else {
            Scanner input = new Scanner(System.in);

            File[] customerFiles = (Files.exists(customersPath) && Files.isDirectory(customersPath))
                    ? customersPath.toFile().listFiles()
                    : null;
            boolean customersPathMissingOrEmpty = customerFiles == null || customerFiles.length == 0;
            System.out.println("Please log in to continue.");
            CommonUtil.printSeparatorLine();
            System.out.println("Enter your username: ");
            String username = input.nextLine();
            CommonUtil.printSeparatorLine();
            System.out.println("Enter your password: ");
            String password = input.nextLine();
            CommonUtil.printSeparatorLine();
            if (customersPathMissingOrEmpty) {
                if (bankersPath.toFile().listFiles() == null) {
                    throw new RuntimeException("Banker files are missing.");
                }
                List<File> fileList =
                        Arrays.stream(Objects.requireNonNull(bankersPath.toFile().listFiles(file -> file.getName().split(
                                "-")[1].equals(username)))).toList();
                if (fileList.size() > 1) {
                    throw new RuntimeException("Banker files are duplicated.");
                }
                Banker banker =
                        (Banker) fileList.stream().filter(file -> {
                            Banker tmpBanker = (Banker) bankerFileHandler.readFromFile(file);
                            return PasswordHasher.validatePassword(password, tmpBanker.getSalt(),
                                    tmpBanker.getPasswordHash());
                        }).findFirst().map(bankerFileHandler::readFromFile)
                                .orElseThrow(() -> new RuntimeException("Invalid username or password."));
                session.initializeSession(banker);

            }

            if (customersPath.toFile().listFiles() == null) {
                throw new RuntimeException("Customer files are missing.");
            }
            Stream<File> fileStream =
                    Arrays.stream(Objects.requireNonNull(customersPath.toFile().listFiles(file -> file.getName().split(
                            "-")[1].equals(username))));
            if (fileStream.count() > 1) {
                throw new RuntimeException("Customer files are duplicated.");
            }


        }


//        System.out.println(banker.getPasswordHash());
//        System.out.println("Enter password to validate: ");
//        String enteredPassword = input.nextLine();
//        System.out.println(PasswordHasher.validatePassword(enteredPassword, salt, banker.getPasswordHash()) ?
//                "Password is valid!" : "Invalid Password!");
    }

    private static Banker acquireFirstEverSession() {
        byte[] salt = PasswordHasher.generateSalt();
        Scanner input = new Scanner(System.in);
        System.out.println("No bankers found in the system. Please create the first banker account.");
        CommonUtil.printSeparatorLine();
        System.out.println("Please enter your First Name: ");
        String firstName = input.nextLine();
        CommonUtil.printSeparatorLine();
        System.out.println("please enter your Last Name: ");
        String lastName = input.nextLine();
        CommonUtil.printSeparatorLine();
        System.out.println("Please enter your username: ");
        String username = input.nextLine();
        CommonUtil.printSeparatorLine();
        System.out.println("Please enter your password: ");
        String password = PasswordHasher.getPasswordHash(input.nextLine(), salt);
        CommonUtil.printSeparatorLine();
        System.out.println("Please enter your email: ");
        String email = input.nextLine();
        CommonUtil.printSeparatorLine();
        System.out.println("Please enter your phone number: ");
        String phoneNumber = input.nextLine();
        Banker banker = new Banker(firstName, lastName, username, PasswordHasher.getPasswordHash(password, salt),
                salt,
                email, phoneNumber);
        ObjectMapper objectMapper = new ObjectMapper();
        bankerFileHandler.writeToFile(banker.getUsername(), banker.getUserId(),
                objectMapper.writeValueAsString(banker));
        System.out.println("Banker account created successfully! You are now logged in as " + banker.getFirstName() + " " + banker.getLastName());
        return banker;
    }
}

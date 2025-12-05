package com.ga.banking.with.java.features;

import com.ga.banking.with.java.entities.Account;
import com.ga.banking.with.java.entities.Banker;
import com.ga.banking.with.java.entities.Customer;
import com.ga.banking.with.java.entities.User;
import com.ga.banking.with.java.enums.AccountType;
import com.ga.banking.with.java.enums.Status;
import com.ga.banking.with.java.enums.UserRole;
import com.ga.banking.with.java.helpers.*;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;

import static com.ga.banking.with.java.helpers.PasswordHasher.generateSalt;
import static com.ga.banking.with.java.helpers.PasswordHasher.getPasswordHash;

public class Auth {
    private static final BankerFileHandler bankerFileHandler = new BankerFileHandler();
    private static final CustomerFileHandler customerFileHandler = new CustomerFileHandler();
    private static final AccountFileHandler accountFileHandler = new AccountFileHandler();
    private static final Scanner input = new Scanner(System.in);
    private static final SecureRandom secureRandom = new SecureRandom();

    private static Banker acquireFirstEverSession() {
        String salt = generateSalt();
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
        Banker banker = new Banker(firstName, lastName, username, password, salt, email, phoneNumber);
        ObjectMapper objectMapper = new ObjectMapper();
        bankerFileHandler.writeToFile(banker.getUsername(), banker.getUserId(),
                objectMapper.writeValueAsString(banker));
        System.out.println("Banker account created successfully! You are now logged in as " + banker.getFirstName() + " " + banker.getLastName());
        return banker;
    }

    private static User login(Path customersPath, Path bankersPath, String username, String password) {
        User user = bankerFlowOnlyIfNoCustomers(customersPath, bankersPath, username, password);
        if (user != null) {
            return user;
        }
        List<File> customerFileList = validatePathAndFiles(customersPath, username, UserRole.Customer);
        Customer customer =
                (Customer) customerFileList.stream().filter(file -> {
                            Customer tmpCustomer = (Customer) customerFileHandler.readFromFile(file);
                            return PasswordHasher.validatePassword(password, tmpCustomer.getSalt(),
                                    tmpCustomer.getPasswordHash());
                        }).findFirst().map(customerFileHandler::readFromFile)
                        .orElse(null);

        List<File> fileList = validatePathAndFiles(bankersPath, username, UserRole.Banker);
        Banker banker =
                (Banker) fileList.stream().filter(file -> {
                            Banker tmpBanker = (Banker) bankerFileHandler.readFromFile(file);
                            return PasswordHasher.validatePassword(password, tmpBanker.getSalt(),
                                    tmpBanker.getPasswordHash());
                        }).findFirst().map(bankerFileHandler::readFromFile)
                        .orElse(null);
        return customer != null ? customer : banker;
    }

    private static User bankerFlowOnlyIfNoCustomers(Path customersPath, Path bankersPath, String username,
                                                    String password) {
        File[] customerFiles = (Files.exists(customersPath) && Files.isDirectory(customersPath))
                ? customersPath.toFile().listFiles()
                : null;
        boolean customersPathMissingOrEmpty = customerFiles == null || customerFiles.length == 0;
        if (customersPathMissingOrEmpty) {
            while (true) {
                List<File> fileList = validatePathAndFiles(bankersPath, username, UserRole.Banker);
                String finalPassword = password;
                User user = fileList.stream().filter(file -> {
                            Banker tmpBanker = (Banker) bankerFileHandler.readFromFile(file);
                            return PasswordHasher.validatePassword(finalPassword, tmpBanker.getSalt(),
                                    tmpBanker.getPasswordHash());
                        }).findFirst().map(bankerFileHandler::readFromFile)
                        .orElse(null);
                if (user != null) {
                    return user;
                } else {
                    System.out.println("Invalid username or password. Please try again.");
                    System.out.println("Enter your username: ");
                    username = input.nextLine();
                    CommonUtil.printSeparatorLine();
                    System.out.println("Enter your password: ");
                    password = input.nextLine();
                    CommonUtil.printSeparatorLine();
                }
            }
        }
        return null;
    }

    private static List<File> validatePathAndFiles(Path path, String username, UserRole role) {
        if (path.toFile().listFiles() == null) {
            throw new RuntimeException(role == UserRole.Banker ? "Banker" : "Customer" + " files are missing.");
        }
        List<File> fileList =
                Arrays.stream(Objects.requireNonNull(path.toFile().listFiles(file -> file.getName().split(
                        "-")[1].equals(username)))).toList();
        if (fileList.size() > 1) {
            throw new RuntimeException(role == UserRole.Banker ? "Banker" : "Customer" + " files are duplicated.");
        }

        return fileList;
    }

    private static List<Account> createBankingAccountsForCustomer(String accountTypeChoice, Customer customer) {
        List<Account> accounts = new ArrayList<>();
        switch (accountTypeChoice) {
            case "1" -> {
                System.out.println("Enter initial deposit amount for Savings Account: ");
                double initialDeposit = Double.parseDouble(input.nextLine().trim());
                Account savingsAccount = new Account(customer.getUserId(), AccountType.Savings, initialDeposit);
                accounts.add(savingsAccount);
                System.out.println("Savings Account created for customer " + customer.getFirstName() + " " +
                        customer.getLastName());
            }
            case "2" -> {
                System.out.println("Enter initial deposit amount for Checking Account: ");
                double initialDeposit = Double.parseDouble(input.nextLine().trim());
                Account checkingAccount = new Account(customer.getUserId(), AccountType.Checking, initialDeposit);
                accounts.add(checkingAccount);
                System.out.println("Checking Account created for customer " + customer.getFirstName() + " " +
                        customer.getLastName());
            }
            case "3" -> {
                System.out.println("Enter initial deposit amount for Savings Account: ");
                double savingsInitialDeposit = Double.parseDouble(input.nextLine().trim());
                Account savingsAccount = new Account(customer.getUserId(), AccountType.Savings,
                        savingsInitialDeposit);
                System.out.println("Enter initial deposit amount for Checking Account: ");
                double checkingInitialDeposit = Double.parseDouble(input.nextLine().trim());
                Account checkingAccount = new Account(customer.getUserId(), AccountType.Checking,
                        checkingInitialDeposit);
                accounts.add(savingsAccount);
                accounts.add(checkingAccount);
                System.out.println("Both Savings and Checking Accounts created for customer " +
                        customer.getFirstName() + " " + customer.getLastName());
            }
            default -> System.out.println("Invalid account type selected. No account created.");
        }
        return accounts;
    }

    public User authenticate() {
        while (true) {
            Path dataPath = Paths.get("Data");
            Path bankersPath = dataPath.resolve("Bankers");
            Path customersPath = dataPath.resolve("Customers");

            boolean noBankersYet = !Files.exists(dataPath) && !Files.exists(bankersPath);
            boolean bankersPathExistsButNoBankersYet = Files.exists(dataPath) && Files.exists(bankersPath) &&
                    (bankersPath.toFile().listFiles() == null || Objects.requireNonNull(bankersPath.toFile().listFiles()).length == 0);
            boolean shouldPromptForBankerCreation = noBankersYet || bankersPathExistsButNoBankersYet;
            if (shouldPromptForBankerCreation) {
                return acquireFirstEverSession();
            } else {
                System.out.println("Please log in to continue.");
                CommonUtil.printSeparatorLine();
                System.out.println("Enter your username: ");
                String username = input.nextLine();
                CommonUtil.printSeparatorLine();
                System.out.println("Enter your password: ");
                String password = input.nextLine();
                CommonUtil.printSeparatorLine();
                User user = login(customersPath, bankersPath, username, password);
                if (user == null) {
                    System.out.println("Invalid username or password.");
                    continue;
                }

                switch (Objects.requireNonNull(user).getStatus()) {
                    case FirstLogin -> {
                        System.out.println("This is your first login. Please change your password.");
                        System.out.println("Enter your new password: ");
                        String newPassword = input.nextLine();
                        String newSalt = generateSalt();
                        user.setPasswordHash(getPasswordHash(newPassword, newSalt));
                        user.setSalt(newSalt);
                        user.setStatus(Status.Active);
                        ObjectMapper objectMapper = new ObjectMapper();
                        customerFileHandler.writeToFile(user.getUsername(), user.getUserId(),
                                objectMapper.writeValueAsString(user));
                        System.out.println("Password changed successfully. You can now log in with your new password.");
                    }
                    case Active -> {
                        System.out.println("Login successful! Welcome, " + user.getFirstName() + " " +
                                user.getLastName() + " (" + user.getRole() + ")");
                        return user;
                    }
                    case Inactive -> System.out.println("Your account is inactive. Please contact support.");
                    case Disabled -> System.out.println("Your account is disabled. Please contact support.");
                    case Locked ->
                            System.out.println("Your account is locked due to multiple failed login attempts. Please " +
                                    "try " +
                                    "again later or contact support.");
                    default -> System.out.println("Your account status is unknown. Please contact support.");
                }
            }
        }
    }


    public Customer createUserForCustomer() {
        System.out.println("Customer username: ");
        String username = input.nextLine();

        // Check if a file with the username already exists
        Path dataPath = Paths.get("Data");
        Path customersPath = dataPath.resolve("Customers");
        while (true) {
            String finalUsername = username;
            File[] existingFiles =
                    customersPath.toFile().listFiles(file -> file.getName().startsWith("Customer-" + finalUsername +
                            "-"));
            if (existingFiles != null && existingFiles.length > 0) {
                System.out.println("Error: A customer with the username '" + username + "' already exists. Please try" +
                        " again.");
                System.out.println("Customer username: ");
                username = input.nextLine();
            } else {
                break;
            }
        }
        String salt = generateSalt();
        CommonUtil.printSeparatorLine();
        System.out.println("Customer First Name: ");
        String firstName = input.nextLine();
        CommonUtil.printSeparatorLine();
        System.out.println("Customer Last Name: ");
        String lastName = input.nextLine();
        CommonUtil.printSeparatorLine();
        System.out.println("Customer email: ");
        String email = input.nextLine();
        CommonUtil.printSeparatorLine();
        System.out.println("Customer phone number: ");
        String phoneNumber = input.nextLine();
        byte[] randomPass = new byte[8];
        secureRandom.nextBytes(randomPass);
        String password = Base64.getEncoder().encodeToString(randomPass);
        Customer customer = new Customer(firstName, lastName, username, getPasswordHash(password, salt), salt, email,
                phoneNumber);
        customer.setStatus(Status.FirstLogin);
        System.out.println("Please select account type for the customer:");
        System.out.println("1. Savings Account");
        System.out.println("2. Checking Account");
        System.out.println("3. Both Savings and Checking Accounts");
        String accountTypeChoice = input.nextLine().trim();
        System.out.println("Customer account created successfully for " + customer.getFirstName() + " " + customer.getLastName() + "!");
        System.out.println("Provide the following temporary password to the customer for their first login: " + password);
        ObjectMapper objectMapper = new ObjectMapper();
        createBankingAccountsForCustomer(accountTypeChoice, customer).forEach(account ->
                accountFileHandler.writeToFile(null, customer.getUserId(), account));
        customerFileHandler.writeToFile(customer.getUsername(), customer.getUserId(),
                objectMapper.writeValueAsString(customer));
        return customer;
    }

    public List<Account> loadUserAccounts(User user) {
        if (user.getRole() == UserRole.Banker) {
            return new ArrayList<>();
        }
        return accountFileHandler.readFromFile(user.getUserId());
    }

    public List<Account> getUserAccounts(String customerId) {
        return accountFileHandler.readFromFile(customerId);
    }
}
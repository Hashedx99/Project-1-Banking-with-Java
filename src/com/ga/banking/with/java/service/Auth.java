package com.ga.banking.with.java.service;

import com.ga.banking.with.java.entities.Account;
import com.ga.banking.with.java.entities.Banker;
import com.ga.banking.with.java.entities.Customer;
import com.ga.banking.with.java.entities.DebitCard;
import com.ga.banking.with.java.entities.MasterCard;
import com.ga.banking.with.java.entities.MasterCardPlatinum;
import com.ga.banking.with.java.entities.MasterCardTitanium;
import com.ga.banking.with.java.entities.Transaction;
import com.ga.banking.with.java.entities.User;
import com.ga.banking.with.java.enums.AccountType;
import com.ga.banking.with.java.enums.Status;
import com.ga.banking.with.java.enums.UserRole;
import com.ga.banking.with.java.helpers.CommonUtil;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static com.ga.banking.with.java.helpers.CommonUtil.parseAccountsFromFile;
import static com.ga.banking.with.java.helpers.CommonUtil.printSeparatorLine;
import static com.ga.banking.with.java.service.PasswordHasher.generateSalt;
import static com.ga.banking.with.java.service.PasswordHasher.getPasswordHash;
import static com.ga.banking.with.java.service.PasswordHasher.isPasswordStrong;
import static com.ga.banking.with.java.service.PasswordHasher.validatePassword;
import static java.util.Arrays.stream;

public class Auth {
    private static final BankerFileHandler bankerFileHandler = new BankerFileHandler();
    private static final CustomerFileHandler customerFileHandler = new CustomerFileHandler();
    private static final AccountFileHandler accountFileHandler = new AccountFileHandler();
    private static final DebitCardFileHandler debitCardFileHandler = new DebitCardFileHandler();
    private static final TransactionFileHandler transactionFileHandler = new TransactionFileHandler();
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
        String tempPass = input.nextLine();
        String password;
        tempPass = validatePasswordRequirements(tempPass);
        password = getPasswordHash(tempPass, salt);
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
                            return validatePassword(password, tmpCustomer.getSalt(),
                                    tmpCustomer.getPasswordHash());
                        }).findFirst().map(customerFileHandler::readFromFile)
                        .orElse(null);

        List<File> fileList = validatePathAndFiles(bankersPath, username, UserRole.Banker);
        Banker banker =
                (Banker) fileList.stream().filter(file -> {
                            Banker tmpBanker = (Banker) bankerFileHandler.readFromFile(file);
                            return validatePassword(password, tmpBanker.getSalt(),
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
                            return validatePassword(finalPassword, tmpBanker.getSalt(),
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
                stream(Objects.requireNonNull(path.toFile().listFiles(file -> file.getName().split(
                        "-")[1].equals(username)))).toList();
        if (fileList.size() > 1) {
            throw new RuntimeException(role == UserRole.Banker ? "Banker" : "Customer" + " files are duplicated.");
        }

        return fileList;
    }

    private static List<Account> createBankingAccountsForCustomer(String accountTypeChoice, Customer customer) {
        List<Account> accounts = new ArrayList<>();
        boolean validChoice = false;

        while (!validChoice) {
            switch (accountTypeChoice) {
                case "1" -> {
                    System.out.println("Enter initial deposit amount for Savings Account: ");
                    double initialDeposit = Double.parseDouble(input.nextLine().trim());
                    Account savingsAccount = new Account(customer.getUserId(), AccountType.Savings, initialDeposit);
                    accounts.add(savingsAccount);
                    System.out.println("Savings Account created for customer " + customer.getFirstName() + " " +
                            customer.getLastName());
                    validChoice = true;
                }
                case "2" -> {
                    System.out.println("Enter initial deposit amount for Checking Account: ");
                    double initialDeposit = Double.parseDouble(input.nextLine().trim());
                    Account checkingAccount = new Account(customer.getUserId(), AccountType.Checking, initialDeposit);
                    accounts.add(checkingAccount);
                    System.out.println("Checking Account created for customer " + customer.getFirstName() + " " +
                            customer.getLastName());
                    validChoice = true;
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
                    validChoice = true;
                }
                default -> {
                    System.out.println("Invalid account type selected. Please select again:");
                    System.out.println("1. Savings Account");
                    System.out.println("2. Checking Account");
                    System.out.println("3. Both Savings and Checking Accounts");
                    accountTypeChoice = input.nextLine().trim();
                }
            }
        }
        return accounts;
    }

    private static String validatePasswordRequirements(String newPassword) {
        if (!isPasswordStrong(newPassword)) {
            while (!isPasswordStrong(newPassword)) {
                System.out.println("Password is not strong enough. Please create a password with at least 8 " +
                        "characters, " +
                        "including uppercase, lowercase, digit, and special character.");
                CommonUtil.printSeparatorLine();
                System.out.println("Please enter your password: ");
                newPassword = input.nextLine();
            }
        }
        return newPassword;
    }

    private static void createDebitCardsForCustomerAccount(Account account) {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("Please select a card type to issue to the customers " + account.getAccountType() + " " +
                "account.");
        System.out.println("1. MasterCard");
        System.out.println("2. MasterCard Titanium");
        System.out.println("3. MasterCard Platinum");
        boolean validCardSelected = false;
        while (!validCardSelected) {
            String cardTypeChoice = input.nextLine().trim();
            switch (cardTypeChoice) {
                case "1" -> {
                    MasterCard debitCard = new MasterCard(account.getAccountId());
                    debitCardFileHandler.writeToFile(null, account.getAccountId(),
                            objectMapper.writeValueAsString(debitCard));
                    System.out.println("MasterCard debit card issued for  " + account.getAccountType() + " account");
                    validCardSelected = true;
                }
                case "2" -> {
                    MasterCardTitanium debitCard = new MasterCardTitanium(account.getAccountId());
                    debitCardFileHandler.writeToFile(null, account.getAccountId(),
                            objectMapper.writeValueAsString(debitCard));
                    System.out.println("MasterCard Titanium debit card issued for " + account.getAccountType() + " " +
                            "account");
                    validCardSelected = true;
                }
                case "3" -> {
                    MasterCardPlatinum debitCard = new MasterCardPlatinum(account.getAccountId());
                    debitCardFileHandler.writeToFile(null, account.getAccountId(),
                            objectMapper.writeValueAsString(debitCard));
                    System.out.println("MasterCard Platinum debit card issued for customer " + account.getAccountType() + " account");
                    validCardSelected = true;
                }
                default -> {
                    System.out.println("Invalid card type selected.");
                    System.out.println("Please select a card type to issue to the customers " + account.getAccountType() + " " +
                            "account.");
                    System.out.println("1. MasterCard");
                    System.out.println("2. MasterCard Titanium");
                    System.out.println("3. MasterCard Platinum");
                }

            }
        }
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
                        newPassword = validatePasswordRequirements(newPassword);
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

    public void resetPassword(User user) {
        ObjectMapper objectMapper = new ObjectMapper();
        String currentPassword;
        String newPassword;
        String confirmPassword;
        while (true) {
            System.out.println("Please enter your current password: ");
            currentPassword = input.nextLine();
            System.out.println("Please enter your new password: ");
            newPassword = input.nextLine();
            System.out.println("Please confirm your new password: ");
            confirmPassword = input.nextLine();
            if (!newPassword.equals(confirmPassword)) {
                System.out.println("New password and confirmation do not match. Please try again.");
            } else {
                break;
            }
        }
        if (!validatePassword(currentPassword, user.getSalt(), user.getPasswordHash())) {
            System.out.println("Current password is incorrect. Password reset failed.");
            return;
        }
        newPassword = validatePasswordRequirements(newPassword);
        user.setPasswordHash(getPasswordHash(newPassword, user.getSalt()));
        if (user.getRole() == UserRole.Banker) {
            bankerFileHandler.writeToFile(user.getUsername(), user.getUserId(),
                    objectMapper.writeValueAsString(user));
        } else if (user.getRole() == UserRole.Customer) {
            customerFileHandler.writeToFile(user.getUsername(), user.getUserId(),
                    objectMapper.writeValueAsString(user));
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
        ObjectMapper objectMapper = new ObjectMapper();
        List<Account> accounts = createBankingAccountsForCustomer(accountTypeChoice, customer);
        accounts.forEach(account ->
                accountFileHandler.writeToFile(null, customer.getUserId(), account));
        customerFileHandler.writeToFile(customer.getUsername(), customer.getUserId(),
                objectMapper.writeValueAsString(customer));
        accounts.forEach(Auth::createDebitCardsForCustomerAccount);
        System.out.println("Customer account created successfully for " + customer.getFirstName() + " " + customer.getLastName() + "!");
        System.out.println("Provide the following temporary password to the customer for their first login: " + password);
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

    public DebitCard loadUserDebitCard(User user, AccountType accountType) {
        if (user.getRole() == UserRole.Banker) {
            return null;
        }
        List<Account> accounts = loadUserAccounts(user);
        Account userAccount = accounts.stream()
                .filter(account -> account.getAccountType() == accountType)
                .findFirst()
                .orElse(null);
        if (userAccount == null) {
            return null;
        }
        return debitCardFileHandler.readFromFile(userAccount.getAccountId());
    }

    public Account getAccountById(String accountId) {
        Path accountsPath = Paths.get("Data").resolve("Accounts");
        if (!Files.exists(accountsPath) || !Files.isDirectory(accountsPath)) {
            return null;
        }

        File[] files = accountsPath.toFile().listFiles();
        if (files == null || files.length == 0) {
            return null;
        }

        for (File file : files) {
            try {
                List<Account> accounts;
                ObjectMapper mapper = new ObjectMapper();
                accounts = parseAccountsFromFile(file, mapper);
                for (Account account : accounts) {
                    if (accountId.equals(account.getAccountId())) {
                        return account;
                    }
                }
            } catch (Exception ignored) {
                System.out.println("An error occurred while reading accounts from file: " + file.getName());
            }
        }

        return null;
    }

    public List<Transaction> loadUserTransactions(User user) {
        if (user.getRole() == UserRole.Banker) {
            return new ArrayList<>();
        }
        if (user.getRole() == UserRole.Customer) {
            return transactionFileHandler.readFromFile(user.getUserId());
        }
        return new ArrayList<>();
    }

    public void createTransactionRecord(User user, Transaction transaction, Account sourceAccount,
                                        Account destinationAccount) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (sourceAccount != null) {
                accountFileHandler.writeToFile(null, user.getUserId(), sourceAccount);
            }
            if (destinationAccount != null) {
                accountFileHandler.writeToFile(null, user.getUserId(), destinationAccount);
            }
            transactionFileHandler.writeToFile(user.getUserId(), transaction.getTransactionId(),
                    objectMapper.writeValueAsString(transaction));
        } catch (Exception e) {
            System.out.println("An error occurred while creating transaction record.");
            System.out.println(e.getMessage());
        }
    }

    public void getAccountTransactions(User user, Account account) {
        List<Transaction> unfilteredTransactions = loadUserTransactions(user);
        List<Transaction> filteredAndSortedTransactions =
                unfilteredTransactions.stream().filter(transaction -> account.getAccountId().equals(transaction.getToAccountId()) || account.getAccountId().equals(transaction.getFromAccountId())).sorted(Comparator.comparing(Transaction::getTimestamp)
                ).toList();
        if (filteredAndSortedTransactions.isEmpty()) {
            System.out.println("No Transactions for account" + account);
            return;
        }
        header(account);
        filteredAndSortedTransactions.forEach(transaction -> transaction.toStatement(account));
    }

    private void header(Account account) {
        String availableBal;
        try {
            availableBal = String.format("%,.2f", account.getBalance());
        } catch (Exception e) {
            availableBal = "";
        }
        printSeparatorLine(267);
        System.out.println("Account Balance: " + availableBal);
        printSeparatorLine(267);
        String format = "%-19s | %-10s | %13s | %-40s %-40s | %-26s | %-10s | %-90s";
        String header = String.format(format,
                "Time",
                "Type",
                "Amount",
                "From Account",
                "To Account",
                "Balance",
                "Status",
                "Description");
        String separator = header.replaceAll("[^|]", "-");
        System.out.println(header + System.lineSeparator() + separator);
    }
}
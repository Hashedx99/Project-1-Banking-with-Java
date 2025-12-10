package com.ga.banking.with.java.service;

import com.ga.banking.with.java.entities.Account;
import com.ga.banking.with.java.entities.DebitCard;
import com.ga.banking.with.java.entities.Transaction;
import com.ga.banking.with.java.entities.User;
import com.ga.banking.with.java.enums.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import static com.ga.banking.with.java.helpers.CommonUtil.waitForUserInput;

public class Session {
    private User user;
    private SessionStatus status;
    private UserRole role;
    private List<Account> accounts;
    private DebitCard savingsDebitCard;
    private DebitCard checkingDebitCard;
    private List<Transaction> transactions;

    public Session() {
        this.status = SessionStatus.Unauthenticated;
    }

    public Session(User user) {
        this.user = user;
        this.status = SessionStatus.Active;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isAuthenticated() {
        return this.status == SessionStatus.Active;
    }

    public boolean isNotAuthenticated() {
        return this.status == SessionStatus.Unauthenticated;
    }

    public void initializeSession(User user, List<Account> accounts, DebitCard savingsDebitCard,
                                  DebitCard checkingDebitCard,
                                  List<Transaction> transactions) {
        this.user = user;
        this.status = SessionStatus.Active;
        this.role = user.getRole();
        this.accounts = accounts;
        this.savingsDebitCard = savingsDebitCard;
        this.checkingDebitCard = checkingDebitCard;
        this.transactions = transactions;
    }

    public void terminateSession() {
        this.status = SessionStatus.Terminated;
    }

    public boolean isNotTerminated() {
        return this.status != SessionStatus.Terminated;
    }

    public void getUserMenu(Auth auth) {
        Scanner input = new Scanner(System.in);
        if (this.isAuthenticated() && this.user.getRole() == UserRole.Banker) {
            bankerMenu(auth, input);
        } else if (this.isAuthenticated() && this.user.getRole() == UserRole.Customer) {
            customerMenu(auth, input);
        } else {
            System.out.println("User is not authenticated.");
        }
        waitForUserInput();
    }

    private void bankerMenu(Auth auth, Scanner input) {
        System.out.println("Banker Menu:");
        System.out.println("Choose an option:");
        System.out.println("1. Create Customer Account");
        System.out.println("2. View Customer Accounts");
        System.out.println("3. Deactivate Customer Account");
        System.out.println("4. Activate Customer Account");
        System.out.println("C. Create Banker Account");
        System.out.println("R. Reset Banker Password");
        System.out.println("Q. Quit");
        switch (input.nextLine()) {
            case "1" -> auth.createUserForCustomer();
            case "2" -> {
                System.out.println("Enter Customer ID to view accounts:");
                String customerId = input.nextLine();
                List<Account> customerAccounts = auth.getUserAccounts(customerId);
                if (customerAccounts == null || customerAccounts.isEmpty()) {
                    System.out.println("No accounts found for Customer ID: " + customerId);
                } else {
                    System.out.println("Accounts for Customer ID: " + customerId);
                    for (Account account : customerAccounts) {
                        System.out.println(account.toString());
                    }
                }
            }
            case "R", "r" -> auth.resetPassword(this.user);
            case "Q", "q" -> {
                System.out.println("Exiting Banker Menu.");
                this.terminateSession();
            }
            default -> System.out.println("Invalid option. Please try again.");
        }
    }


    private void customerMenu(Auth auth, Scanner input) {
        System.out.println("Customer Menu:");
        System.out.println("Choose an option:");
        System.out.println("1. View My Accounts");
        System.out.println("2. Show Transaction History");
        System.out.println("3. Withdraw Funds");
        System.out.println("4. Deposit Funds to My Account");
        System.out.println("5. Deposit Funds to Another Account");
        System.out.println("6. Transfer Funds to My Account");
        System.out.println("7. Transfer Funds to Another Account");
        System.out.println("R. Reset My Password");
        System.out.println("Q. Quit");
        switch (input.nextLine()) {
            case "1" -> {
                if (accounts == null || accounts.isEmpty()) {
                    System.out.println("You have no accounts.");
                } else {
                    System.out.println("Your Accounts:");
                    for (Account account : accounts) {
                        System.out.println(account.toString());
                    }
                }
            }
            case "2" -> {
                System.out.println("Select an account to view transactions or B to go back:");
                Account selectedAccount = getAccount(input);
                if (selectedAccount == null) {
                    System.out.println("Operation cancelled, returning to menu.");
                    return;
                }

                auth.getAccountTransactions(this.user, selectedAccount);
            }
            case "3" -> {
                System.out.println("Select an account to withdraw from or B to go back:");
                Account selectedAccount = getAccount(input);
                if (selectedAccount == null) {
                    System.out.println("Operation cancelled, returning to menu.");
                    return;
                }
                if (selectedAccount.getStatus().equals(AccountStatus.Frozen)) {
                    System.out.println("Selected account is frozen. Cannot withdraw funds.");
                    System.out.println("Please clear any overdrafts to unfreeze the account.");
                    return;
                }
                System.out.println("Enter amount to withdraw:");
                double withdrawAmount = input.nextDouble();
                double withdrawResult = debitCard(selectedAccount).withdrawFunds(withdrawAmount, selectedAccount);
                if (withdrawResult != -1) {
                    auth.createTransactionRecord(this.user, new Transaction(UUID.randomUUID().toString(),
                            selectedAccount.getAccountId(), null, withdrawAmount, selectedAccount.getBalance(),
                            null,
                            LocalDateTime.now(),
                            TransactionStatus.COMPLETED, "Withdrawal from account " + selectedAccount.getAccountId(),
                            TransactionType.WITHDRAWAL), selectedAccount, null);
                    System.out.println("Withdrawal successful!");
                }

            }
            case "4" -> {
                System.out.println("Select an account to deposit to or B to go back:");
                Account selectedAccount = getAccount(input);
                if (selectedAccount == null) {
                    System.out.println("Operation cancelled, returning to menu.");
                    return;
                }
                System.out.println("Enter amount to deposit:");
                double depositAmount = input.nextDouble();
                double depositResult = debitCard(selectedAccount).depositFunds(depositAmount, selectedAccount, true);
                if (depositResult != -1) {
                    auth.createTransactionRecord(this.user, new Transaction(UUID.randomUUID().toString(),
                            null, selectedAccount.getAccountId(), depositAmount, null, selectedAccount.getBalance(),
                            LocalDateTime.now(),
                            TransactionStatus.COMPLETED, "Deposit to account " + selectedAccount.getAccountId(),
                            TransactionType.DEPOSIT), selectedAccount, null);
                    System.out.println("Deposit successful!");
                }
            }
            case "5" -> {
                Account selectedAccount = getOtherAccount(input, auth);
                if (selectedAccount == null) {
                    System.out.println("Operation cancelled, returning to menu.");
                    return;
                }
                System.out.println("Enter amount to deposit:");
                double depositAmount = input.nextDouble();
                double depositResult = debitCard(selectedAccount).depositFunds(depositAmount, selectedAccount, false);
                if (depositResult != -1) {
                    auth.createTransactionRecord(this.user, new Transaction(UUID.randomUUID().toString(),
                            null, selectedAccount.getAccountId(), depositAmount, null, selectedAccount.getBalance(),
                            LocalDateTime.now(),
                            TransactionStatus.COMPLETED, "Deposit to account " + selectedAccount.getAccountId(),
                            TransactionType.DEPOSIT), selectedAccount, null);
                    System.out.println("Deposit successful!");
                }
            }
            case "6" -> {
                System.out.println("Select an account to transfer from or B to go back:");
                Account selectedAccount = getAccount(input);
                System.out.println("Select an account to transfer to:");
                Account otherAccount = getAccount(input);
                if (otherAccount == null || selectedAccount == null) {
                    System.out.println("Operation cancelled, returning to menu.");
                    return;
                }
                if (selectedAccount.getStatus().equals(AccountStatus.Frozen)) {
                    System.out.println("Selected account is frozen. Cannot withdraw funds.");
                    System.out.println("Please clear any overdrafts to unfreeze the account.");
                    return;
                }
                System.out.println("Enter amount to transfer:");
                double transferAmount = input.nextDouble();
                double transferResult = debitCard(selectedAccount).transferFunds(transferAmount, selectedAccount,
                        otherAccount,
                        true);
                if (transferResult != -1) {
                    auth.createTransactionRecord(this.user, new Transaction(UUID.randomUUID().toString(),
                                    selectedAccount.getAccountId(), otherAccount.getAccountId(), transferAmount,
                                    selectedAccount.getBalance(), otherAccount.getBalance(),
                                    LocalDateTime.now(), TransactionStatus.COMPLETED,
                                    "Transfer from " + selectedAccount.getAccountId() + " -> " +
                                            otherAccount.getAccountId(), TransactionType.TRANSFER), selectedAccount,
                            otherAccount);
                    System.out.println("Transfer successful!");
                }
            }
            case "7" -> {
                System.out.println("Select an account to transfer from or B to go back:");
                Account selectedAccount = getAccount(input);
                System.out.println("Select an account to transfer to:");
                Account otherAccount = getOtherAccount(input, auth);
                if (otherAccount == null || selectedAccount == null) {
                    System.out.println("Operation cancelled, returning to menu.");
                    return;
                }
                if (selectedAccount.getStatus().equals(AccountStatus.Frozen)) {
                    System.out.println("Selected account is frozen. Cannot withdraw funds.");
                    System.out.println("Please clear any overdrafts to unfreeze the account.");
                    return;
                }
                System.out.println("Enter amount to transfer:");
                double transferAmount = input.nextDouble();
                double transferResult = debitCard(selectedAccount).transferFunds(transferAmount, selectedAccount,
                        otherAccount,
                        false);
                if (transferResult != -1) {
                    auth.createTransactionRecord(this.user, new Transaction(UUID.randomUUID().toString(),
                            selectedAccount.getAccountId(), otherAccount.getAccountId(), transferAmount,
                            selectedAccount.getBalance(), otherAccount.getBalance(),
                            LocalDateTime.now(), TransactionStatus.COMPLETED, "Transfer from " +
                            selectedAccount.getAccountId() + " -> " + otherAccount.getAccountId(),
                            TransactionType.TRANSFER), selectedAccount, null);
                    System.out.println("Transfer successful!");
                }
            }
            case "R", "r" -> auth.resetPassword(this.user);
            case "Q", "q" -> {
                System.out.println("Exiting Customer Menu.");
                this.terminateSession();
            }
            default -> System.out.println("Invalid option. Please try again.");
        }
        this.transactions = auth.loadUserTransactions(this.user);
    }

    private Account getAccount(Scanner input) {
        while (true) {
            for (int i = 0; i < accounts.size(); i++) {
                System.out.println((i + 1) + ". " + accounts.get(i).toString());
            }
            String accountChoice = input.nextLine();
            if (accountChoice.equalsIgnoreCase("B")) {
                return null;
            }
            int accountChoiceInt;
            try {
                accountChoiceInt = Integer.parseInt(accountChoice.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter the number of an account or B to go back.");
                continue;
            }
            if (accountChoiceInt < 1 || accountChoiceInt > accounts.size()) {
                System.out.println("Invalid account selection.");
            } else {
                return accounts.get(accountChoiceInt - 1);
            }
        }
    }

    private Account getOtherAccount(Scanner input, Auth auth) {
        //TODO:: this is very inefficient, if i have time i will create an indexing class, that will index accounts
        // by accountId for fast retrieval
        while (true) {
            System.out.println("Enter the account ID of the other account or B to go back:");
            String otherAccountId = input.nextLine();
            if (otherAccountId.equalsIgnoreCase("B")) {
                return null;
            }
            Account account = auth.getAccountById(otherAccountId);
            if (account == null) {
                System.out.println("Account not found.");
            } else {
                return account;
            }
        }
    }

    private DebitCard debitCard(Account account) {
        if (account.getAccountType() == AccountType.Savings) {
            return this.savingsDebitCard;
        } else if (account.getAccountType() == AccountType.Checking) {
            return this.checkingDebitCard;
        }
        throw new RuntimeException("No Debit Card found for account" + account.getAccountId());
    }
}

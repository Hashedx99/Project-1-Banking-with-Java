package com.ga.banking.with.java.entities;

import com.ga.banking.with.java.enums.SessionStatus;
import com.ga.banking.with.java.enums.UserRole;
import com.ga.banking.with.java.features.Auth;

import java.util.List;
import java.util.Scanner;

public class Session {
    private User user;
    private SessionStatus status;
    private UserRole role;
    private List<Account> accounts;
    private DebitCard debitCard;
    private List<Transaction> transactions;

    public Session() {
        this.status = SessionStatus.Unauthenticated;
    }

    public Session(User user) {
        this.user = user;
        this.status = SessionStatus.Active;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public boolean isAuthenticated() {
        return this.status == SessionStatus.Active;
    }

    public boolean isNotAuthenticated() {
        return this.status == SessionStatus.Unauthenticated;
    }

    public void initializeSession(User user, List<Account> accounts, DebitCard debitCard, List<Transaction> transactions) {
        this.user = user;
        this.status = SessionStatus.Active;
        this.role = user.getRole();
        this.accounts = accounts;
        this.debitCard = debitCard;
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
            case "B", "b" -> auth.resetPassword(this.user);
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
        System.out.println("2. Withdraw Funds");
        System.out.println("3. Deposit Funds to My Account");
        System.out.println("4. Deposit Funds to Another Account");
        System.out.println("5. Transfer Funds to My Account");
        System.out.println("6. Transfer Funds to Another Account");
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
                System.out.println("Select an account to withdraw from:");
                Account selectedAccount = getAccount(input);
                System.out.println("Enter amount to withdraw:");
                double withdrawAmount = input.nextDouble();
                this.debitCard.withdrawFunds(withdrawAmount, selectedAccount);

            }
            case "3" -> {
                System.out.println("Select an account to deposit to:");
                Account selectedAccount = getAccount(input);
                System.out.println("Enter amount to deposit:");
                double depositAmount = input.nextDouble();
                this.debitCard.depositFunds(depositAmount, selectedAccount, true);
            }
            case "4" -> {
                System.out.println("Select an account to deposit to:");
                Account selectedAccount = getOtherAccount(input, auth);
                System.out.println("Enter amount to deposit:");
                double depositAmount = input.nextDouble();
                this.debitCard.depositFunds(depositAmount, selectedAccount, false);
            }
            case "5" -> {
                System.out.println("Select an account to transfer from:");
                Account selectedAccount = getAccount(input);
                System.out.println("Enter amount to transfer:");
                double transferAmount = input.nextDouble();
                this.debitCard.transferFunds(transferAmount, selectedAccount, selectedAccount, true);
            }
            case "6" -> {
                System.out.println("Select an account to transfer from:");
                Account selectedAccount = getAccount(input);
                System.out.println("Enter amount to transfer:");
                double transferAmount = input.nextDouble();
                Account otherAccount = getOtherAccount(input, auth);
                this.debitCard.transferFunds(transferAmount, selectedAccount, otherAccount, false);
            }
            case "R", "r" -> auth.resetPassword(this.user);
            case "Q", "q" -> {
                System.out.println("Exiting Customer Menu.");
                this.terminateSession();
            }
            default -> System.out.println("Invalid option. Please try again.");
        }
    }

    private Account getAccount(Scanner input) {
        while (true) {
            for (int i = 0; i < accounts.size(); i++) {
                System.out.println((i + 1) + ". " + accounts.get(i).toString());
            }
            int accountChoice = input.nextInt();
            if (accountChoice < 1 || accountChoice > accounts.size()) {
                System.out.println("Invalid account selection.");
            } else {
                return accounts.get(accountChoice - 1);
            }
        }
    }

    private Account getOtherAccount(Scanner input, Auth auth) {
        //TODO:: this is very inefficient, if i have time i will create an indexing class, that will index accounts
        // by accountId for fast retrieval
        while (true) {
            System.out.println("Enter the account ID of the other account:");
            String otherAccountId = input.nextLine();
            Account account = auth.getAccountById(otherAccountId);
            if (account == null) {
                System.out.println("Account not found.");
            } else {
                return account;
            }
        }
    }
}

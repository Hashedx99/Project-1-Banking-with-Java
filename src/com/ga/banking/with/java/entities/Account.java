package com.ga.banking.with.java.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ga.banking.with.java.enums.AccountStatus;
import com.ga.banking.with.java.enums.AccountType;

import java.time.LocalDate;
import java.util.UUID;

public class Account {
    @JsonProperty("accountId")
    private String accountId;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("accountType")
    private AccountType accountType;

    @JsonProperty("balance")
    private double balance;

    @JsonProperty("status")
    private AccountStatus status;

    @JsonProperty("createdAt")
    private LocalDate createdAt;

    @JsonProperty("overdraftCount")
    private int overdraftCount;

    public Account(String userId, AccountType accountType, double balance) {
        this.accountId = UUID.randomUUID().toString();
        this.userId = userId;
        this.accountType = accountType;
        this.balance = balance;
        this.status = AccountStatus.Active;
        this.createdAt = LocalDate.now();
        this.overdraftCount = 0;
    }

    @JsonCreator
    public Account(@JsonProperty("accountId") String accountId,
                   @JsonProperty("userId") String userId,
                   @JsonProperty("accountType") AccountType accountType,
                   @JsonProperty("balance") double balance,
                   @JsonProperty("status") AccountStatus status,
                   @JsonProperty("createdAt") LocalDate createdAt,
                   @JsonProperty("overdraftCount") int overdraftCount) {
        this.accountId = accountId;
        this.userId = userId;
        this.accountType = accountType;
        this.balance = balance;
        this.status = status;
        this.createdAt = createdAt;
        this.overdraftCount = overdraftCount;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getBalance() {
        return balance;
    }

    public int getOverdraftCount() {
        return overdraftCount;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public double deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive.");
            return balance;
        }
        balance += amount;
        return balance;
    }

    public double withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return balance;
        }
        if (balance < 0) {
            if (amount > 100) {
                System.out.println("Account is overdrawn. Cannot withdraw more than $100 while balance is negative.");
                return balance;
            }
            handleOverdraft(amount);
            return balance;
        }
        if (amount > balance) {
            handleOverdraft(amount);
            return balance;
        }
        balance -= amount;
        return balance;
    }

    private void handleOverdraft(double amount) {
        this.overdraftCount++;
        balance -= amount;
        System.out.println("Overdrafted: " + amount);
        if (overdraftCount == 2) {
            System.out.println("Warning: You have reached the maximum number of overdrafts allowed.");
            System.out.println("your account is now frozen until you pay off the negative balance.");
            this.status = AccountStatus.Frozen;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Account ").append(this.accountId)
                .append(" | Type: ").append(this.accountType)
                .append(" | Balance: ").append(this.balance)
                .append(" | Status: ").append(this.status);

        if (this.overdraftCount > 0) {
            sb.append(" | Overdrafts: ").append(this.overdraftCount);
        }

        return sb.toString();
    }
}

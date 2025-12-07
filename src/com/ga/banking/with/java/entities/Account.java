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
        if (amount > balance) {
            System.out.println("Insufficient funds for withdrawal.");
            return balance;
        }
        balance -= amount;
        return balance;
    }

    @Override
    public String toString() {
        return "accountId='" + accountId + '\'' +
                ", userId='" + userId + '\'' +
                ", accountType=" + accountType +
                ", balance=" + balance +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", overdraftCount=" + overdraftCount;
    }
}

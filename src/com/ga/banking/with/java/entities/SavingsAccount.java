package com.ga.banking.with.java.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ga.banking.with.java.enums.AccountStatus;
import com.ga.banking.with.java.enums.AccountType;

import java.time.LocalDate;

public class SavingsAccount extends Account {
    public SavingsAccount(String userId, double balance) {
        super(userId, AccountType.Savings, balance);
    }

    @JsonCreator
    public SavingsAccount(
            @JsonProperty("accountId") String accountId,
            @JsonProperty("userId") String userId,
            @JsonProperty("accountType") AccountType accountType,
            @JsonProperty("balance") double balance,
            @JsonProperty("status") AccountStatus status,
            @JsonProperty("createdAt") LocalDate createdAt,
            @JsonProperty("overdraftCount") int overdraftCount
    ) {
        super(accountId, userId, accountType, balance, status, createdAt, overdraftCount);
    }
}

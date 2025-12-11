package com.ga.banking.with.java.entities;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ga.banking.with.java.enums.AccountStatus;
import com.ga.banking.with.java.enums.AccountType;

import java.time.LocalDate;

public class CheckingAccount extends Account {
    public CheckingAccount(String userId, double balance) {
        super(userId, AccountType.Checking, balance);
    }

    @JsonCreator
    public CheckingAccount(
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

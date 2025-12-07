package com.ga.banking.with.java.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ga.banking.with.java.enums.TransactionStatus;

import java.time.LocalDateTime;

public class Transaction {
    private final String transactionId;
    private final String fromAccountId;
    private final String toAccountId;
    private final double amount;
    private final LocalDateTime timestamp;
    private final TransactionStatus status;
    private final String description;

    @JsonCreator
    public Transaction(
            @JsonProperty("transactionId") String transactionId,
            @JsonProperty("fromAccountId") String fromAccountId,
            @JsonProperty("toAccountId") String toAccountId,
            @JsonProperty("amount") double amount,
            @JsonProperty("timestamp") LocalDateTime timestamp,
            @JsonProperty("status") TransactionStatus status,
            @JsonProperty("description") String description) {
        this.transactionId = transactionId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.timestamp = timestamp;
        this.status = status;
        this.description = description;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}

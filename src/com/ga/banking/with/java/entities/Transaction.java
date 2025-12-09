package com.ga.banking.with.java.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ga.banking.with.java.enums.TransactionStatus;
import com.ga.banking.with.java.enums.TransactionType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private final String transactionId;
    private final String fromAccountId;
    private final String toAccountId;
    private final double amount;
    private final Double fromAccountPostTransactionBalance;
    private final Double toAccountPostTransactionBalance;
    private final LocalDateTime timestamp;
    private final TransactionStatus status;
    private final String description;
    private final TransactionType transactionType;

    @JsonCreator
    public Transaction(
            @JsonProperty("transactionId") String transactionId,
            @JsonProperty("fromAccountId") String fromAccountId,
            @JsonProperty("toAccountId") String toAccountId,
            @JsonProperty("amount") double amount,
            @JsonProperty("fromAccountPostTransactionBalance") Double fromAccountPostTransactionBalance,
            @JsonProperty("toAccountPostTransactionBalance") Double toAccountPostTransactionBalance,
            @JsonProperty("timestamp") LocalDateTime timestamp,
            @JsonProperty("status") TransactionStatus status,
            @JsonProperty("description") String description,
            @JsonProperty("transactionType") TransactionType transactionType
    ) {
        this.transactionId = transactionId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.fromAccountPostTransactionBalance = fromAccountPostTransactionBalance;
        this.toAccountPostTransactionBalance = toAccountPostTransactionBalance;
        this.timestamp = timestamp;
        this.status = status;
        this.description = description;
        this.transactionType = transactionType;
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

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public Double getFromAccountPostTransactionBalance() {
        return fromAccountPostTransactionBalance;
    }

    public Double getToAccountPostTransactionBalance() {
        return toAccountPostTransactionBalance;
    }


    public void toStatement(Account account) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String time = (timestamp != null) ? timestamp.format(fmt) : "";
        String type = (transactionType != null) ? transactionType.name() : "";
        String statusStr = (status != null) ? status.name() : "";
        String amountStr = String.format("%,.2f", amount);
        String toId = (toAccountId != null) ? toAccountId : "";
        String fromId = (fromAccountId != null) ? fromAccountId + (toId.isBlank() ? "" : " -->") : "";

        String fromBal = (fromAccountPostTransactionBalance != null) ? String.format("%,.2f",
                fromAccountPostTransactionBalance) : "";
        String toBal = (toAccountPostTransactionBalance != null) ? String.format("%,.2f",
                toAccountPostTransactionBalance) : "";

        String desc = (description != null && !description.isEmpty()) ? description : "";

        // Columns: timestamp(19), type(10), amount(13, right), fromAccount(40), toAccount(40), fromBal(26, right),
        // status(10), description(rest)
        String format = "%-19s | %-10s | %13s | %-40s %-40s | %26s | %-10s | %s";

        System.out.println(String.format(format, time, type, amountStr, fromId, toId,
                account.getAccountId().equals(fromAccountId) ? fromBal : toBal,
                statusStr, desc).trim());
    }

}

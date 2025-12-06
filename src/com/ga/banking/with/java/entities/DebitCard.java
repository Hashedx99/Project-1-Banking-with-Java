package com.ga.banking.with.java.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ga.banking.with.java.enums.CardType;


public class DebitCard {
    private String accountNumber;
    private CardType cardType;
    private double withdrawalLimit;
    private double transferLimitOwnAccount;
    private double transferLimitOtherAccount;
    private double depositLimitOwnAccount;
    private double depositLimitOtherAccount;

    @JsonCreator
    public DebitCard(
            @JsonProperty("accountNumber") String accountNumber,
            @JsonProperty("cardType") CardType cardType,
            @JsonProperty("withdrawalLimit") double withdrawalLimit,
            @JsonProperty("transferLimitOwnAccount") double transferLimitOwnAccount,
            @JsonProperty("transferLimitOtherAccount") double transferLimitOtherAccount,
            @JsonProperty("depositLimitOwnAccount") double depositLimitOwnAccount,
            @JsonProperty("depositLimitOtherAccount") double depositLimitOtherAccount
    ) {
        this.accountNumber = accountNumber;
        this.cardType = cardType;
        this.withdrawalLimit = withdrawalLimit;
        this.transferLimitOwnAccount = transferLimitOwnAccount;
        this.transferLimitOtherAccount = transferLimitOtherAccount;
        this.depositLimitOwnAccount = depositLimitOwnAccount;
        this.depositLimitOtherAccount = depositLimitOtherAccount;
    }

    public double withdrawFunds(double amount) {
        if (isAmountInvalid(amount, withdrawalLimit, "Withdrawal")) {
            return -1;
        }
        System.out.println("Withdrew: " + amount);
        return amount;
    }

    public double transferFundsToOwnAccount(double amount) {
        if (isAmountInvalid(amount, transferLimitOwnAccount, "Transfer")) {
            return -1;
        }
        System.out.println("Transferred: " + amount);
        return amount;
    }

    public double transferFundsToOtherAccount(double amount) {
        if (isAmountInvalid(amount, transferLimitOtherAccount, "Transfer")) {
            return -1;
        }
        System.out.println("Transferred: " + amount);
        return amount;
    }

    public double depositFundsToOwnAccount(double amount) {
        if (isAmountInvalid(amount, depositLimitOwnAccount, "Deposit")) {
            return -1;
        }
        System.out.println("Deposited: " + amount);
        return amount;
    }

    public double depositFundsToOtherAccount(double amount) {
        if (isAmountInvalid(amount, depositLimitOtherAccount, "Deposit")) {
            return -1;
        }
        System.out.println("Deposited: " + amount);
        return amount;
    }


    private boolean isAmountInvalid(double amount, double limit, String operationType) {
        if (amount < 0) {
            System.out.println(operationType + " amount cannot be negative.");
            return true;
        }
        if (amount > limit) {
            System.out.println(operationType + " amount exceeds the limit.");
            return true;
        }
        return false;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public CardType getCardType() {
        return cardType;
    }

    public double getWithdrawalLimit() {
        return withdrawalLimit;
    }

    public double getTransferLimitOwnAccount() {
        return transferLimitOwnAccount;
    }

    public double getTransferLimitOtherAccount() {
        return transferLimitOtherAccount;
    }

    public double getDepositLimitOwnAccount() {
        return depositLimitOwnAccount;
    }

    public double getDepositLimitOtherAccount() {
        return depositLimitOtherAccount;
    }
}

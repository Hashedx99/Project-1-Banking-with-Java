package com.ga.banking.with.java.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ga.banking.with.java.enums.CardType;


public class DebitCard {
    private final String accountNumber;
    private final CardType cardType;
    private final double withdrawalLimit;
    private final double transferLimitOwnAccount;
    private final double transferLimitOtherAccount;
    private final double depositLimitOwnAccount;
    private final double depositLimitOtherAccount;

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

    public double withdrawFunds(double amount, Account account) {
        if (isAmountInvalid(amount, withdrawalLimit, "Withdrawal")) {
            return -1;
        }
        account.withdraw(amount);
        System.out.println("Withdrew: " + amount);
        return amount;
    }

    public double transferFunds(double amount, Account fromAccount, Account toAccount, boolean isOwnAccount) {
        if (fromAccount.getAccountId().equals(toAccount.getAccountId())) {
            System.out.println("Cannot transfer to the same account.");
            return -1;
        }
        if (isAmountInvalid(amount, isOwnAccount ? transferLimitOwnAccount : transferLimitOtherAccount, "Transfer")) {
            return -1;
        }
        toAccount.deposit(fromAccount.withdraw(amount));
        System.out.println("Transferred: " + amount);
        return amount;
    }

    public double depositFunds(double amount, Account account, boolean isOwnAccount) {
        if (isAmountInvalid(amount, isOwnAccount ? depositLimitOwnAccount : depositLimitOtherAccount, "Deposit")) {
            return -1;
        }
        account.deposit(amount);
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

package com.ga.banking.with.java.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ga.banking.with.java.enums.CardType;

import java.time.LocalDate;


public class DebitCard {
    private final String accountNumber;
    private final CardType cardType;
    private final double withdrawalLimit;
    private final double transferLimitOwnAccount;
    private final double transferLimitOtherAccount;
    private final double depositLimitOwnAccount;
    private final double depositLimitOtherAccount;
    private double dailyWithdrawnAmount;
    private double dailyTransferredOwnAmount;
    private double dailyTransferredOtherAmount;
    private double dailyDepositedOwnAmount;
    private double dailyDepositedOtherAmount;
    private LocalDate dailySpentDate;

    @JsonCreator
    public DebitCard(
            @JsonProperty("accountNumber") String accountNumber,
            @JsonProperty("cardType") CardType cardType,
            @JsonProperty("withdrawalLimit") double withdrawalLimit,
            @JsonProperty("transferLimitOwnAccount") double transferLimitOwnAccount,
            @JsonProperty("transferLimitOtherAccount") double transferLimitOtherAccount,
            @JsonProperty("depositLimitOwnAccount") double depositLimitOwnAccount,
            @JsonProperty("depositLimitOtherAccount") double depositLimitOtherAccount,
            @JsonProperty("dailyWithdrawnAmount") double dailyWithdrawnAmount,
            @JsonProperty("dailyTransferredOwnAmount") double dailyTransferredOwnAmount,
            @JsonProperty("dailyTransferredOtherAmount") double dailyTransferredOtherAmount,
            @JsonProperty("dailyDepositedOwnAmount") double dailyDepositedOwnAmount,
            @JsonProperty("dailyDepositedOtherAmount") double dailyDepositedOtherAmount,
            @JsonProperty("dailySpentDate") LocalDate dailySpentDate
    ) {
        this.accountNumber = accountNumber;
        this.cardType = cardType;
        this.withdrawalLimit = withdrawalLimit;
        this.transferLimitOwnAccount = transferLimitOwnAccount;
        this.transferLimitOtherAccount = transferLimitOtherAccount;
        this.depositLimitOwnAccount = depositLimitOwnAccount;
        this.depositLimitOtherAccount = depositLimitOtherAccount;
        this.dailyWithdrawnAmount = dailyWithdrawnAmount;
        this.dailyTransferredOwnAmount = dailyTransferredOwnAmount;
        this.dailyTransferredOtherAmount = dailyTransferredOtherAmount;
        this.dailyDepositedOwnAmount = dailyDepositedOwnAmount;
        this.dailyDepositedOtherAmount = dailyDepositedOtherAmount;
        this.dailySpentDate = dailySpentDate;
    }

    public double withdrawFunds(double amount, Account account) {
        resetDailyLimitsIfNeeded();
        if (isAmountInvalid(amount, (withdrawalLimit-dailyWithdrawnAmount), "Withdrawal")) {
            return -1;
        }
        dailyWithdrawnAmount += amount;
        account.withdraw(amount);
        System.out.println("Withdrew: " + amount);
        return amount;
    }

    public double transferFunds(double amount, Account fromAccount, Account toAccount, boolean isOwnAccount) {
        resetDailyLimitsIfNeeded();
        if (fromAccount.getAccountId().equals(toAccount.getAccountId())) {
            System.out.println("Cannot transfer to the same account.");
            return -1;
        }
        if (isAmountInvalid(amount, isOwnAccount ? (transferLimitOwnAccount - dailyTransferredOwnAmount) :
                        (transferLimitOtherAccount - dailyTransferredOtherAmount),
                "Transfer")) {
            return -1;
        }
        toAccount.deposit(fromAccount.withdraw(amount));
        System.out.println("Transferred: " + amount);
        return amount;
    }

    public double depositFunds(double amount, Account account, boolean isOwnAccount) {
        if (isAmountInvalid(amount, isOwnAccount ? (depositLimitOwnAccount - dailyDepositedOwnAmount) :
                        (depositLimitOtherAccount - dailyDepositedOtherAmount),
                "Deposit")) {
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

    private boolean isOverdraft(Account account, double amount) {
        if (amount > account.getBalance() && account.getOverdraftCount() < 2) {
            System.out.println("Insufficient funds in the account.");
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


    private void resetDailyLimitsIfNeeded() {
        if (dailySpentDate == null || !dailySpentDate.equals(LocalDate.now())) {
            dailyWithdrawnAmount = 0;
            dailyTransferredOwnAmount = 0;
            dailyTransferredOtherAmount = 0;
            dailyDepositedOwnAmount = 0;
            dailyDepositedOtherAmount = 0;
            dailySpentDate = LocalDate.now();
        }
    }
}

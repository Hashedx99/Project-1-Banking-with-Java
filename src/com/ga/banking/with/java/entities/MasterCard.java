package com.ga.banking.with.java.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ga.banking.with.java.enums.CardType;

import java.time.LocalDate;

public class MasterCard extends DebitCard {

    public MasterCard(String accountNumber) {
        super(accountNumber, CardType.MasterCard, 5000,
                20000, 10000,
                200000, 100000, 5000,
                20000, 10000,
                200000, 100000, LocalDate.now());
    }

    @JsonCreator
    public MasterCard(
            @JsonProperty("accountNumber") String accountNumber,
            @JsonProperty("cardType") CardType cardType,
            @JsonProperty("withdrawalLimit") double withdrawalLimit,
            @JsonProperty("transferLimitOwnAccount") double transferLimitOwnAccount,
            @JsonProperty("transferLimitOtherAccounts") double transferLimitOtherAccounts,
            @JsonProperty("depositLimitOwnAccount") double depositLimitOwnAccount,
            @JsonProperty("depositLimitOtherAccounts") double depositLimitOtherAccounts,
            @JsonProperty("dailyWithdrawnAmount") double dailyWithdrawnAmount,
            @JsonProperty("dailyTransferredOwnAmount") double dailyTransferredOwnAmount,
            @JsonProperty("dailyTransferredOtherAmount") double dailyTransferredOtherAmount,
            @JsonProperty("dailyDepositedOwnAmount") double dailyDepositedOwnAmount,
            @JsonProperty("dailyDepositedOtherAmount") double dailyDepositedOtherAmount,
            @JsonProperty("dailySpentDate") LocalDate dailySpentDate
    ) {
        super(accountNumber, cardType, withdrawalLimit, transferLimitOwnAccount,
                transferLimitOtherAccounts, depositLimitOwnAccount, depositLimitOtherAccounts, dailyWithdrawnAmount,
                dailyTransferredOwnAmount, dailyTransferredOtherAmount, dailyDepositedOwnAmount,
                dailyDepositedOtherAmount, dailySpentDate);
    }
}

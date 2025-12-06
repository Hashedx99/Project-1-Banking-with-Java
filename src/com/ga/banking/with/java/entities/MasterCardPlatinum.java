package com.ga.banking.with.java.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ga.banking.with.java.enums.CardType;

public class MasterCardPlatinum extends DebitCard {

    public MasterCardPlatinum(String accountNumber) {
        super(accountNumber, CardType.MasterCardPlatinum, 20000,
                80000, 40000,
                200000, 100000);
    }

    @JsonCreator
    public MasterCardPlatinum(
            @JsonProperty("accountNumber") String accountNumber,
            @JsonProperty("cardType") CardType cardType,
            @JsonProperty("withdrawalLimit") double withdrawalLimit,
            @JsonProperty("transferLimitOwnAccount") double transferLimitOwnAccount,
            @JsonProperty("transferLimitOtherAccounts") double transferLimitOtherAccounts,
            @JsonProperty("depositLimitOwnAccount") double depositLimitOwnAccount,
            @JsonProperty("depositLimitOtherAccounts") double depositLimitOtherAccounts
    ) {
        super(accountNumber, cardType, withdrawalLimit, transferLimitOwnAccount,
                transferLimitOtherAccounts, depositLimitOwnAccount, depositLimitOtherAccounts);
    }
}

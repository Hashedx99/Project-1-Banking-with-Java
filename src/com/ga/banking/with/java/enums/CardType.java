package com.ga.banking.with.java.enums;

public enum CardType {
    MasterCardPlatinum("MasterCard Platinum"),
    MasterCardTitanium("MasterCard Titanium"),
    MasterCard("MasterCard");

    private final String cardTypeName;

    CardType(String cardTypeName) {
        this.cardTypeName = cardTypeName;
    }

    public String getCard() {
        return this.cardTypeName;
    }
}

package com.zedeff.vendingma.models;

import java.math.BigDecimal;

public enum Coin {

    TWO_POUNDS("£2", new BigDecimal("2")),
    ONE_POUND("£1", new BigDecimal("1")),
    FIFTY_PENCE("£0.5", new BigDecimal("0.5")),
    TWENTY_PENCE("£0.20", new BigDecimal("0.2")),
    TEN_PENCE("£0.10", new BigDecimal("0.10")),
    FIVE_PENCE("£0.05", new BigDecimal("0.05")),
    TWO_PENCE("£0.02", new BigDecimal("0.02")),
    ONE_PENNY("£0.01", new BigDecimal("0.01"));

    private String name;
    private BigDecimal denomination;

    Coin(String name, BigDecimal denomination) {
        this.name = name;
        this.denomination = denomination;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getDenomination() {
        return denomination;
    }
}

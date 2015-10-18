package com.zedeff.vendingma.models;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PurchaseResult {

    private boolean isSuccessful;
    private String failedReason;
    private BigDecimal amountToReturn;

    public PurchaseResult(boolean isSuccessful, String failedReason, BigDecimal amountToReturn) {
        this.isSuccessful = isSuccessful;
        this.failedReason = failedReason;
        this.amountToReturn = amountToReturn;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public String getFailedReason() {
        return failedReason;
    }

    public BigDecimal getAmountToReturn() {
        return amountToReturn;
    }

    public Map<Coin, Integer> getCoinsToDispense() {
        if (amountToReturn.equals(BigDecimal.ZERO)) {
            return new HashMap<>();
        }

        Map<Coin, Integer> coinsToDispense = new HashMap<>();
        BigDecimal amountToDispense = amountToReturn;

        for (Coin coin : Coin.values()) {
            BigDecimal coinQuantityToReturn = amountToDispense.divideToIntegralValue(coin.getDenomination());
            if (coinQuantityToReturn.compareTo(BigDecimal.ONE) >= 0) {
                coinsToDispense.put(coin, coinQuantityToReturn.intValue());
                amountToDispense = amountToDispense.subtract(coin.getDenomination().multiply(coinQuantityToReturn));
            }
        }

        return coinsToDispense;
    }
}

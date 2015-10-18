package com.zedeff.vendingma.models;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class PurchaseResultTest {

    @Test
    public void getCoinsToDispenseIsEmptyWhenNothingToReturn() {
        PurchaseResult purchaseResult = new PurchaseResult(true, null, BigDecimal.ZERO);

        assertThat(purchaseResult.getCoinsToDispense(), is(notNullValue()));
        assertThat(purchaseResult.getCoinsToDispense().size(), is(0));
    }

    @Test
    public void getCoinsToDispenseReturnsOnePenny() {
        PurchaseResult purchaseResult = new PurchaseResult(true, null, new BigDecimal("0.01"));

        assertThat(purchaseResult.getCoinsToDispense(), is(notNullValue()));
        assertThat(purchaseResult.getCoinsToDispense().size(), is(1));
        assertThat(purchaseResult.getCoinsToDispense().get(Coin.ONE_PENNY), is(equalTo(1)));
        assertThat(Coin.ONE_PENNY.getName(), is(equalTo("£0.01")));
    }

    @Test
    public void getCoinsToDispenseReturnsTwoPence() {
        PurchaseResult purchaseResult = new PurchaseResult(true, null, new BigDecimal("0.02"));

        assertThat(purchaseResult.getCoinsToDispense(), is(notNullValue()));
        assertThat(purchaseResult.getCoinsToDispense().size(), is(1));
        assertThat(purchaseResult.getCoinsToDispense().get(Coin.TWO_PENCE), is(equalTo(1)));
    }

    @Test
    public void getCoinsToDispenseReturnsThreePence() {
        PurchaseResult purchaseResult = new PurchaseResult(true, null, new BigDecimal("0.03"));

        assertThat(purchaseResult.getCoinsToDispense(), is(notNullValue()));
        assertThat(purchaseResult.getCoinsToDispense().size(), is(2));
        assertThat(purchaseResult.getCoinsToDispense().get(Coin.ONE_PENNY), is(equalTo(1)));
        assertThat(purchaseResult.getCoinsToDispense().get(Coin.TWO_PENCE), is(equalTo(1)));
    }

    @Test
    public void getCoinsToDispenseReturnsFourPence() {
        PurchaseResult purchaseResult = new PurchaseResult(true, null, new BigDecimal("0.04"));

        assertThat(purchaseResult.getCoinsToDispense(), is(notNullValue()));
        assertThat(purchaseResult.getCoinsToDispense().size(), is(1));
        assertThat(purchaseResult.getCoinsToDispense().get(Coin.TWO_PENCE), is(equalTo(2)));
    }

    @Test
    public void getCoinsToDispenseReturnsFivePence() {
        PurchaseResult purchaseResult = new PurchaseResult(true, null, new BigDecimal("0.05"));

        assertThat(purchaseResult.getCoinsToDispense(), is(notNullValue()));
        assertThat(purchaseResult.getCoinsToDispense().size(), is(1));
        assertThat(purchaseResult.getCoinsToDispense().get(Coin.FIVE_PENCE), is(equalTo(1)));
    }

    @Test
    public void getCoinsToDispenseReturnsSixPence() {
        PurchaseResult purchaseResult = new PurchaseResult(true, null, new BigDecimal("0.06"));

        assertThat(purchaseResult.getCoinsToDispense(), is(notNullValue()));
        assertThat(purchaseResult.getCoinsToDispense().size(), is(2));
        assertThat(purchaseResult.getCoinsToDispense().get(Coin.FIVE_PENCE), is(equalTo(1)));
        assertThat(purchaseResult.getCoinsToDispense().get(Coin.ONE_PENNY), is(equalTo(1)));
    }

    @Test
    public void getCoinsToDispenseReturnsPoundsAndPence() {
        PurchaseResult purchaseResult = new PurchaseResult(true, null, new BigDecimal("7.68"));

        assertThat(purchaseResult.getCoinsToDispense(), is(notNullValue()));
        assertThat(purchaseResult.getCoinsToDispense().size(), is(7));
        assertThat(purchaseResult.getCoinsToDispense().get(Coin.TWO_POUNDS), is(equalTo(3)));
        assertThat(purchaseResult.getCoinsToDispense().get(Coin.ONE_POUND), is(equalTo(1)));
        assertThat(purchaseResult.getCoinsToDispense().get(Coin.FIFTY_PENCE), is(equalTo(1)));
        assertThat(purchaseResult.getCoinsToDispense().get(Coin.TEN_PENCE), is(equalTo(1)));
        assertThat(purchaseResult.getCoinsToDispense().get(Coin.FIVE_PENCE), is(equalTo(1)));
        assertThat(purchaseResult.getCoinsToDispense().get(Coin.TWO_PENCE), is(equalTo(1)));
        assertThat(purchaseResult.getCoinsToDispense().get(Coin.ONE_PENNY), is(equalTo(1)));
    }
}

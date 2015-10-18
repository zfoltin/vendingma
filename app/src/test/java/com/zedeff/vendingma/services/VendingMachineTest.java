package com.zedeff.vendingma.services;

import com.zedeff.vendingma.models.Item;
import com.zedeff.vendingma.models.PurchaseResult;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.core.IsNot.not;

public class VendingMachineTest {

    private VendingMachine vendingMachine = new VendingMachineImpl();

    @Test
    public void emptyMachineHasNoStock() {
        assertThat(vendingMachine.getStockLevels(), is(notNullValue()));
        assertThat(vendingMachine.getStockLevels().size(), is(0));
    }

    @Test
    public void addStockAddsTheCorrectQuantity() {
        Item item = new Item("Coke", new BigDecimal("1.75"));
        vendingMachine.addStock(item, 5);

        Map<Item, Integer> stockLevels = vendingMachine.getStockLevels();

        assertThat(stockLevels, hasKey(item));
        assertThat(stockLevels, hasEntry(item, 5));

        Item stockItem = stockLevels.keySet().iterator().next();
        assertThat(stockItem.getName(), is(equalTo(item.getName())));
        assertThat(stockItem.getPrice(), is(equalTo(item.getPrice())));
    }

    @Test
    public void addStockMultipleTimesAccumulatesQuantity() {
        Item item = new Item("Coke", new BigDecimal("1.75"));
        vendingMachine.addStock(item, 5);
        vendingMachine.addStock(item, 7);

        assertThat(vendingMachine.getStockLevels(), hasEntry(item, 12));
    }

    @Test
    public void addDifferentStocks() {
        Item item1 = new Item("Coke", new BigDecimal("1.75"));
        vendingMachine.addStock(item1, 5);
        Item item2 = new Item("Orange drink", new BigDecimal("1.25"));
        vendingMachine.addStock(item2, 2);

        Map<Item, Integer> stockLevels = vendingMachine.getStockLevels();

        assertThat(stockLevels, hasEntry(item1, 5));
        assertThat(stockLevels, hasEntry(item2, 2));
    }

    @Test
    public void purchaseNonExistingItemFailsWithErrorMessage() {
        Item item = new Item("Coke", new BigDecimal("1.75"));
        PurchaseResult purchaseResult = vendingMachine.purchase(item, BigDecimal.TEN);

        assertThat(purchaseResult.isSuccessful(), is(false));
        assertThat(purchaseResult.getFailedReason(), is(not(isEmptyOrNullString())));
        assertThat(purchaseResult.getAmountToReturn(), is(equalTo(BigDecimal.TEN)));
    }

    @Test
    public void purchaseUsingInsufficientAmountFailsWithErrorMessage() {
        Item item = new Item("Coke", new BigDecimal("1.75"));
        vendingMachine.addStock(item, 5);
        PurchaseResult purchaseResult = vendingMachine.purchase(item, new BigDecimal("1.5"));

        assertThat(purchaseResult.isSuccessful(), is(false));
        assertThat(purchaseResult.getFailedReason(), is(not(isEmptyOrNullString())));
        assertThat(purchaseResult.getAmountToReturn(), is(equalTo(BigDecimal.ZERO)));
    }

    @Test
    public void purchaseSuccessfulWithExactAmount() {
        Item item = new Item("Coke", new BigDecimal("1.75"));
        vendingMachine.addStock(item, 5);
        PurchaseResult purchaseResult = vendingMachine.purchase(item, new BigDecimal("1.75"));

        assertThat(purchaseResult.isSuccessful(), is(true));
        assertThat(purchaseResult.getFailedReason(), isEmptyOrNullString());
        assertThat(purchaseResult.getAmountToReturn(), is(equalTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))));
    }

    @Test
    public void purchaseSuccessfulWithAmountToReturn() {
        Item item = new Item("Coke", new BigDecimal("1.75"));
        vendingMachine.addStock(item, 5);
        PurchaseResult purchaseResult = vendingMachine.purchase(item, new BigDecimal("1.85"));

        assertThat(purchaseResult.isSuccessful(), is(true));
        assertThat(purchaseResult.getFailedReason(), isEmptyOrNullString());
        assertThat(purchaseResult.getAmountToReturn(), is(equalTo(new BigDecimal("0.1").setScale(2, RoundingMode.HALF_UP))));
    }

    @Test
    public void getItemForNameFindsItem() {
        Item item = new Item("Coke", new BigDecimal("1.75"));
        vendingMachine.addStock(item, 5);

        Item result = vendingMachine.getItemForName(item.getName());

        assertThat(result.getName(), is(equalTo(item.getName())));
        assertThat(result.getPrice(), is(equalTo(item.getPrice())));
    }

    @Test
    public void getItemForNameDoesNotFindNonExistentItem() {
        Item item = new Item("Coke", new BigDecimal("1.75"));
        vendingMachine.addStock(item, 5);

        Item result = vendingMachine.getItemForName("Not coke");

        assertThat(result, is(nullValue()));
    }
}

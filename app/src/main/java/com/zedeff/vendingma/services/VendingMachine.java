package com.zedeff.vendingma.services;

import com.zedeff.vendingma.models.Item;
import com.zedeff.vendingma.models.PurchaseResult;

import java.math.BigDecimal;
import java.util.Map;

public interface VendingMachine {

    void addStock(Item item, int amount);

    Map<Item, Integer> getStockLevels();

    PurchaseResult purchase(Item item, BigDecimal amount);
}

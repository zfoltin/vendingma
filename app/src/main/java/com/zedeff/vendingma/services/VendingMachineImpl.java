package com.zedeff.vendingma.services;

import android.support.annotation.Nullable;

import com.zedeff.vendingma.models.PurchaseResult;
import com.zedeff.vendingma.models.Item;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class VendingMachineImpl implements VendingMachine {

    private Map<Item, Integer> stock = new HashMap<>();

    @Override
    public void addStock(Item item, int amount) {
        if (stock.containsKey(item)) {
            amount += stock.get(item);
        }
        stock.put(item, amount);
    }

    @Override
    public Map<Item, Integer> getStockLevels() {
        return stock;
    }

    @Override
    public PurchaseResult purchase(Item item, BigDecimal amount) {
        if (!stock.containsKey(item) || stock.get(item) < 1) {
            return new PurchaseResult(false, "Item is not available", amount);
        }
        if (item.getPrice().compareTo(amount) > 0) {
            return new PurchaseResult(false, "Not enough money, keep those coins coming!", BigDecimal.ZERO);
        }

        stock.put(item, stock.get(item) - 1);

        return new PurchaseResult(true, null, amount.subtract(item.getPrice()).setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    @Nullable
    public Item getItemForName(String itemName) {
        for (Item i : stock.keySet()) {
            if (i.getName().equals(itemName)) {
                return i;
            }
        }
        return null;
    }
}

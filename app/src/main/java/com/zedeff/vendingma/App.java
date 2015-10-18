package com.zedeff.vendingma;

import android.app.Application;

import com.zedeff.vendingma.models.Item;
import com.zedeff.vendingma.services.VendingMachine;
import com.zedeff.vendingma.services.VendingMachineImpl;

import java.math.BigDecimal;

public class App extends Application {

    private static VendingMachine vendingMachine = new VendingMachineImpl();

    @Override
    public void onCreate() {
        super.onCreate();

        // TODO: remove this - testing only
        vendingMachine.addStock(new Item("Cool Coke", new BigDecimal("1.75")), 2);
        vendingMachine.addStock(new Item("Bitter beer", new BigDecimal("3.75")), 1);
        vendingMachine.addStock(new Item("Empty bucket", new BigDecimal("0.15")), 0);
    }

    public static VendingMachine getVendingMachine() {
        return vendingMachine;
    }
}

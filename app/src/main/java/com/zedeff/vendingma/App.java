package com.zedeff.vendingma;

import android.app.Application;

import com.zedeff.vendingma.services.VendingMachine;
import com.zedeff.vendingma.services.VendingMachineImpl;

public class App extends Application {

    private static VendingMachine vendingMachine = new VendingMachineImpl();

    public static VendingMachine getVendingMachine() {
        return vendingMachine;
    }
}

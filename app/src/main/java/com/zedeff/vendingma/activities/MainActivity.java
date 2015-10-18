package com.zedeff.vendingma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;
import android.widget.ListView;

import com.zedeff.vendingma.App;
import com.zedeff.vendingma.R;
import com.zedeff.vendingma.adapters.StockAdapter;
import com.zedeff.vendingma.models.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.empty_view)
    View emptyView;
    @Bind(R.id.stock_container)
    View stockContainerView;
    @Bind(R.id.stock_list)
    ListView stockList;

    private StockAdapter stockAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        stockAdapter = new StockAdapter(this, null);
        stockList.setAdapter(stockAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean isEmpty = App.getVendingMachine().getStockLevels().size() == 0;
        showEmptyView(isEmpty);

        if (!isEmpty) {
            stockAdapter.update(getStockList(App.getVendingMachine().getStockLevels()));
        }
    }

    private void showEmptyView(boolean isEmpty) {
        emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        stockContainerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    private List<Pair<Item, Integer>> getStockList(Map<Item, Integer> stockLevels) {
        List<Pair<Item, Integer>> items = new ArrayList<>();
        for (Item i : stockLevels.keySet()) {
            items.add(new Pair<>(i, stockLevels.get(i)));
        }

        sortListByItemName(items);

        return items;
    }

    private void sortListByItemName(List<Pair<Item, Integer>> items) {
        Collections.sort(items, new Comparator<Pair<Item, Integer>>() {
            @Override
            public int compare(Pair<Item, Integer> lhs, Pair<Item, Integer> rhs) {
                return lhs.first.getName().compareToIgnoreCase(rhs.first.getName());
            }
        });
    }

    @OnClick(R.id.fab)
    void onAdd() {
        startActivity(new Intent(MainActivity.this, AddStockActivity.class));
    }
}

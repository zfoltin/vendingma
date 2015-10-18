package com.zedeff.vendingma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.zedeff.vendingma.App;
import com.zedeff.vendingma.R;
import com.zedeff.vendingma.adapters.StockAdapter;
import com.zedeff.vendingma.models.Item;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.empty_view)
    View emptyView;
    @Bind(R.id.stock_list)
    ListView stockList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (App.getVendingMachine().getStockLevels().size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            stockList.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            stockList.setVisibility(View.VISIBLE);

            List<Item> items = new ArrayList<>(App.getVendingMachine().getStockLevels().keySet());
            stockList.setAdapter(new StockAdapter(this, items));
        }
    }

    @OnClick(R.id.fab)
    void onAdd() {
        startActivity(new Intent(MainActivity.this, AddStockActivity.class));
    }
}

package com.zedeff.vendingma.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

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

    @Bind(R.id.coordinator_layout)
    View coordinatorLayoutView;
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

        stockAdapter = new StockAdapter(this, new ArrayList<Pair<Item, Integer>>());
        stockList.setAdapter(stockAdapter);
        stockList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pair<Item, Integer> itemWithQuantity = stockAdapter.getItem(position);
                if (itemWithQuantity.second > 0) {
                    Intent intent = PurchaseActivity.getIntent(MainActivity.this, itemWithQuantity.first.getName());
                    startActivity(intent);
                } else {
                    Snackbar.make(coordinatorLayoutView, getString(R.string.error_out_of_stock), Snackbar.LENGTH_LONG).show();
                }
            }
        });
        registerForContextMenu(stockList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    private void updateList() {
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.stock_list) {
            getMenuInflater().inflate(R.menu.stock_context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.restock) {
            askForRestockQuantity(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void askForRestockQuantity(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.label_restock);

        final EditText restockQuantityView = new EditText(this);
        restockQuantityView.setInputType(InputType.TYPE_CLASS_NUMBER);
        restockQuantityView.setHint(getString(R.string.label_quantity_to_restock));
        builder.setView(restockQuantityView);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int quantity;
                try {
                    quantity = Integer.parseInt(restockQuantityView.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, getString(R.string.error_invalid_amount), Toast.LENGTH_LONG).show();
                    return;
                }

                Pair<Item, Integer> itemWithQuantity = stockAdapter.getItem(position);
                App.getVendingMachine().addStock(itemWithQuantity.first, quantity);
                updateList();
            }
        });

        builder.show();

        setRestockQuantityViewMargin(restockQuantityView);
    }

    private void setRestockQuantityViewMargin(EditText restockQuantityView) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = (int) getResources().getDimension(R.dimen.double_spacing);
        layoutParams.setMargins(margin, 0, margin, 0);
        restockQuantityView.setLayoutParams(layoutParams);
    }

    @OnClick(R.id.fab)
    void onAdd() {
        startActivity(new Intent(MainActivity.this, AddStockActivity.class));
    }
}

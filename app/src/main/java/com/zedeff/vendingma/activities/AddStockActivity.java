package com.zedeff.vendingma.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;

import com.zedeff.vendingma.App;
import com.zedeff.vendingma.models.Item;
import com.zedeff.vendingma.R;

import java.math.BigDecimal;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddStockActivity extends AppCompatActivity {

    @Bind(R.id.name)
    EditText nameView;
    @Bind(R.id.price)
    EditText priceView;
    @Bind(R.id.quantity)
    EditText quantityView;

    private String name;
    private BigDecimal price;
    private int quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.bind(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.add_button)
    void onAddItem() {
        if (isValid()) {
            Item item = new Item(name, price);
            App.getVendingMachine().addStock(item, quantity);
            finish();
        }
    }

    private boolean isValid() {
        return validateName() && validatePrice() && validateQuantity();
    }

    private boolean validateName() {
        name = nameView.getText().toString();
        if (TextUtils.isEmpty(name)) {
            nameView.setError("Item name is missing");
            return false;
        }
        return true;
    }

    private boolean validatePrice() {
        try {
            price = new BigDecimal(priceView.getText().toString().replace(",", ""));
        } catch (NumberFormatException e) {
            priceView.setError("Price is invalid");
            return false;
        }
        return true;
    }

    private boolean validateQuantity() {
        try {
            quantity = Integer.parseInt(quantityView.getText().toString());
        } catch (NumberFormatException e) {
            quantityView.setError("Quantity is invalid");
            return false;
        }

        if (quantity < 1) {
            quantityView.setError("Quantity should be greater than 0");
            return false;
        }
        return true;
    }
}

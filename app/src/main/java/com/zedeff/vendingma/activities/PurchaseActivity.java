package com.zedeff.vendingma.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zedeff.vendingma.App;
import com.zedeff.vendingma.R;
import com.zedeff.vendingma.models.Coin;
import com.zedeff.vendingma.models.Item;
import com.zedeff.vendingma.models.PurchaseResult;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PurchaseActivity extends AppCompatActivity {

    public static final String ITEM_NAME = "item_name";

    @Bind(R.id.container)
    View containerView;
    @Bind(R.id.item_to_purchase)
    TextView itemToPurchaseView;
    @Bind(R.id.money_given)
    EditText moneyGivenView;

    private Item item;

    public static Intent getIntent(Context context, String itemName) {
        Intent intent = new Intent(context, PurchaseActivity.class);
        intent.putExtra(ITEM_NAME, itemName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.bind(this);

        setItemInfo();
    }

    private void setItemInfo() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String itemName = extras.getString(ITEM_NAME);
            item = App.getVendingMachine().getItemForName(itemName);
            if (item != null) {
                itemToPurchaseView.setText(String.format(getString(R.string.label_item_info), item.getName(), item.getPrice().toString()));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.purchase_button)
    void onPurchase() {
        BigDecimal amount;
        try {
            amount = new BigDecimal(moneyGivenView.getText().toString());
        } catch (NumberFormatException e) {
            moneyGivenView.setError(getString(R.string.error_invalid_amount));
            return;
        }
        
        PurchaseResult purchaseResult = App.getVendingMachine().purchase(item, amount);
        if (!purchaseResult.isSuccessful()) {
            Snackbar.make(containerView, purchaseResult.getFailedReason(), Snackbar.LENGTH_LONG).show();
            return;
        }

        if (purchaseResult.getCoinsToDispense().size() == 0) {
            Toast.makeText(this, getString(R.string.label_thank_you), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        showDialogWithCoinsToDispense(purchaseResult);
    }

    private void showDialogWithCoinsToDispense(PurchaseResult purchaseResult) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(String.format(getString(R.string.label_your_change), purchaseResult.getAmountToReturn().toString()));

        CharSequence[] coinList = getCoinListToDispense(purchaseResult.getCoinsToDispense());
        builder.setItems(coinList, null);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.show();
    }

    @NonNull
    private CharSequence[] getCoinListToDispense(Map<Coin, Integer> coinsToDispense) {
        CharSequence[] coinList = new CharSequence[coinsToDispense.size()];
        int i = 0;
        for (Map.Entry<Coin, Integer> entry : coinsToDispense.entrySet()) {
            coinList[i] = entry.getKey().getName() + " x " + entry.getValue().toString();
            i++;
        }

        Arrays.sort(coinList);
        return coinList;
    }
}

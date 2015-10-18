package com.zedeff.vendingma.adapters;

import android.app.Activity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zedeff.vendingma.R;
import com.zedeff.vendingma.models.Item;

import java.math.BigDecimal;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StockAdapter extends ArrayAdapter<Pair<Item, Integer>> {

    private Activity context;
    private final List<Pair<Item, Integer>> stock;

    public StockAdapter(Activity context, List<Pair<Item, Integer>> stock) {
        super(context, -1, stock);
        this.context = context;
        this.stock = stock;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.item_stock, parent, false);
            rowView.setTag(new ViewHolder(rowView));
        }

        ViewHolder viewHolder = (ViewHolder) rowView.getTag();
        Pair<Item, Integer> itemWithQuantity = stock.get(position);
        viewHolder.update(itemWithQuantity.first, itemWithQuantity.second);

        return rowView;
    }

    public void update(List<Pair<Item, Integer>> stock) {
        this.stock.clear();
        this.stock.addAll(stock);
        notifyDataSetChanged();
    }

    class ViewHolder {

        @Bind(R.id.name)
        TextView nameView;
        @Bind(R.id.quantity)
        TextView quantityView;
        @Bind(R.id.price)
        TextView priceView;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }

        public void update(Item item, int quantity) {
            nameView.setText(item.getName());
            quantityView.setText(quantity == 0 ? "Out of stock :(" : String.format("Only %d left", quantity));
            priceView.setText(String.format("£%s", item.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
        }
    }
}

package com.zedeff.vendingma.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zedeff.vendingma.models.Item;
import com.zedeff.vendingma.R;

import java.math.BigDecimal;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StockAdapter extends ArrayAdapter<Item> {

    private Activity context;
    private List<Item> items;

    public StockAdapter(Activity context, List<Item> items) {
        super(context, -1, items);
        this.context = context;
        this.items = items;
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
        viewHolder.update(items.get(position));

        return rowView;
    }

    class ViewHolder {

        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.price)
        TextView price;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }

        public void update(Item item) {
            name.setText(item.getName());
            price.setText(String.format("£%s", item.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
        }
    }
}

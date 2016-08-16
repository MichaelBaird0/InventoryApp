package com.example.android.inventory;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventory.ItemContract.ItemEntry;

public class ItemCursorAdapter extends CursorAdapter {

    public ItemCursorAdapter(Activity context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView item = (TextView) view.findViewById(R.id.product);
        TextView price = (TextView) view.findViewById(R.id.price);
        final TextView quantity = (TextView) view.findViewById(R.id.product_quantity);

        String givenItem = cursor.getString(cursor.getColumnIndexOrThrow(ItemEntry.PRODUCT));
        String givenPrice = cursor.getString(cursor.getColumnIndexOrThrow(ItemEntry.PRICE));
        final String givenQuantity = cursor.getString(cursor.getColumnIndexOrThrow(ItemEntry.QUANTITY));

        item.setText(givenItem);
        price.setText(givenPrice);
        quantity.setText(givenQuantity);

        Button itemSale = (Button) view.findViewById(R.id.sold);
        itemSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.parseInt(givenQuantity);

                if(qty > 0)
                {
                    qty--;
                }
                quantity.setText(String.valueOf(qty));
            }
        });
    }
}

package com.example.android.inventory;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        TextView quantity = (TextView) view.findViewById(R.id.quantity);

        String givenItem = cursor.getString(cursor.getColumnIndexOrThrow(ItemEntry.PRODUCT));
        String givenPrice = cursor.getString(cursor.getColumnIndexOrThrow(ItemEntry.PRICE));
        String givenQuantity = cursor.getString(cursor.getColumnIndexOrThrow(ItemEntry.QUANTITY));

        item.setText(givenItem);
        price.setText(givenPrice);
        quantity.setText(givenQuantity);
    }
}

package com.example.android.inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.inventory.ItemContract.ItemEntry;

import java.io.FileDescriptor;
import java.io.IOException;

public class ItemDetailActivity extends AppCompatActivity {

    private String name;
    private int inStock;
    private double cost;
    private ContentValues addValue;
    private ContentValues subValue;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        long _id = getIntent().getLongExtra("KEY", -1);
        Uri itemUri = ItemEntry.buildItemsUri(_id);
        Cursor c = getContentResolver().query(itemUri, null, null, null, null);


        if (c.moveToNext()) {
            name = c.getString(c.getColumnIndexOrThrow(ItemEntry.PRODUCT));
            cost = Double.parseDouble(c.getString(c.getColumnIndexOrThrow(ItemEntry.PRICE)));
            inStock = Integer.parseInt(c.getString
                    (c.getColumnIndexOrThrow(ItemEntry.QUANTITY)));
            id = c.getLong(c.getColumnIndexOrThrow(ItemEntry._ID));
        }

        TextView itemName = (TextView) findViewById(R.id.product_name);
        TextView itemPrice = (TextView) findViewById(R.id.product_price);
        final TextView itemQuantity = (TextView) findViewById(R.id.product_quantity);
        ImageView itemImage = (ImageView) findViewById(R.id.product_image);

        itemName.setText(name);
        itemPrice.setText("$" + String.valueOf(cost));
        itemQuantity.setText(String.valueOf(inStock));

        Glide.with(itemImage.getContext())
                .load(getBitMapFromUri(ItemEntry.CONTENT_URI))
                .override(100, 100) // resizes the image to these dimensions (in pixel)
                .centerCrop() // this cropping technique scales the image so that it fills the requested bounds and then crops the extra.
                .into(itemImage);
        //itemImage.setImageBitmap(getBitMapFromUri(ItemEntry.CONTENT_URI));

        Button increase = (Button) findViewById(R.id.increase);
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = ++inStock;
                itemQuantity.setText(String.valueOf(qty));

                addValue = new ContentValues();
                addValue.put(ItemEntry.QUANTITY, String.valueOf(qty));
                Uri uri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, id);
                getContentResolver().update(uri, addValue, null, null);
            }
        });

        Button decrease = (Button) findViewById(R.id.decrease);
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = --inStock;
                if (inStock == 0) {
                    qty = inStock;
                }
                itemQuantity.setText(String.valueOf(qty));

                subValue = new ContentValues();
                subValue.put(ItemEntry.QUANTITY, String.valueOf(qty));
                Uri uri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, id);
                getContentResolver().update(uri, subValue, null, null);
            }
        });

        Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ItemDetailActivity.this);
                alert.setMessage("Do you want to delete this item?");
                alert.setCancelable(true);


                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getContentResolver().delete(ItemEntry.CONTENT_URI,
                                ItemEntry.PRODUCT + " = ? ", new String[]{name});
                        Intent intent = new Intent(ItemDetailActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                alert.create().show();
            }
        });


        Button order = (Button) findViewById(R.id.order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent email = new Intent(Intent.ACTION_SENDTO);
                email.setType("*/*");
                email.putExtra(Intent.EXTRA_SUBJECT, "Order more of product!");
                if (email.resolveActivity(getPackageManager()) != null) {
                    startActivity(email);
                }
            }
        });
    }

    private Bitmap getBitMapFromUri(Uri uri) {
        ParcelFileDescriptor parcelFileDescriptor = null;
        try {
            parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(uri, ItemEntry.IMAGE);
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        } catch (Exception e) {
            Log.e("Failed Image Load", "Failed to load image.", e);
            return null;
        } finally {
            try {
                if (parcelFileDescriptor != null) {
                    parcelFileDescriptor.close();
                }
            } catch (IOException e) {
                Log.e("Error Closing PFile", "Error closing ParcelFile Descriptor", e);
            }
        }
    }
}

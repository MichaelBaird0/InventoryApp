package com.example.android.inventory;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.inventory.ItemContract.ItemEntry;

import java.io.FileDescriptor;
import java.io.IOException;

public class ItemDetail extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        String URL = ItemEntry.CONTENT_TYPE;
        Uri items = Uri.parse(URL);

        Cursor c = getContentResolver().query(items, null, null, null, null);

        String name = c.getString(c.getColumnIndexOrThrow(ItemEntry.PRODUCT));
        double cost = Double.parseDouble(c.getString(c.getColumnIndexOrThrow(ItemEntry.PRICE)));
        int inStock = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(ItemEntry.QUANTITY)));

        c.close();

        TextView itemName = (TextView) findViewById(R.id.product_name);
        TextView itemPrice = (TextView) findViewById(R.id.product_price);
        TextView itemQuantity = (TextView) findViewById(R.id.product_qunatity);
        ImageView itemImage = (ImageView) findViewById(R.id.product_image);

        itemName.setText(name);
        itemPrice.setText(R.string.dollar_sign + String.valueOf(cost));
        itemQuantity.setText(inStock);
        itemImage.setImageBitmap(getBitMapFromUri(ItemEntry.CONTENT_URI));
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

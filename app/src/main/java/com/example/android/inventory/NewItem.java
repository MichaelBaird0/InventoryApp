package com.example.android.inventory;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.inventory.ItemContract.ItemEntry;

import java.io.IOException;

public class NewItem extends AppCompatActivity {

    private EditText itemName;
    private EditText price;
    private EditText quantity;
    private Button imageButton;
    private Button done;

    private static final int PICK_IMAGE = 1;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        itemName = (EditText) findViewById(R.id.input_item);
        price = (EditText) findViewById(R.id.add_price);
        quantity = (EditText) findViewById(R.id.add_quantity);

        imageButton = (Button) findViewById(R.id.image_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageIntent;

                if (Build.VERSION.SDK_INT < 19) {
                    imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
                } else {
                    imageIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    imageIntent.addCategory(Intent.CATEGORY_OPENABLE);
                }

                imageIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(imageIntent, "Select Picture"), PICK_IMAGE);
            }
        });

        done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();

                values.put(ItemEntry.PRODUCT, itemName.getText().toString());
                values.put(ItemEntry.PRICE, price.getText().toString());
                values.put(ItemEntry.QUANTITY, quantity.getText().toString());
                values.put(ItemEntry.IMAGE, uri.toString());

                Uri returnUri = getContentResolver().insert(ItemEntry.CONTENT_URI, values);
                Toast.makeText(getBaseContext(), returnUri.toString() + "inserted!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            String[] projection = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            cursor.close();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView image = (ImageView) findViewById(R.id.product_image);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                Log.e("Attaching Image", "Error attaching the image to product", e);
            }
        }
    }
}

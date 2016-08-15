package com.example.android.inventory;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbHelper handler = new DbHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();
        final Cursor cursor = db.rawQuery("SELECT  * FROM " + ItemContract.ItemEntry.TABLE_NAME
                , null);

        ListView listView = (ListView) findViewById(R.id.inventory_list);
        final ItemCursorAdapter adapter = new ItemCursorAdapter(this, cursor);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent detailIntent = new Intent(MainActivity.this, ItemDetail.class);
                detailIntent.putExtra("_id", id);
                startActivity(detailIntent);
            }
        });

        cursor.close();

        Button addItem = (Button) findViewById(R.id.add_item);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newItemIntent = new Intent(MainActivity.this, NewItem.class);
                startActivity(newItemIntent);
            }
        });

        Button sale = (Button) findViewById(R.id.sale);
        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String URL = ItemContract.ItemEntry.CONTENT_TYPE;
                Uri items = Uri.parse(URL);

                Cursor c = getContentResolver().query(items, null, null, null, null);
                int inStock = Integer.parseInt(c.getString(c.getColumnIndexOrThrow
                        (ItemContract.ItemEntry.QUANTITY)));
                c.close();

                TextView itemQuantity = (TextView) findViewById(R.id.product_qunatity);

                if (inStock > 0) {
                    int qty = inStock - 1;
                    itemQuantity.setText(qty);
                }
            }
        });
    }
}

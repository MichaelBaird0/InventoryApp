package com.example.android.inventory;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        if(adapter.isEmpty())
        {
            TextView empty = (TextView) findViewById(R.id.empty);
            empty.setText(R.string.ask_input);
        }
        else {
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Intent detailIntent = new Intent(MainActivity.this, ItemDetailActivity.class);
                    detailIntent.putExtra("_id", id);
                    startActivity(detailIntent);
                }
            });

            cursor.close();

            Button itemSale = (Button) findViewById(R.id.sold);
            itemSale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getBaseContext(), "Sold one item", Toast.LENGTH_SHORT).show();

                    TextView itemQuantity = (TextView) findViewById(R.id.product_quantity);
                    int inStock = Integer.parseInt(itemQuantity.getText().toString());

                    if (inStock > 0) {
                        int qty = inStock - 1;
                        itemQuantity.setText(qty);
                    }
                }
            });
        }

        Button addItem = (Button) findViewById(R.id.add_item);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newItemIntent = new Intent(MainActivity.this, NewItemActivity.class);
                startActivity(newItemIntent);
            }
        });
    }
}

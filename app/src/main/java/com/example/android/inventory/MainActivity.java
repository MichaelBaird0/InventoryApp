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

import com.example.android.inventory.ItemContract.ItemEntry;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbHelper handler = new DbHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();
        final Cursor cursor = db.rawQuery("SELECT  * FROM " + ItemEntry.TABLE_NAME, null);

        ListView listView = (ListView) findViewById(R.id.inventory_list);
        final ItemCursorAdapter adapter = new ItemCursorAdapter(this, cursor);
        listView.setAdapter(adapter);

        if (adapter.isEmpty()) {
            TextView empty = (TextView) findViewById(R.id.empty);
            empty.setText(R.string.ask_input);
        } else {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                        long id) {
                    Cursor c = (Cursor) adapter.getItem(position);
                    c.moveToPosition(position);
                    long _id = c.getLong(c.getColumnIndexOrThrow(ItemEntry._ID));

                    Intent detailIntent = new Intent(MainActivity.this, ItemDetailActivity.class);
                    detailIntent.putExtra("KEY",_id);  //MAGIC
                    startActivity(detailIntent);
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

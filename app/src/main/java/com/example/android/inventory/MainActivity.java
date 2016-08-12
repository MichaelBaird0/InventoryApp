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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbHelper handler = new DbHelper(this);
        SQLiteDatabase db = handler.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + ItemReaderContract.ItemEntry.TABLE_NAME
                , null);

        ListView listView = (ListView) findViewById(R.id.inventory_list);
        ItemCursorAdapter adapter = new ItemCursorAdapter(this, cursor);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String URL = "content://com.example.android.inventory/items";
                Uri items = Uri.parse(URL);

                Intent detailIntent = new Intent(Intent.ACTION_VIEW, items);
                detailIntent.setClass(MainActivity.this, ItemDetail.class);
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
    }
}

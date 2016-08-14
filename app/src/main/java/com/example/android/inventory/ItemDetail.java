package com.example.android.inventory;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.inventory.ItemContract.ItemEntry;

public class ItemDetail extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        String URL = ItemEntry.CONTENT_TYPE;
        Uri items = Uri.parse(URL);


    }
}

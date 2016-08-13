package com.example.android.inventory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper
{
    //DbHelper constructor
    public DbHelper(Context context)
    {
        super(context, ItemContract.ItemEntry.DATABASE_NAME, null,
                ItemContract.ItemEntry.DATABASE_VERSION);
    }

    //Create the SQLiteDataBase
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(ItemContract.ItemEntry.DATABASE_CREATE);
    }

    //Upgrade the SQLiteDataBase
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("drop table if exists " + ItemContract.ItemEntry.TABLE_NAME);
        onCreate(db);
    }
}

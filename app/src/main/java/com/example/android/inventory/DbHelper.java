package com.example.android.inventory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.inventory.ItemContract.ItemEntry;

public class DbHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "items.db";

    //DbHelper constructor
    public DbHelper(Context context)
    {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    //Create the SQLiteDataBase
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String DATABASE_CREATE = "CREATE TABLE IF DOES NOT EXISTS "
                + ItemEntry.TABLE_NAME + "("
        db.execSQL(DATABASE_CREATE);
    }

    //Upgrade the SQLiteDataBase
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("drop table if exists " + ItemEntry.TABLE_NAME);
        onCreate(db);
    }
}

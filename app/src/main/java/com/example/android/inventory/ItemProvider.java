package com.example.android.inventory;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

public class ItemProvider extends ContentProvider {
    //Defining Uri
    static final String CONTENT_AUTHORITY = "com.example.android.inventory";
    static final String URL = "content://" + CONTENT_AUTHORITY + "/items";
    static final Uri CONTENT_URI = Uri.parse(URL);

    DbHelper dbHelper;
    SQLiteDatabase db;

    private static HashMap<String, String> ItemMap;

    //Integers used in the Uri
    static final int ITEMS = 1;
    static final int ITEMS_ID = 2;

    //map content Uri "patterns"
    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CONTENT_AUTHORITY, "items", ITEMS);
        uriMatcher.addURI(CONTENT_AUTHORITY, "items/#", ITEMS_ID);
    }

    //Make sure dbHelper has SQLiteHelper initialized
    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();

        if (dbHelper == null) {
            return false;
        } else {
            return true;
        }
    }

    //Cursor for reading from the table
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        //Auto-generated method stub
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        //Table name to read from
        builder.setTables(ItemReaderContract.ItemEntry.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case ITEMS:
                builder.setProjectionMap(ItemMap);
                break;
            case ITEMS_ID:
                builder.appendWhere(ItemReaderContract.ItemEntry.ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        if (sortOrder == null || sortOrder == "") {
            //Sort on ID by default
            sortOrder = ItemReaderContract.ItemEntry.ID;
        }

        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null,
                sortOrder);
        try {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        catch (NullPointerException e)
        {
            Log.e("Cursor Notification","Cursor cannot properly set notification", e);
        }

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long row = db.insert(ItemReaderContract.ItemEntry.TABLE_NAME, "", contentValues);

        if (row > 0) {
            Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        throw new SQLException("Fail to add a new record into " + uri);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;

        switch (uriMatcher.match(uri)) {
            case ITEMS:
                count = db.update(ItemReaderContract.ItemEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case ITEMS_ID:
                count = db.update(ItemReaderContract.ItemEntry.TABLE_NAME, values,
                        ItemReaderContract.ItemEntry.ID + " = " + uri.getLastPathSegment() +
                                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported Uri " + uri);
        }
        try{
            getContext().getContentResolver().notifyChange(uri, null);
        }
        catch (NullPointerException e)
        {
            Log.e("Notify Change", "Unable to update the data", e);
        }
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;

        switch (uriMatcher.match(uri)) {
            case ITEMS:
                count = db.delete(ItemReaderContract.ItemEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEMS_ID:
                String id = uri.getLastPathSegment();
                count = db.delete(ItemReaderContract.ItemEntry.TABLE_NAME,
                        ItemReaderContract.ItemEntry.ID + " = " + id +
                                (!TextUtils.isEmpty(selection) ? "AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported Uri " + uri);
        }
        return count;
    }


    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ITEMS:
                return "vnd.android.cursor.dir/vnd.example.items";
            case ITEMS_ID:
                return "vnd.android.cursor.item/vnd.example.items";
            default:
                throw new IllegalArgumentException("Unsupported Uri " + uri);
        }
    }
}

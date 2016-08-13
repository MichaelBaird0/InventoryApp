package com.example.android.inventory;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.android.inventory.ItemContract.ItemEntry;

public class ItemProvider extends ContentProvider {
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private static final UriMatcher mUriMatcher = buildUriMatcher();

    //Integers used in the Uri
    static final int ITEMS = 1;
    static final int ITEMS_ID = 2;

    //map content Uri "patterns"
    static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ItemContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, ItemContract.PATH, ITEMS);
        uriMatcher.addURI(authority, ItemContract.PATH + "/#", ITEMS_ID);

        return uriMatcher;
    }

    //Make sure dbHelper has SQLiteHelper initialized
    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return true;
    }

    //Cursor for reading from the table
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch (mUriMatcher.match(uri)) {
            case ITEMS:
                retCursor = dbHelper.getReadableDatabase().query(
                        ItemEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case ITEMS_ID:
                retCursor = dbHelper.getReadableDatabase().query(
                        ItemEntry.TABLE_NAME,
                        projection,
                        ItemEntry._ID + " = ? ",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        db = dbHelper.getWritableDatabase();
        Uri returnUri;

        switch (mUriMatcher.match(uri)) {
            case ITEMS:
                long _id = db.insert(ItemEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = ItemEntry.buildItemsUri(_id);
                } else {
                    throw new SQLException("Failed to insert into row" + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unkown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdated;

        if (values == null) {
            throw new IllegalArgumentException("Cannot have null content value");
        }
        switch (mUriMatcher.match(uri)) {
            case ITEMS:
                rowsUpdated = db.update(ItemEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case ITEMS_ID:
                rowsUpdated = db.update(ItemEntry.TABLE_NAME, values, ItemEntry._ID + " = ? ",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted;

        switch (mUriMatcher.match(uri)) {
            case ITEMS:
                rowsDeleted = db.delete(
                        ItemEntry.TABLE_NAME, selection, selectionArgs);
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"
                        + ItemEntry.TABLE_NAME + "'");
                break;
            case ITEMS_ID:
                rowsDeleted = db.delete(ItemContract.ItemEntry.TABLE_NAME,
                        ItemEntry._ID + " = ? ",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"
                        + ItemEntry.TABLE_NAME + "'");
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return rowsDeleted;
    }


    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case ITEMS:
                return ItemEntry.CONTENT_TYPE;
            case ITEMS_ID:
                return ItemEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unkown uri: " + uri);
        }
    }
}

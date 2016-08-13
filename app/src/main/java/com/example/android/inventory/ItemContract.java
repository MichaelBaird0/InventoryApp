package com.example.android.inventory;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class ItemContract {

    //Authority of the content provider
    public static final String CONTENT_AUTHORITY = "com.example.android.inventory";

    //Added schema of the content provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //Path of the content provider
    public static final String PATH = "items";

    public static abstract class ItemEntry implements BaseColumns {
        public static final Uri CONTENT__URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;

        public static final String TABLE_NAME = "inventory";

        public static final String PRODUCT = "product";

        public static final String IMAGE = "image";

        public static final String QUANTITY = "quantity";

        public static final String DETAIL = "detail";

        public static final String PRICE = "price";

        public static Uri buildItemsUri(long id) {
            return ContentUris.withAppendedId(CONTENT__URI, id);
        }
    }
}

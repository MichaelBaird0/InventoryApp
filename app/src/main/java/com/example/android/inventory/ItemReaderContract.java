package com.example.android.inventory;

import android.provider.BaseColumns;

public final class ItemReaderContract {

    public ItemReaderContract() {
    }

    public static abstract class ItemEntry implements BaseColumns {
        public static final String DATABASE_NAME = "storage";
        public static final String TABLE_NAME = "inventory";
        public static final String ID = "id";
        public static final String PRODUCT = "product";
        public static final String IMAGE = "image";
        public static final String QUANTITY = "quantity";
        public static final String DETAIL = "detail";
        public static final String PRICE = "price";

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_CREATE = "create table if not exists inventory" +
                "(_id integer primary key, product text, image text, quantity text," + "" +
                "detail text, price text);";
    }
}

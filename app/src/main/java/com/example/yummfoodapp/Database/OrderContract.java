package com.example.yummfoodapp.Database;

import android.net.Uri;
import android.provider.BaseColumns;

public class OrderContract {
    public void OrderContract() {
    }

    // sqllite database
    // contentauthority requires you to enter your package name
    public static final String CONTENT_AUTHORITY = "com.example.yummfoodapp";
    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    //table name
    public static final String PATH = "ordering";

    public static abstract class OrderEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PATH);

        public static final String TABLE_NAME = "ordering";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_EXTRAFIRST = "extrafirst";
        //cream
        public static final String COLUMN_EXTRASECOND= "extrasecond";
    }
}

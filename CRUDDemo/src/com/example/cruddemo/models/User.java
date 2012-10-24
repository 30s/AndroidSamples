package com.example.cruddemo.models;

import android.provider.BaseColumns;

public class User {
    public class Meta implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
    
        public static final String SQL_CREATE_USERS =
            "CREATE TABLE " + TABLE_NAME + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY, " +
            COLUMN_USERNAME + " TEXT, " + 
            COLUMN_PASSWORD + " TEXT" + 
            " )";
        public static final String SQL_DELETE_USERS =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
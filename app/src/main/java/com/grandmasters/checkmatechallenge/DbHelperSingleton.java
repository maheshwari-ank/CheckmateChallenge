package com.grandmasters.checkmatechallenge;

import android.content.Context;

public class DbHelperSingleton {
    private static DatabaseHelper instance;

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }
}

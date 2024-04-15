package com.grandmasters.checkmatechallenge;

import android.content.Context;

public class DataManagerSingleton {
    private static DataManager instance;

    public static DataManager getInstance(Context context) {
        if (instance == null) {
            instance = new DataManager(context.getApplicationContext());
        }
        return instance;
    }
}


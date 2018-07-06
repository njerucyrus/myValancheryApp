package com.hudutech.mymanjeri;

import android.content.Context;
import android.content.SharedPreferences;

public class Config {

    public static boolean isAdmin(Context mContext) {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences("AUTH_DATA",
                Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean("isAdmin", false);
    }

    public static boolean isSBAdmin(Context mContext) {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences("AUTH_DATA",
                Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean("isSBAdmin", false);
    }



}

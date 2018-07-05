package com.hudutech.mymanjeri;

import android.content.Context;
import android.content.SharedPreferences;

public class Config {

    public Context mContext;

    public Config(Context mContext) {
        this.mContext = mContext;
    }

    public boolean isAdmin() {
        SharedPreferences sharedPrefs = mContext.getSharedPreferences("AUTH_DATA",
                Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean("isAdmin", false);

    }

}

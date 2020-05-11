package com.example.skin_core.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SkinSharedPreference {

    private static final String SKIN_SHARED = "skins";

    private static final String KEY_SKIN_PATH = "skin-path";

    private final SharedPreferences mPreferences;

    private static SkinSharedPreference mInstance;

    private SkinSharedPreference(Context context) {
        mPreferences = context.getSharedPreferences(SKIN_SHARED, Context.MODE_PRIVATE);
    }

    public static SkinSharedPreference getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SkinSharedPreference.class) {
                if (mInstance == null) {
                    mInstance = new SkinSharedPreference(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    public void setSkin(String skinPath) {
        mPreferences.edit().putString(KEY_SKIN_PATH, skinPath).apply();
    }

    public String getSkin() {
        return mPreferences.getString(KEY_SKIN_PATH, null);
    }

}

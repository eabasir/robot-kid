package com.ansari.smartplug.setting;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Eabasir on 5/26/2016.
 */
public class AppSettings {


    private final String KEY_MAIN_PREF = "com.ansari.smartuplug";

    private static AppSettings Instance;

    public static AppSettings getInstance(Context context) {
        if (Instance == null) {
            Instance = new AppSettings(context);
        }
        return Instance;
    }

    private Context mContext;

    private SharedPreferences mSharedPreferences;

    private AppSettings(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(KEY_MAIN_PREF, Context.MODE_PRIVATE);

    }


    public boolean isDateTimeSyncedForFirstTime()
    {
        return 	mSharedPreferences.getBoolean("dateTimeSync",false);
    }

    public void syncDateTimeForFirstTime(boolean isSet)
    {
        mSharedPreferences.edit().putBoolean("dateTimeSync", isSet).commit();

    }

    public void setSensorAutoRefresh (boolean state)
    {
        mSharedPreferences.edit().putBoolean("sensorAutoRefresh" , state).commit();
    }

    public boolean getSensorAutoRefresh()
    {
        return 	mSharedPreferences.getBoolean("sensorAutoRefresh",false);
    }







}

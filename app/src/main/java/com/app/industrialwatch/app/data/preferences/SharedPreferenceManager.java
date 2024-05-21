package com.app.industrialwatch.app.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

final public class SharedPreferenceManager {

    private static SharedPreferenceManager sharedPreferenceManager = null;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static void setSingletonInstance(Context context) {
        synchronized (SharedPreferenceManager.class) {
            if (sharedPreferenceManager == null)
                sharedPreferenceManager = new SharedPreferenceManager(context);
            else
                Log.d("Shared","SharedPreferenceManager instance already exists.");
        }
    }

    private SharedPreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences("industrial_watch", 0);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public static SharedPreferenceManager getInstance() {
        return sharedPreferenceManager;
    }

    public void clearPreferences() {
        editor.clear();
        editor.commit();
    }

    public String read(String valueKey, String valueDefault) {
        return sharedPreferences.getString(valueKey, valueDefault);
    }

    public void save(String valueKey, String value) {
        editor.putString(valueKey, value);
        editor.commit();
    }

    public int read(String valueKey, int valueDefault) {
        return sharedPreferences.getInt(valueKey, valueDefault);
    }

    public void save(String valueKey, int value) {
        editor.putInt(valueKey, value);
        editor.commit();
    }

    public boolean read(String valueKey, boolean valueDefault) {
        return sharedPreferences.getBoolean(valueKey, valueDefault);
    }

    public void save(String valueKey, boolean value) {
        editor.putBoolean(valueKey, value);
        editor.commit();
    }

    public long read(String valueKey, long valueDefault) {
        return sharedPreferences.getLong(valueKey, valueDefault);
    }

    public void save(String valueKey, long value) {
        editor.putLong(valueKey, value);
        editor.commit();
    }

    public float read(String valueKey, float valueDefault) {
        return sharedPreferences.getFloat(valueKey, valueDefault);
    }

    public void save(String valueKey, float value) {
        editor.putFloat(valueKey, value);
        editor.commit();
    }

}

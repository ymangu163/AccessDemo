package ym.ha.script.utils;

import android.content.Context;
import android.content.SharedPreferences;

import ym.ha.script.activity.AppContext;

public class AppSharePref {

    /************************定义 KEY*****************************/
    private final String KEY_AUTHORIZED = "authorized";
    private final String KEY_NIGHT_MASK = "key_night_mask";
    private final String KEY_MASK_START_TIME = "mask_start_time";
    private final String KEY_MASK_END_TIME = "mask_end_time";
    private final String KEY_HAHA_ID = "haha_id";
    private final String KEY_AUTHO_CHECKED = "autho_checked";


    /************************结束定义 KEY*****************************/
    private SharedPreferences mSharedPreferences = null;
    private static AppSharePref sSnapSharePref;

    public static AppSharePref getInstance() {
        if (sSnapSharePref == null) {
            synchronized (AppSharePref.class) {
                if (sSnapSharePref == null) {
                    sSnapSharePref = new AppSharePref();
                }
            }
        }
        return sSnapSharePref;
    }

    private AppSharePref() {
        mSharedPreferences = AppContext.getInstance().getSharedPreferences("hahaha.prefs", Context.MODE_PRIVATE);
    }


    private void putBoolean(String keyString, boolean value) {
        mSharedPreferences.edit().putBoolean(keyString, value).apply();
    }

    private boolean getBoolean(String keyString) {
        return mSharedPreferences.getBoolean(keyString, false);
    }

    private boolean getBoolean(String keyString, boolean defValue) {
        return mSharedPreferences.getBoolean(keyString, defValue);
    }

    private void putString(String keyString, String value) {
        mSharedPreferences.edit().putString(keyString, value).apply();
    }

    private String getString(String keyString) {
        return mSharedPreferences.getString(keyString, "");
    }

    private String getString(String keyString, String defValue) {
        return mSharedPreferences.getString(keyString, defValue);
    }

    private void putInt(String keyString, int value) {
        mSharedPreferences.edit().putInt(keyString, value).apply();
    }

    private int getInt(String keyString) {
        return mSharedPreferences.getInt(keyString, -1);
    }

    private int getInt(String keyString, int defaultValue) {
        return mSharedPreferences.getInt(keyString, defaultValue);
    }

    private void putLong(String keyString, long value) {
        mSharedPreferences.edit().putLong(keyString, value).apply();
    }

    private long getLong(String keyString) {
        return mSharedPreferences.getLong(keyString, -1);
    }

    private long getLong(String keyString, long defValue) {
        return mSharedPreferences.getLong(keyString, defValue);
    }

    /*********************** 业务 *****************************/

    public void setAuthorized(boolean value) {
        putBoolean(KEY_AUTHORIZED, value);
    }

    public boolean getAuthorized() {
        return getBoolean(KEY_AUTHORIZED);
    }

    public void setNightMask(boolean value) {
        putBoolean(KEY_NIGHT_MASK, value);
    }

    public boolean getNightMask() {
        return getBoolean(KEY_NIGHT_MASK, true);
    }

    public void setMaskStartTime(String value) {
        putString(KEY_MASK_START_TIME, value);
    }

    public String getMaskStartTime() {
        return getString(KEY_MASK_START_TIME, "00:00");
    }

    public void setMasEndTime(String value) {
        putString(KEY_MASK_END_TIME, value);
    }

    public String getMaskEndTime() {
        return getString(KEY_MASK_END_TIME, "08:00");
    }

    public void setHahaId(String value) {
        putString(KEY_HAHA_ID, value);
    }

    public String getHahaId() {
        return getString(KEY_HAHA_ID);
    }

    public void setAuthoChecked(boolean value) {
        putBoolean(KEY_AUTHO_CHECKED, value);
    }

    public boolean getAuthoChecked() {
        return getBoolean(KEY_AUTHO_CHECKED, false);
    }


}

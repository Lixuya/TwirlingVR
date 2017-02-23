package com.twirling.libtwirling.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.twirling.libtwirling.utils.TextUtil;

/**
 * Created by MagicBean on 2016/01/13 15:15:28
 */
public class SharedPreferencesHelper {
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    private long adInterval;

    private SharedPreferencesHelper() {
    }

    private static SharedPreferencesHelper helper;

    public static SharedPreferencesHelper getIns() {
        if (helper == null) {
            helper = new SharedPreferencesHelper();
        }
        return helper;
    }

    public void initialize(Context context, String name) {
        if (TextUtil.isValidate(name)) {
            sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        } else {
            sp = PreferenceManager.getDefaultSharedPreferences(context);
        }
        edit = sp.edit();
    }

    public void saveVideoItem(String videoItem) {
        edit.putString("_videoItem", videoItem).commit();
    }

    public String getVideoList() {
        return sp.getString("_hot_words", "");
    }

}

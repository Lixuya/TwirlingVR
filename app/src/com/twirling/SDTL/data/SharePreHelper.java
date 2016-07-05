package com.twirling.SDTL.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.twirling.SDTL.utils.TextUtil;

/**
 * Created by MagicBean on 2016/01/13 15:15:28
 */
public class SharePreHelper {
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    private long adInterval;

    private SharePreHelper() {
    }

    private static SharePreHelper helper;

    public static SharePreHelper getIns() {
        if (helper == null) {
            helper = new SharePreHelper();
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

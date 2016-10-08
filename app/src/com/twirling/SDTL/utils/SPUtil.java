package com.twirling.SDTL.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.twirling.SDTL.App;
import com.twirling.SDTL.Constants;

/**
 * Created by xieqi on 2016/9/26.
 */

public class SPUtil {
    private static final String TAG = Constants.class.getSimpleName();
    //
    private static String mobile = "";
    private static boolean isLogin = false;

    public static String getUserMobile() {
        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(App.getInst().getApplicationContext());
        String mobile = pre.getString("mobile", "");
        Log.d(TAG, "get " + mobile);
        return mobile;
    }

    public static void setUserMobile(String mobile) {
        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(App.getInst().getApplicationContext());
        SharedPreferences.Editor editor = pre.edit();
        editor.putString("mobile", mobile);
        editor.commit();
        Log.d(TAG, "set " + mobile);
    }

    public static boolean getIsLogin() {
        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(App.getInst().getApplicationContext());
        boolean isLogin = pre.getBoolean("isLogin", false);
        Log.d(TAG, "get " + isLogin);
        return isLogin;
    }

    public static void setIsLogin(boolean isLogin) {
        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(App.getInst().getApplicationContext());
        SharedPreferences.Editor editor = pre.edit();
        editor.putBoolean("isLogin", isLogin);
        editor.commit();
        Log.d(TAG, "set " + isLogin);
    }
}

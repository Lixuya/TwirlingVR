package com.twirling.libtwirling.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


/**
 * Created by xieqi on 2016/9/26.
 */

public class SPUtil {
    private static final String TAG = SPUtil.class.getSimpleName();
    //
    private static String mobile = "";
    private static boolean isLogin = false;

    public static String getUserMobile(Context context) {
        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String mobile = pre.getString("mobile", "");
        Log.d(TAG, "get " + mobile);
        return mobile;
    }

    public static void setUserMobile(Context context, String mobile) {
        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = pre.edit();
        editor.putString("mobile", mobile);
        editor.commit();
        Log.d(TAG, "set " + mobile);
    }

    public static boolean getIsLogin(Context context) {
        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        boolean isLogin = pre.getBoolean("isLogin", false);
        Log.d(TAG, "get " + isLogin);
        return isLogin;
    }

    public static void setIsLogin(Context context, boolean isLogin) {
        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = pre.edit();
        editor.putBoolean("isLogin", isLogin);
        editor.commit();
        Log.d(TAG, "set " + isLogin);
    }
}

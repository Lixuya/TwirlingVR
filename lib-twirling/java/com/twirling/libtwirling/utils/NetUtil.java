package com.twirling.libtwirling.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by xieqi on 2016/12/24.
 */

public class NetUtil {

    public static boolean isNetEnable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isNetEnable = connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
        return isNetEnable;
    }
}

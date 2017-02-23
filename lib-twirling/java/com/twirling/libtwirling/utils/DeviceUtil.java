package com.twirling.libtwirling.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import com.twirling.libtwirling.model.DeviceInfo;

/**
 * Created by MagicBean on 2016/03/10 00:0:03
 */
public class DeviceUtil {

    public static DeviceInfo getDeviceInfo(Context context) {
        String modelName = android.os.Build.MODEL;
        String modelVerson = android.os.Build.VERSION.RELEASE;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int appVersion = 0;
        try {
            appVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        DeviceInfo mDeviceInfo = new DeviceInfo(tm.getDeviceId(), modelVerson, appVersion, modelName);
        mDeviceInfo.setDeviceBrand(android.os.Build.BRAND);
        return mDeviceInfo;
    }
}

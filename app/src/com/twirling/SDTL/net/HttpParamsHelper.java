package com.twirling.SDTL.net;

import android.content.pm.PackageManager;

import com.twirling.SDTL.App;
import com.twirling.libtwirling.utils.DeviceInfo;
import com.twirling.libtwirling.utils.DeviceUtils;

import java.util.HashMap;

/**
 * Created by MagicBean on 2016/03/02 19:19:53
 */
public class HttpParamsHelper {
    public static DeviceInfo device;
    private static StringBuilder sb;

    public synchronized static HashMap<String, Object> createParams() {
        if (device == null) {
            device = DeviceUtils.getDeviceInfo(App.getInst());
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("device.id", device.deviceId);
        params.put("device.osVersion", device.deviceOsVersion);
        params.put("device.type", device.deviceType);
        params.put("device.appVersion", device.deviceAppVersion);
        params.put("device.model", device.deviceModel);
        // 加上渠道标记
//        params.put("apk.channel", getChannelName());
        return params;
    }

    public synchronized static String getUrlDeviceInfo() {
        if (device == null) {
            device = DeviceUtils.getDeviceInfo(App.getInst());
        }
        if (sb == null) {
            sb = new StringBuilder();
            sb.append("&device.id=").append(device.deviceId);
            sb.append("&device.osVersion=").append(device.deviceOsVersion);
            sb.append("&device.appVersion=").append(device.deviceAppVersion);
            sb.append("&device.model=").append(device.deviceModel);
            sb.append("&device.type=").append(device.deviceType);
            // 加上渠道标记
            sb.append("&apk.channel=").append(getChannelName());
        }
        return sb.toString();
    }

    //
    public static String getChannelName() {
        String channel = "";
        try {
            channel = App.getInst().getPackageManager().getApplicationInfo(//
                    App.getInst().getPackageName(), PackageManager.GET_META_DATA)//
                    .metaData.getString("CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channel;
    }
}

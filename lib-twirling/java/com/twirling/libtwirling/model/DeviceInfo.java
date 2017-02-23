package com.twirling.libtwirling.model;

/**
 * Created by MagicBean on 2016/03/10 00:0:58
 */
public class DeviceInfo {
    public String deviceId;
    public String deviceOsVersion;
    public String deviceType = "android";
    public int deviceAppVersion;
    public String deviceModel;
    public String deviceBrand = "";

    public DeviceInfo(String deviceId, String deviceOsVersion, int deviceAppVersion, String deviceModel) {
        this.deviceId = deviceId;
        this.deviceOsVersion = deviceOsVersion;
        this.deviceAppVersion = deviceAppVersion;
        this.deviceModel = deviceModel;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    @Override
    public String toString() {
        return "deviceId " + deviceId
                + " deviceOsVersion " + deviceOsVersion
                + " deviceAppVersion " + deviceAppVersion
                + " deviceModel " + deviceModel
                + " deviceBrand " + deviceBrand;
    }
}

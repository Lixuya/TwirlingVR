package com.twirling.libtwirling.utils.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by xieqi on 2016/12/5.
 */

public class SenserUtils {
    //
    public SenserUtils(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
//        sensor =sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//        sensor =sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // 注册陀螺仪传感器，
        // 并设定传感器向应用中输出的时间间隔类型是
        // SensorManager.SENSOR_DELAY_GAME(20000微秒)
        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                    Log.d("陀螺仪", "x:" + event.values[0] + " y:" + event.values[1] + " z:" + event.values[2]);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

}

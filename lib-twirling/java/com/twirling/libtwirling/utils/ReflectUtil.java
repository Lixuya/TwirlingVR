package com.twirling.libtwirling.utils;

import android.media.AudioTrack;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Target: 反射Android Api的hide方法
 */
public class ReflectUtil {
	public static void getHideFunction(AudioTrack audioplayer){
		Class<?> clazz = null;
		try {
			clazz = Class.forName("android.media.AudioTrack");
			Method method = clazz.getMethod("getLatency");
			method.setAccessible(true);
			Log.w("getLatency", method.invoke(audioplayer) + "");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}

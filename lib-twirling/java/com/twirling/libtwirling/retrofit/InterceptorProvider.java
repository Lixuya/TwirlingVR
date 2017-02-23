package com.twirling.libtwirling.retrofit;

import com.orhanobut.logger.Logger;
import com.twirling.libtwirling.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Target: provide interceptor
 */
public class InterceptorProvider {
	private Interceptor interceptor = null;

	public Interceptor init() {
		logInterceptor();
//		tokenInterceptor();
		return interceptor;
	}

	public void logInterceptor() {
		if (BuildConfig.DEBUG) {
			interceptor = new HttpLoggingInterceptor()
					.setLevel(HttpLoggingInterceptor.Level.BODY);
		}
	}

	public void tokenInterceptor() {
		interceptor = new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {
				HttpUrl url = chain.request().url();
				Logger.i("request url:" + url.toString());
				Request newRequest = chain.request().newBuilder().addHeader("token", "").build();
				Response response = chain.proceed(newRequest);
				return response;
			}
		};
	}
}

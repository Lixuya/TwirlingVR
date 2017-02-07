package com.twirling.libtwirling.retrofit;

import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Target: 提供Retrofit的接口
 */
public class RetrofitProvider {
	private static String ENDPOINT = "";

	private RetrofitProvider() {
	}

	public static Retrofit getInstance(String endpoint) {
		ENDPOINT = endpoint;
		return SingletonHolder.INSTANCE;
	}

	public static Retrofit getInstance() {
		if (TextUtils.isEmpty(ENDPOINT)){
			throw new RuntimeException("Retrofit not init api server!");
		}
		return SingletonHolder.INSTANCE;
	}


	/**
	 * Target: 提供唯一的Retrofit单例
	 */
	private static class SingletonHolder {
		private static final Retrofit INSTANCE = create();
		private static final int TIME_OUT = 20;

		private static Retrofit create() {
			OkHttpClient okHttpClient = new OkHttpClient.Builder()
					.readTimeout(TIME_OUT, TimeUnit.SECONDS)
					.connectTimeout(TIME_OUT, TimeUnit.SECONDS)
					.writeTimeout(TIME_OUT, TimeUnit.SECONDS)
					.hostnameVerifier(new HostnameVerifier() {
						@Override
						public boolean verify(String hostname, SSLSession session) {
							return true;
						}
					})
					.addInterceptor(new InterceptorProvider().init())
					.build();
			//
			return new Retrofit.Builder()
					.baseUrl(ENDPOINT)
					.client(okHttpClient)
					.addConverterFactory(GsonConverterFactory.create())
					.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
					.build();
		}
	}
}

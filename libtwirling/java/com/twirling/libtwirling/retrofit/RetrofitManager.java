package com.twirling.libtwirling.retrofit;

import com.orhanobut.logger.Logger;
import com.twirling.SDTL.Constants;
import com.twirling.SDTL.net.SimpleCookieJar;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 谢秋鹏 on 2016/01/12 16:16:08
 */
public class RetrofitManager {
	//
	private static Api mApiService = null;
	private static OkHttpClient okHttpClient = null;
	//
	private static Retrofit mRetrofit = null;

	@Inject
	public RetrofitManager() {

	}

	public static Api getService() {
		if (checkNull()) {
			init();
		}
		return mApiService;
	}

	private static void init() {
		int TIME_OUT = 20;
		okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {
				HttpUrl url = chain.request().url();
				Logger.i("request url:" + url.toString());
//                if (TextUtil.isValidate(App.getInst().getUser().token)) {
				Request newRequest = chain.request().newBuilder().addHeader("token", "").build();
				Response response = chain.proceed(newRequest);
				return response;
//                } else {
//                    return chain.proceed(chain.request());
//                }
			}
		}).writeTimeout(TIME_OUT, TimeUnit.SECONDS)
				.readTimeout(TIME_OUT, TimeUnit.SECONDS)
				.connectTimeout(TIME_OUT, TimeUnit.SECONDS)
				.cookieJar(new SimpleCookieJar())
				.hostnameVerifier(new HostnameVerifier() {
					@Override
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				})
//                .sslSocketFactory(SSLContextUtil.getUnsafeSLLContext().getSocketFactory())
				.build();
		String base_url = getServerUrl();
		mRetrofit = new Retrofit.Builder()
				.baseUrl(base_url)
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		mApiService = mRetrofit.create(Api.class);
	}

	public static OkHttpClient getOkHttpClient() {
		checkNull();
		return okHttpClient;
	}

	public static String getWebBaseUrl() {
		return getServerUrl();
	}

	public static String getServerUrl() {
		String url = Constants.PATH_SERVER_API;
		if (!url.endsWith("/")) {
			url += "/";
		}
		return url;
	}

	public static void resetRetrofit() {
		mRetrofit = null;
	}

	private static boolean checkNull() {
		return mRetrofit == null ? true : false;
	}
}
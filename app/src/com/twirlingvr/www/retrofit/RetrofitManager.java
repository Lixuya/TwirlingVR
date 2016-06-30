package com.twirlingvr.www.retrofit;

import com.orhanobut.logger.Logger;
import com.twirlingvr.www.net.SimpleCookieJar;
import com.twirlingvr.www.utils.Constants;

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
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by 谢秋鹏 on 2016/01/12 16:16:08
 */
public class RetrofitManager {
    //
    private static Api mApiService;
    private static OkHttpClient okHttpClient = null;
    //
    public static Retrofit mRetrofit;
    public static String base_url;
    public static int TIME_OUT = 20;

    @Inject
    public RetrofitManager() {

    }

    private static boolean checkNull() {
        return mRetrofit == null ? true : false;
    }

    private static void init() {
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
        base_url = getServerUrl();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(base_url)
//                .client(okHttpClient)
//                .addConverterFactory(new StringConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApiService = mRetrofit.create(Api.class);
    }

    public static Api getService() {
        if (checkNull()) {
            init();
        }
        return mApiService;
    }

    public static OkHttpClient getOkHttpClient() {
        checkNull();
        return okHttpClient;
    }

    public static String getWebBaseUrl() {
        return getServerUrl();
    }

    public static String getServerUrl() {
        String url = Constants.PATH_SERVER;
        if (!url.endsWith("/")) {
            url += "/";
        }
        return url;
    }

    public static void resetRetrofit() {
        mRetrofit = null;
    }
}

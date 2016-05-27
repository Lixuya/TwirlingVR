package com.twirlingvr.www.net;

import com.orhanobut.logger.Logger;
import com.twirlingvr.www.utils.TextUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
 * Created by MagicBean on 2016/01/12 16:16:08
 */
public class Api {
    public static Retrofit mRetrofit;
    private static ApiService mApiService;
    public static String base_url;
    private static OkHttpClient okHttpClient = null;
    public static int TIME_OUT = 20;

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
                Response respnse = chain.proceed(newRequest);
                return respnse;
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
        mApiService = mRetrofit.create(ApiService.class);
    }

    public static ApiService getRetrofit() {
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
        String url = "http://www.twirlingvr.com/";
//                SharePreHelper.getIns().getServerUrl();
        if (!TextUtil.isValidate(url)) {
            url = "http://www.twirlingvr.com/";
        } else {
            if (!url.endsWith("/")) {
                url += "/";
            }
        }
        return url;
    }

    public static String getImageUrl(String fileKey) {
        return base_url + "ctl/resource/download" + "?resId=" + fileKey + HttpParamsHelper.getUrlDeviceInfo();
    }

    public static void resetRetrofit() {
        mRetrofit = null;
    }
}

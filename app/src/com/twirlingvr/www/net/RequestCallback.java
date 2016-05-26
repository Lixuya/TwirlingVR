package com.twirlingvr.www.net;

import android.app.Activity;

import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by MagicBean on 2016/01/12 17:17:15
 */
public abstract class RequestCallback<T> implements Callback<T> {
    private WeakReference<Activity> mWeakReference;

    public RequestCallback() {
    }

    public RequestCallback(Activity activity) {

    }

    public RequestCallback(Activity activity, boolean cancel) {

    }

    @Override
    public void onFailure(Throwable t) {
        t.printStackTrace();
        if (t instanceof Exception) {
            if (t instanceof ConnectException) {
//                com.zhiri.bear.utils.T.showShort(App.getInst(), "连接服务器失败!");
            } else if (t instanceof SocketTimeoutException) {
//                com.zhiri.bear.utils.T.showShort(App.getInst(), "连接服务器超时!");
            }
        }
        Logger.i("http failure:" + t.toString());
        onFinish();
    }

    @Override
    public void onResponse(Response<T> response) {
        Logger.i("http code:" + response.code() + "--success:" + response.isSuccess() + "-- message:" + response.message());
        if (response.isSuccess()) {
            onSuccess(response.body());
            if (response.body() instanceof HttpResponse) {
                if (((HttpResponse) response.body()).code == 401 || ((HttpResponse) response.body()).code == 403 || ((HttpResponse) response.body()).code == 505) {//401 token过期 403禁止登录 505已在其他设备登陆
//                    App.getInst().toLogin();
                } else if (((HttpResponse) response.body()).code == 90001 || ((HttpResponse) response.body()).code == 90002) {//90001 必须更新  // 90002 有更新
//                    CheckUpdateUtil.checkNewVersion();
                }
            }
        } else {
            onError(response.errorBody());
        }
        onFinish();
    }

    public void onError(ResponseBody responseBody) {
    }

    public abstract void onSuccess(T response);

    public abstract void onFinish();
}

package com.twirling.SDTL.retrofit;


import com.twirling.SDTL.model.DataArray;
import com.twirling.SDTL.model.User;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by MagicBean on 2016/01/13 10:10:37
 */
public interface Api {
    // 获取视频列表
    @GET("AppVideo/makelist")
    Observable<DataArray> getVideoList(@QueryMap Map<String, Object> params);

    @GET("Login/login")
    Observable<User> login(@QueryMap Map<String, Object> params);
}
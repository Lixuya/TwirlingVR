package com.twirling.SDTL.retrofit;


import com.twirling.SDTL.model.AudioItem;
import com.twirling.SDTL.model.DataArray;
import com.twirling.SDTL.model.LiveItem;
import com.twirling.SDTL.model.VideoItem;
import com.twirling.SDTL.oauth.WXBack;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by MagicBean on 2016/01/13 10:10:37
 */
public interface Api {
    // 获取视频列表
    @GET("Audio/selectAll")
    Observable<DataArray<AudioItem>> getAudioList(@QueryMap Map<String, Object> params);

    // 获取视频列表
    @GET("AppVideo/makelist")
    Observable<DataArray<VideoItem>> getVideoList(@QueryMap Map<String, Object> params);

    // 获取视频列表
    @GET("AppHLS/makelist")
    Observable<DataArray<LiveItem>> getLiveList();

    @GET("Login/login")
    Observable<DataArray> login(@QueryMap Map<String, Object> params);

    @GET("Login/loginWX")
    Observable<DataArray> loginWX(@QueryMap Map<String, Object> params);

    @GET("https://api.weixin.qq.com/sns/oauth2/access_token")
    Observable<WXBack> WXConfirm(@QueryMap Map<String, Object> params);
}
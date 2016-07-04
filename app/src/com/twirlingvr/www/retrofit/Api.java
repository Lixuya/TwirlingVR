package com.twirlingvr.www.retrofit;


import com.twirlingvr.www.model.DataArray;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by MagicBean on 2016/01/13 10:10:37
 */
public interface Api {
    // 获取视频列表
    @GET("App_Run/app_video_deal.aspx")
    Observable<DataArray> getVideoList(@QueryMap Map<String, Object> params);

}

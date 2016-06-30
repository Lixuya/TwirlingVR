package com.twirlingvr.www.module;

import android.content.Context;

import com.twirlingvr.www.App;
import com.twirlingvr.www.retrofit.Api;
import com.twirlingvr.www.retrofit.RetrofitManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 谢秋鹏 on 2016/6/29.
 */
@Module
public class AppModule {
    private final App application;

    public AppModule(App application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application;
    }

//    @Provides
//    @Singleton
//    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
//        return jobExecutor;
//    }

    @Provides
    @Singleton
    Api providesApiService(RetrofitManager retrofitManager) {
        return retrofitManager.getService();
    }

//    @Provides
//    @Singleton
//    SpfManager provideSpfManager() {
//        return new SpfManager(application);
//    }

//    @Provides
//    @Singleton
//    DBManager provideDBManager() {
//        return new DBManager(application);
//    }
}
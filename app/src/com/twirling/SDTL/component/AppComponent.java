package com.twirling.SDTL.component;

import android.content.Context;

import com.twirling.SDTL.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by 谢秋鹏 on 2016/6/29.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
	Context context();  // 提供Applicaiton的Context

//    ThreadExecutor threadExecutor();   // 线程池

//	Api apiService();  // 所有Api请求的管理类

//    SpfManager spfManager();  // SharedPreference管理类

//    DBManager dbManager();  // 数据库管理类
}
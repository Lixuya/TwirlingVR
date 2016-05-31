package com.twirlingvr.www;

import android.app.Activity;
import android.app.Application;
import android.database.ContentObserver;
import android.os.Bundle;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * Created by MagicBean on 2016/02/23 11:11:38
 */
public class App extends Application {
    public static App INS;
    //    private static User user;
    private WeakReference<Activity> mCurrentActivity;
    public static Map<String, Long> services = new HashMap<String, Long>();
    public static ContentObserver observer = null;
    @Override
    public void onCreate() {
        super.onCreate();
        INS = this;
        Logger.init("bear").setLogLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);
//        SharePreHelper.getIns().initialize(this, null);
//        initializeSocialSdk();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                mCurrentActivity = new WeakReference<Activity>(activity);
                Logger.i("showActivity onActivityCreated:" + activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Logger.i("showActivity onActivityStarted:" + activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Logger.i("showActivity onActivityResumed:" + activity.getClass().getSimpleName());
                if (mCurrentActivity != null) {
                    mCurrentActivity.clear();
                    mCurrentActivity = null;
                }
                mCurrentActivity = new WeakReference<Activity>(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Logger.i("showActivity onActivityPaused:" + activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Logger.i("showActivity onActivityStopped:" + activity.getClass().getSimpleName());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Logger.i("showActivity onActivityDestroyed:" + activity.getClass().getSimpleName());
//                mCurrentActivity.clear();
            }
        });
    }

    private void initializeSocialSdk() {
//        //微信 appid appsecret
//        PlatformConfig.setWeixin("wx47214887960b7089", "c4e6874610b2e68f8265973db902a9e8");
//        // QQ和Qzone appid appkey
//        PlatformConfig.setQQZone("1105134189", "c7zkrvggWrj3a3eo");
//        //新浪微博 appkey appsecret
//        PlatformConfig.setSinaWeibo("1510346764", "32cc9fb610a37756ed25cca5f023b0ea");
    }

    public static App getInst() {
        return INS;
    }

//    public User getUser() {
//        if (user == null) {
//            user = UserManager.getIns().getUser();
//        }
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    public void toLogin() {
//        setUser(null);
//        UserManager.getIns().clearToken();
//        if (mCurrentActivity != null) {
//            Activity act = mCurrentActivity.get();
//            if (act != null) {
//                Intent intent = new Intent(this, LoginActivity.class);
////                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                intent.putExtra("fromHome", true);
//                act.startActivity(intent);
//            }
//        }
//    }
//
//    public static long downloadId;

    public Activity getCurrentShowActivity() {
        return mCurrentActivity.get();
    }

    public static String valueGetKey(long value) {
        Set set = services.entrySet();//新建一个不可重复的集合
        ArrayList<String> arr = new ArrayList<String>();//新建一个集合
        Iterator it = set.iterator();//遍历的类
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();//找到所有key-value对集合
            if (entry.getValue().equals(value)) {//通过判断是否有该value值
                String s = (String) entry.getKey();//取得key值
                arr.add(s);
            }
        }
        return arr.get(0);
    }
}

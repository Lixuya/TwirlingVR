package com.twirlingvr.www.data;

import android.util.Log;

import com.twirlingvr.www.App;
import com.twirlingvr.www.model.VideoItem;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by 谢秋鹏 on 2016/5/30.
 */
public class RealmHelper {

    private RealmHelper() {
    }

    private static RealmHelper helper;

    public static RealmHelper getIns() {
        if (helper == null) {
            helper = new RealmHelper();
            RealmConfiguration realmConfig = new RealmConfiguration.Builder(App.getInst().getApplicationContext()).build();
            Realm.setDefaultConfiguration(realmConfig);
        }
        return helper;
    }

    public void insertVideoItem(VideoItem item) {
        Realm.getDefaultInstance().beginTransaction();
        Realm.getDefaultInstance().copyToRealmOrUpdate(item);
        Realm.getDefaultInstance().commitTransaction();
    }

    public List<VideoItem> selectVideoList() {
        List<VideoItem> puppies = Realm.getDefaultInstance().where(VideoItem.class).findAll();
        Log.v("dataList", puppies.size() + "");
        return puppies;
    }
}

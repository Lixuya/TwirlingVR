package com.twirlingvr.www.data;

import com.twirlingvr.www.App;
import com.twirlingvr.www.model.VideoItem;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

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
        RealmResults<VideoItem> puppies = Realm.getDefaultInstance().where(VideoItem.class).findAll();
                //.sort("updateTime", Sort.DESCENDING);
        List<VideoItem> list = new ArrayList<VideoItem>();
        for (VideoItem item : puppies) {
            VideoItem obj = new VideoItem();
            obj.setTitle(item.getTitle());
            obj.setImageName(item.getImageName());
            obj.setVideoName(item.getVideoName());
            obj.setUpdateTime(item.getUpdateTime());
            list.add(obj);
        }
        return puppies;
    }

    public void deleteVideoItem(VideoItem item) {
        Realm.getDefaultInstance().beginTransaction();
        item.deleteFromRealm();
        Realm.getDefaultInstance().commitTransaction();
    }

    public void deleteVideoItem(String videoName) {
        Realm.getDefaultInstance().beginTransaction();
        VideoItem videoItem = Realm.getDefaultInstance().where(VideoItem.class).equalTo("videoName", videoName).findFirst();
        videoItem.deleteFromRealm();
        Realm.getDefaultInstance().commitTransaction();
    }

    public void insertPath(){

    }
}

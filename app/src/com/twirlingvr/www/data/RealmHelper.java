package com.twirlingvr.www.data;

import com.twirlingvr.www.App;
import com.twirlingvr.www.model.VideoItem;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by 谢秋鹏 on 2016/5/30.
 */
public class RealmHelper {
    private Realm realm = null;

    private RealmHelper() {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(App.getInst().getApplicationContext()).build();
        Realm.setDefaultConfiguration(realmConfig);
        realm = Realm.getDefaultInstance();
    }

    private static RealmHelper helper;

    public static RealmHelper getIns() {
        if (helper == null) {
            helper = new RealmHelper();
        }
        return helper;
    }

    public void insertVideoItem(VideoItem item) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(item);
        realm.commitTransaction();
    }

    public void updateDownloadId(final String videoName, final long downloadId) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                VideoItem videoItem = realm.where(VideoItem.class)
                        .equalTo("videoName", videoName)
                        .findFirst();
                videoItem.setDownloadId(downloadId);
            }
        });
    }

    public void updateDownloadId(final long downloadId) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                VideoItem videoItem = realm.where(VideoItem.class)
                        .equalTo("downloadId", downloadId)
                        .findFirst();
                videoItem.setDownloadId(0);
            }
        });
    }

    public List<VideoItem> selectVideoList() {
        RealmResults<VideoItem> puppies = realm.where(VideoItem.class)
                .notEqualTo("downloadId", (long) 0)
                .findAll()
                .sort("updateTime", Sort.ASCENDING);
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

    public VideoItem selectVideoItem(final String videoName) {
        VideoItem videoItem = realm.where(VideoItem.class)
                .equalTo("videoName", videoName)
                .notEqualTo("downloadId", 0)
                .findFirst();
        return videoItem;
    }

    public void deleteVideoItem(VideoItem item) {
        realm.beginTransaction();
        item.deleteFromRealm();
        realm.commitTransaction();
    }

    public String selectVideoName(long downloadId) {
        VideoItem videoItem = realm.where(VideoItem.class)
                .equalTo("downloadId", downloadId)
                .findFirst();
        return videoItem.getVideoName();
    }

    public void deleteVideoItem(String videoName) {
        realm.beginTransaction();
        VideoItem videoItem = Realm.getDefaultInstance().where(VideoItem.class).equalTo("videoName", videoName).findFirst();
        videoItem.deleteFromRealm();
        realm.commitTransaction();
    }

    public void insertPath() {

    }
}

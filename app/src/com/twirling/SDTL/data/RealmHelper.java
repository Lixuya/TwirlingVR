package com.twirling.SDTL.data;

import android.content.Context;

import com.twirling.SDTL.App;
import com.twirling.SDTL.model.VideoItem;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by 谢秋鹏 on 2016/5/30.
 */
public class RealmHelper extends com.twirling.libtwirling.database.RealmHelper {
	protected static RealmHelper instance = null;

	private RealmHelper(Context context) {
		super(context);
	}

	public static RealmHelper getInstance() {
		return getInstance(App.getInst().getApplicationContext());
	}

	public static RealmHelper getInstance(Context context) {
		if (instance == null) {
			instance = new RealmHelper(context);
		}
		return instance;
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
						.equalTo("AppAndroidOnline", videoName)
						.findFirst();
				if (videoItem == null) {
					return;
				}
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
				if (videoItem == null) {
					return;
				}
				videoItem.setDownloadId(1);
			}
		});
	}

	public List<VideoItem> selectVideoList() {
		RealmResults<VideoItem> puppies = realm.where(VideoItem.class)
				.notEqualTo("downloadId", (long) 0)
				.findAll()
				.sort("downloadTime", Sort.ASCENDING);
//        for (VideoItem item : puppies) {
//            Log.i("selectVideoList", item.toString());
//        }
		return puppies;
	}

	public VideoItem selectVideoItem(final String videoName) {
		VideoItem videoItem = realm.where(VideoItem.class)
				.equalTo("AppAndroidOnline", videoName)
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
		return videoItem.getAppAndroidOnline();
	}

	public VideoItem selectVideoItem(long downloadId) {
		VideoItem videoItem = realm.where(VideoItem.class)
				.equalTo("downloadId", downloadId)
				.findFirst();
		return videoItem;
	}

	public void deleteVideoItem(String videoName) {
		realm.beginTransaction();
		VideoItem videoItem = Realm.getDefaultInstance().where(VideoItem.class).equalTo("AppAndroidOnline", videoName).findFirst();
		videoItem.deleteFromRealm();
		realm.commitTransaction();
	}
}

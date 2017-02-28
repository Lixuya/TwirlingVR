package com.twirling.SDTL.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.twirling.SDTL.Constants;
import com.twirling.SDTL.R;
import com.twirling.SDTL.data.RealmHelper;
import com.twirling.SDTL.databinding.ActivityOnlineBinding;
import com.twirling.SDTL.model.OnlineModel;
import com.twirling.SDTL.model.VideoItem;
import com.twirling.player.activity.VRPlayerActivity;

import io.reactivex.functions.Consumer;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadEvent;
import zlc.season.rxdownload2.entity.DownloadFlag;

public class PlayLoadActivity extends AppCompatActivity {
	private VideoItem videoItem = null;
	private OnlineModel onlineModel = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		initData();
		//
		ActivityOnlineBinding anb = DataBindingUtil.setContentView(this, R.layout.activity_online);
		anb.setItem(onlineModel);
		anb.setPresenter(new Presenter());
		//
		checkDownload();
	}

	public class Presenter {
		public void onIvDownload(View view) {
			RxDownload.getInstance()
					.context(PlayLoadActivity.this)
					.maxDownloadNumber(5)
					.serviceDownload(onlineModel.getVideoUrl(),
							videoItem.getName() + ".mp4",
							Constants.PATH_MOVIES)
					.subscribe(new Consumer<Object>() {
						@Override
						public void accept(Object o) throws Exception {
							onlineModel.setProgress(1);
							checkDownload();
						}
					});
		}

		public void onIvPlay(View view) {
			// 跳转
			Intent intent = new Intent();
			intent.setClass(PlayLoadActivity.this, VRPlayerActivity.class);
			if (onlineModel.getDownloadStatus() == DownloadFlag.COMPLETED) {
				intent.putExtra("VideoItem", onlineModel.getVideoPath());
			} else {
				intent.putExtra("VideoItem", onlineModel.getVideoUrl());
			}
			startActivity(intent);
		}
	}

	private void initData() {
		videoItem = getIntent().getExtras().getParcelable("videoItem");
		videoItem = RealmHelper.getInstance().selectVideoItem(videoItem);
		onlineModel = new OnlineModel(PlayLoadActivity.this);
		onlineModel.setImageUrl(Constants.PATH_RESOURCE + videoItem.getFolder() + videoItem.getImage());
		onlineModel.setVideoUrl(Constants.PATH_RESOURCE + videoItem.getFolder() + videoItem.getAppAndroidOnline());
		onlineModel.setVideoName(videoItem.getName());
		onlineModel.setVideoPath(Constants.PATH_MOVIES + onlineModel.getVideoName() + ".mp4");
		onlineModel.setProgress(videoItem.getProgress());
	}

	private void checkDownload() {
		RxDownload.getInstance()
				.context(PlayLoadActivity.this)
				.receiveDownloadStatus(onlineModel.getVideoUrl())
				.subscribe(new Consumer<DownloadEvent>() {
					@Override
					public void accept(DownloadEvent event) throws Exception {
						long downloading = event.getDownloadStatus().getDownloadSize();
						long total = event.getDownloadStatus().getTotalSize();
						int progress = onlineModel.getProgress();
						if (total != 0 && progress != 100) {
							progress = (int) (downloading * 100f / total);
						}
						onlineModel.setDownloadStatus(event.getFlag());
						onlineModel.setProgress(progress);
						onlineModel.setPercent(event.getDownloadStatus().getPercent());
						videoItem.setProgress(progress);
//						RealmHelper.getInstance().insertVideoItem(videoItem);
					}
				});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
							| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_FULLSCREEN
							| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		}
	}
}

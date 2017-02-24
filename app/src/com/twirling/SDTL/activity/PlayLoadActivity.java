package com.twirling.SDTL.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.twirling.SDTL.Constants;
import com.twirling.SDTL.R;
import com.twirling.SDTL.databinding.ActivityOnlineBinding;
import com.twirling.SDTL.model.OnlineModel;
import com.twirling.SDTL.model.VideoItem;
import com.twirling.player.activity.VRPlayerActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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
//        StatusBarUtil.setTransparent(PlayLoadActivity.this);
		//
		Disposable disposable = RxDownload.getInstance()
				.receiveDownloadStatus(onlineModel.getVideoUrl())
				.subscribe(new Consumer<DownloadEvent>() {
					@Override
					public void accept(DownloadEvent event) throws Exception {
						if (event.getFlag() == DownloadFlag.FAILED) {
							Throwable throwable = event.getError();
							Log.w("Error", throwable);
						} else {
							onlineModel.setDownloadStatus(event.getFlag());
							int progress = (int) (event.getDownloadStatus().getDownloadSize() / event.getDownloadStatus().getTotalSize());
							onlineModel.setProgress(progress);
						}
					}
				});
	}

	public class Presenter {
		public void onIvDownload(View view) {
			RxView.clicks(view)
					.debounce(300, TimeUnit.MILLISECONDS)
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(new Consumer<Object>() {
						@Override
						public void accept(Object o) {
							RxDownload.getInstance()
									.context(PlayLoadActivity.this)
									.maxDownloadNumber(3)
									.serviceDownload(onlineModel.getVideoUrl(), videoItem.getAppAndroidOffline(), Constants.PATH_DOWNLOAD)
									.subscribe(new Consumer<Object>() {
										@Override
										public void accept(Object o) throws Exception {
											Toast.makeText(PlayLoadActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
											Drawable iconDownload = new IconicsDrawable(PlayLoadActivity.this)
													.icon(FontAwesome.Icon.faw_cloud_download)
													.color(Color.parseColor("#00CF00"))
													.sizeDp(53);
											onlineModel.setIconDownload(iconDownload);
										}
									});
						}
					});
		}

		public void onIvPlay(View view) {
			RxView.clicks(view)
					.subscribe(new Consumer<Object>() {
						@Override
						public void accept(Object o) {
							Intent intent = new Intent();
							intent.putExtra("VideoItem", Constants.PATH_RESOURCE + videoItem.getFolder() + videoItem.getAppAndroidOnline());
							intent.setClass(PlayLoadActivity.this, VRPlayerActivity.class);
							startActivity(intent);
						}
					});
		}
	}

	private void initData() {
		videoItem = getIntent().getExtras().getParcelable("videoItem");
		onlineModel = new OnlineModel(PlayLoadActivity.this);
		onlineModel.setVideoName(videoItem.getAppAndroidOnline());
		onlineModel.setImageUrl(Constants.PATH_RESOURCE + videoItem.getFolder() + videoItem.getImage());
		onlineModel.setVideoUrl(Constants.PATH_RESOURCE + videoItem.getFolder() + videoItem.getAppAndroidOffline());
		//
//		VideoItem itemInDB = RealmHelper.getInstance().selectVideoItem(videoName);
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

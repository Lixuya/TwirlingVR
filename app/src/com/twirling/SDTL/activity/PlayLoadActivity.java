package com.twirling.SDTL.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding2.view.RxView;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.twirling.SDTL.App;
import com.twirling.SDTL.Constants;
import com.twirling.SDTL.R;
import com.twirling.SDTL.data.RealmHelper;
import com.twirling.SDTL.download.DownloadChangeObserver;
import com.twirling.SDTL.download.DownloadService;
import com.twirling.SDTL.model.VideoItem;
import com.twirling.player.activity.VRPlayerActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


public class PlayLoadActivity extends AppCompatActivity {
	@BindView(R.id.iv_download)
	ImageView iv_download;

	@BindView(R.id.iv_play)
	ImageView iv_play;

	@BindView(R.id.iv_video_image)
	ImageView iv_video_image;

	@BindView(R.id.pb_download)
	ProgressBar mPbLoading;

	private String imageUrl;
	private Drawable icon_click;

	private CountDownTimer selfTimer = new CountDownTimer(20 * 1000, 1000) {
		public void onTick(long millSec) {
			mPbLoading.setProgress((int) ((20 * 1000 - millSec) / 1000));
		}

		@Override
		public void onFinish() {

		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playpanel);
		ButterKnife.bind(this);
//        StatusBarUtil.setTransparent(PlayLoadActivity.this);
		//
		VideoItem videoItem = (VideoItem) getIntent().getExtras().getParcelable("videoItem");
		Log.e("PlayLoadActivity", videoItem.toString());
		String videoName = videoItem.getAppAndroidOnline();
		imageUrl = Constants.PATH_RESOURCE + videoItem.getFolder() + Constants.PAPH_IMAGE + videoItem.getImage();
		//
		initView(videoItem);
		VideoItem itemInDB = RealmHelper.getIns().selectVideoItem(videoName);
		if (itemInDB == null) {
			iv_download.setBackgroundColor(Color.TRANSPARENT);
			iv_download.setEnabled(true);
			return;
		}
		long downLoadId = itemInDB.getDownloadId();
		if (downLoadId == 0) {
			return;
		} else if (downLoadId == 1) {
			mPbLoading.setProgress(100);
			iv_download.setImageDrawable(icon_click);
			iv_download.setEnabled(false);
			return;
		}
		iv_download.setEnabled(false);
		iv_download.setImageDrawable(icon_click);
		DownloadChangeObserver pco = (DownloadChangeObserver) App.observers.get(downLoadId);
		if (pco == null) {
			return;
		}
		pco.setProgressListener(new DownloadChangeObserver.ProgressListener() {
			@Override
			public void invoke(int progress) {
				mPbLoading.setProgress(progress);
			}
		});
	}

	public void initView(final VideoItem videoItem) {
		Glide.with(getBaseContext()).load(imageUrl).into(iv_video_image);
		icon_click = new IconicsDrawable(getBaseContext())
				.icon(FontAwesome.Icon.faw_cloud_download)
				.color(Color.parseColor("#00CF00"))
				.sizeDp(53);
		Drawable icon = new IconicsDrawable(getBaseContext())
				.icon(FontAwesome.Icon.faw_cloud_download)
				.color(Color.parseColor("#000000"))
				.sizeDp(53);
		iv_download.setImageDrawable(icon);
		RxView.clicks(iv_download)
				.debounce(300, TimeUnit.MILLISECONDS)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<Object>() {
					@Override
					public void accept(Object o) {
						iv_download.setImageDrawable(icon_click);
						//
						Intent intent = new Intent(App.getInst().getApplicationContext(), DownloadService.class);
						intent.putExtra("videoItem", videoItem);
						startService(intent);
						//
						if (iv_download.isEnabled() && !App.observers.isEmpty()) {
							selfTimer.start();
						}
						iv_download.setEnabled(false);
					}
				});
		Drawable icon2 = new IconicsDrawable(getBaseContext())
				.icon(FontAwesome.Icon.faw_play_circle_o)
				.color(Color.parseColor("#000000"))
				.sizeDp(50);
		iv_play.setImageDrawable(icon2);
		RxView.clicks(iv_play)
				.subscribe(new Consumer<Object>() {
					@Override
					public void accept(Object o) {
						Intent intent = new Intent();
						intent.putExtra("VideoItem", Constants.PATH_RESOURCE + videoItem.getFolder() + Constants.PAPH_VIDEO + videoItem.getAppAndroidOnline());
						intent.setClass(PlayLoadActivity.this, VRPlayerActivity.class);
						startActivity(intent);
					}
				});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		selfTimer.cancel();
		selfTimer = null;
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

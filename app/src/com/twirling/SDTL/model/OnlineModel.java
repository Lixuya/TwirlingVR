package com.twirling.SDTL.model;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.twirling.SDTL.BR;

import zlc.season.rxdownload2.entity.DownloadFlag;

/**
 * Target: PlayLoad界面元素
 */
public class OnlineModel extends BaseObservable {
	//
	private String videoUrl;
	private String imageUrl;
	private Drawable iconDownload,
			iconDownloadGreen;
	private Drawable iconPlay;
	private String videoName;
	private String videoPath;
	private int progress = 0;
	private String percent = "";
	private int downloadStatus = DownloadFlag.NORMAL;

	public OnlineModel(Context context) {
		iconDownload = new IconicsDrawable(context)
				.icon(FontAwesome.Icon.faw_cloud_download)
				.color(Color.parseColor("#FFFFFF"))
				.sizeDp(53);
		iconPlay = new IconicsDrawable(context)
				.icon(FontAwesome.Icon.faw_play_circle)
				.color(Color.parseColor("#FFFFFF"))
				.sizeDp(53);
		iconDownloadGreen = new IconicsDrawable(context)
				.icon(FontAwesome.Icon.faw_cloud_download)
				.color(Color.parseColor("#00CF00"))
				.sizeDp(53);
	}

	@Bindable
	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
		notifyPropertyChanged(BR.videoName);
	}

	@Bindable
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
		notifyPropertyChanged(BR.imageUrl);
	}

	@Bindable
	public Drawable getIconDownload() {
		return iconDownload;
	}

	public void setIconDownload(Drawable iconDownload) {
		this.iconDownload = iconDownload;
		notifyPropertyChanged(BR.iconDownload);
	}

	@Bindable
	public Drawable getIconPlay() {
		return iconPlay;
	}

	public void setIconPlay(Drawable iconPlay) {
		this.iconPlay = iconPlay;
		notifyPropertyChanged(BR.iconPlay);
	}

	@Bindable
	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
		notifyPropertyChanged(BR.videoPath);
	}

	@Bindable
	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		if (progress != 0) {
			setIconDownload(iconDownloadGreen);
			if (progress >= 1) {
				this.progress = progress;
			} else {
				this.progress = 1;
			}
		} else {
			setIconDownload(iconDownload);
		}
		notifyPropertyChanged(BR.progress);
	}

	@Bindable
	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
		notifyPropertyChanged(BR.videoUrl);
	}

	@Bindable
	public int getDownloadStatus() {
		return downloadStatus;
	}

	public void setDownloadStatus(int downloadStatus) {
		this.downloadStatus = downloadStatus;
		notifyPropertyChanged(BR.downloadStatus);
	}

	@Bindable
	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
		notifyPropertyChanged(BR.percent);
	}
}

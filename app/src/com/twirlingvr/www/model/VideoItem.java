package com.twirlingvr.www.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 谢秋鹏 on 2016/5/30.
 */
public class VideoItem extends RealmObject implements Parcelable {
    @PrimaryKey
    private String name = "";
    private String image = "",
            video = "",
            videoUri = "",
            describe = "",
            androidoffline = "";
    private boolean isaudio;
    private long downloadTime = 0,
            downloadId = 0;

    protected VideoItem(Parcel in) {
        name = in.readString();
        image = in.readString();
        video = in.readString();
        videoUri = in.readString();
        downloadTime = in.readLong();
        downloadId = in.readLong();
    }

    public long getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(long downloadTime) {
        this.downloadTime = downloadTime;
    }

    public long getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(long downloadId) {
        this.downloadId = downloadId;
    }

    public VideoItem() {
    }

    public VideoItem(List<String> item) {
        name = item.get(2);
        video = item.get(4);
        image = item.get(6);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(video);
        dest.writeString(videoUri);
        dest.writeLong(downloadTime);
        dest.writeLong(downloadId);
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    public static final Creator<VideoItem> CREATOR = new Creator<VideoItem>() {
        @Override
        public VideoItem createFromParcel(Parcel in) {
            VideoItem videoItem = new VideoItem();
            videoItem.name = in.readString();
            videoItem.image = in.readString();
            videoItem.video = in.readString();
            videoItem.videoUri = in.readString();
            videoItem.downloadTime = in.readLong();
            videoItem.downloadId = in.readLong();
            return videoItem;
        }

        @Override
        public VideoItem[] newArray(int size) {
            return new VideoItem[size];
        }
    };

    @Override
    public String toString() {
        String str = getDownloadId() + " " + getName() + " " + getImage() + " " + getVideo() + " " + getDownloadTime();
        return str;
    }
}

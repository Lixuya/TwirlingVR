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
    private String title;
    private String imageName,
            videoName,
            fileUri;
    private long updateTime = 0;

    protected VideoItem(Parcel in) {
        title = in.readString();
        imageName = in.readString();
        videoName = in.readString();
        fileUri = in.readString();
        updateTime = in.readLong();
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public VideoItem() {
    }

    public VideoItem(List<String> item) {
        title = item.get(2);
        videoName = item.get(4);
        imageName = item.get(6);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(imageName);
        dest.writeString(videoName);
        dest.writeString(fileUri);
        dest.writeLong(updateTime);
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public static final Creator<VideoItem> CREATOR = new Creator<VideoItem>() {
        @Override
        public VideoItem createFromParcel(Parcel in) {
            VideoItem videoItem = new VideoItem();
            videoItem.title=in.readString();
            videoItem.imageName=in.readString();
            videoItem.videoName=in.readString();
            videoItem.fileUri=in.readString();
            videoItem.updateTime=in.readLong();
            return videoItem;
        }

        @Override
        public VideoItem[] newArray(int size) {
            return new VideoItem[size];
        }
    };
}

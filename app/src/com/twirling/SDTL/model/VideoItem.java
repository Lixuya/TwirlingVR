package com.twirling.SDTL.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 谢秋鹏 on 2016/5/30.
 */
public class VideoItem extends RealmObject implements Parcelable {
    @PrimaryKey
    private String name = "";
    private String image = "";
    private String video = "";
    private String videoUri = "";

    private String movieId = "";
    private String describe = "";
    private String sort = "";
    private int isatmos = 0;
    private long downloadTime = 0;

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getIsatmos() {
        return isatmos;
    }

    public void setIsatmos(int isatmos) {
        this.isatmos = isatmos;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
    private long downloadId = 0;

    protected VideoItem(Parcel in) {
        name = in.readString();
        image = in.readString();
        video = in.readString();
        videoUri = in.readString();
        describe = in.readString();
        movieId = in.readString();
        sort = in.readString();
        //
        downloadTime = in.readLong();
        downloadId = in.readLong();
        //
        isatmos = in.readInt();
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
        dest.writeString(movieId);
        dest.writeString(sort);
        dest.writeString(describe);
        dest.writeInt(isatmos);
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
            videoItem.movieId = in.readString();
            videoItem.sort = in.readString();
            videoItem.describe = in.readString();
            videoItem.isatmos = in.readInt();
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
        String str = getDownloadId() + " :downloadId "//
                + getName() + " :name " //
                + getImage() + " :image "//
                + getVideo() + " :video "//
                + getDownloadTime() + " :time " //
                + getIsatmos() + " :isatmos "//
                + getMovieId() + " :movieid "//
                + getSort() + " :sort "//
                + getDescribe() + " :describe ";
        return str;
    }
}
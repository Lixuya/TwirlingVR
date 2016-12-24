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
    private int ID = 0;
    private int UserID = 0;
    private String Name = "";
    private String Folder = "";
    private String Image = "";
    private String Sort = "";
    private int VrAudio = -1;
    private String AppAndroidOffline = "";
    private String AppIOSOffline = "";
    private String AppAndroidOnline = "";
    private String Describe = "";
    private long downloadId = 0;
    private long downloadTime = 0;

    public String getDescribe() {
        return Describe;
    }

    public void setDescribe(String describe) {
        this.Describe = describe;
    }

    public int getVrAudio() {
        return VrAudio;
    }

    public void setVrAudio(int vrAudio) {
        this.VrAudio = vrAudio;
    }

    public String getSort() {
        return Sort;
    }

    public void setSort(String sort) {
        this.Sort = sort;
    }

    protected VideoItem(Parcel in) {
        ID = in.readInt();
        UserID = in.readInt();
        Name = in.readString();
        Folder = in.readString();
        Image = in.readString();
        Sort = in.readString();
        VrAudio = in.readInt();
        AppAndroidOffline = in.readString();
        AppAndroidOnline = in.readString();
        Describe = in.readString();
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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }

    public String getAppAndroidOnline() {
        return AppAndroidOnline;
    }

    public void setAppAndroidOnline(String appAndroidOnline) {
        this.AppAndroidOnline = appAndroidOnline;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeInt(UserID);
        dest.writeString(Name);
        dest.writeString(Folder);
        dest.writeString(Image);
        dest.writeString(Sort);
        dest.writeInt(VrAudio);
        dest.writeString(AppAndroidOffline);
        dest.writeString(AppAndroidOnline);
        dest.writeString(Describe);
        dest.writeLong(downloadTime);
        dest.writeLong(downloadId);
        dest.writeString(AppIOSOffline);
    }

    public static final Creator<VideoItem> CREATOR = new Creator<VideoItem>() {
        @Override
        public VideoItem createFromParcel(Parcel in) {
            VideoItem videoItem = new VideoItem();
            videoItem.ID = in.readInt();
            videoItem.UserID = in.readInt();
            videoItem.Name = in.readString();
            videoItem.Folder = in.readString();
            videoItem.Image = in.readString();
            videoItem.Sort = in.readString();
            videoItem.VrAudio = in.readInt();
            videoItem.AppAndroidOffline = in.readString();
            videoItem.AppAndroidOnline = in.readString();
            videoItem.Describe = in.readString();
            videoItem.downloadTime = in.readLong();
            videoItem.downloadId = in.readLong();
            videoItem.AppIOSOffline = in.readString();
            return videoItem;
        }

        @Override
        public VideoItem[] newArray(int size) {
            return new VideoItem[size];
        }
    };

    @Override
    public String toString() {
        String str = "downloadId: " + getDownloadId() +//
                " Name: " + getName() +  //
                " Image: " + getImage() + //
                " AppAndroidOnline: " + getAppAndroidOnline() + //
                " time: " + getDownloadTime() +  //
                " VrAudio: " + getVrAudio() + //
                " AppAndroidOffline: " + getAppAndroidOffline() +//
                " Sort: " + getSort() + //
                " Describe: " + getDescribe() +//
                " Folder: " + getFolder() +
                " AppIOSOffline " + getAppIOSOffline();
        return str;
    }

    public String getAppAndroidOffline() {
        return AppAndroidOffline;
    }

    public void setAppAndroidOffline(String appAndroidOffline) {
        this.AppAndroidOffline = appAndroidOffline;
    }

    public String getFolder() {
        return Folder;
    }

    public void setFolder(String folder) {
        this.Folder = folder;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getAppIOSOffline() {
        return AppIOSOffline;
    }

    public void setAppIOSOffline(String appIOSOffline) {
        AppIOSOffline = appIOSOffline;
    }
}

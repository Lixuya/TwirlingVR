package com.twirling.SDTL.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xieqi on 2016/11/3.
 */

public class AudioItem implements Parcelable {
    private String id;

    private String title;

    private String cover;

    private String audio;

    private String tag;

    protected AudioItem(Parcel in) {
        id = in.readString();
        title = in.readString();
        cover = in.readString();
        audio = in.readString();
        tag = in.readString();
    }

    public static final Creator<AudioItem> CREATOR = new Creator<AudioItem>() {
        @Override
        public AudioItem createFromParcel(Parcel in) {
            return new AudioItem(in);
        }

        @Override
        public AudioItem[] newArray(int size) {
            return new AudioItem[size];
        }
    };

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCover() {
        return this.cover;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getAudio() {
        return this.audio;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return this.tag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(cover);
        dest.writeString(audio);
        dest.writeString(tag);
    }
}

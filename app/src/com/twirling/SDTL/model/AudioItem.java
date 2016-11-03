package com.twirling.SDTL.model;

/**
 * Created by xieqi on 2016/11/3.
 */

public class AudioItem {
    private String id;

    private String title;

    private String cover;

    private String audio;

    private String tag;

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
}

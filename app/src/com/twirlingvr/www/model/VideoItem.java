package com.twirlingvr.www.model;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 谢秋鹏 on 2016/5/30.
 */
public class VideoItem extends RealmObject {
    @PrimaryKey
    private String title;
    private String imageName,
            videoName;

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
}

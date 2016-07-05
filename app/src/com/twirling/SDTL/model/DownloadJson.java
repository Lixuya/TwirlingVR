package com.twirling.SDTL.model;

/**
 * Created by 谢秋鹏 on 2016/6/24.
 */
public class DownloadJson {
    private String id = "http://twirlingvr.com";
    private String name = "全景音视频测试";
    private String description = "全景音视频测试";
    private Elements elements = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Elements getElements() {
        return elements;
    }

    public void setElements(Elements elements) {
        this.elements = elements;
    }
}

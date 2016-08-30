package com.twirling.SDTL.model;

/**
 * Created by xieqi on 2016/8/29.
 */
public class LiveItem {
    private String id;
    private String name;
    private String image;
    private String flv;
    private String hls;
    private String flvHd;
    private String hlsHd;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setFlv(String flv) {
        this.flv = flv;
    }

    public String getFlv() {
        return flv;
    }

    public void setHls(String hls) {
        this.hls = hls;
    }

    public String getHls() {
        return hls;
    }

    public void setFlvHd(String flvHd) {
        this.flvHd = flvHd;
    }

    public String getFlvHd() {
        return flvHd;
    }

    public void setHlsHd(String hlsHd) {
        this.hlsHd = hlsHd;
    }

    public String getHlsHd() {
        return hlsHd;
    }
}

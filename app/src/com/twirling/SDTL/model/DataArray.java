package com.twirling.SDTL.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 谢秋鹏 on 2016/5/26.
 */
public class DataArray {
    private int status = 0;
    private String msg = "";
    private List<VideoItem> data = new ArrayList<VideoItem>();

    public List<VideoItem> getData() {
        return data;
    }

    public void setData(List<VideoItem> data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return msg.toString() + "  " + status;
    }
}

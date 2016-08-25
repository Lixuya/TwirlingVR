package com.twirling.SDTL.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 谢秋鹏 on 2016/5/26.
 */
public class DataArray {
    private String status = "";
    private String msg = "";
    private List<VideoItem> data = new ArrayList<VideoItem>();

    public List<VideoItem> getData() {
        return data;
    }

    public void setData(List<VideoItem> data) {
        this.data = data;
    }
}

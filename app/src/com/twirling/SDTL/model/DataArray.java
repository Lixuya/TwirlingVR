package com.twirling.SDTL.model;

import java.util.List;

/**
 * Created by 谢秋鹏 on 2016/5/26.
 */
public class DataArray<T> {
    private int status = 0;
    private String msg = "";
    private List<T> data = null;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
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
        return msg.toString() + "  " + status + " " + data.toString();
    }
}

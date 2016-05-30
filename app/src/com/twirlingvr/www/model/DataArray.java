package com.twirlingvr.www.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 谢秋鹏 on 2016/5/26.
 */
public class DataArray {
    private String result = "";
    private List<List<String>> content = new ArrayList<List<String>>();

    public List<List<String>> getContent() {
        return content;
    }

    public void setContent(List<List<String>> content) {
        this.content = content;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

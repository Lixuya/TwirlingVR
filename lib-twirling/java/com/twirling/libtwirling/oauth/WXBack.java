package com.twirling.libtwirling.oauth;

/**
 * Auto-generated: 2016-10-12 16:11:16
 *
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
 */
public class WXBack {

    private String accessToken;
    private int expiresIn;
    private String refreshToken;
    private String openid;
    private String scope;
    private String unionid;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getUnionid() {
        return unionid;
    }

    @Override
    public String toString() {
        String str = "accessToken: " + getAccessToken() +//
                " expiresIn: " + getExpiresIn() +  //
                " openid: " + getOpenid() + //
                " scope: " + getScope() + //
                " unionid: " + getUnionid();
        return str;
    }
}
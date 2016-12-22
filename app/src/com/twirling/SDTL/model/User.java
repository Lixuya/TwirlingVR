package com.twirling.SDTL.model;

/**
 * Created by 谢秋鹏 on 2016/8/5.
 */
public class User {
    private String Id;
    private String OpenId;
    private String NickName;
    private String Mobile;
    private String IsVip;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getOpenId() {
        return OpenId;
    }

    public void setOpenId(String openId) {
        OpenId = openId;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getIsVip() {
        return IsVip;
    }

    public void setIsVip(String isVip) {
        IsVip = isVip;
    }
}

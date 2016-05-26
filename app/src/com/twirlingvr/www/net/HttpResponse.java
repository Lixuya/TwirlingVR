package com.twirlingvr.www.net;

import com.twirlingvr.www.utils.TextUtil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by 谢秋鹏 on 2015/10/16 10:10:32
 */
public class HttpResponse<T> {
    public int code;
    public int flag;
    public String[] msg;
    public ArrayList<T> data = new ArrayList<>();
    public boolean state;
    public static final int SUCCESS = 0;
    public static final int NO_LOGIN = 401;//未登录
    public static final int DISABLE_LOGIN = 403;//禁用
    public static final int LOGIN_IN_OTHER_DEVICE = 505;//在其他设备上登陆

    public int getCode() {
        return code;
    }

    public ArrayList<T> getData() {
        return data;
    }

    public T getDataFrist() {
        if (TextUtil.isValidate(data)) {
            return data.get(0);
        }
        return null;
    }

    public boolean isSuccess() {
        if (code != SUCCESS) {
        }
        return code == 0 ? true : false;
    }

    public String getMessage() {
        return TextUtil.isValidate(msg) ? msg[0] : "";
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "data=" + data +
                ", msg=" + Arrays.toString(msg) +
                ", flag=" + flag +
                ", code=" + code +
                '}';
    }
}

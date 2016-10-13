package com.twirling.SDTL.oauth;

import android.app.Activity;

import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.twirling.SDTL.Constants;

/**
 * 解决问题：OAuth登陆类：微信
 * <p/>
 * Created by 谢秋鹏 on 2015/8/14.
 */
public class LoginWX {
    private Activity activity = null;
    private static LoginWX instance = null;
    private IWXAPI wxAPI = null;

    private LoginWX(Activity activity) {
        this.activity = activity;
    }

    public static LoginWX getInstance(Activity activity) {
        if (instance == null) {
            instance = new LoginWX(activity);
        }
        return instance;
    }

    public IWXAPI getWxAPI() {
        return wxAPI;
    }

    // 注册到微信
    public void initData() {
        Logger.i(activity.getLocalClassName() + " initData ");
//        wxAPI = WXAPIFactory.createWXAPI(activity,
//                Constants.WX_APP_ID,
//                true);
        wxAPI = WXAPIFactory.createWXAPI(activity, null);
        wxAPI.registerApp(Constants.WX_APP_ID);
        //
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = System.currentTimeMillis() + "";
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        //这个地方现在必须写死
        req.state = "twirling_login";
        wxAPI.sendReq(req);
    }
}
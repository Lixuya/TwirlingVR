package com.twirling.SDTL.wxapi;

import android.app.Activity;
import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.twirling.SDTL.Constants;
import com.twirling.SDTL.oauth.LoginWX;

/**
 * Created by 谢秋鹏 on 2016/8/5.
 */
public class WXEntryActivity extends Activity {
    //
    private IWXAPI wxAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wxAPI = WXAPIFactory.createWXAPI(WXEntryActivity.this,
                Constants.WX_APP_ID,
                true);
        wxAPI.handleIntent(getIntent(), handler);
    }

    IWXAPIEventHandler handler = new IWXAPIEventHandler() {
        @Override
        public void onReq(BaseReq baseReq) {
            Logger.w("onReq " + baseReq);
        }

        @Override
        public void onResp(BaseResp baseResp) {
            Logger.w("onResp " + baseResp);
            String result = " ";
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    LoginWX.getInstance(WXEntryActivity.this).loginWX(((SendAuth.Resp) baseResp).code);
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    result = "用户取消";
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    result = "用户拒绝";
                    break;
            }
        }
    };
}

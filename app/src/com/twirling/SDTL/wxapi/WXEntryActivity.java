package com.twirling.SDTL.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.twirling.SDTL.Constants;
import com.twirling.SDTL.model.DataArray;
import com.twirling.SDTL.oauth.WXBack;
import com.twirling.SDTL.retrofit.RetrofitManager;
import com.twirling.SDTL.utils.EncodeUtil;
import com.twirling.SDTL.utils.SPUtil;

import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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

    public void loginWX(String wxCode) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("appid", EncodeUtil.urlEnodeUTF8(Constants.WX_APP_ID));
        params.put("secret", EncodeUtil.urlEnodeUTF8(Constants.WX_APP_SECRET));
        params.put("code", EncodeUtil.urlEnodeUTF8(wxCode));
        params.put("grant_type", Constants.WX_APP_GRANT_TYPE);
        RetrofitManager.getService().WXConfirm(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<WXBack>() {
                    @Override
                    public void call(WXBack wxBack) {
                        Logger.d(wxBack.toString());
                        HashMap<String, Object> params = new HashMap<>();
                        params.put("mobile", "");
                        params.put("openid", wxBack.getOpenid());
                        RetrofitManager.getService().loginWX(params)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Action1<DataArray>() {
                                    @Override
                                    public void call(DataArray dataArray) {
                                        Logger.d(dataArray.toString());
                                        if (dataArray.getStatus() == 200) {
                                            Toast.makeText(WXEntryActivity.this, dataArray.getMsg(), Toast.LENGTH_SHORT).show();
                                            Constants.USER_MOBILE = "";
                                            Constants.USER_IMAGE = FontAwesome.Icon.faw_wechat;
                                            SPUtil.setIsLogin(true);
                                            finish();
                                        } else if (dataArray.getStatus() == 400) {
                                            Toast.makeText(WXEntryActivity.this, dataArray.getMsg(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        Logger.d(throwable.toString());
                                    }
                                });
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Logger.d(throwable.toString());
                    }
                });

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
                    loginWX(((SendAuth.Resp) baseResp).code);
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

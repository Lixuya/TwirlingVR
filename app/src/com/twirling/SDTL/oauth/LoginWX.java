package com.twirling.SDTL.oauth;

import android.app.Activity;
import android.widget.Toast;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.twirling.SDTL.App;
import com.twirling.SDTL.Constants;
import com.twirling.SDTL.model.DataArray;
import com.twirling.SDTL.model.User;
import com.twirling.SDTL.retrofit.RetrofitManager;
import com.twirling.libtwirling.oauth.WXBack;
import com.twirling.libtwirling.utils.EncodeUtil;
import com.twirling.libtwirling.utils.SPUtil;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * 解决问题：OAuth登陆类：微信
 * <p/>
 * Created by 谢秋鹏 on 2015/8/14.
 */
public class LoginWX {
    private Activity activity = null;
    private static LoginWX instance = null;

    private LoginWX(Activity activity) {
        this.activity = activity;
    }

    public static LoginWX getInstance(Activity activity) {
        if (instance == null) {
            instance = new LoginWX(activity);
        }
        return instance;
    }

    public void initData() {
        Logger.i(activity.getLocalClassName() + " initData ");
        IWXAPI wxAPI = WXAPIFactory.createWXAPI(activity,
                Constants.WX_APP_ID,
                true);
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


    public void loginWX(String wxCode) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("appid", EncodeUtil.urlEnodeUTF8(Constants.WX_APP_ID));
        params.put("secret", EncodeUtil.urlEnodeUTF8(Constants.WX_APP_SECRET));
        params.put("code", EncodeUtil.urlEnodeUTF8(wxCode));
        params.put("grant_type", Constants.WX_APP_GRANT_TYPE);
        RetrofitManager.getService().WXConfirm(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<WXBack>() {
                    @Override
                    public void accept(WXBack wxBack) {
                        Logger.d(wxBack.toString());
                        HashMap<String, Object> params = new HashMap<>();
                        params.put("openid", wxBack.getOpenid().toString());
                        RetrofitManager.getService().loginWX(params)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Consumer<DataArray<User>>() {
                                    @Override
                                    public void accept(DataArray dataArray) {
                                        Logger.d(dataArray.toString());
                                        if (dataArray.getStatus() == 200) {
                                            Toast.makeText(activity, dataArray.getMsg(), Toast.LENGTH_SHORT).show();
                                            User user = (User) dataArray.getData().get(0);
                                            Constants.USER_MOBILE = user.getMobile();
                                            Constants.USER_IMAGE = FontAwesome.Icon.faw_weixin;
                                            SPUtil.setIsLogin(App.getInst(), true);
                                            activity.finish();
                                            instance = null;
                                        } else if (dataArray.getStatus() == 400) {
                                            Toast.makeText(activity, dataArray.getMsg(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) {
                                        Logger.d(throwable.toString());
                                        instance = null;
                                    }
                                });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Logger.d(throwable.toString());
                    }
                });

    }

}
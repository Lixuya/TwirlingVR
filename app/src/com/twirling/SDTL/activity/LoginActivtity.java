package com.twirling.SDTL.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.twirling.SDTL.Constants;
import com.twirling.SDTL.R;
import com.twirling.SDTL.model.DataArray;
import com.twirling.SDTL.retrofit.RetrofitManager;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by 谢秋鹏 on 2016/8/5.
 */
public class LoginActivtity extends AppCompatActivity {
    @BindView(R.id.edt_cellphone)
    EditText edt_cellphone;

    @BindView(R.id.edt_password)
    EditText edt_password;

    @BindView(R.id.btn_login)
    Button btn_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        RxView.clicks(btn_login)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        login();
                    }
                });
    }

    private void login() {
        final String mobile = edt_cellphone.getText().toString();
        String password = edt_password.getText().toString();
        HashMap<String, Object> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("password", password);
        RetrofitManager.getService().login(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DataArray>() {
                    @Override
                    public void call(DataArray dataArray) {
                        if (dataArray.getStatus() == 200) {
                            Toast.makeText(LoginActivtity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                            Constants.USER_MOBILE = mobile;
                            Constants.USER_IMAGE = FontAwesome.Icon.faw_user;
                            finish();
                        } else if (dataArray.getStatus() == 400) {
                            Toast.makeText(LoginActivtity.this, "账号密码错误", Toast.LENGTH_SHORT).show();
                        } else if (dataArray.getStatus() == -1) {
                            Toast.makeText(LoginActivtity.this, "手机号密码不能为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("LoginActivity", throwable.toString());
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        // Toast.makeText(LoginActivtity.this, dataArray.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

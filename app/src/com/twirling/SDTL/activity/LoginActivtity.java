package com.twirling.SDTL.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.twirling.SDTL.R;
import com.twirling.SDTL.model.User;
import com.twirling.SDTL.retrofit.RetrofitManager;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
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
        String password = edt_password.getText().toString();
        HashMap<String, Object> params = new HashMap<>();
        params.put("mobile", edt_cellphone.getText());
        params.put("password", password);
        RetrofitManager.getService().login(params)
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User dataArray) {
                        Log.w("tag", dataArray.toString());
                    }
                });
    }
}

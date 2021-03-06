package com.twirling.SDTL.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.twirling.SDTL.App;
import com.twirling.SDTL.Constants;
import com.twirling.SDTL.R;
import com.twirling.SDTL.model.DataArray;
import com.twirling.SDTL.retrofit.RetrofitManager;
import com.twirling.libtwirling.utils.SPUtil;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by xieqi on 2016/10/12.
 */

public class LoginActivity extends AppCompatActivity {
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
				.subscribe(new Consumer<Object>() {
					@Override
					public void accept(Object o) throws Exception {
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
				.subscribe(new Consumer<DataArray>() {
					@Override
					public void accept(DataArray dataArray) {
						if (dataArray.getStatus() == 200) {
							Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
							Constants.USER_MOBILE = mobile;
							Constants.USER_IMAGE = FontAwesome.Icon.faw_mobile;
							SPUtil.setUserMobile(App.getInst(), mobile);
							SPUtil.setIsLogin(App.getInst(), true);
							finish();
						} else if (dataArray.getStatus() == 400) {
							Toast.makeText(LoginActivity.this, "账号密码错误", Toast.LENGTH_SHORT).show();
						} else if (dataArray.getStatus() == -1) {
							Toast.makeText(LoginActivity.this, "手机号密码不能为空", Toast.LENGTH_SHORT).show();
						}
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) {
						Log.e("WXEntryActivity", throwable.toString());
					}
				});
	}
}

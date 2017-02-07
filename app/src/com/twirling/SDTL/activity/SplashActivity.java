package com.twirling.SDTL.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.twirling.SDTL.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by 谢秋鹏 on 2015/6/29.
 */
public class SplashActivity extends AppCompatActivity {
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_splash);
//        DialogLoading.getInstance().show();
		Observable.timer(2, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
			@Override
			public void accept(Long aLong) {
//                DialogLoading.getInstance().dismiss();
				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
				SplashActivity.this.startActivity(intent);
				SplashActivity.this.finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}
}

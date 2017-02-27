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
 * Target: SplashActivity
 */
public class SplashActivity extends AppCompatActivity {
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_splash);
		Observable.timer(2, TimeUnit.SECONDS)
				.subscribe(new Consumer<Long>() {
					@Override
					public void accept(Long aLong) {
						Intent intent = new Intent(SplashActivity.this, MainActivity.class);
						SplashActivity.this.startActivity(intent);
						Observable.timer(2, TimeUnit.SECONDS)
								.subscribe(new Consumer<Long>() {
									@Override
									public void accept(Long aLong) {
										finish();
									}
								});

					}
				});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}
}

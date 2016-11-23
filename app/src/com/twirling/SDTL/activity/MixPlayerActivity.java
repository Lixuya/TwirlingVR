package com.twirling.SDTL.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.twirling.SDTL.R;
import com.twirling.SDTL.player.OpenMXPlayer;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by xieqi on 2016/11/21.
 */

public class MixPlayerActivity extends AppCompatActivity {
    private OpenMXPlayer openMXPlayer = null;
    private boolean isPaused = true;
    private boolean isPlaying = false;
    //
    @BindView(R.id.iv_play)
    ImageView iv_play;
    @BindView(R.id.iv_stop)
    ImageView iv_stop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openMXPlayer = new OpenMXPlayer();
        openMXPlayer.setProfileId(0);
        openMXPlayer.setAudioIndex(0);
    }

    private void togglePause() {
        if (isPaused) {
            openMXPlayer.play();
            isPaused = false;
        } else {
            openMXPlayer.stop();
            isPaused = true;
        }
    }

    private void initData() {

    }

    private void initView() {
        RxView.clicks(iv_play)
                .filter(new Func1<Void, Boolean>() {
                    @Override
                    public Boolean call(Void aVoid) {
                        return isPlaying == false;
                    }
                })
                .throttleFirst(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isPlaying = true;
                        togglePause();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(getClass() + "", throwable.toString());
                    }
                });
        RxView.clicks(iv_stop)
                .filter(new Func1<Void, Boolean>() {
                    @Override
                    public Boolean call(Void aVoid) {
                        return isPlaying == true;
                    }
                })
                .throttleFirst(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isPlaying = false;
                        iv_stop.setVisibility(View.INVISIBLE);
                        iv_play.setVisibility(View.VISIBLE);
                        togglePause();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(getClass() + "", throwable.toString());
                    }
                });
    }
}

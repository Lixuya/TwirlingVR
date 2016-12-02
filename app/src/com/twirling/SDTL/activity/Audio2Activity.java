package com.twirling.SDTL.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.twirling.SDTL.R;
import com.twirling.SDTL.model.AudioItem;
import com.twirling.audio.player.OpenMXPlayer;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class Audio2Activity extends AppCompatActivity {
    @BindView(R.id.iv_stop)
    ImageView iv_stop;

    @BindView(R.id.iv_play)
    ImageView iv_play;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_video_image)
    ImageView iv_video_image;

    private String imageUrl,
            videoUrl,
            videoName,
            title;
    //
    private OpenMXPlayer openMXPlayer = null;
    private boolean isPaused = true;
    private boolean isPlaying = false;

    //
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio2);
        ButterKnife.bind(this);
        //
        initData();
        //
        initView();
    }

    private void initData() {
        final AudioItem audioItem = (AudioItem) getIntent().getExtras().getParcelable("AudioItem");
        videoName = audioItem.getAudio();
        imageUrl = audioItem.getCover();
        title = audioItem.getTitle();
        //
        openMXPlayer = new OpenMXPlayer();
        openMXPlayer.setProfileId(0);
        openMXPlayer.setAudioIndex(0);
    }

    private void initView() {
        tv_title.setText(title);
        Glide.with(getBaseContext()).load(imageUrl).into(iv_video_image);
        RxView.clicks(iv_play)
                .filter(new Func1<Void, Boolean>() {
                    @Override
                    public Boolean call(Void aVoid) {
                        return false;
                    }
                })
                .throttleFirst(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        isPlaying = true;
                        iv_play.setVisibility(View.INVISIBLE);
                        iv_stop.setVisibility(View.VISIBLE);
                        openMXPlayer.setDataSource(videoName);
                        togglePause();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(getClass() + "", throwable.toString());
                    }
                });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}

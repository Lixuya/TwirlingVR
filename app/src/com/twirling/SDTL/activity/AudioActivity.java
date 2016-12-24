package com.twirling.SDTL.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxSeekBar;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.twirling.SDTL.R;
import com.twirling.SDTL.model.AudioItem;
import com.twirling.SDTL.player.OpenMXPlayer;

import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.egl.EGLConfig;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

import static com.mikepenz.iconics.Iconics.TAG;

public class AudioActivity extends GvrActivity implements GvrView.StereoRenderer {
    @BindView(R.id.iv_stop)
    ImageView iv_stop;

    @BindView(R.id.iv_pause)
    ImageView iv_pause;

    @BindView(R.id.iv_play)
    ImageView iv_play;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_yaw)
    TextView tv_yaw;

    @BindView(R.id.tv_pitch)
    TextView tv_pitch;

    @BindView(R.id.iv_video_image)
    ImageView iv_video_image;

    @BindView(R.id.gvrview)
    GvrView gvrView;

    @BindView(R.id.seek_bar)
    SeekBar seekBar;

    private String imageUrl,
            audioUrl,
            title;
    //
    private OpenMXPlayer openMXPlayer = null;
    private boolean isPaused = true;
    float[] headRotationEular = new float[3];
    private BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();
    Handler handler = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio2);
        ButterKnife.bind(this);
        //
        this.lifecycleSubject.onNext(ActivityEvent.CREATE);
        //
        initData();
        //
        initView();
    }

    @Override
    protected void onStart() {
        iv_play.performClick();
        super.onStart();
    }

    private void initData() {
        handler = new Handler(getMainLooper()) {
            public void handleMessage(Message msg) {
                tv_yaw.setText("水平角: " + (int) (headRotationEular[1] * 180 / Math.PI) + " °");
                tv_pitch.setText("仰角: " + (int) (-headRotationEular[0] * 180 / Math.PI) + " °");
            }
        };
        final AudioItem audioItem = (AudioItem) getIntent().getExtras().getParcelable("AudioItem");
        audioUrl = audioItem.getAudio();
        imageUrl = audioItem.getCover();
        title = audioItem.getTitle();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        try {
            if (openMXPlayer != null) {
                openMXPlayer.pause();
            }
        } catch (Exception e) {
            Log.i("onPause", "error");
            e.printStackTrace();
        }
        isPaused = true;
        super.onPause();
    }

    @Override
    protected void onStop() {
        try {
            if (openMXPlayer != null) {
                openMXPlayer.stop();
            }
        } catch (Exception e) {
            Log.i("onStop", "error");
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        this.lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
    }

    private void initView() {
        tv_title.setText(title);
        Drawable icon = new IconicsDrawable(getBaseContext())
                .icon(FontAwesome.Icon.faw_play)
                .color(Color.parseColor("#FFFFFF"))
                .sizeDp(25);
        iv_play.setImageDrawable(icon);
        Drawable icon2 = new IconicsDrawable(getBaseContext())
                .icon(FontAwesome.Icon.faw_pause)
                .color(Color.parseColor("#FFFFFF"))
                .sizeDp(25);
        iv_pause.setImageDrawable(icon2);
        Drawable icon3 = new IconicsDrawable(getBaseContext())
                .icon(FontAwesome.Icon.faw_stop)
                .color(Color.parseColor("#FFFFFF"))
                .sizeDp(25);
        iv_stop.setImageDrawable(icon3);
        Glide.with(getBaseContext()).load(imageUrl).into(iv_video_image);
        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (openMXPlayer == null) {
                    openMXPlayer = new OpenMXPlayer();
                    openMXPlayer.setProfileId(1);
                    openMXPlayer.setAudioIndex(0);
                }
                openMXPlayer.setDataSource(audioUrl);
                togglePause();
            }
        });
        RxView.clicks(iv_pause)
                .throttleFirst(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycle.bindUntilEvent(lifecycleSubject, ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        togglePause();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(getClass() + "", throwable.toString());
                    }
                });
        RxView.clicks(iv_stop)
                .throttleFirst(3, TimeUnit.SECONDS)
                .filter(new Func1<Void, Boolean>() {
                    @Override
                    public Boolean call(Void aVoid) {
                        return openMXPlayer != null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycle.bindUntilEvent(lifecycleSubject, ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        isPaused = false;
                        togglePause();
                        openMXPlayer.stop();
                        seekBar.setProgress(0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(getClass() + "", throwable.toString());
                    }
                });
        //
        gvrView.setEGLConfigChooser(8, 8, 8, 8, 16, 8);
        gvrView.setRenderer(this);
        gvrView.setTransitionViewEnabled(true);
        setGvrView(gvrView);
        //
        RxSeekBar.userChanges(seekBar)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        if (openMXPlayer != null) {
                            isPaused = true;
                        }
                        return openMXPlayer != null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer progress) {
                        openMXPlayer.seek((float) progress);
                        Observable.timer(2, TimeUnit.SECONDS)
                                .subscribe(new Action1<Long>() {
                                    @Override
                                    public void call(Long aLong) {
                                        isPaused = false;
                                    }
                                });
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
            iv_play.setVisibility(View.INVISIBLE);
            iv_pause.setVisibility(View.VISIBLE);
            openMXPlayer.play();
            isPaused = false;
        } else {
            iv_play.setVisibility(View.VISIBLE);
            iv_pause.setVisibility(View.INVISIBLE);
            openMXPlayer.pause();
            isPaused = true;
        }
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

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        headTransform.getEulerAngles(headRotationEular, 0);
        handler.sendEmptyMessage(0);
        if (openMXPlayer == null || openMXPlayer.getDaa() == null) {
            return;
        }
        if (!isPaused) {
            seekBar.setProgress(openMXPlayer.getDuration());
        }
        openMXPlayer.getDaa().setGyroscope(headRotationEular);
        if (seekBar.getProgress() >= 99) {
            seekBar.setProgress(100);
            isPaused = false;
            togglePause();
            openMXPlayer.stop();
        }
    }

    @Override
    public void onDrawEye(Eye eye) {

    }

    @Override
    public void onFinishFrame(Viewport viewport) {

    }

    @Override
    public void onSurfaceChanged(int i, int i1) {

    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {

    }

    @Override
    public void onRendererShutdown() {

    }
}

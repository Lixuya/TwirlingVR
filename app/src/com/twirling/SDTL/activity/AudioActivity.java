package com.twirling.SDTL.activity;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;
import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;
import com.jakewharton.rxbinding.widget.RxSeekBar;
import com.twirling.SDTL.Constants;
import com.twirling.SDTL.R;
import com.twirling.SDTL.model.DownloadJson;
import com.twirling.SDTL.model.Elements;
import com.twirling.SDTL.model.VideoItem;
import com.twirling.SDTL.player.OpenMXPlayer;
import com.twirling.SDTL.utils.DeviceUtils;
import com.twirling.SDTL.utils.FileUtil;

import javax.microedition.khronos.egl.EGLConfig;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


/**
 * Created by 谢秋鹏 on 2016/6/8.
 */
public class AudioActivity extends GvrActivity implements GvrView.StereoRenderer {
    //
    private String TAG = "AudioActivity";
    private float[] headView;
    private float[] headRotation;
    private float[] headRotationEular;
    private OpenMXPlayer openMXPlayer = null;
    //
    private boolean isPaused = false;
    private Uri videoUri = null;
    private int profileId = 11;
    private int channels = 8;

    private String audioPath = "";
    private String jsonName = "";
    float[][] metadata = null;
    //
    @BindView(R.id.status_text)
    TextView statusText;
    @BindView(R.id.video_view)
    VrVideoView video_view;
    @BindView(R.id.seek_bar)
    SeekBar seekBar;
    @BindView(R.id.gvrview)
    GvrView gvrView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_audio);
        ButterKnife.bind(this);
        initData();
        //
        initView();
        //
        openMXPlayer = new OpenMXPlayer();
        openMXPlayer.setProfileId(profileId);
        openMXPlayer.setDataSource(audioPath);
    }

    private void initData() {
        // initData
        VideoItem videoItem = getIntent().getParcelableExtra("videoItem");
        String name = videoItem.getAppAndroidOffline().split("\\.")[0];
        videoUri = Uri.parse(Constants.URI_DOWNLOAD_LOCAL + name + "video.mp4");
        //
        jsonName = name + "data.json";
        loadJson(jsonName);
        //
        boolean isWave = videoItem.getVrAudio() == 12
                || videoItem.getVrAudio() == 0
                || channels == 8
                || DeviceUtils.getDeviceInfo().deviceBrand.equals("samsung");
        if (true) {
            audioPath = Constants.URI_DOWNLOAD_LOCAL + name + "sound.wav";
        } else {
            audioPath = Constants.URI_DOWNLOAD_LOCAL + name + "audio.mp4";
        }
        //
        Log.w(TAG, videoUri + "  " + audioPath + " " + profileId);
    }

    private void initView() {
        headView = new float[16];
        headRotation = new float[4];
        headRotationEular = new float[3];
        //
        gvrView.setEGLConfigChooser(8, 8, 8, 8, 16, 8);
        gvrView.setRenderer(this);
        gvrView.setTransitionViewEnabled(true);
        setGvrView(gvrView);
        //
        RxSeekBar.userChanges(seekBar)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer progress) {
                        video_view.seekTo(progress);
                        //seetPlayer
                        float percent = (float) progress / video_view.getDuration() * 100f;
                        if (Math.ceil(openMXPlayer.presentationTimeUs) == Math.ceil(openMXPlayer.duration)) {
                            openMXPlayer.clearSource();
                            openMXPlayer = new OpenMXPlayer();
                            openMXPlayer.setProfileId(profileId);
                            openMXPlayer.setDataSource(audioPath);
                            openMXPlayer.play();
                        }
                        openMXPlayer.seek(percent);
                        updateStatusText();
                    }
                });
        try {
            video_view.setInfoButtonEnabled(false);
            video_view.loadVideo(videoUri, new VrVideoView.Options());
        } catch (Exception e) {
            e.printStackTrace();
        }
        video_view.setEventListener(new VrVideoEventListener() {
            @Override
            public void onLoadSuccess() {
                seekBar.setMax((int) video_view.getDuration());
                updateStatusText();
            }

            @Override
            public void onLoadError(String errorMessage) {
                Toast.makeText(getBaseContext(), "Error loading video: " + errorMessage, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onClick() {
                togglePause();
            }

            @Override
            public void onNewFrame() {
                seekBar.setProgress((int) video_view.getCurrentPosition());
                updateStatusText();
            }

            @Override
            public void onCompletion() {
            }
        });
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        headTransform.getHeadView(headView, 0);
        headTransform.getQuaternion(headRotation, 0);
        headTransform.getEulerAngles(headRotationEular, 0);
        openMXPlayer.getDaa().setGyroscope(headRotationEular);
        if (metadata != null) {
            openMXPlayer.getDaa().setMetadataFromJson(metadata);
        }
    }

    @Override
    public void onDrawEye(Eye eye) {
//        Log.i(TAG, "onDrawEye");
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
//        Log.i(TAG, "onFinishFrame");
    }

    @Override
    public void onSurfaceChanged(int i, int i1) {
        Log.i(TAG, "onSurfaceChanged");
    }

    @Override
    public void onCardboardTrigger() {
        Log.i(TAG, "onCardboardTrigger");
        super.onCardboardTrigger();
    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
        Log.i(TAG, "onSurfaceCreated");
    }

    @Override
    public void onRendererShutdown() {

    }

    private void updateStatusText() {
        StringBuilder status = new StringBuilder();
        status.append(isPaused ? "Paused: " : "Playing: ");
        status.append(String.format("%.2f", video_view.getCurrentPosition() / 1000f));
        status.append(" / ");
        status.append(video_view.getDuration() / 1000f);
        status.append(" seconds.");
        statusText.setText(status.toString());
    }

    private void togglePause() {
        if (isPaused) {
            video_view.playVideo();
            openMXPlayer.play();
            isPaused = false;
        } else {
            video_view.pauseVideo();
            openMXPlayer.pause();
            isPaused = true;
        }
        updateStatusText();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        video_view.pauseRendering();
        openMXPlayer.pause();
        isPaused = true;
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        video_view.resumeRendering();
        openMXPlayer.play();
        updateStatusText();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        video_view.shutdown();
        openMXPlayer.stop();
        super.onDestroy();
    }

    private void loadJson(String fileName) {
        String response = FileUtil.readFromSDCard(fileName);
        DownloadJson dj = JSON.parseObject(response, DownloadJson.class);
        Elements.SoundGroupBean sgb = dj.getElements().getSound_group().get(0);
        channels = sgb.getChannels();
        profileId = sgb.getProfileID();
        //
        String md = sgb.getMetadata();
        if (TextUtils.isEmpty(md)) {
            return;
        }
        String[] strs = md.split(";");
        String[][] strings = new String[strs.length][];
        for (int i = 0; i < strs.length; i++) {
            strings[i] = strs[i].replace(";", "").split(",");
            for (int j = 0; j < strings[i].length; j++) {
                if (metadata == null) {
                    metadata = new float[strs.length][strings[i].length];
                }
                metadata[i][j] = Float.valueOf(strings[i][j].replace(",", ""));
            }
        }
    }
}

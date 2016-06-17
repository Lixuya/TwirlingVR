package com.twirlingvr.www.activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;
import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;
import com.twirlingvr.www.R;
import com.twirlingvr.www.player.OpenMXPlayer;
import com.twirlingvr.www.utils.Constants;
import com.twirlingvr.www.utils.TextUtil;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by 谢秋鹏 on 2016/6/8.
 */
public class AudioActivity extends GvrActivity implements GvrView.StereoRenderer {
    private float[] headView;
    //    private float[] modelPosition;
    private float[] headRotation;
    private float[] headRotationEular;
    private OpenMXPlayer openMXPlayer = null;
    private String TAG = "AudioActivity";
    //
    private static final String STATE_IS_PAUSED = "isPaused";
    private static final String STATE_PROGRESS_TIME = "progressTime";
    private static final String STATE_VIDEO_DURATION = "videoDuration";
    public static final int LOAD_VIDEO_STATUS_UNKNOWN = 0;
    public static final int LOAD_VIDEO_STATUS_SUCCESS = 1;
    public static final int LOAD_VIDEO_STATUS_ERROR = 2;
    private int loadVideoStatus = LOAD_VIDEO_STATUS_UNKNOWN;
    private boolean isPaused = false;
    private Uri fileUri = null;
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
        //
        gvrView.setEGLConfigChooser(8, 8, 8, 8, 16, 8);
        gvrView.setRenderer(this);
        gvrView.setTransitionViewEnabled(true);
        gvrView.setOnCardboardBackButtonListener(
                new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                });
        setGvrView(gvrView);
        //
        openMXPlayer = new OpenMXPlayer();
        openMXPlayer.setDataSource(Constants.URL_AAC);
        headView = new float[16];
        headRotation = new float[4];
        headRotationEular = new float[3];
        //
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    video_view.seekTo(progress);
                    updateStatusText();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //
        video_view.setEventListener(new VrVideoEventListener() {
            @Override
            public void onLoadSuccess() {
                loadVideoStatus = LOAD_VIDEO_STATUS_SUCCESS;
                seekBar.setMax((int) video_view.getDuration());
                updateStatusText();
            }

            @Override
            public void onLoadError(String errorMessage) {
                loadVideoStatus = LOAD_VIDEO_STATUS_ERROR;
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
                video_view.seekTo(0);
            }
        });
        //
        loadVideoStatus = LOAD_VIDEO_STATUS_UNKNOWN;
        //
        String uri = getIntent().getStringExtra("videoUrl");
        fileUri = Uri.parse(uri);
        if (!TextUtil.isValidate(uri)) {
            return;
        }

    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        headTransform.getHeadView(headView, 0);
        headTransform.getQuaternion(headRotation, 0);
        headTransform.getEulerAngles(headRotationEular, 0);
        //
        if (openMXPlayer != null) {
            openMXPlayer.setMetadata(headRotationEular);
        }

    }

    @Override
    public void onDrawEye(Eye eye) {
        Log.i(TAG, "onDrawEye");
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
        Log.i(TAG, "onFinishFrame");
    }

    @Override
    public void onSurfaceChanged(int i, int i1) {
        Log.i(TAG, "onSurfaceChanged");
    }

    @Override
    public void onCardboardTrigger() {
        super.onCardboardTrigger();
    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
        openMXPlayer.play();
        try {
            video_view.loadVideo(fileUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        } else {
            video_view.pauseVideo();
        }
        isPaused = !isPaused;
        updateStatusText();
    }
}

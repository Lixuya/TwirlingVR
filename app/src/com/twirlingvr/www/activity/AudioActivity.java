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
import com.twirlingvr.www.model.VideoItem;
import com.twirlingvr.www.player.OpenMXPlayer;
import com.twirlingvr.www.utils.Constants;

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
    private Uri videoUri = null;
    private String audioPath = "";
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
//        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        // initData
        VideoItem videoItem = getIntent().getParcelableExtra("videoItem");
        String name = videoItem.getAndroidoffline().split("\\.")[0];
        videoUri = Uri.parse(Constants.URI_DOWNLOAD_LOCAL + name + "video.mp4");
        audioPath = Constants.URI_DOWNLOAD_LOCAL + name + "audio.mp4";
        Log.w("videoUri", videoUri.toString() + "   " + audioPath);
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
//        GvrViewerParams params = new GvrViewerParams();
//        params.createFromUri(videoUri);
//        gvrView.updateGvrViewerParams(params);
        //
        openMXPlayer = new OpenMXPlayer();
        openMXPlayer.setDataSource(audioPath);
        headView = new float[16];
        headRotation = new float[4];
        headRotationEular = new float[3];
        //
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    video_view.seekTo(progress);
                    //
                    float percent = progress * 100 / video_view.getDuration();
                    openMXPlayer.seek(percent);
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
        try {
            video_view.loadVideo(videoUri, new VrVideoView.Options());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        loadVideoStatus = LOAD_VIDEO_STATUS_UNKNOWN;
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        headTransform.getHeadView(headView, 0);
        headTransform.getQuaternion(headRotation, 0);
        headTransform.getEulerAngles(headRotationEular, 0);
        openMXPlayer.setMetadata(headRotationEular);
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
//        try {
//            videoOptions.inputFormat = VrVideoView.Options.FORMAT_DEFAULT;
//            video_view.loadVideo(videoUri, videoOptions);
//            openMXPlayer.play();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
        } else {
            video_view.pauseVideo();
            openMXPlayer.pause();
        }
        isPaused = !isPaused;
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
//        video_view.playVideo();
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
}

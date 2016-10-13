package com.twirling.SDTL.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;
import com.twirling.SDTL.R;
import com.twirling.SDTL.model.DataArray;
import com.twirling.SDTL.model.LiveItem;
import com.twirling.SDTL.retrofit.RetrofitManager;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class HLSActivity extends AppCompatActivity {
    //
    private static final String TAG = HLSActivity.class.getSimpleName();
    private static final String STATE_IS_PAUSED = "isPaused";
    private static final String STATE_PROGRESS_TIME = "progressTime";
    private static final String STATE_VIDEO_DURATION = "videoDuration";
    public static final int LOAD_VIDEO_STATUS_UNKNOWN = 0;
    public static final int LOAD_VIDEO_STATUS_SUCCESS = 1;
    public static final int LOAD_VIDEO_STATUS_ERROR = 2;
    private int loadVideoStatus = LOAD_VIDEO_STATUS_UNKNOWN;
    private Uri fileUri;
    private boolean isPaused = false;
    //
//    private List<Object> datas = new ArrayList<Object>();
    //
    @BindView(R.id.status_text)
    TextView statusText;
    @BindView(R.id.video_view)
    VrVideoView videoWidgetView;
    @BindView(R.id.seek_bar)
    SeekBar seekBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ButterKnife.bind(this);
        //
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoWidgetView.seekTo(progress);
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
        videoWidgetView.setEventListener(new VrVideoEventListener() {
            @Override
            public void onLoadSuccess() {
                loadVideoStatus = LOAD_VIDEO_STATUS_SUCCESS;
                seekBar.setMax((int) videoWidgetView.getDuration());
                updateStatusText();
            }

            @Override
            public void onLoadError(String errorMessage) {
                loadVideoStatus = LOAD_VIDEO_STATUS_ERROR;
                Log.e("error", "Error loading video:" + errorMessage);
                Toast.makeText(HLSActivity.this, "Error loading video: " + errorMessage, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onClick() {
                togglePause();
            }

            @Override
            public void onNewFrame() {
                seekBar.setProgress((int) videoWidgetView.getCurrentPosition());
                updateStatusText();
            }


            @Override
            public void onCompletion() {
                videoWidgetView.seekTo(0);
            }
        });
        //
        loadVideo();
    }

    private void loadVideo() {
        loadVideoStatus = LOAD_VIDEO_STATUS_UNKNOWN;
        RetrofitManager.getService().getLiveList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DataArray<LiveItem>>() {
                    @Override
                    public void call(DataArray<LiveItem> dataArray) {
                        String uri = dataArray.getData().get(0).getHls();
                        Log.w(HLSActivity.TAG, uri);
                        fileUri = Uri.parse(uri);
                        //
                        try {
                            videoWidgetView.setInfoButtonEnabled(false);
                            VrVideoView.Options options = new VrVideoView.Options();
                            options.inputFormat = VrVideoView.Options.FORMAT_HLS;
                            options.inputType = VrVideoView.Options.TYPE_MONO;
                            videoWidgetView.loadVideo(fileUri, options);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("HLSActivity", throwable.toString());
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        // Toast.makeText(WXEntryActivity.this, dataArray.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong(STATE_PROGRESS_TIME, videoWidgetView.getCurrentPosition());
        savedInstanceState.putLong(STATE_VIDEO_DURATION, videoWidgetView.getDuration());
        savedInstanceState.putBoolean(STATE_IS_PAUSED, isPaused);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        long progressTime = savedInstanceState.getLong(STATE_PROGRESS_TIME);
        long duration = savedInstanceState.getLong(STATE_VIDEO_DURATION);
        isPaused = savedInstanceState.getBoolean(STATE_IS_PAUSED);
        //
        seekBar.setMax((int) duration);
        seekBar.setProgress((int) progressTime);
        videoWidgetView.seekTo(progressTime);
        if (isPaused) {
            videoWidgetView.pauseVideo();
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoWidgetView.pauseRendering();
        isPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoWidgetView.resumeRendering();
        updateStatusText();
    }

    @Override
    protected void onDestroy() {
        videoWidgetView.shutdown();
        super.onDestroy();
    }

    private void updateStatusText() {
        StringBuilder status = new StringBuilder();
        status.append(isPaused ? "Paused: " : "Playing: ");
        status.append(String.format("%.2f", videoWidgetView.getCurrentPosition() / 1000f));
        status.append(" / ");
        status.append(videoWidgetView.getDuration() / 1000f);
        status.append(" seconds.");
        statusText.setText(status.toString());
    }

    private void togglePause() {
        if (isPaused) {
            videoWidgetView.playVideo();
        } else {
            videoWidgetView.pauseVideo();
        }
        isPaused = !isPaused;
        updateStatusText();
    }
}

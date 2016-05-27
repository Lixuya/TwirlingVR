package com.twirlingvr.www.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;
import com.twirlingvr.www.R;

import java.io.IOException;

public class SimpleVrVideoActivity extends Activity {
    private static final String TAG = SimpleVrVideoActivity.class.getSimpleName();
    private static final String STATE_IS_PAUSED = "isPaused";
    private static final String STATE_PROGRESS_TIME = "progressTime";
    private static final String STATE_VIDEO_DURATION = "videoDuration";
    public static final int LOAD_VIDEO_STATUS_UNKNOWN = 0;
    public static final int LOAD_VIDEO_STATUS_SUCCESS = 1;
    public static final int LOAD_VIDEO_STATUS_ERROR = 2;
    private int loadVideoStatus = LOAD_VIDEO_STATUS_UNKNOWN;

    public int getLoadVideoStatus() {
        return loadVideoStatus;
    }

    private Uri fileUri;
    //    private VideoLoaderTask backgroundVideoLoaderTask;
    private VrVideoView videoWidgetView;
    private SeekBar seekBar;
    private TextView statusText;
    private boolean isPaused = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBarListener());
        statusText = (TextView) findViewById(R.id.status_text);

        videoWidgetView = (VrVideoView) findViewById(R.id.video_view);
        videoWidgetView.setEventListener(new ActivityEventListener());

        loadVideoStatus = LOAD_VIDEO_STATUS_UNKNOWN;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String uri = bundle.getString("videoUrl");
        fileUri = Uri.parse(uri);

        Intent videoIntent = new Intent(Intent.ACTION_VIEW);
        videoIntent.setDataAndType(fileUri, "video/*");
        onNewIntent(videoIntent);
        // handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i(TAG, this.hashCode() + ".onNewIntent()");
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Log.i(TAG, "ACTION_VIEW Intent received");
            fileUri = intent.getData();
            if (fileUri == null) {
                Log.w(TAG, "No data uri specified. Use \"-d /path/filename\".");
            } else {
                Log.i(TAG, "Using file " + fileUri.toString());
            }
        } else {
            Log.i(TAG, "Intent is not ACTION_VIEW. Using the default video.");
            fileUri = null;
        }
        try {
            videoWidgetView.loadVideo(fileUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
//    if (backgroundVideoLoaderTask != null) {
//      // Cancel any task from a previous intent sent to this activity.
//      backgroundVideoLoaderTask.cancel(true);
//    }
//    backgroundVideoLoaderTask = new VideoLoaderTask();
//    backgroundVideoLoaderTask.execute(fileUri);
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
        super.onRestoreInstanceState(savedInstanceState);
        long progressTime = savedInstanceState.getLong(STATE_PROGRESS_TIME);
        videoWidgetView.seekTo(progressTime);
        seekBar.setMax((int) savedInstanceState.getLong(STATE_VIDEO_DURATION));
        seekBar.setProgress((int) progressTime);
        isPaused = savedInstanceState.getBoolean(STATE_IS_PAUSED);
        if (isPaused) {
            videoWidgetView.pauseVideo();
        }
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

    private void togglePause() {
        if (isPaused) {
            videoWidgetView.playVideo();
        } else {
            videoWidgetView.pauseVideo();
        }
        isPaused = !isPaused;
        updateStatusText();
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

    private class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
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
    }

    private class ActivityEventListener extends VrVideoEventListener {

        @Override
        public void onLoadSuccess() {
            Log.i(TAG, "Sucessfully loaded video " + videoWidgetView.getDuration());
            loadVideoStatus = LOAD_VIDEO_STATUS_SUCCESS;
            seekBar.setMax((int) videoWidgetView.getDuration());
            updateStatusText();
        }

        @Override
        public void onLoadError(String errorMessage) {
            loadVideoStatus = LOAD_VIDEO_STATUS_ERROR;
            Toast.makeText(
                    SimpleVrVideoActivity.this, "Error loading video: " + errorMessage, Toast.LENGTH_LONG)
                    .show();
            Log.e(TAG, "Error loading video: " + errorMessage);
        }

        @Override
        public void onClick() {
            togglePause();
        }

        @Override
        public void onNewFrame() {
            updateStatusText();
            seekBar.setProgress((int) videoWidgetView.getCurrentPosition());
        }


        @Override
        public void onCompletion() {
            videoWidgetView.seekTo(0);
        }
    }

//    class VideoLoaderTask extends AsyncTask<Uri, Void, Boolean> {
//        @Override
//        protected Boolean doInBackground(Uri... uri) {
//            try {
//                if (uri == null || uri.length < 1 || uri[0] == null) {
//                    videoWidgetView.loadVideoFromAsset("congo.mp4");
//                } else {
//                    videoWidgetView.loadVideo(fileUri[0]);
//                }
//            } catch (IOException e) {
//                // An error here is normally due to being unable to locate the file.
//                loadVideoStatus = LOAD_VIDEO_STATUS_ERROR;
//                // Since this is a background thread, we need to switch to the main thread to show a toast.
//                videoWidgetView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast
//                                .makeText(SimpleVrVideoActivity.this, "Error opening file. ", Toast.LENGTH_LONG)
//                                .show();
//                    }
//                });
//                Log.e(TAG, "Could not open video: " + e);
//            }
//            return true;
//        }
//    }
}

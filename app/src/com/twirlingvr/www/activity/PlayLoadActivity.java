package com.twirlingvr.www.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.twirlingvr.www.App;
import com.twirlingvr.www.R;
import com.twirlingvr.www.data.RealmHelper;
import com.twirlingvr.www.model.VideoItem;
import com.twirlingvr.www.utils.Constants;
import com.twirlingvr.www.utils.DownloadChangeObserver;
import com.twirlingvr.www.utils.DownloadService;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlayLoadActivity extends Activity {
    @BindView(R.id.button)
    ImageView load;

    @BindView(R.id.button2)
    Button play;

    @BindView(R.id.iv_video_image)
    ImageView iv_video_image;

    @BindView(R.id.pb_download)
    ProgressBar mPbLoading;

    private String imageUrl,
            videoUrl,
            videoName;
    private CountDownTimer selfTimer = new CountDownTimer(20 * 1000, 1000) {
        public void onTick(long millSec) {
            mPbLoading.setProgress((int) ((20 * 1000 - millSec) / 1000));
        }

        @Override
        public void onFinish() {

        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playpanel);
        ButterKnife.bind(this);
        //
        StatusBarUtil.setTransparent(PlayLoadActivity.this);
        //
        final VideoItem videoItem = (VideoItem) getIntent().getExtras().getParcelable("videoItem");
        videoName = videoItem.getVideoName();
        videoUrl = Constants.PAPH_VIDEO + videoName;
        imageUrl = Constants.PAPH_IMAGE + videoItem.getImageName();
        //
//        String title = getIntent().getStringExtra("title");
//        TextView tv_title = (TextView) findViewById(R.id.tv_title);
//        tv_title.setText(title);
        //
        Glide.with(getBaseContext()).load(imageUrl).into(iv_video_image);
        //
        load.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //
                load.setBackgroundColor(Color.parseColor("#C0C0C0"));
                load.setEnabled(false);
                //
                Intent intent = new Intent(App.getInst().getApplicationContext(), DownloadService.class);
                intent.putExtra("videoItem", videoItem);
                startService(intent);
                //
                selfTimer.start();
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("videoUrl", videoUrl);
                intent.setClass(PlayLoadActivity.this, SimpleVrVideoActivity.class);
                startActivity(intent);
            }
        });
        VideoItem itemInDB = RealmHelper.getIns().selectVideoItem(videoName);
        if (itemInDB == null) {
            load.setBackgroundColor(Color.TRANSPARENT);
            load.setEnabled(true);
            return;
        }
        long downLoadId = itemInDB.getDownloadId();
        if (downLoadId == 0) {
            return;
        } else if (downLoadId == 1) {
            mPbLoading.setProgress(100);
            load.setEnabled(false);
        }
        load.setBackgroundColor(Color.parseColor("#C0C0C0"));
        DownloadChangeObserver pco = (DownloadChangeObserver) App.observers.get(downLoadId);
        if (pco == null) {
            return;
        }
        pco.setProgressListener(new DownloadChangeObserver.ProgressListener() {
            @Override
            public void invoke(int progress) {
                mPbLoading.setProgress(progress);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        selfTimer.cancel();
        selfTimer = null;
    }
}

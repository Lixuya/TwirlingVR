package com.twirlingvr.www.activity;

import android.app.Activity;
import android.content.Intent;
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
    Button load;

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
        DownloadChangeObserver pco = (DownloadChangeObserver) App.observer;
        if (pco == null || RealmHelper.getIns().selectVideoName(pco.getDownloadId()).equals(videoName)) {
            load.setVisibility(View.INVISIBLE);
            return;
        }
        load.setVisibility(View.VISIBLE);
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

package com.twirlingvr.www.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.twirlingvr.www.R;
import com.twirlingvr.www.data.RealmHelper;
import com.twirlingvr.www.model.VideoItem;
import com.twirlingvr.www.utils.Constants;
import com.twirlingvr.www.utils.DownloadService;

public class PlayLoadActivity extends Activity {
    private Button load,
            play;
    private ProgressBar mPbLoading;
    private String imageUrl,
            videoUrl,
            videoName;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playpanel);
        //
        StatusBarUtil.setTranslucent(PlayLoadActivity.this, 60);
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
        final ImageView iv_video_image = (ImageView) findViewById(R.id.iv_video_image);
        Glide.with(getBaseContext()).load(imageUrl).into(iv_video_image);
        //
        load = (Button) findViewById(R.id.button);
        load.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //
                videoItem.setUpdateTime(System.currentTimeMillis());
                RealmHelper.getIns().insertVideoItem(videoItem);
                //
                Intent intent = new Intent(PlayLoadActivity.this, DownloadService.class);
                intent.putExtra("url", videoUrl);
                intent.putExtra("videoName", videoName);
                startService(intent);
            }
        });
        play = (Button) findViewById(R.id.button2);
        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("videoUrl", videoUrl);
                //设置跳转新的activity，参数（当前对象，跳转到的class）
                intent.setClass(PlayLoadActivity.this, SimpleVrVideoActivity.class);
                //启动Activity 没有返回
                startActivity(intent);
            }
        });
        mPbLoading = (ProgressBar) findViewById(R.id.pb_loading);
    }
}

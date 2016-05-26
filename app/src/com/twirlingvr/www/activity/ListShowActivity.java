package com.twirlingvr.www.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TabHost;

import com.twirlingvr.www.R;

import java.io.File;


public class ListShowActivity extends Activity {

    public static String playuri;
    public static int id = 0;
    private Button line;
    FrameLayout.LayoutParams lp1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videolist);
        TabHost tabhost = (TabHost) this.findViewById(R.id.tabhost);
        tabhost.setup();
        tabhost.addTab(tabhost.newTabSpec("tab1")
                .setIndicator("在线")
                .setContent(
                R.id.tab1_txt));
        tabhost.addTab(tabhost.newTabSpec("tab2")
                .setIndicator("本地")
                .setContent(R.id.tab2_txt));
        tabhost.setCurrentTab(id);
        //
        line = (Button) findViewById(R.id.tab1_txt);
        Button local = new Button(this);
        Button dele = new Button(this);
        lp1 = new FrameLayout.LayoutParams(100, 100);
        lp1.setMargins(0, 500, 0, 0);
        dele.setLayoutParams(lp1);

        local.setBackgroundResource(R.drawable.jiaoyu);
        dele.setBackgroundResource(R.drawable.delete);
        FrameLayout localLayout = (FrameLayout) findViewById(R.id.tab2_txt);

        final File file = new File("sdcard/test.mp4");
        if (file.exists()) {

            localLayout.addView(local);
            localLayout.addView(dele);
            dele.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    file.delete();
                    refresh();
                }
            });

        }
        line.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent();
                playuri = "http://www.twirlingvr.com/App_Media/videos/_tianjin_jiaoyu_1920x1080_5mb_a.mp4";
                intent.setClass(ListShowActivity.this, PlayLoadActivity.class);
                startActivity(intent);
                id = 0;
            }
        });
        local.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent();
                playuri = "file:///sdcard/test.mp4";
                intent.putExtra("uri", playuri);
                intent.setClass(ListShowActivity.this, SimpleVrVideoActivity.class);
                startActivity(intent);
                id = 1;
            }
        });
    }

    public void refresh() {
        finish();
        Intent intent = new Intent(ListShowActivity.this, ListShowActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onCreate(null);
    }
}

package com.twirlingvr.www.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.twirlingvr.www.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class PlayLoadActivity extends Activity {
    private Button load;
    private Button play;
    private ProgressBar mPbLoading;
    private String loadurl;
    private String savepath = "sdcard/test.mp4";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playpanel);

        load = (Button) findViewById(R.id.button);
        load.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                loadurl = ListShowActivity.playuri;
                new Thread(networkTask).start();
            }
        });
        play = (Button) findViewById(R.id.button2);
        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("uri", ListShowActivity.playuri);
                //设置跳转新的activity，参数（当前对象，跳转到的class）
                intent.setClass(PlayLoadActivity.this, SimpleVrVideoActivity.class);
                //启动Activity 没有返回
                startActivity(intent);
            }
        });
        mPbLoading = (ProgressBar) findViewById(R.id.pb_loading);
    }

    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            Down(loadurl, savepath);

        }
    };
    public int contentLength;
    public File file;

    public void Down(String _urlStr, String _filePath) {
        file = new File(_filePath);
        //如果目标文件已经存在，则删除。产生覆盖旧文件的效果
        if (file.exists()) {
            file.delete();
        }
        try {
            // 构造URL
            URL url = new URL(_urlStr);
            // 打开连接
            URLConnection con = url.openConnection();
            //获得文件的长度
            contentLength = con.getContentLength();
            mPbLoading.setMax(contentLength);
            System.out.println("长度 :" + contentLength);
            // 输入流
            InputStream is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;

            // 输出的文件流
            FileOutputStream os = new FileOutputStream(_filePath);

            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
                mPbLoading.setProgress((int) file.length());

            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

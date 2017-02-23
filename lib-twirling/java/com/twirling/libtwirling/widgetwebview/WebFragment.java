package com.twirling.libtwirling.widgetwebview;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.twirling.libtwirling.R;

/**
 * Created by 谢秋鹏 on 2016/6/12.
 */
public class WebFragment extends Fragment {
    protected WebView webView = null;
    protected String url = "";

    @TargetApi(Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web, container, false);
        webView = (WebView) rootView.findViewById(R.id.wv);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // 2 设置缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        // 开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        // 3 H5缓存
        String cacheDirPath = this.getContext().getCacheDir().getAbsolutePath() + "/webViewCache ";
        // 开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        // 开启Application H5 Caches 功能
        webSettings.setAppCacheEnabled(true);
        // 设置Application Caches 缓存目录
        webSettings.setAppCachePath(cacheDirPath);
        // 4 JS
//      this.addJavascriptInterface(new MyJavaScriptInterface(this), "WidgetWebView");
        MyWebViewClient client = new MyWebViewClient();
        webView.setWebViewClient(client);
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean isHandle = false;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_HOVER_ENTER:
                    case MotionEvent.ACTION_HOVER_EXIT:
                    case MotionEvent.ACTION_HOVER_MOVE:
                        isHandle = true;
                        break;
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_SCROLL:
                        isHandle = false;
                        break;
                }
                return isHandle;
            }
        });
        url = "http://yun.twirlingvr.com/index.php";
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);  // , 1是可选写的
        //noinspection ResourceType
        webView.loadUrl(url);
        //
        loadPage(4);
        return rootView;
    }

    public void loadPage(int index) {
        // 1是可选写的
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1);
        webView.setLayoutParams(lp);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}

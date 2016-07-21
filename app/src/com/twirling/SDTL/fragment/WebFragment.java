package com.twirling.SDTL.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.twirling.SDTL.R;
import com.twirling.SDTL.widget.MyWebViewClient;

/**
 * Created by 谢秋鹏 on 2016/6/12.
 */
public class WebFragment extends Fragment {
    private WebView webView = null;

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
        // 设置数据库缓存路径
        webSettings.setGeolocationDatabasePath(cacheDirPath);
        // 开启Application H5 Caches 功能
        webSettings.setAppCacheEnabled(true);
        // 设置Application Caches 缓存目录
        webSettings.setAppCachePath(cacheDirPath);
        // 4 JS
//      this.addJavascriptInterface(new MyJavaScriptInterface(this), "WidgetWebView");
        MyWebViewClient client = new MyWebViewClient();
        webView.setWebViewClient(client);
        //
        loadPage(4);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void loadPage(int index) {
        String url = "";
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);  // , 1是可选写的
        switch (index) {
            case 3:
            case 4:
                url = "http://www.twirlingvr.com/index.html";
                lp.setMargins(0, -250, 0, 0);
                break;
            case 5:
                url = "http://www.twirlingvr.com/audio.html?ch=707562";
                lp.setMargins(0, -220, 0, 0);
                break;
            case 6:
                url = "http://www.twirlingvr.com/service.html";
                lp.setMargins(0, -220, 0, 0);
                break;
            case 7:
                url = "http://www.twirlingvr.com/App_Web/blog/essaylist.html?ch=time";
                lp.setMargins(0, 0, 0, 0);
                break;
            case 8:
                url = "http://yun.twirlingvr.com";
                lp.setMargins(0, -250, 0, 0);
                break;
            case 9:
                url = "http://www.twirlingvr.com/index.html#contact";
                lp.setMargins(0, -250, 0, 0);
                break;
            default:
                lp.setMargins(0, 0, 0, 0);
                break;
        }
        webView.setLayoutParams(lp);
        webView.loadUrl(url);
    }
}

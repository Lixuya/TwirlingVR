package com.twirling.SDTL.widget;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by 谢秋鹏 on 2015/7/21.
 */
public class MyWebViewClient extends WebViewClient {

    // Interface
    public interface WebViewListener {
        public void loadPage();
    }

    private WebViewListener listener = null;

    public void setWebViewListener(WebViewListener listener) {
        this.listener = listener;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
    }

    // 在加载页面资源时调用，每一个资源（比如图片）的加载都会调用一次。
    @Override
    public void onLoadResource(WebView view, String url) {
        return;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

        return super.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return super.shouldInterceptRequest(view, request);
    }

    // 重写此方法才能够处理在浏览器中的按键事件。
    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        return super.shouldOverrideKeyEvent(view, event);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }

    // 重写此方法可以让webview处理https请求。
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        super.doUpdateVisitedHistory(view, url, isReload);
    }
}
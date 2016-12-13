package com.twirling.SDTL.fragment;

import com.twirling.libtwirling.widgetwebview.WebFragment;

/**
 * Created by 谢秋鹏 on 2016/6/12.
 */
public class FragmentWeb extends WebFragment {

    @Override
    public void loadPage(int index) {
        super.loadPage(index);
        switch (index) {
            case 6:
                url = "http://yun.twirlingvr.com/index.php";
                break;
            case 7:
                url = "http://yun.twirlingvr.com/index.php/home/index/product.html";
                break;
            case 8:
                url = "http://yun.twirlingvr.com/index.php/home/audio/audioList.html";
                break;
            case 9:
                url = "http://yun.twirlingvr.com/index.php/home/index/recording.html";
                break;
            case 10:
                url = "http://yun.twirlingvr.com/index.php/home/index/about.html";
                break;
            default:
                break;
        }
        webView.loadUrl(url);
    }
}

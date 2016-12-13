package com.twirling.SDTL.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.twirling.SDTL.R;
import com.twirling.SDTL.fragment.FragmentWeb;

/**
 * Created by 谢秋鹏 on 2016/7/7.
 */
public class WebActivity extends FragmentActivity {
    private FragmentWeb fragmentWeb = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        fragmentWeb = (FragmentWeb) getSupportFragmentManager().findFragmentById(R.id.web);
        fragmentWeb.loadPage(getIntent().getFlags());
    }
}

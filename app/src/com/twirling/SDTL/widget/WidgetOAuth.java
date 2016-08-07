package com.twirling.SDTL.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.twirling.SDTL.R;

/**
 * 解决问题：首页下方的三个oauth按钮
 * <p/>
 * Created by 谢秋鹏 on 2015/7/9.
 */
public class WidgetOAuth extends LinearLayout implements View.OnClickListener {
    //
    private ImageButton btn_login_qq = null;
    private ImageButton btn_login_wx = null;
    private ImageButton btn_login_wb = null;

    //
    public WidgetOAuth(Context context, AttributeSet attrs) {
        super(context, attrs);
        View rowView = LayoutInflater.from(context).inflate(R.layout.oauth, this);
        //
        findView(rowView);
    }

    private void findView(View view) {
        // 1 WX
        btn_login_qq = (ImageButton) view.findViewById(R.id.btn_login_qq);
        btn_login_qq.setOnClickListener(this);
        btn_login_qq.setVisibility(View.GONE);
//        btn_login_qq.setVisibility(View.VISIBLE);
        // 2 QQ
        btn_login_wx = (ImageButton) view.findViewById(R.id.btn_login_wx);
        btn_login_wx.setOnClickListener(this);
        btn_login_wx.setVisibility(View.GONE);
        btn_login_wx.setVisibility(View.VISIBLE);
        // 3 WB
        btn_login_wb = (ImageButton) view.findViewById(R.id.btn_login_sina);
        btn_login_wb.setOnClickListener(this);
        btn_login_wb.setVisibility(View.GONE);
//        btn_login_wb.setVisibility(View.VISIBLE);
    }

    private Activity getActivity() {
        Activity activity = null;
        if (getContext() instanceof Activity) {
            activity = (Activity) getContext();
        }
        return activity;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login_qq:
//                LoginQQ.getInstance().initTencentQQ(getActivity());
                break;
            case R.id.btn_login_wx:
//                LoginWX.getInstance(getActivity()).initData();
                break;
            case R.id.btn_login_sina:
//                LoginSinaWB.getInstance().bindListener(getActivity());
                break;
        }
    }
}

package com.twirlingvr.www.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import com.twirlingvr.www.R;

/**
 * Created by 谢秋鹏 on 2016/6/28.
 */
public class DialogLoading extends Dialog {
    private static DialogLoading dialogLoading = null;
    private AnimationDrawable AniDraw = null;

    public DialogLoading(Context context) {
        super(context, R.style.MaterialBaseTheme_Dialog);
        setContentView(R.layout.dialog_loading);
        ImageView animationIV = (ImageView) findViewById(R.id.iv_loading);
        animationIV.setBackgroundResource(R.drawable.animation_loading_gif);
        AniDraw = (AnimationDrawable) animationIV.getBackground();
        AniDraw.start();
    }

    public static DialogLoading getInstance(Context context) {
        if (dialogLoading == null) {
            dialogLoading = new DialogLoading(context);
            dialogLoading.show();
        }
        return dialogLoading;
    }

    @Override
    public void show() {
        AniDraw.start();
        super.show();
    }

    @Override
    public void dismiss() {
        AniDraw.stop();
        super.dismiss();
    }
}

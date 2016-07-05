package com.twirling.SDTL.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import com.twirling.SDTL.App;
import com.twirling.SDTL.R;

/**
 * Created by 谢秋鹏 on 2016/6/28.
 */
public class DialogLoading extends Dialog {
    private static DialogLoading dialogLoading = null;
    private AnimationDrawable AniDraw = null;
    private static Context context = null;

    public DialogLoading(Context context) {
        super(context, R.style.MaterialBaseTheme_Dialog);
        setContentView(R.layout.dialog_loading);
        ImageView animationIV = (ImageView) findViewById(R.id.iv_loading);
        animationIV.setBackgroundResource(R.drawable.animation_loading_gif);
        AniDraw = (AnimationDrawable) animationIV.getBackground();
    }

    public static DialogLoading getInstance() {
        if (dialogLoading == null) {
            context = App.getInst().getCurrentShowActivity();
            dialogLoading = new DialogLoading(context);
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
        dialogLoading = null;
    }
}

package com.twirlingvr.www.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 解决问题：弹出自定义确认窗口
 *
 * @1 确认后执行动作
 * <p/>
 * Created by 谢秋鹏 on 2015/8/1.
 */
@Singleton
public class ModuleAlertDialog extends AlertDialog.Builder {

    @Inject
    public ModuleAlertDialog(Context context) {
        super(context);
        setAlertDialog();
    }

    public void setAlertDialog() {
        //this.setTitle("提示");
        this.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onConfirm();
                dialog.dismiss();
            }
        });
        this.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public AlertDialog.Builder setMessage(CharSequence message) {
        AlertDialog.Builder builder = super.setMessage(message);
        builder.create().show();
        return builder;
    }

    protected void onConfirm() {
        ((Activity) getContext()).finish();
    }
}

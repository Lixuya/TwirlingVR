package com.twirlingvr.www.module;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.twirlingvr.www.App;
import com.twirlingvr.www.R;

/**
 * 解决问题：弹出自定义确认窗口
 *
 * @1 确认后执行动作
 * <p/>
 * Created by 谢秋鹏 on 2015/8/1.
 */
//@Module
public class ModuleAlertDialog extends AlertDialog.Builder {

    public ModuleAlertDialog(Context context) {
        super(context, R.style.MaterialBaseTheme_Light_AlertDialog);
        setAlertDialog();
    }

    public void setAlertDialog() {
        this.setTitle("提示");
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
        App.getInst().getCurrentShowActivity().finish();
    }
}

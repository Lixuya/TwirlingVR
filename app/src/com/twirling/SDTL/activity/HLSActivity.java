package com.twirling.SDTL.activity;

import android.net.Uri;
import android.util.Log;

import com.google.vr.sdk.widgets.video.VrVideoView;
import com.twirling.SDTL.model.DataArray;
import com.twirling.SDTL.model.LiveItem;
import com.twirling.SDTL.retrofit.RetrofitManager;

import java.io.IOException;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by xieqi on 2016/11/23.
 */

public class HLSActivity extends com.twirling.player.activity.HLSActivity {

    @Override
    protected void loadUrl(final VrVideoView videoWidgetView) {
        videoWidgetView.setInfoButtonEnabled(false);
        final VrVideoView.Options options = new VrVideoView.Options();
        options.inputFormat = VrVideoView.Options.FORMAT_HLS;
        options.inputType = VrVideoView.Options.TYPE_MONO;
        RetrofitManager.getService().getLiveList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DataArray<LiveItem>>() {
                    @Override
                    public void call(DataArray<LiveItem> dataArray) {
                        String uri = dataArray.getData().get(0).getHls();
                        try {
                            videoWidgetView.loadVideo(Uri.parse(uri), options);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("HLSActivity", throwable.toString());
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        // Toast.makeText(WXEntryActivity.this, dataArray.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

package com.twirling.SDTL.activity;

import android.net.Uri;
import android.util.Log;

import com.google.vr.sdk.widgets.video.VrVideoView;
import com.twirling.SDTL.model.DataArray;
import com.twirling.SDTL.model.LiveItem;
import com.twirling.SDTL.retrofit.RetrofitManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
				.subscribe(new Consumer<DataArray<LiveItem>>() {
					@Override
					public void accept(DataArray<LiveItem> dataArray) throws Exception {
						String uri = dataArray.getData().get(0).getHls();
						videoWidgetView.loadVideo(Uri.parse(uri), options);
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(Throwable throwable) {
						Log.e("HLSActivity", throwable.toString());
					}
				});
	}
}

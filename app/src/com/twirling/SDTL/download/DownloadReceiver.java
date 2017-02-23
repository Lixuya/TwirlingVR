package com.twirling.SDTL.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.orhanobut.logger.Logger;
import com.twirling.SDTL.App;
import com.twirling.SDTL.data.RealmHelper;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


/**
 * Created by MagicBean on 2016/03/14 18:18:21
 */
public class DownloadReceiver extends BroadcastReceiver {
	private DownloadManager downloadManager;
	private String path = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		downloadManager = (DownloadManager) App.getInst().getSystemService(context.DOWNLOAD_SERVICE);
		switch (intent.getAction()) {
			case DownloadManager.ACTION_DOWNLOAD_COMPLETE:
			default:
				long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
				Observable.just(id)
						.observeOn(AndroidSchedulers.mainThread())
						.map(new Function<Long, Long>() {
							@Override
							public Long apply(Long id) {
								printDownloadInformation(id);
								return id;
							}
						})
						.subscribeOn(AndroidSchedulers.mainThread())
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(new Consumer<Long>() {
							@Override
							public void accept(Long id) {
								RealmHelper.getInstance().updateDownloadId(id);
							}
						});
				break;
			case DownloadManager.ACTION_NOTIFICATION_CLICKED:
//                Toast.makeText(context, "点击通知了....", Toast.LENGTH_LONG).show();
//                Intent install = new Intent(Intent.ACTION_VIEW);
//                String uriString = FileUtil.getFilePathFromUri(context, Uri.parse(path));
//                Logger.i("uriString " + uriString);
//                install.setDataAndType(Uri.fromFile(new File(uriString)), Constants.MIME_MP4);
//                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(install);
				break;
		}

	}

	//TODO 这里把所有的列都打印一下，有什么需求，就怎么处理,文件的本地路径就是path
	private void printDownloadInformation(long id) {
		DownloadManager.Query query = new DownloadManager.Query();
		query.setFilterById(id);
		//
		Cursor cursor = downloadManager.query(query);
		int columnCount = cursor.getColumnCount();
		while (cursor.moveToNext()) {
			for (int j = 0; j < columnCount; j++) {
				String columnName = cursor.getColumnName(j);
				String string = cursor.getString(j);
				if (columnName.equals("local_uri")) {
					path = string;
				}
				if (string != null) {
					Logger.i(columnName + ": " + string);
				} else {
					Logger.i(columnName + ": null");
				}
			}
		}
		cursor.close();
	}
}
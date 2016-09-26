package com.twirling.SDTL.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.util.Pair;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.twirling.SDTL.App;
import com.twirling.SDTL.Constants;
import com.twirling.SDTL.data.RealmHelper;
import com.twirling.SDTL.model.VideoItem;
import com.twirling.SDTL.utils.Decompress;
import com.twirling.SDTL.utils.FileUtil;

import java.io.File;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
                // 普通模式下载成功，全景声删除下载zip,解压缩
                Observable.just(id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(new Func1<Long, Observable<VideoItem>>() {
                            @Override
                            public Observable<VideoItem> call(Long id) {
                                return Observable.just(RealmHelper.getIns().selectVideoItem(id));
                            }
                        })
                        .filter(new Func1<VideoItem, Boolean>() {
                            @Override
                            public Boolean call(VideoItem item) {
                                return item != null;
                            }
                        })
                        .filter(new Func1<VideoItem, Boolean>() {
                            @Override
                            public Boolean call(VideoItem item) {
                                return !TextUtils.isEmpty(item.getAppAndroidOffline());
                            }
                        })
                        .filter(new Func1<VideoItem, Boolean>() {
                            @Override
                            public Boolean call(VideoItem item) {
                                return item.getVrAudio() != -1;
                            }
                        })
                        .map(new Func1<VideoItem, Pair<String, String>>() {
                            @Override
                            public Pair<String, String> call(VideoItem item) {
                                String androidOffine = item.getAppAndroidOffline();
                                String fileFolder = androidOffine.substring(0, item.getAppAndroidOffline().length() - 4);
                                return new Pair<String, String>(androidOffine, fileFolder);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(new Action1<Pair<String, String>>() {
                            @Override
                            public void call(Pair<String, String> pair) {
                                new Decompress(Constants.PAPH_DOWNLOAD_LOCAL + pair.first, Constants.PAPH_DOWNLOAD_LOCAL + pair.second).unzip();
                                FileUtil.delete(new File(Constants.PAPH_DOWNLOAD_LOCAL + pair.first));
                                FileUtil.delete(new File(Constants.PAPH_DOWNLOAD_LOCAL + pair.second));
                            }
                        });
                // 打印Id，更新uploadId为1
                Observable.just(id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Func1<Long, Long>() {
                            @Override
                            public Long call(Long id) {
                                printDownloadInformation(id);
                                return id;
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long id) {
                                RealmHelper.getIns().updateDownloadId(id);
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
package com.twirling.SDTL.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.twirling.SDTL.data.RealmHelper;
import com.twirling.SDTL.model.VideoItem;

import java.io.File;

/**
 * Created by MagicBean on 2016/03/14 18:18:21
 */
public class DownloadReceiver extends BroadcastReceiver {
    private DownloadManager downloadManager;
    private String path = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case DownloadManager.ACTION_DOWNLOAD_COMPLETE:
            default:
                downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                //
                VideoItem item = RealmHelper.getIns().selectVideoItem(id);
                if (item == null) {
                    return;
                }
                String fileFolder = "";
                RealmHelper.getIns().updateDownloadId(id);
                if (!TextUtils.isEmpty(item.getAndroidoffline())) {
//                DialogLoading.getInstance().show();
                    fileFolder = item.getAndroidoffline().substring(0, item.getAndroidoffline().length() - 4);
                    if (item.getIsatmos() == 1) {
                        new Decompress(Constants.PAPH_DOWNLOAD_LOCAL + item.getAndroidoffline(), Constants.PAPH_DOWNLOAD_LOCAL + fileFolder).unzip();
                    }
                    FileUtil.delete(new File(Constants.PAPH_DOWNLOAD_LOCAL + item.getAndroidoffline()));
                    FileUtil.delete(new File(Constants.PAPH_DOWNLOAD_LOCAL + fileFolder));
//                DialogLoading.getInstance().dismiss();
                }
                printDownloadInformation(id);
                break;
            case DownloadManager.ACTION_NOTIFICATION_CLICKED: Toast.makeText(context, "点击通知了....", Toast.LENGTH_LONG).show();
                Intent install = new Intent(Intent.ACTION_VIEW);
                String uriString = FileUtil.getFilePathFromUri(context, Uri.parse(path));
                Logger.i("uriString " + uriString);
                install.setDataAndType(Uri.fromFile(new File(uriString)), Constants.MIME_MP4);
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
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
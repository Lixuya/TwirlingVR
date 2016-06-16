package com.twirlingvr.www.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.twirlingvr.www.data.RealmHelper;

import java.io.File;

/**
 * Created by MagicBean on 2016/03/14 18:18:21
 */
public class DownloadReceiver extends BroadcastReceiver {
    private DownloadManager downloadManager;
    private String path = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            //TODO 判断这个id与之前的id是否相等，如果相等说明是之前的那个要下载的文件
            downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            RealmHelper.getIns().updateDownloadId(id);
            //
            printDownloadInformation(id);
            //TODO 判断这个id与之前的id是否相等，如果相等说明是之前的那个要下载的文件
//            if (id == App.downloadId) {
//                Intent install = new Intent(Intent.ACTION_VIEW);
//                // 转换path路径 否则报解析包错误
//                String uriString = getFilePathFromUri(context, Uri.parse(path));
//                Logger.i("-----------------------CompleteReceiver 转换后----路径uriString = " + uriString);
//                install.setDataAndType(Uri.fromFile(new File(uriString)), Constants.MIME_TYPE);
//                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(install);
//            }
        }
        //
        else if (action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
            Toast.makeText(context, "点击通知了....", Toast.LENGTH_LONG).show();
            Intent install = new Intent(Intent.ACTION_VIEW);
            //TODO 转换path路径 否则报解析包错误
            String uriString = FileUtil.getFilePathFromUri(context, Uri.parse(path));
            Logger.i("uriString " + uriString);
            install.setDataAndType(Uri.fromFile(new File(uriString)), Constants.MIME_TYPE);
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(install);
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
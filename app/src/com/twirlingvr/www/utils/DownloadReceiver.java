package com.twirlingvr.www.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.File;

/**
 * Created by MagicBean on 2016/03/14 18:18:21
 */
public class DownloadReceiver extends BroadcastReceiver {
    private DownloadManager downloadManager;

    String path = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            //TODO 判断这个id与之前的id是否相等，如果相等说明是之前的那个要下载的文件
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(id);
            downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            //
//            downloadManager.remove(App.services.get(App.valueGetKey(id)));
            //
            Cursor cursor = downloadManager.query(query);
            int columnCount = cursor.getColumnCount();
            //TODO 这里把所有的列都打印一下，有什么需求，就怎么处理,文件的本地路径就是path
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
            // 如果sdcard不可用时下载下来的文件，那么这里将是一个内容提供者的路径，这里打印出来，有什么需求就怎么样处理  if(path.startsWith("content:")) {
//            if (path.startsWith("content:")) {
//                Logger.i("-----------------------CompleteReceiver 下载完了----路径path = " + path.toString());
//            }
            //TODO 判断这个id与之前的id是否相等，如果相等说明是之前的那个要下载的文件
//            if (id == App.downloadId) {
//                Intent install = new Intent(Intent.ACTION_VIEW);
//                //TODO 转换path路径 否则报解析包错误
//                String uriString = getFilePathFromUri(context, Uri.parse(path));
//                Logger.i("-----------------------CompleteReceiver 转换后----路径uriString = " + uriString);
//                install.setDataAndType(Uri.fromFile(new File(uriString)), Constants.MIME_TYPE);
//                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(install);
//            }
        } else if (action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
            Toast.makeText(context, "点击通知了....", Toast.LENGTH_LONG).show();
            Intent install = new Intent(Intent.ACTION_VIEW);
            //TODO 转换path路径 否则报解析包错误
            String uriString = getFilePathFromUri(context, Uri.parse(path));
            Logger.i("-----------------------CompleteReceiver 转换后----路径uriString = " + uriString);
            install.setDataAndType(Uri.fromFile(new File(uriString)), Constants.MIME_TYPE);
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(install);
        }
    }

    /**
     * 转换 path路径
     */
    public static String getFilePathFromUri(Context c, Uri uri) {
        String filePath = null;
        if ("content".equals(uri.getScheme())) {
            String[] filePathColumn = {MediaStore.MediaColumns.DATA};
            ContentResolver contentResolver = c.getContentResolver();

            Cursor cursor = contentResolver.query(uri, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        } else if ("file".equals(uri.getScheme())) {
            filePath = new File(uri.getPath()).getAbsolutePath();
        }
        return filePath;
    }
}
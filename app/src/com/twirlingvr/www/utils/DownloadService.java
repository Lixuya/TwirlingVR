package com.twirlingvr.www.utils;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.twirlingvr.www.App;

/**
 * Created by MagicBean on 2016/03/14 15:15:56
 */
public class DownloadService extends IntentService {
    private DownloadManager dm;
    private DownloadChangeObserver downloadObserver;
    private long enqueue = 0;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        downloadObserver = new DownloadChangeObserver(null);
//        getContentResolver().registerContentObserver(CONTENT_URI, true, downloadObserver);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("thread", "IntentService 线程：" + Thread.currentThread().getId());
        String url = intent.getStringExtra("url");
        String videoName = intent.getStringExtra("videoName");
        long downloadId = startDownload(url, videoName);
        intent.putExtra("downloadId", downloadId);
        //
        App.services.put(videoName, intent.getLongExtra("downloadId", 0));
//        Message message = new Message();
//        message.obj = intent;
//        message.what = 0;
//        App.getInst().getHandler().sendMessage(message);
    }

    private long startDownload(String url, String videoName) {
        if (!TextUtil.isValidate(url)) return 0;
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setMimeType(Constants.MIME_TYPE);
        request.setDescription("下载..");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES, videoName);
        request.setVisibleInDownloadsUi(true);
        request.allowScanningByMediaScanner();
        enqueue = dm.enqueue(request);
        return dm.enqueue(request);
//      getContentResolver().registerContentObserver(DownloadManager.COLUMN_URI,true,downloadObserver);
    }

    @Override
    public void onDestroy() {
//        getContentResolver().unregisterContentObserver(downloadObserver);
        super.onDestroy();
    }

    class DownloadChangeObserver extends ContentObserver {
        public DownloadChangeObserver(Handler handler) {
            super(handler);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onChange(boolean selfChange) {
//            queryDownloadStatus();
            int[] size = getBytesAndStatus(enqueue);
            if (size != null) {
                Logger.i("progress：" + (size[0] * 100) / size[1] + "%");
            }
        }
    }

    public int[] getBytesAndStatus(long downloadId) {
        int[] bytesAndStatus = new int[]{-1, -1, 0};
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = null;
        try {
            c = dm.query(query);
            if (c != null && c.moveToFirst()) {
                bytesAndStatus[0] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                bytesAndStatus[1] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                bytesAndStatus[2] = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return bytesAndStatus;
    }

    // 查询下载状态
    private void queryDownloadStatus(long enqueue) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(enqueue);
        Cursor c = dm.query(query);
        if (c != null && c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));

            int reasonIdx = c.getColumnIndex(DownloadManager.COLUMN_REASON);
            int titleIdx = c.getColumnIndex(DownloadManager.COLUMN_TITLE);
            int fileSizeIdx = c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
            int bytesDLIdx = c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
            String title = c.getString(titleIdx);
            int fileSize = c.getInt(fileSizeIdx);
            int bytesDL = c.getInt(bytesDLIdx);

            // Translate the pause reason to friendly text.
            int reason = c.getInt(reasonIdx);
            StringBuilder sb = new StringBuilder();
            sb.append(title).append("\n");
            sb.append("Downloaded ").append(bytesDL).append(" / ").append(fileSize);

            // Display the status
            Logger.i("tag" + sb.toString());
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    Logger.i("tag" + "+ STATUS_PAUSED");
                case DownloadManager.STATUS_PENDING:
                    Logger.i("tag" + "STATUS_PENDING");
                case DownloadManager.STATUS_RUNNING:
                    //正在下载，不做任何事情
                    Logger.i("tag" + "STATUS_RUNNING");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    //完成
                    Logger.i("tag" + "下载完成");
//                  dowanloadmanager.remove(lastDownloadId);
                    break;
                case DownloadManager.STATUS_FAILED:
                    //清除已下载的内容，重新下载
                    Logger.i("tag" + "STATUS_FAILED");
                    dm.remove(enqueue);
                    break;
            }
        }
    }
}

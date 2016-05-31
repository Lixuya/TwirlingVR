package com.twirlingvr.www.utils;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.twirlingvr.www.App;

/**
 * Created by MagicBean on 2016/03/14 15:15:56
 */
public class DownloadService extends IntentService {
    private DownloadManager dm;
    private ContentObserver downloadObserver;
    private long downloadId = 0;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downloadObserver = new DownloadChangeObserver(handler);
        getContentResolver().registerContentObserver(Constants.CONTENT_URI, true, downloadObserver);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("thread", "IntentService 线程：" + Thread.currentThread().getId());
        String url = intent.getStringExtra("url");
        String videoName = intent.getStringExtra("videoName");
        downloadId = startDownload(url, videoName);
        ((DownloadChangeObserver) downloadObserver).setDownloadId(downloadId);
        //
        App.services.put(videoName, downloadId);
        App.observer = downloadObserver;
    }

    private long startDownload(String url, String videoName) {
        if (!TextUtil.isValidate(url)) {
            return 0;
        }
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setMimeType(Constants.MIME_TYPE);
        request.setDescription("下载..");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, videoName);
        request.setVisibleInDownloadsUi(true);
        request.allowScanningByMediaScanner();
        downloadId = dm.enqueue(request);
        return downloadId;
    }

    @Override
    public void onDestroy() {
//        getContentResolver().unregisterContentObserver(downloadObserver);
        super.onDestroy();
    }
}

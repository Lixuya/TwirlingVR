package com.twirlingvr.www.utils;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.twirlingvr.www.App;
import com.twirlingvr.www.data.RealmHelper;
import com.twirlingvr.www.model.VideoItem;

/**
 * Created by MagicBean on 2016/03/14 15:15:56
 */
public class DownloadService extends IntentService {
    private DownloadManager dm;
    private ContentObserver downloadObserver;

    public Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            VideoItem videoItem = (VideoItem) msg.obj;
            RealmHelper.getIns().insertVideoItem(videoItem);
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
        VideoItem videoItem = intent.getParcelableExtra("videoItem");
        String videoName = videoItem.getVideoName();
        String url = Constants.PAPH_VIDEO + videoName;
        //
        if (!TextUtil.isValidate(url) || !TextUtil.isValidate(videoName)) {
            return;
        }
        Uri uri = Uri.parse(url);
        long downloadId = startDownload(uri, videoName);
        //
        videoItem.setUpdateTime(System.currentTimeMillis());
        videoItem.setDownloadId(downloadId);
        Message message = new Message();
        message.obj = videoItem;
        handler.sendMessage(message);
        //
        App.observers.put(downloadId, downloadObserver);
        ((DownloadChangeObserver) downloadObserver).setDownloadId(downloadId);
    }

    private long startDownload(Uri uri, String videoName) {
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setMimeType(Constants.MIME_TYPE);
        request.setDescription("下载..");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, videoName);
        request.setVisibleInDownloadsUi(true);
        request.allowScanningByMediaScanner();
        long downloadId = dm.enqueue(request);
        return downloadId;
    }

    @Override
    public void onDestroy() {
        App.observers.remove(downloadObserver);
//        getContentResolver().unregisterContentObserver(downloadObserver);
        super.onDestroy();
    }
}

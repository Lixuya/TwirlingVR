package com.twirling.SDTL.download;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.twirling.SDTL.App;
import com.twirling.SDTL.Constants;
import com.twirling.SDTL.data.RealmHelper;
import com.twirling.SDTL.model.VideoItem;
import com.twirling.libtwirling.utils.TextUtil;

/**
 * Created by MagicBean on 2016/03/14 15:15:56
 */
public class DownloadService extends IntentService {
    private DownloadManager dm;
    private ContentObserver downloadObserver;
    private String mime = "";
    private String name = "";

    public Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    VideoItem videoItem = (VideoItem) msg.obj;
                    RealmHelper.getInstance().insertVideoItem(videoItem);
                    break;
                case 1:
                default:
                    Toast.makeText(getBaseContext(), "请打开手机存储权限", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downloadObserver = new DownloadChangeObserver(handler);
        getContentResolver().registerContentObserver(Uri.parse(Constants.URI_DOWNLOAD), true, downloadObserver);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        VideoItem videoItem = intent.getParcelableExtra("videoItem");
        String videoName = videoItem.getAppAndroidOnline();
        String url = "";
        if (videoItem.getVrAudio() == -1) {
            url = Constants.PATH_RESOURCE + videoItem.getFolder() + Constants.PAPH_VIDEO + videoName;
            mime = Constants.MIME_MP4;
            name = videoName;
        } else {
//        else if (DeviceUtil.getDeviceInfo().deviceBrand.equals("samsung")) {
            url = Constants.PATH_RESOURCE + videoItem.getFolder() + Constants.PATH_DOWNLOAD + videoItem.getAppIOSOffline();
            mime = Constants.MIME_ZIP;
            name = videoItem.getAppAndroidOffline();
        }
//        else {
//            url = Constants.PATH_RESOURCE + videoItem.getFolder() + Constants.PATH_DOWNLOAD + videoItem.getAppAndroidOffline();
//            mime = Constants.MIME_ZIP;
//            name = videoItem.getAppAndroidOffline();
//        }
        //
        if (!TextUtil.isValidate(url) || !TextUtil.isValidate(videoName)) {
            return;
        }
        Uri uri = Uri.parse(url);
        long downloadId = startDownload(uri, videoName);
        //
        videoItem.setDownloadTime(System.currentTimeMillis());
        videoItem.setDownloadId(downloadId);
        Message message = new Message();
        message.obj = videoItem;
        message.what = 0;
        handler.sendMessage(message);
        //
        App.observers.put(downloadId, downloadObserver);
        ((DownloadChangeObserver) downloadObserver).setDownloadId(downloadId);

    }

    private long startDownload(Uri uri, String videoName) {
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setMimeType(mime);
        request.setDescription("下载..");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);
        request.setVisibleInDownloadsUi(true);
        request.allowScanningByMediaScanner();
        long downloadId = 0;
        try {
            downloadId = dm.enqueue(request);
        } catch (Exception e) {
            handler.sendEmptyMessage(1);
        }
        return downloadId;
    }

    @Override
    public void onDestroy() {
        App.observers.remove(downloadObserver);
//        getContentResolver().unregisterContentObserver(downloadObserver);
        super.onDestroy();
    }
}

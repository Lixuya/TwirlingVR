package com.twirling.SDTL.download;

import android.app.DownloadManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;

import com.orhanobut.logger.Logger;
import com.twirling.SDTL.App;

/**
 * Created by 谢秋鹏 on 2016/5/31.
 */
public class DownloadChangeObserver extends ContentObserver {
    private long downloadId = 0;
    private DownloadManager dm = null;

    public DownloadChangeObserver(Handler handler) {
        super(handler);
        dm = (DownloadManager) App.getInst().getApplicationContext().getSystemService(
                App.getInst().getApplicationContext().DOWNLOAD_SERVICE);
    }

    public long getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(long downloadId) {
        this.downloadId = downloadId;
    }

    //
    public interface ProgressListener {
        public void invoke(int progress);
    }

    private ProgressListener listener = null;

    public void setProgressListener(ProgressListener listener) {
        this.listener = listener;
    }

    @Override
    public void onChange(boolean selfChange) {
//            queryDownloadStatus(downloadId);
        int[] size = getBytesAndStatus(downloadId);
        int progress = (int) (((double) size[0] / (double) size[1]) * 100);
        if (size == null || size[0] == -1) {
            return;
        }
//        Log.i("observer", (double) size[0] / (double) size[1] + " " + progress + " " + size[0] + " " + size[1]);
        if (listener != null) {
            listener.invoke(progress);
        }
    }

    //
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
    private void queryDownloadStatus(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
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
                    dm.remove(downloadId);
                    break;
            }
        }
    }
}

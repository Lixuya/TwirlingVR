package com.twirling.SDTL;

import android.os.Environment;

/**
 * Created by 谢秋鹏 on 2016/5/26.
 */
public class Constants {
    public static final String MIME_MP4 = "video/mpeg4";
    public static final String MIME_ZIP = "application/zip";
    //
    public static final String PATH_SERVER = "http://www.twirlingvr.com/";
    // 图片
    public static final String PAPH_IMAGE = PATH_SERVER + "APP_Videos/images/";
    // 在线
    public static final String PAPH_VIDEO = PATH_SERVER + "APP_Videos/videos/";
    // 下载
    public static final String PAPH_DOWNLOAD = PATH_SERVER + "APP_Videos/downloads/";
    //
    public static final String PAPH_DOWNLOAD_LOCAL = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/";
    //
    public static final String URI_DOWNLOAD_LOCAL ="file:///storage/emulated/0/Download/";
    public static final String URI_DOWNLOAD = "content://downloads/my_downloads/";
    public static final String URL_AAC = "http://twirlingvr.com/test/wxyz9.mp4";
}

package com.twirling.SDTL;

import android.os.Environment;

/**
 * Created by 谢秋鹏 on 2016/5/26.
 */
public class Constants {
    public static final String MIME_MP4 = "video/mpeg4";
    public static final String MIME_ZIP = "application/zip";
    //
    public static final String PATH_SERVER_API = "http://yun-dev.twirlingvr.com/Admin";
    public static final String PATH_RESOURCE = "yun-twirlingvr.oss-cn-hangzhou.aliyuncs.com";
    public static final String PATH_JAVA = "http://120.27.136.111:23223/";
    // 图片
    public static final String PAPH_IMAGE = PATH_RESOURCE + "APP_Movies/images";
    // 在线
    public static final String PAPH_VIDEO = PATH_RESOURCE + "APP_Movies/videos";
    // 下载
    public static final String PAPH_DOWNLOAD = PATH_RESOURCE + "APP_Movies/downloads";
    //
    public static final String PAPH_DOWNLOAD_LOCAL = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/";
    //
    public static final String URI_DOWNLOAD_LOCAL ="file:///storage/emulated/0/Download/";
    public static final String URI_DOWNLOAD = "content://downloads/my_downloads/";
//    public static final String URL_AAC = "http://twirlingvr.com/test/wxyz9.mp4";
}

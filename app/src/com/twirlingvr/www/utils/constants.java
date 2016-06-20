package com.twirlingvr.www.utils;

import android.net.Uri;

/**
 * Created by 谢秋鹏 on 2016/5/26.
 */
public class Constants {
    public static final String MIME_MP4 = "video/mpeg4";
    public static final String MIME_ZIP = "application/zip";
    //
    public static final String PATH_SERVER = "http://www.twirlingvr.com/";
    public static final String PAPH_IMAGE = PATH_SERVER + "App_Media/images/";
    public static final String PAPH_VIDEO = PATH_SERVER + "App_Media/videos/";
    //
    public static final Uri URI_DOWNLOAD = Uri.parse("content://downloads/my_downloads/");
    public static final String URI_VIDEO = "file:///storage/emulated/0/Download/";
    public static final String URL_AAC = "http://twirlingvr.com/test/wxyz9.mp4";
}

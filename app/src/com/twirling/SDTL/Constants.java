package com.twirling.SDTL;

import android.os.Environment;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;

/**
 * Created by 谢秋鹏 on 2016/5/26.
 */
public class Constants {
    public static final String MIME_MP4 = "video/mpeg4";
    public static final String MIME_ZIP = "application/zip";
    //
    public static final String PATH_SERVER_API = "http://yun-dev.twirlingvr.com/Admin";
    public static final String PATH_RESOURCE = "http://yun-twirlingvr.oss-cn-hangzhou.aliyuncs.com/";
    public static final String PATH_JAVA = "http://120.27.136.111:23223/";
    // 图片
    public static final String PAPH_IMAGE = "image/";
    // 在线
    public static final String PAPH_VIDEO = "video/";
    // 下载
    public static final String PAPH_DOWNLOAD = "download/";
    public static String USER_MOBILE = "null";//"13661126580"
    public static final String USER_MOBILE_DEFAULT = "null";
    public static FontAwesome.Icon USER_IMAGE = FontAwesome.Icon.faw_user_secret;
    public static final FontAwesome.Icon USER_IMAGE_DEFAULT = FontAwesome.Icon.faw_user_secret;
    //
    public static final String PAPH_DOWNLOAD_LOCAL = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/";
    //
    public static final String URI_DOWNLOAD_LOCAL = "file:///storage/emulated/0/Download/";
    public static final String URI_DOWNLOAD = "content://downloads/my_downloads/";
//    public static final String URL_AAC = "http://twirlingvr.com/test/wxyz9.mp4";
}

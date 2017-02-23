package com.twirling.libtwirling;

import android.os.Environment;

/**
 * Target: 常量
 */
public class Constants {
	// API
	private static final String API_HOST = "http://yun-dev.twirlingvr.com";
	public static final String PATH_SERVER_API = API_HOST + "/Admin/";

	// PATH
	private static final String PAPH_ROOT = Environment.getExternalStorageDirectory() + "";
	public static final String PAPH_IMAGE = "image/";
	public static final String PAPH_VIDEO = "video/";
	public static final String PATH_RESOURCE = "https://yun-twirlingvr.oss-cn-hangzhou.aliyuncs.com/";
	public static final String PATH_JAVA = "http://120.27.136.111:23223/";
	public static final String PATH_DOWNLOAD = PAPH_ROOT + "/" + Environment.DIRECTORY_DOWNLOADS + "/";

	// URI
	public static final String URI_DOWNLOAD_LOCAL = "file:///storage/emulated/0/Download/";
	public static final String URI_DOWNLOAD = "content://downloads/my_downloads/";

	// WX
	public static final String WX_APP_ID = "wx8b5e5c421b626c6c";
	public static final String WX_APP_SECRET = "581e5c6efcbeeb2d759579ade08b39db";
	public static final String WX_APP_GRANT_TYPE = "authorization_code";

	// MIME
	public static final String MIME_MP4 = "video/mpeg4";
	public static final String MIME_ZIP = "application/zip";

	// USER
	public static String USER_MOBILE = "null";//"13661126580"
	public static final String USER_MOBILE_DEFAULT = "null";
}

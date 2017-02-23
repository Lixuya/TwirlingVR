package com.twirling.SDTL.retrofit;

import com.twirling.SDTL.Constants;
import com.twirling.libtwirling.retrofit.RetrofitProvider;

/**
 * Created by 谢秋鹏 on 2016/01/12 16:16:08
 */
public class RetrofitManager {
	public static Api getService() {
		return RetrofitProvider.getInstance(getServerUrl()).create(Api.class);
	}

	private static String getServerUrl() {
		String url = Constants.PATH_SERVER_API;
		if (!url.endsWith("/")) {
			url += "/";
		}
		return url;
	}
}
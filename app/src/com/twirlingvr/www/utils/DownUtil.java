package com.twirlingvr.www.utils;

/**
 * Created by 谢秋鹏 on 2016/5/27.
 */
public class DownUtil {
//    //OkHttpClient请求Client
//    private static final OkHttpClient client = new OkHttpClient();
//
//    //这个是非ui线程回调，不可直接操作UI
//    final ProgressListener progressResponseListener = new ProgressListener() {
//        @Override
//        public void onProgress(long bytesRead, long contentLength, boolean done) {
//            Log.e("TAG", "bytesRead:" + bytesRead);
//            Log.e("TAG", "contentLength:" + contentLength);
//            Log.e("TAG", "done:" + done);
//            if (contentLength != -1) {
//                //长度未知的情况下回返回-1
//                Log.e("TAG", (100 * bytesRead) / contentLength + "% done");
//            }
//            Log.e("TAG", "================================");
//        }
//    };
//
//    //这个是ui线程回调，可直接操作UI
//    final UIProgressListener uiProgressResponseListener = new UIProgressListener() {
//        @Override
//        public void onUIProgress(long bytesRead, long contentLength, boolean done) {
//            Log.e("TAG", "bytesRead:" + bytesRead);
//            Log.e("TAG", "contentLength:" + contentLength);
//            Log.e("TAG", "done:" + done);
//            if (contentLength != -1) {
//                //长度未知的情况下回返回-1
//                Log.e("TAG", (100 * bytesRead) / contentLength + "% done");
//            }
//            Log.e("TAG", "================================");
//            //ui层回调
//            downloadProgeress.setProgress((int) ((100 * bytesRead) / contentLength));
//            //Toast.makeText(getApplicationContext(), bytesRead + " " + contentLength + " " + done, Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        public void onUIStart(long bytesRead, long contentLength, boolean done) {
//            super.onUIStart(bytesRead, contentLength, done);
//            Toast.makeText(getApplicationContext(),"start",Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onUIFinish(long bytesRead, long contentLength, boolean done) {
//            super.onUIFinish(bytesRead, contentLength, done);
//            Toast.makeText(getApplicationContext(),"end",Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    //构造请求
//    final Request request1 = new Request.Builder()
//            .url("http://121.41.119.107:81/test/1.doc")
//            .build();
//
//    //包装Response使其支持进度回调
//    ProgressHelper.addProgressResponseListener(client, uiProgressResponseListener).newCall(request1).enqueue(new Callback() {
//        @Override
//        public void onFailure(Request request, IOException e) {
//            Log.e("TAG", "error ", e);
//        }
//
//        @Override
//        public void onResponse(Response response) throws IOException {
//            Log.e("TAG", response.body().string());
//        }
//    });
}

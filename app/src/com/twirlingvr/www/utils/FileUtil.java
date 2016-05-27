package com.twirlingvr.www.utils;

import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by 谢秋鹏 on 2016/5/27.
 */
public class FileUtil {
    public int contentLength;
    public File file;

    public void down(String _urlStr, String _filePath, ProgressBar mPbLoading) {
        file = new File(_filePath);
        //如果目标文件已经存在，则删除。产生覆盖旧文件的效果
        if (file.exists()) {
            file.delete();
        }
        try {
            // 构造URL
            URL url = new URL(_urlStr);
            // 打开连接
            URLConnection con = url.openConnection();
            //获得文件的长度
            contentLength = con.getContentLength();
            mPbLoading.setMax(contentLength);
            System.out.println("长度 :" + contentLength);
            // 输入流
            InputStream is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            FileOutputStream os = new FileOutputStream(_filePath);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
                mPbLoading.setProgress((int) file.length());
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

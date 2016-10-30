package com.example.administrator.bjnews.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/10/28 0028.
 * 网络缓存工具类
 */
public class NetCacheUtils {

    // 用于构造器
    private final Handler handler;
    private final LocalCacheUtils localCacheUtils;
    private final MemoryCacheUtils memorycacheutils;

    // 网络请求标志位
    public  static final  int SUCESS = 1;   // 请求图片成功
    public  static final  int FAILED = 2;   // 请求图片失败

    public NetCacheUtils(Handler handler, LocalCacheUtils localCacheUtils, MemoryCacheUtils memorycacheutils) {
        this.handler=handler;
        this.localCacheUtils=localCacheUtils;
        this.memorycacheutils=memorycacheutils;
    }

    // 网络请求图片

    public void getBitmapFromNet(String imageUrl, int position) {
        new Thread(new MyRunnable(imageUrl,position)).start();
    }

    class MyRunnable implements Runnable {

        private final String imageUrl;
        private final int position;

        public MyRunnable(String imageUrl, int position) {
            this.imageUrl=imageUrl;
            this.position=position;
        }

        @Override
        public void run() {
           // 联网请求图片(联网要报错，故需要捕获)
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(imageUrl).openConnection();
                connection.setRequestMethod("GET"); // GET一定要大写，不然死活请求不了，要么就不写
                connection.setConnectTimeout(5000); // 超时5s
                connection.setReadTimeout(5000);    // 读取超时
                connection.connect();
                int code = connection.getResponseCode();
                if (code==200) {
                    // 联网成功(得到的是一个输入流)
                    InputStream is = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is); // 得到图片

                    Message msg=Message.obtain();
                    msg.what=SUCESS;
                    msg.obj=bitmap; // 携带图片
                    msg.arg1=position;
                    handler.sendMessage(msg);

                    is.close();

                    // 备份到内存
                    memorycacheutils.putBitmap(imageUrl,bitmap);
                    // 备份到本地
                    localCacheUtils.putBitmap(imageUrl,bitmap);
                }

            } catch (IOException e) {   // 联网失败

                e.printStackTrace();
                Message msg=Message.obtain();
                msg.what=FAILED;
                msg.arg1=position;
                handler.sendMessage(msg);
            }
        }
    }
}

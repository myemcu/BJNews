package com.example.administrator.bjnews.utils;

import android.graphics.Bitmap;
import android.os.Handler;

/**
 * Created by Administrator on 2016/10/28 0028.
 * 自定义三级缓存工具类
 */
public class BitmapUtils {

    private NetCacheUtils netCacheUtils;    // 定义网络缓存对象
    /*
    * 三级缓存原理：
    * 从内存中取图片
    * 从本地取图片(保存一份到内存)
    * 从网络取图片(保存一份到内存，保存一份到本地)
    * */


    public BitmapUtils(Handler handler) {
        netCacheUtils=new NetCacheUtils(handler);
    }

    public Bitmap getBitmapFromUrl(String imageUrl, int position) {
        // 从内存取

        // 从本地取

        // 从网络取(不能立刻得到图片，通过Handler发就好了)
        netCacheUtils.getBitmapFromNet(imageUrl,position);


        return null;
    }
}

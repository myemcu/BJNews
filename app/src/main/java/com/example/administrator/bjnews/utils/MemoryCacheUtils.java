package com.example.administrator.bjnews.utils;

import android.graphics.Bitmap;

import org.xutils.cache.LruCache;

/**
 * Created by Administrator on 2016/10/31 0031.
 * 内存缓存工具类
 */
public class MemoryCacheUtils {

    private LruCache<String, Bitmap> lruCache;

    public MemoryCacheUtils() {
        // 设置图片使用的内存空间大小
        int maxSize = ((int) Runtime.getRuntime().maxMemory()/1024)/8;
        lruCache = new LruCache<String,Bitmap>(maxSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {// 计算每一张图片的大小
                return (value.getRowBytes() * value.getHeight())/1024;
            }
        };

    }

    // 根据图片Url，保存图片到内存中。
    public void putBitmap(String imagerUrl, Bitmap bitmap) {
        lruCache.put(imagerUrl, bitmap);
    }

    // 根据图片Url，从内存中获取图片。
    public Bitmap getBitmap(String imageUrl) {
        return lruCache.get(imageUrl);
    }
}

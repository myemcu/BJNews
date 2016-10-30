package com.example.administrator.bjnews.utils;

import android.graphics.Bitmap;
import android.os.Handler;

/**
 * Created by Administrator on 2016/10/28 0028.
 * 自定义三级缓存工具类
 */
public class BitmapUtils {

    private NetCacheUtils netCacheUtils;        // 网络缓存(实质上就是单纯的网络加载，有网有图片)
    private LocalCacheUtils localCacheUtils;    // 本地缓存(缓存到本地的机身SD卡中)
    private MemoryCacheUtils memorycacheutils;  // 内存缓存(运行前，先将本地数据清除)


    /*
    * 三级缓存原理：
    * 从内存中取图片
    * 从本地取图片(保存一份到内存)
    * 从网络取图片(保存一份到内存，保存一份到本地)
    * */


    public BitmapUtils(Handler handler) {
        memorycacheutils = new MemoryCacheUtils();
        localCacheUtils = new LocalCacheUtils(memorycacheutils);    // 本地缓存
        netCacheUtils=new NetCacheUtils(handler,localCacheUtils,memorycacheutils);   // 创建网络缓存
    }

    public Bitmap getBitmapFromUrl(String imageUrl, int position) {
        // 从内存取(运行前，先将本地数据清除)
        if (memorycacheutils!=null) {
            Bitmap bitmap =  memorycacheutils.getBitmap(imageUrl);
            if (bitmap!=null) {
                LogUtil.e("内存缓存的图片"+position);
                return bitmap;
            }
        }

        // 从本地取
        if (localCacheUtils!=null) {
            Bitmap bitmap =  localCacheUtils.getBitmap(imageUrl);
            if (bitmap!=null) {
                LogUtil.e("本地缓存的图片"+position);
                return bitmap;
            }
        }

        // 从网络取(不能立刻得到图片，通过Handler发就好了)
        netCacheUtils.getBitmapFromNet(imageUrl,position);

        return null;
    }
}

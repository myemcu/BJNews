package com.example.administrator.bjnews.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.administrator.bjnews.GuideActivity;
import com.example.administrator.bjnews.SplashActivity;

/**
 * Created by Administrator on 2016/9/7 0007.
 * 保存软件保存参数
 */
public class CacheUtil {
    // 设置软件保存参数
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("myemcu",Context.MODE_PRIVATE); // 创建数据持久化对象
        sp.edit().putBoolean(key,value).commit();
    }

    // 获取软件保存参数
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("myemcu",Context.MODE_PRIVATE); // 创建数据持久化对象

        return sp.getBoolean(key,false);
    }
}

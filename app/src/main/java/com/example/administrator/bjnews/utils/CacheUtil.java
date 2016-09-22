package com.example.administrator.bjnews.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.administrator.bjnews.GuideActivity;
import com.example.administrator.bjnews.SplashActivity;

/**
 * Created by Administrator on 2016/9/7 0007.
 * 保存软件参数
 */
public class CacheUtil {
    // 设置软件保存参数
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("myemcu",Context.MODE_PRIVATE); // 创建数据持久化对象
        sp.edit().putBoolean(key,value).commit();
    }

    // 获取软件参数
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("myemcu",Context.MODE_PRIVATE); // 创建数据持久化对象

        return sp.getBoolean(key,false);
    }

    // 设置数据缓存
    public static void putString(Context context, String key, String values) {
        SharedPreferences sp = context.getSharedPreferences("myemcu",Context.MODE_PRIVATE); // 创建数据持久化对象
        sp.edit().putString(key,values).commit();
    }

    // 获取数据缓存
    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("myemcu",Context.MODE_PRIVATE); // 创建数据持久化对象
        return sp.getString(key,"");    // 若key没去到，则返回""(空字符串)
    }
}

package com.example.administrator.bjnews;

import android.app.Application;

import com.example.administrator.bjnews.volley.VolleyManager;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/9/12 0012.
 */
public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        x.Ext.init(this);       // 初始化xUtils3(使用其联网请求功能)
        x.Ext.setDebug(true);

        VolleyManager.init(this);

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
    }
}

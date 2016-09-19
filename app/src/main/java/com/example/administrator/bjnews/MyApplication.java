package com.example.administrator.bjnews;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 2016/9/12 0012.
 */
public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        x.Ext.init(this);       // 初始化xUtils3(使用其联网请求功能)
        x.Ext.setDebug(true);
    }
}

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

        x.Ext.init(this);
        x.Ext.setDebug(true);
    }
}

package com.example.administrator.bjnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.example.administrator.bjnews.base.BasePager;

/**
 * Created by Administrator on 2016/9/12 0012.
 * 智慧服务
 */
public class SmartServicePager extends BasePager {

    private TextView txt;

    public SmartServicePager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        System.out.println("智慧服务的数据被初始化了....");

        // 设置标题
        tv_title.setText("智慧服务");

        // 设置内容(子视图)
        txt = new TextView(context); // 在当前页创建一个TextView文本
        txt.setGravity(Gravity.CENTER);
        txt.setText("智慧服务的内容");
        txt.setTextSize(25);
        txt.setTextColor(Color.RED);

        fl_base_content.addView(txt);   // 把子视图添加到FrameLayout中
    }
}

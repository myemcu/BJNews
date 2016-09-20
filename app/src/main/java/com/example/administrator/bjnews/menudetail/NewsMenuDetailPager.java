package com.example.administrator.bjnews.menudetail;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.bjnews.base.MenuDetailBasePager;

/**
 * Created by Administrator on 2016/9/20 0020.
 * 新闻菜单详情页面
 */

public class NewsMenuDetailPager extends MenuDetailBasePager{

    private TextView txt;

    public NewsMenuDetailPager(Context context) {
        super(context);
    }

    @Override

    public View initView() {    // 视图
        // 设置内容(子视图)
        txt = new TextView(context); // 在当前页创建一个TextView文本
        txt.setGravity(Gravity.CENTER);
        txt.setText("新闻菜单详情页面");
        txt.setTextSize(25);
        txt.setTextColor(Color.RED);

        return txt;
    }

    @Override

    public void initData() {    // 数据
        super.initData();
        txt.setText("新闻菜单详情页面");
        System.out.println("新闻菜单详情页面的数据初始化了..");
    }
}

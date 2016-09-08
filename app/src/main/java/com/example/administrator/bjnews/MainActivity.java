package com.example.administrator.bjnews;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.util.DebugUtils;
import android.view.Gravity;
import android.widget.TextView;


import com.example.administrator.bjnews.utils.DensityUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Created by Administrator on 2016/9/7 0007.
 * 主页面
 */
public class MainActivity extends SlidingFragmentActivity{  // 继承开源项目SlidingMenu-master中的所需Activity
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*TextView txt = new TextView(this);
        txt.setText("欢迎来到主页面——北京新闻客户端");
        txt.setGravity(Gravity.CENTER);
        txt.setTextSize(20);
        txt.setTextColor(0xcd0000cd);
        setContentView(txt);*/

        setContentView(R.layout.content);                               // 设置主页面(帧布局，常与Fragment配合使用)
        setBehindContentView(R.layout.leftmenu);                        // 设置左侧菜单方法
        SlidingMenu slidingMenu = getSlidingMenu();                     // 设置模式(左，右，左右)
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);    // 设置边沿滑动
        slidingMenu.setBehindOffset(DensityUtil.dip2px(this,200));      // 代码中操作像素均这样写
    }
}

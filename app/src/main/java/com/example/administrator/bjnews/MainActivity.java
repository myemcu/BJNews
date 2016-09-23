package com.example.administrator.bjnews;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;


import com.example.administrator.bjnews.fragment.ContentFragment;
import com.example.administrator.bjnews.fragment.LeftMenuFragment;
import com.example.administrator.bjnews.utils.DensityUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Created by Administrator on 2016/9/7 0007.
 * 主页面
 */
public class MainActivity extends SlidingFragmentActivity{


    public SlidingMenu SlidingMenu;                                        // 定义侧滑菜单对象

    public static final String LEFTMENU_TAG = "leftmenu_tag";                 // static意思是不需要实例化而直接调用
    public static final String MAIN_TAG = "main_tag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);  // 设置隐藏标题
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content);                                   // 设置主页面(帧布局，常与Fragment配合使用)
        setBehindContentView(R.layout.leftmenu);                            // 设置侧滑菜单页面

        SlidingMenu = getSlidingMenu();                                     // 创建侧滑菜单对象
        SlidingMenu.setMode(SlidingMenu.LEFT);                              // 设置模式(左，右，左右)
        SlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);    // 设置边沿滑动
        SlidingMenu.setBehindOffset(DensityUtil.dip2px(this,200));          // 代码中操作像素均这样写

        /*将写好的Fragment(侧滑Fragment，主页Fragment)，加到MainActivity中*/
        initFragment();
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();                   // 创建Fragment管理器对象(得到FragmentManager)
        FragmentTransaction ft = fm.beginTransaction();                     // 创建Fragment传输对象(开启事务)
        ft.replace(R.id.fl_leftmenu,new LeftMenuFragment(), LEFTMENU_TAG);  // Ctrl+Alt+C抽取String(替换Fragment)
        ft.replace(R.id.fl_main,new ContentFragment(), MAIN_TAG);           // 用replace(把老的删除再add，优化性能)而不用add(像图片轮播那种，一个图片就是一个Fragment，故用add)
        ft.commit();                                                        // 事物提交
    }

    // 得到左侧菜单Fragment(通过获取对应Tag，从而获得对应Fragment)
    public LeftMenuFragment getLeftMenuFragment() {
        FragmentManager fm = getSupportFragmentManager();
        LeftMenuFragment leftMenuFragment = (LeftMenuFragment) fm.findFragmentByTag(LEFTMENU_TAG);
        return leftMenuFragment;
    }

    // 得到正文Fragment(通过获取对应Tag，从而获得对应Fragment)
    public ContentFragment getContentFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment contentFragment = (ContentFragment) fm.findFragmentByTag(MAIN_TAG);
        return contentFragment;
    }
}

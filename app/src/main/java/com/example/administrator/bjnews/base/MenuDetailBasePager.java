package com.example.administrator.bjnews.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.administrator.bjnews.MainActivity;
import com.example.administrator.bjnews.R;

/**
 * Created by Administrator on 2016/9/12 0012.
 * 左侧菜单(新闻，专题，组图，互动)基类
 */
public abstract class MenuDetailBasePager {

    public final Context context;

    /*代表各个页面*/
    public View rootView;

    public MenuDetailBasePager(Context context) { // 传入上下文
        this.context=context;
        rootView = initView();
    }

    // 初始化视图(抽象方法)
    public abstract View initView() ;

    // 当孩子需要初始化数据时，重写该方法，并在适当的时候调用。
    public void initData() {

    }
}

package com.example.administrator.bjnews.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.administrator.bjnews.R;

/**
 * Created by Administrator on 2016/9/12 0012.
 * 公共页/基类
 */
public class BasePager {

    public final Context context;

    public TextView     tv_title;
    public ImageButton  ib_menu;
    public FrameLayout  fl_base_content;

    /*代表各个页面*/
    public View rootView;

    public BasePager(Context context) { // 传入上下文
        this.context=context;
        rootView = initView();
    }

    private View initView() {

        View view = View.inflate(context,R.layout.base_pager,null);                 // 加载一个布局文件

        // 对这个布局中的控件进行实例化
        tv_title = (TextView) view.findViewById(R.id.tv_title);                     // 标题栏文字
        ib_menu = (ImageButton) view.findViewById(R.id.ib_menu);                    // 标题栏图片按钮
        fl_base_content = (FrameLayout) view.findViewById(R.id.fl_base_content);    // 帧布局

        return view;
    }

    // 当孩子需要初始化数据时，重写该方法，并在适当的时候调用。
    public void initData() {

    }
}

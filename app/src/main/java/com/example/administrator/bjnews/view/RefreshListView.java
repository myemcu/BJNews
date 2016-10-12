package com.example.administrator.bjnews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.bjnews.R;

/**
 * Created by Administrator on 2016/10/11 0011.
 * 自定义用于下拉刷新的ListView
 */

public class RefreshListView extends ListView{

    private LinearLayout headerView;    // 自定义一个视图(包含下拉刷新控件和顶部轮播图)

    private View ll_pulldown_refresh;   // 下拉刷新控件
    private ImageView iv_header_arrow;
    private ProgressBar pb_header_status;
    private TextView tv_header_status;
    private TextView tv_header_time;

    private int refreshHeight; // 下拉刷新控件的高

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initHeaderView(context);    // 初始化下拉刷新视图
    }

    // 初始化下拉刷新视图
    private void initHeaderView(Context context) {

        headerView = (LinearLayout) View.inflate(context, R.layout.refresh_header, null);   // 下拉刷新的列表

        ll_pulldown_refresh =  headerView.findViewById(R.id.ll_pulldown_refresh);           // 下拉刷新的布局
        iv_header_arrow = (ImageView) headerView.findViewById(R.id.iv_header_arrow);        // 向上的红色箭头
        pb_header_status = (ProgressBar) headerView.findViewById(R.id.pb_header_status);    // 环形进度条
        tv_header_status = (TextView) headerView.findViewById(R.id.tv_header_status);       // 上方文字
        tv_header_time = (TextView) headerView.findViewById(R.id.tv_header_time);           // 下方文字

//        View.setPading(0,-控件高度,0,0);	// 完全隐藏
//        View.setPading(0,0,0,0);			// 完全显示
//        View.setPading(0, 控件高度,0,0);	// 两倍显示

        ll_pulldown_refresh.measure(0,0);                           // 调用系统方法测量
        refreshHeight = ll_pulldown_refresh.getMeasuredHeight();    // 获取测量值
        ll_pulldown_refresh.setPadding(0,-refreshHeight,0,0);       // 隐藏显示(默认)
//        ll_pulldown_refresh.setPadding(0,0,0,0);                    // 完整显示
//        ll_pulldown_refresh.setPadding(0,refreshHeight,0,0);        // 两倍显示

        // 以头的方式加载进入
        addHeaderView(headerView);
    }
}

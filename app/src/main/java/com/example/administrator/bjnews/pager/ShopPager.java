package com.example.administrator.bjnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.example.administrator.bjnews.R;
import com.example.administrator.bjnews.base.BasePager;

/**
 * Created by Administrator on 2016/9/12 0012.
 * 商城热卖
 */
public class ShopPager extends BasePager {

    /*private TextView txt;*/

    private MaterialRefreshLayout refresh_layout; // 下拉刷新布局
    private RecyclerView recycler_view;
    private ProgressBar pb_loading;


    public ShopPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        System.out.println("商城热卖的数据被初始化了....");

        /*// 设置内容(子视图)
        txt = new TextView(context); // 在当前页创建一个TextView文本
        txt.setGravity(Gravity.CENTER);
        txt.setText("商城热卖的内容");
        txt.setTextSize(25);
        txt.setTextColor(Color.RED);*/

        // 1 设置标题
        tv_title.setText("商城热卖");
        // 2 创建视图
        View view = View.inflate(context, R.layout.shop_hot_sell,null);
        refresh_layout  = (MaterialRefreshLayout)   view.findViewById(R.id.refresh_layout);
        recycler_view   = (RecyclerView)            view.findViewById(R.id.recycler_view);
        pb_loading      = (ProgressBar)             view.findViewById(R.id.pb_loading);

        // 3 添加view到FrameLayout中
        fl_base_content.addView(view);
    }
}

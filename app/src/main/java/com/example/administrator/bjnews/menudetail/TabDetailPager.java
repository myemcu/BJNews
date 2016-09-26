package com.example.administrator.bjnews.menudetail;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.bjnews.R;
import com.example.administrator.bjnews.base.MenuDetailBasePager;
import com.example.administrator.bjnews.bean.NewsCenterBean_Hand;
import com.example.administrator.bjnews.utils.Url;

/**
 * Created by Administrator on 2016/9/23 0023.
 * 页签详情页面
 */

public class TabDetailPager extends MenuDetailBasePager {

    private String url;

    private final NewsCenterBean_Hand.NewsCenterBean_Data.Children_Data childrenDatas;
//    private TextView txt;

    // 构造器
    public TabDetailPager(Context context, NewsCenterBean_Hand.NewsCenterBean_Data.Children_Data data) {
        super(context);
        this.childrenDatas=data;
    }

    @Override
    public View initView() {    // 视图

        View view = View.inflate(context, R.layout.tabdetail_pager,null);

        return view;
    }

    @Override
    public void initData() {    // 数据
        super.initData();
        System.out.println("---------"+childrenDatas.getTitle());
        url= Url.BASE_URL+childrenDatas.getUrl();
        System.out.println("url==="+url);
    }
}

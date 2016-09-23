package com.example.administrator.bjnews.menudetail;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.bjnews.R;
import com.example.administrator.bjnews.base.MenuDetailBasePager;
import com.example.administrator.bjnews.bean.NewsCenterBean_Hand;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/20 0020.
 * 新闻菜单详情页面
 */

public class NewsMenuDetailPager extends MenuDetailBasePager{

    // 左侧菜单对应的新闻详情页面-的-页签页面数据
    private List<NewsCenterBean_Hand.NewsCenterBean_Data.Children_Data> childrenDatas;

    // 左侧菜单对应的新闻详情页面-的-页签页面集合
    private ArrayList<MenuDetailBasePager> detailBasePagers;

    // 实例化完成后，ViewPager的布局搞定
    @ViewInject(R.id.vp_news_menu_detailpager)
    private ViewPager vp_news_menu_detailpager;

    private TabDetailPager tabDetailpager;          // 单页签页面
    private MenuDetailBasePager detailBasePager;

    // ViewPager使用:
    //              (1) 加载布局文件(initView())
    //              (2) 在代码中实例(initView())
    //              (3) 准备ViewPager页面
    //              (4) 设置适配器

    // 构造器(Ctrl+B，查看在哪个地方调用它)
    public NewsMenuDetailPager(Context context, NewsCenterBean_Hand.NewsCenterBean_Data data) {   // Ctrl+B 查看哪个地方调用它
        super(context);

        childrenDatas = data.getChildren(); // 获取data[0]根，中的子children集合
    }

    @Override
    public View initView() {    // 视图

        View view = View.inflate(context, R.layout.news_menu_detail_pager,null);    // (1) 在新闻详情页面中动态加载布局文件
        x.view().inject(this,view);                                                 // (2) 准备用注解的方式对加载页中的ViewPager进行实例化

        return view;
    }

    @Override
    public void initData() {    // 数据
        super.initData();
        System.out.println("新闻菜单详情页面的数据初始化了..");

        // (3) 准备ViewPager的页面(集合形式)
        detailBasePagers = new ArrayList<>();

        for (int i=0;i<childrenDatas.size();i++) {
            tabDetailpager = new TabDetailPager(context,childrenDatas.get(i)); // 单页签页面
            detailBasePagers.add(tabDetailpager); // 页签页面集合<--单页签页面
        }

        // (4) 适配：整合(页面、数据)
        vp_news_menu_detailpager.setAdapter(new NewsMenuDetailPagerAdapter());
    }
//==========================================================================================
    private class NewsMenuDetailPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return detailBasePagers.size(); // 集合大小
        }
    //-----------------------------------------------------------------------------------
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    //-----------------------------------------------------------------------------------
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            detailBasePager=detailBasePagers.get(position);
            View rootView = detailBasePager.rootView;
            detailBasePager.initData();
            container.addView(rootView);

            return rootView;
        }
    //-----------------------------------------------------------------------------------
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}

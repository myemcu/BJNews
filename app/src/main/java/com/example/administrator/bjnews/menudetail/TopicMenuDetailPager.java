package com.example.administrator.bjnews.menudetail;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.bjnews.MainActivity;
import com.example.administrator.bjnews.R;
import com.example.administrator.bjnews.base.MenuDetailBasePager;
import com.example.administrator.bjnews.bean.NewsCenterBean_Hand;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/20 0020.
 * 专题菜单详情页面
 */

public class TopicMenuDetailPager extends MenuDetailBasePager{

    /*private TextView txt;

    public TopicMenuDetailPager(Context context) {
        super(context);
    }

    @Override

    public View initView() {    // 视图
        // 设置内容(子视图)
        txt = new TextView(context); // 在当前页创建一个TextView文本
        txt.setGravity(Gravity.CENTER);
        txt.setText("专题菜单详情页面");
        txt.setTextSize(25);
        txt.setTextColor(Color.RED);

        return txt;
    }

    @Override

    public void initData() {    // 数据
        super.initData();
        txt.setText("专题菜单详情页面");
        System.out.println("专题菜单详情页面的数据初始化了..");
    }*/

    // 左侧菜单对应的专题详情页面-的-页签页面数据
    private List<NewsCenterBean_Hand.NewsCenterBean_Data.Children_Data> childrenDatas;

    // 左侧菜单对应的专题详情页面-的-页签页面集合
    private ArrayList<MenuDetailBasePager> detailBasePagers;

    @ViewInject(R.id.tabpage_indicator)
//    private TabPageIndicator tabpage_indicator;
    private TabLayout tabpage_indicator;

    // 实例化完成后，ViewPager的布局搞定
    @ViewInject(R.id.vp_news_menu_detailpager)
    private ViewPager vp_news_menu_detailpager;

    private TopicTabDetailPager topicTabDetailPager;          // 单页签页面
    private MenuDetailBasePager detailBasePager;
    private NewsMenuDetailPagerAdapter adapter;

    // ViewPager使用:
    //              (1) 加载布局文件(initView())
    //              (2) 在代码中实例(initView())
    //              (3) 准备ViewPager页面
    //              (4) 设置适配器

    // 构造器(Ctrl+B，查看在哪个地方调用它)
    public TopicMenuDetailPager(Context context, NewsCenterBean_Hand.NewsCenterBean_Data data) {   // Ctrl+B 查看哪个地方调用它
        super(context);

        childrenDatas = data.getChildren(); // 获取data[0]根，中的子children集合
    }

    @Override
    public View initView() {    // 视图

        View view = View.inflate(context, R.layout.topic_news_menu_detail_pager,null);    // (1) 在专题详情页面中动态加载布局文件
        x.view().inject(this,view);                                                 // (2) 准备用注解的方式对加载页中的ViewPager进行实例化

        return view;
    }

    // 指示器右边图片按钮">"的点击事件
    @Event(value = R.id.ib_next_tab)
    private void nextTab(View view) {
        // 切换到下一ViewPager页面
        vp_news_menu_detailpager.setCurrentItem(vp_news_menu_detailpager.getCurrentItem()+1);
    }

    @Override
    public void initData() {    // 数据
        super.initData();
        System.out.println("专题菜单详情页面的数据初始化了..");

        // (3) 准备ViewPager的页面(集合形式)
        detailBasePagers = new ArrayList<>();

        for (int i=0;i<childrenDatas.size();i++) {
            topicTabDetailPager = new TopicTabDetailPager(context,childrenDatas.get(i)); // 单页签页面
            detailBasePagers.add(topicTabDetailPager); // 页签页面集合<--单页签页面
        }

        adapter = new NewsMenuDetailPagerAdapter();
        // (4) 适配：整合(页面、数据)
        vp_news_menu_detailpager.setAdapter(adapter);

        // 关联ViewPager，使Indicator得以显示
//        tabpage_indicator.setViewPager(vp_news_menu_detailpager);
        tabpage_indicator.setupWithViewPager(vp_news_menu_detailpager);

        // 设为滚动模式
        tabpage_indicator.setTabMode(TabLayout.MODE_SCROLLABLE);

        // 使用TabPageIndicator实现vp页面监听
//        tabpage_indicator.setOnPageChangeListener(new MyOnPageChangeListener());
        vp_news_menu_detailpager.setOnPageChangeListener(new MyOnPageChangeListener());

        // 此处注掉后，对TabLayout的效果更好
        /*for (int i = 0; i < tabpage_indicator.getTabCount(); i++) {
            TabLayout.Tab tab = tabpage_indicator.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }*/
    }

    // 是否显示侧滑菜单
    private void isEnableSlidingMenu(boolean isEnableSlidingMenu) {

        MainActivity mainActivity = (MainActivity) context;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();

        if (isEnableSlidingMenu) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }
        else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {  // 相关代码在ContentFragment中
            if (position==0) {  // 开启侧滑
                isEnableSlidingMenu(true);
            }else {             // 关闭侧滑
                isEnableSlidingMenu(false);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    //==========================================================================================
    private class NewsMenuDetailPagerAdapter extends PagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            return childrenDatas.get(position).getTitle();
        }

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
        //----------------------------------------------------------------------------------
        public View getTabView(int position){
            View view = LayoutInflater.from(context).inflate(R.layout.tab_item, null);
            TextView tv= (TextView) view.findViewById(R.id.textView);
            tv.setText(childrenDatas.get(position).getTitle());
            ImageView img = (ImageView) view.findViewById(R.id.imageView);
            img.setImageResource(R.drawable.dot_focus);
            return view;
            }

        //-----------------------------------------------------------------------------------
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}

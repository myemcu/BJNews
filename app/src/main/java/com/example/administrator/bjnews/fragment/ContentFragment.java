package com.example.administrator.bjnews.fragment;


import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import com.example.administrator.bjnews.MainActivity;
import com.example.administrator.bjnews.R;
import com.example.administrator.bjnews.adapter.ContentFragmentAdapter;
import com.example.administrator.bjnews.base.BaseFragment;
import com.example.administrator.bjnews.base.BasePager;
import com.example.administrator.bjnews.pager.ShoppingCartPager;
import com.example.administrator.bjnews.pager.HomePager;
import com.example.administrator.bjnews.pager.NewsCenterPager;
import com.example.administrator.bjnews.pager.SettingPager;
import com.example.administrator.bjnews.pager.ShopPager;
import com.example.administrator.bjnews.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

// 说明：新类继承父类，就是要实现父类中的相关方法，其中抽象方法必须实现，其它方法根据需要选择实现。

/**
 * Created by Administrator on 2016/9/8 0008.
 * 主页Fragment
 */
public class ContentFragment extends BaseFragment{

    @ViewInject(R.id.vp_content)
    private NoScrollViewPager vp_content;

    @ViewInject(R.id.rg_bottom_tag)
    private RadioGroup rg_bottom_tag;

    private static final String TAG = ContentFragment.class.getSimpleName();

    //  各页面集合
    private ArrayList<BasePager> basePagers; // 以BasePager为中心构建集合

    @Override
    public View initView() {

        View view = View.inflate(context,R.layout.content_fragment,null);   // inflate将一个xml中定义的布局控件找出来

        //使用xUtils3实现注解，以方便实例化
        x.view().inject(this,view); // this:ContentFragment.java，view:content_fragment.xml(把当前XML中的视图注入到当前的java中)

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.i(TAG,"主页数据初始化完成..");

        /*ViewPager的使用：
        *               1 在xml中定义
        *               2 在java中实例化
        *               3 准备数据(集合)
        *               4 设置适配器*/

        basePagers = new ArrayList<>();                         // 创建集合对象
        basePagers.add(new HomePager(context));                 // 首页
        basePagers.add(new NewsCenterPager(context));           // 新闻中心
        basePagers.add(new ShopPager(context));                 // 商城热卖
        basePagers.add(new ShoppingCartPager(context));            // 政要指南
        basePagers.add(new SettingPager(context));              // 设置

        ContentFragmentAdapter contentFragmentAdapter = new ContentFragmentAdapter(basePagers);
        vp_content.setAdapter(contentFragmentAdapter);    // 设置ViewPager适配器

        // 默认选中首页
        rg_bottom_tag.check(R.id.rb_home);
        // 设置RadioGroup状态监听
        rg_bottom_tag.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        // 监听页面的改变
        vp_content.addOnPageChangeListener(new MyOnPageChangeListene());
        basePagers.get(0).initData();   // 默认选择显示首页与加载首页数据
        isEnableSlidingMenu(false);     // 默认首页无侧滑菜单
    }

    // 得到新闻中心
    public NewsCenterPager getNewsCenterPager() {
        return (NewsCenterPager) basePagers.get(1);
    }

    private class MyOnPageChangeListene implements ViewPager.OnPageChangeListener {

        @Override
        // 页面滑动
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        // 所选页面
        public void onPageSelected(int position) {
            basePagers.get(position).initData();    // 获取指定页面的初始化数据
        }

        @Override
        // 页面滑动状态切换
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        // 根据所选的RadioButton，切换到指定ViewPager，如：vp_content.setCurrentItem(0);
        // false：关闭动画效果

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {

                case R.id.rb_home:          // 首页
                        vp_content.setCurrentItem(0,false);
                        isEnableSlidingMenu(false);
                     break;

                case R.id.rb_newscenter:    // 新闻中心
                        vp_content.setCurrentItem(1,false);
                        isEnableSlidingMenu(true);
                    break;

                case R.id.rb_smartservices: // 智慧服务
                        vp_content.setCurrentItem(2,false);
                        isEnableSlidingMenu(false);
                    break;

                case R.id.rb_govaffair:     // 政要指南
                        vp_content.setCurrentItem(3,false);
                        isEnableSlidingMenu(false);
                    break;

                case R.id.rb_setting:       // 设置
                        vp_content.setCurrentItem(4,false);
                        isEnableSlidingMenu(false);
                    break;

                default:break;
            }
        }
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
}

package com.example.administrator.bjnews.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.bjnews.base.BasePager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/9 0009.
 */
public class ContentFragmentAdapter extends PagerAdapter{

    private final ArrayList<BasePager> pagers;

    // 构造器
    public ContentFragmentAdapter(ArrayList<BasePager> pagers) {
        this.pagers=pagers;
    }

    // 必须实现
    @Override
    public int getCount() {
        return pagers.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    // 还需实现

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        BasePager basePager = pagers.get(position);
        View rootView = basePager.rootView;

        // 注释掉这个是为了处理错误的数据预加载(e.g:选择某RadioButton，出现下一个页面数据)
        // 带来的新问题是ViewPager切换不动了。
        // basePager.initData();   // 调各个页面的initData();
        // 解决之道：监听页面的改变

        container.addView(rootView);

        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View) object);
    }
}

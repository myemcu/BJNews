package com.example.administrator.bjnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/9/13 0013.
 * 自定义不可滑动的ViewPager(销毁触摸事件)
 */
public class NoScrollViewPager extends ViewPager{

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
}

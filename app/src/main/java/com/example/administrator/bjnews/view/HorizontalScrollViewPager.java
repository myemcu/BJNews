package com.example.administrator.bjnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/9/27 0027.
 */

public class HorizontalScrollViewPager extends ViewPager {

    private float startX,startY;        // 起点
    private float endX,endY;            // 终点
    private float distanceX,distanceY;  // 偏移

    public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {

            // 按下
            case MotionEvent.ACTION_DOWN:
                    getParent().requestDisallowInterceptTouchEvent(true);   // 拦截自己
                    // 1 记录第一次按下的坐标
                    startX=ev.getX();
                    startY=ev.getY();
                    break;

            // 滑动
            case MotionEvent.ACTION_MOVE:
                    // 2 去到新的坐标
                    endX=ev.getX();
                    endY=ev.getY();
                    // 3 偏移量计算
                    distanceX=endX-startX;
                    distanceY=endY-startY;
                    // 4 判断滑动方向
                    if (Math.abs(distanceX) > Math.abs(distanceY)) {// 水平方向
                        if (getCurrentItem()==0 && distanceX>0) {// 首项且为后滑
                            getParent().requestDisallowInterceptTouchEvent(false); // 不反拦截
                        }
                        else if ((getCurrentItem()==getAdapter().getCount()-1) && distanceX<0) { // 末项且为左滑
                            getParent().requestDisallowInterceptTouchEvent(false); // 不反拦截
                        }
                        else
                            getParent().requestDisallowInterceptTouchEvent(true); // 反拦截
                    }else {// 竖直方向
                        getParent().requestDisallowInterceptTouchEvent(false); // 不反拦截
                    }
                    break;

            // 释放
            case MotionEvent.ACTION_UP:
                break;
        }


        return super.dispatchTouchEvent(ev);
    }
}

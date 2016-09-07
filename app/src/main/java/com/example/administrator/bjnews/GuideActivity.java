package com.example.administrator.bjnews;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * 创建引导页面 on 2016/9/6 0006.
 */

public class GuideActivity extends Activity{

    private static final String TAG = GuideActivity.class.getSimpleName();
    // 实例化所对应的XML控件
    private ViewPager viewpager_guide;          // ViewPager-ID
    private Button btn_start_main;
    private LinearLayout ll_point_group;

    private ArrayList<ImageView> imageViews;    // 定义数组集并指定泛型为ImageView

    private ImageView iv_red_point;             // 实例化红点
    private int marginLeft;                     // 总间距

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        // 2 在代码中实例化
        viewpager_guide = (ViewPager) findViewById(R.id.viewpager_guide);       // ViewPager
        btn_start_main  = (Button) findViewById(R.id.btn_start_main);           // 主按钮
        ll_point_group  = (LinearLayout) findViewById(R.id.ll_point_group);     // 线性布局

        iv_red_point = (ImageView) findViewById(R.id.iv_red_point);             // 红点实例化

        /*
        * ViewPager使用：
        *               1.在布局文件中定义
        *               2.在代码中实例化
        *               3.准备数据
        *               4.设置适配器
        * *****************************************************************************************/

        // 3 准备数据
        int[] ids = {R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};         // 定义ViewPager图片数组
        imageViews = new ArrayList<>();                                                 // 创建imageViews集合对象
        for (int i=0;i<ids.length;i++) {
            ImageView imageView = new ImageView(this);                                  // 创建imageView对象
            imageView.setBackgroundResource(ids[i]);                                    // 设置imageView背景
            imageViews.add(imageView);                                                  // 加入到集合
            // 添加3个灰点(因为每个页面都有一个灰色点，故对3个灰色点的操作应放于此处)
            ImageView normal_point = new ImageView(this);
            normal_point.setImageResource(R.drawable.normal_point);                     // 添加一个灰色点
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10,10);    // 线性布局高宽为10(dp值为XML中的3倍)
            if (i!=0) {
                params.leftMargin=10;                                                   // 除第一个点外，左边距均为30
            }
            normal_point.setLayoutParams(params);                                       // 设置灰点间距
            ll_point_group.addView(normal_point);                                       // 添加至线性布局
        }

        // 4 设置适配器
        viewpager_guide.setAdapter(new MyPagerAdpter());

        /*------------------------------实现红点在页面上的滑屏移动-----------------------------------*/

        // 1 监听总间距(算法：第一个点的左边距-第0个点的左边距)
        iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener()); // 得到视图树观察者的onLayout监听执行

        // 2 监听页面百分比
        viewpager_guide.addOnPageChangeListener(new MyOnPageChangeListener());

    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        // 页面滚动回调
        //                           当前页面位置     当前页面滑动百分比      页面滑动了多少的像素值
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.i(TAG,"position=="+position+",positionOffset=="+positionOffset+",positionOffsetPixels=="+positionOffsetPixels);

            // 红点移动的距离 = (页面序号 + 页面偏移百分比) * 点间距
            int slideLeft = (int) (position+positionOffset)*marginLeft;

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(10,10); // 红点的父布局为相对布局(红点的宽高均为10)
            params.leftMargin=slideLeft;
            iv_red_point.setLayoutParams(params);
        }

        @Override
        // 页面选中回调
        public void onPageSelected(int position) {

        }

        @Override
        // 滚动切换回调
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener{

        @Override
        public void onGlobalLayout() {

            iv_red_point.getViewTreeObserver().removeOnGlobalLayoutListener(this);  // 确保下面这句只执行一次
            marginLeft = ll_point_group.getChildAt(1).getLeft()-ll_point_group.getChildAt(0).getLeft(); // 线性布局中的第1个孩子的左边距-第1个孩子的左边距
            Log.d(TAG,"marginLeft=="+marginLeft);   // 测量结果为marginLeft==60，但被执行两次
        }
    }

    class MyPagerAdpter extends PagerAdapter{

        @Override
        // 获取集合容量
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        //container就是ViewPager
        //position当前添加到哪个页面
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);

            container.addView(imageView); // 添加到容器(把页面添加到ViewPager中)

            return imageView;
        }
    }
}

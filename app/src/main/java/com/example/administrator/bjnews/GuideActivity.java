package com.example.administrator.bjnews;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * 创建引导页面 on 2016/9/6 0006.
 */

public class GuideActivity extends Activity{

    // 实例化所对应的XML控件
    private ViewPager viewpager_guide;          // ViewPager-ID
    private Button btn_start_main;
    private LinearLayout ll_point_group;

    private ArrayList<ImageView> imageViews;    // 定义数组集并指定泛型为ImageView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        // 2 在代码中实例化
        viewpager_guide = (ViewPager) findViewById(R.id.viewpager_guide);
        btn_start_main  = (Button) findViewById(R.id.btn_start_main);
        ll_point_group  = (LinearLayout) findViewById(R.id.ll_point_group);

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
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30,30);    // 线性布局高宽为10(dp值为XML中的3倍)
            if (i!=0) {
                params.leftMargin=30;                                                   // 除第一个点外，左边距均为30
            }
            normal_point.setLayoutParams(params);                                       // 设置灰点间距
            ll_point_group.addView(normal_point);                                       // 添加至线性布局
        }

        // 4 设置适配器
        viewpager_guide.setAdapter(new MyPagerAdpter());
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

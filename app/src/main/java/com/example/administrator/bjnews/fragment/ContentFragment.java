package com.example.administrator.bjnews.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.example.administrator.bjnews.R;
import com.example.administrator.bjnews.base.BaseFragment;
import com.example.administrator.bjnews.base.BasePager;
import com.example.administrator.bjnews.pager.GovaffairPager;
import com.example.administrator.bjnews.pager.HomePager;
import com.example.administrator.bjnews.pager.NewsCenterPager;
import com.example.administrator.bjnews.pager.SettingPager;
import com.example.administrator.bjnews.pager.SmartServicePager;

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
    private ViewPager vp_content;

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

        rg_bottom_tag.check(R.id.rb_home);  // 默认选中首页

        /*ViewPage的使用：
        *               1 在xml中定义
        *               2 在java中实例化
        *               3 准备数据(集合)
        *               4 设置适配器*/

        basePagers = new ArrayList<>();                     // 创建集合对象
        basePagers.add(new HomePager(context));             // 首页
        basePagers.add(new NewsCenterPager(context));       // 新闻中心
        basePagers.add(new SmartServicePager(context));     // 智慧服务
        basePagers.add(new GovaffairPager(context));        // 政要指南
        basePagers.add(new SettingPager(context));          // 设置

        vp_content.setAdapter(new ContentFragmentAdapter());
    }

    class ContentFragmentAdapter extends PagerAdapter {

        // 必须实现

        @Override
        public int getCount() {
            return basePagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        // 还需实现

        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            BasePager basePager = basePagers.get(position);
            View rootView = basePager.rootView;
            basePager.initData();   // 调各个页面的initData();
            container.addView(rootView);

            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);
        }
    }
}

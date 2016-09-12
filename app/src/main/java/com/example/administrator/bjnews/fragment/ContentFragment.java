package com.example.administrator.bjnews.fragment;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.example.administrator.bjnews.R;
import com.example.administrator.bjnews.base.BaseFragment;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

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
//    private TextView txt;

    @Override
    public View initView() {
        /*txt = new TextView(context);
        txt.setGravity(Gravity.CENTER);
        txt.setTextSize(25);
        txt.setTextColor(Color.RED);
        return txt;*/

        View view = View.inflate(context,R.layout.content_fragment,null);   // inflate将一个xml中定义的布局控件找出来

        //使用xUtils3实现注解，以方便实例化
        x.view().inject(this,view); // this:ContentFragment.java，view:content_fragment.xml(把当前XML中的视图注入到当前的java中)

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.i(TAG,"主页数据初始化完成..");
        //txt.setText("主页内容..\nFragment");
        rg_bottom_tag.check(R.id.rb_home);
    }
}

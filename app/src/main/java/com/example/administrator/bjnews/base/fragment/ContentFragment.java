package com.example.administrator.bjnews.base.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.bjnews.R;
import com.example.administrator.bjnews.base.BaseFragment;

// 说明：新类继承父类，就是要实现父类中的相关方法，其中抽象方法必须实现，其它方法根据需要选择实现。

/**
 * Created by Administrator on 2016/9/8 0008.
 * 主页Fragment
 */
public class ContentFragment extends BaseFragment{

    private static final String TAG = ContentFragment.class.getSimpleName();
    private TextView txt;

    @Override
    public View initView() {
        /*txt = new TextView(context);
        txt.setGravity(Gravity.CENTER);
        txt.setTextSize(25);
        txt.setTextColor(Color.RED);
        return txt;*/

        View view = View.inflate(context,R.layout.content_fragment,null);//inflate将一个xml中定义的布局控件找出来
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Log.i(TAG,"主页数据初始化完成..");
        //txt.setText("主页内容..\nFragment");
    }
}

package com.example.administrator.bjnews.base.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.bjnews.base.BaseFragment;

// 说明：新类继承父类，就是要实现父类中的相关方法，其中抽象方法必须实现，其它方法根据需要选择实现。
/**
 * Created by Administrator on 2016/9/8 0008.
 * 侧滑菜单Fragment
 */
public class LeftMenuFragment extends BaseFragment{

    private static final String TAG = LeftMenuFragment.class.getSimpleName();
    private TextView txt;

    @Override
    public View initView() {
        txt = new TextView(context);
        txt.setGravity(Gravity.CENTER);
        txt.setTextSize(25);
        txt.setTextColor(Color.RED);
        return txt;
    }

    @Override
    public void initData() {
        super.initData();
        Log.i(TAG,"侧滑菜单数据初始化完成..");
        txt.setText("侧滑菜单..\nFragment");
    }
}

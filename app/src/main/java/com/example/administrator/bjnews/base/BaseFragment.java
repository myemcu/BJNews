package com.example.administrator.bjnews.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment; //V4代表向下兼容
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.bjnews.MainActivity;

/**
 * Created by Administrator on 2016/9/8 0008.
 * Fragment公共类，侧滑菜单与主页面Fragment都用的上的一个公共Fragment类
 */
public abstract class BaseFragment extends Fragment{

    public Context context; // 定义上下文

    @Override
    //在这个方法中，需要得到上下文
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
    }

    @Nullable   // 表示定义的字段可以为空
    @Override
    // 这个方法是返回一个View
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return initView();
    }

    // 由孩子强制实现特定效果
    public abstract View initView();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    // 当孩子需要初始化数据的时候，重新该方法
    public void initData() {

    }
}

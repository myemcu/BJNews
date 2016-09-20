package com.example.administrator.bjnews.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.bjnews.R;
import com.example.administrator.bjnews.base.BaseFragment;
import com.example.administrator.bjnews.bean.NewsCenterBean;
import com.example.administrator.bjnews.utils.DensityUtil;

import java.util.List;

// 说明：新类继承父类，就是要实现父类中的相关方法，其中抽象方法必须实现，其它方法根据需要选择实现。
/**
 * Created by Administrator on 2016/9/8 0008.
 * 侧滑菜单Fragment
 */
public class LeftMenuFragment extends BaseFragment{

    private static final String TAG = LeftMenuFragment.class.getSimpleName();
    private List<NewsCenterBean.DataBean> leftMenuData; // 左侧菜单的数据
    private ListView listView;

    @Override
    public View initView() {
        listView = new ListView(context);                           // 以Java方式(动态)创建ListView
        listView.setBackgroundColor(Color.BLACK);                   // 背景黑色
        listView.setPadding(0, DensityUtil.dip2px(context,40),0,0); // 顶边距40
        listView.setDividerHeight(0);                               // 分隔线高
        listView.setCacheColorHint(Color.TRANSPARENT);              // 设置透明(防止在低版本中的按下变色)
        listView.setSelector(android.R.color.transparent);          // 选择器透明(当选择某条的时候，无颜色变化)


        return listView;
    }

    @Override
    public void initData() {
        super.initData();
        Log.i(TAG,"侧滑菜单数据初始化完成..");
    }

    public void setData(List<NewsCenterBean.DataBean> Data) {
        this.leftMenuData=Data;     // 将从形参中传递进来的数据赋值到本Fragment中的对象leftMenuData
        for (int i=0; i<leftMenuData.size(); i++) {
            System.out.println(leftMenuData.get(i).getTitle()+"-----------");
        }
        listView.setAdapter(new LeftMenuAdapter());
    }

    private class LeftMenuAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return leftMenuData.size();         // 返回列表项条目数
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = (TextView) View.inflate(context, R.layout.item_leftmenu,null);  // 文本几乎不耗资源，故不作优化

            return txt;
        }
    }
}

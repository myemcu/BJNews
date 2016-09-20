package com.example.administrator.bjnews.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.bjnews.MainActivity;
import com.example.administrator.bjnews.R;
import com.example.administrator.bjnews.base.BaseFragment;
import com.example.administrator.bjnews.bean.NewsCenterBean;
import com.example.administrator.bjnews.pager.NewsCenterPager;
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
    public TextView txt;

    private int seclectPosition;    // 被点击的item位置
    private LeftMenuAdapter adapter;

    @Override
    public View initView() {
        listView = new ListView(context);                           // 以Java方式(动态)创建ListView
        listView.setBackgroundColor(Color.BLACK);                   // 背景黑色
        listView.setPadding(0, DensityUtil.dip2px(context,40),0,0); // 顶边距40
        listView.setDividerHeight(0);                               // 分隔线高
        listView.setCacheColorHint(Color.TRANSPARENT);              // 设置透明(防止在低版本中的按下变色)
        listView.setSelector(android.R.color.transparent);          // 选择器透明(当选择某条的时候，无颜色变化)

        // 设置列表项点击事件(1.点击项高亮，2.收起左侧菜单，3.切换到对应页面(新闻，专题，组图，互动))
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 1.点击项高亮
                seclectPosition=position;           // 记录点击位置
                adapter.notifyDataSetChanged();     // 数据刷新
                // 2.收起左侧菜单
                MainActivity mainactivity = (MainActivity) context;
                mainactivity.getSlidingMenu().toggle();     // 自动开关切换
                // 3.点击切换到对应页面
                switchPager(seclectPosition);
            }
        });
        return listView;
    }

    // 切换到对应页面
    private void switchPager(int seclectPosition) {
        MainActivity mainactivity = (MainActivity) context;
        ContentFragment contentFragment = mainactivity.getContentFragment();
        NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
        newsCenterPager.switchPager(seclectPosition);
    }

    @Override
    public void initData() {
        super.initData();
//        Log.i(TAG,"侧滑菜单数据初始化完成..");
        // 设置显示侧滑按钮

    }

    public void setData(List<NewsCenterBean.DataBean> Data) {

        this.leftMenuData=Data;     // 将从形参中传递进来的数据赋值到本Fragment中的对象leftMenuData

        for (int i=0; i<leftMenuData.size(); i++) {
            System.out.println(leftMenuData.get(i).getTitle()+"-----------");
        }

        adapter = new LeftMenuAdapter();
        listView.setAdapter(adapter);

        switchPager(seclectPosition);
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

            // 文本几乎不耗资源，故不作优化
            txt = (TextView) View.inflate(context, R.layout.item_leftmenu,null);
            txt.setText(leftMenuData.get(position).getTitle()); // 必须联网

            /*if (seclectPosition==position) {
                txt.setEnabled(true);
            }else {
                txt.setEnabled(false);
            }*/

            txt.setEnabled(seclectPosition==position);  // 与上边效果一样(所选位置高亮)

            return txt;
        }
    }
}

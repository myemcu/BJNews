package com.myemcu.refreshlistview_sample;

import android.app.Activity;
import android.graphics.Color;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.myemcu.refreshlistview_lib.RefreshListView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final int PULL_DOWN_REFRESH = 1; // 下拉刷新
    private static final int PULL_UP_REFRESH = 2;   // 上拉刷新

    private RefreshListView refrsh_list;

    private ArrayList<String> msg;  // 定义数组列表

    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refrsh_list = (RefreshListView) findViewById(R.id.refrsh_list);

        // 准备数据
        msg=new ArrayList<>();
        for (int i=0;i<50;i++) {
            msg.add("我是RefreshListView列表项_"+i);
        }

        // 设置适配器
        myAdapter=new MyAdapter();
        refrsh_list.setAdapter(myAdapter);

        // 设置上、下拉刷新监听
        refrsh_list.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onPullDownRefresh() {
                getDataFromNet();   // 联网请求
            }

            @Override
            public void onLoadMore() {
                getMoreDataFromNet();
            }
        });
    }

    private void getMoreDataFromNet() {
        // 因隔一段时间才有数据，故先来个子线程休眠
        new Thread(){
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(2000);
                // 准备数据
//                msg=new ArrayList<>();  // 加载更多的数据需要跟在先前数据其后，故不再new
                for (int i=0;i<50;i++) {
                    msg.add("我是加载更多后的数据项_"+i);
                }
                handler.sendEmptyMessage(PULL_UP_REFRESH);
            }
        }.start();
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PULL_DOWN_REFRESH: // 下拉刷新
                        myAdapter=new MyAdapter();
                        refrsh_list.setAdapter(myAdapter);
                        refrsh_list.OnRefreshFinish(true);
                        break;
                case PULL_UP_REFRESH:   // 上拉刷新
                        myAdapter.notifyDataSetChanged();   // 刷新适配器
                        refrsh_list.OnRefreshFinish(true);
                        break;
            }
        }
    };

    // 联网请求
    private void getDataFromNet() {
        // 因隔一段时间才有数据，故先来个子线程休眠
        new Thread(){
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(2000);
                // 准备数据
                msg=new ArrayList<>();
                for (int i=0;i<50;i++) {
                    msg.add("我是下拉刷新后的数据项_"+i);
                }
                handler.sendEmptyMessage(PULL_DOWN_REFRESH);
            }
        }.start();
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return msg.size();
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

            TextView txt = new TextView(MainActivity.this);
            txt.setText(msg.get(position));
            txt.setTextSize(18);
            txt.setTextColor(Color.BLUE);

            return txt;
        }
    }
}

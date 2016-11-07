package com.myemcu.recyclerviewdemo;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.myemcu.recyclerviewdemo.adapter.RecyclerViewAdapter;

import java.util.ArrayList;

// 须在工程管理器中添加Recycler库"recyclerview-v7:25.0.0"

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rcyview;
    private ArrayList<String> datas;
    private RecyclerViewAdapter adapter;

    private Button btn_add,btn_del,btn_list,btn_grid;

    private SwipeRefreshLayout swipe_refresh_layout;    // 原生下拉刷新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcyview = (RecyclerView) findViewById(R.id.rcyview);

        btn_add = (Button) findViewById(R.id.btn_add);
        btn_del = (Button) findViewById(R.id.btn_del);
        btn_list = (Button) findViewById(R.id.btn_list);
        btn_grid = (Button) findViewById(R.id.btn_grid);

        swipe_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        // 准备数据源(集合)
        datas=new ArrayList<>();    // new出集合
        for (int i=0;i<50;i++) {    // 50条数据(50条String)
            datas.add("Data_"+i);
        }

        // 设置适配器
        adapter=new RecyclerViewAdapter(this,datas);// this:上下文，Datas数据源
        rcyview.setAdapter(adapter);

        // 设置默认布局管理器
        rcyview.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));  // 最后一项为是否倒序，中间为垂直方向滑动
        //rcyview.setLayoutManager(new GridLayoutManager(MainActivity.this,2,LinearLayoutManager.VERTICAL,false)); //  网格布局，2列，垂直，正序
        //rcyview.scrollToPosition(6);   // 首项显示定位到item_6
        //rcyview.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));    // 瀑布流

        // 设置分隔线
        rcyview.addItemDecoration(new MyItemDecoration(this,MyItemDecoration.VERTICAL_LIST));

        // 设置item的点击事件(适配器中)
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String data) {
                Toast.makeText(MainActivity.this,"你点击的是："+data,Toast.LENGTH_SHORT).show();
            }
        });

        // 设置Button点击事件
        btn_add.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        btn_list.setOnClickListener(this);
        btn_grid.setOnClickListener(this);

        initRefresh();
    }

    private void initRefresh() {    // 设置原生下拉刷新属性
        // 设置圈圈颜色
        swipe_refresh_layout.setColorSchemeResources(android.R.color.holo_red_light,android.R.color.holo_orange_light,android.R.color.holo_blue_light);
        // 设置刷新控件的背景色
        swipe_refresh_layout.setProgressBackgroundColorSchemeColor(getResources().getColor(android.R.color.white));
        // 设置滑动距离
        swipe_refresh_layout.setDistanceToTriggerSync(100);
        // 设置大小模式
        swipe_refresh_layout.setSize(SwipeRefreshLayout.DEFAULT);
        // 设置下拉刷新的监听
        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {          // 在主线程中new的
                    @Override
                    public void run() {                             // 在主线程中执行

//                        adapter.delAllData();                       // 覆盖原来


                        refreshData();


//                        adapter.addAllData(datas);                  // 覆盖原来

                        swipe_refresh_layout.setRefreshing(false);  // 设置圈圈隐藏
                        adapter.notifyItemRangeChanged(0,50);       // 刷新数据
                        rcyview.scrollToPosition(0);
                    }
                },2000);                                            // 延时2s执行refreshData();
            }
        });

    }

    public void refreshData() { // 下拉刷新后的方法
        // datas = new ArrayList<>();   // 不能加
        for (int i=0;i<50;i++) {    // 50条数据(50条String)
            datas.add(0,"SwipeData_"+i);
        }
    }

    int cnt;
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_add:
                                adapter.addData(0,"我是新增项"+cnt);
                                rcyview.scrollToPosition(0);
                                cnt++;
                                break;

            case R.id.btn_del:
                                adapter.delData(0);
                                rcyview.scrollToPosition(0);
                                if (cnt>0) {
                                    cnt--;
                                }
                                break;

            case R.id.btn_list:
                                rcyview.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));  // 最后一项为是否倒序，中间为垂直方向滑动
                                break;

            case R.id.btn_grid:
                                rcyview.setLayoutManager(new GridLayoutManager(MainActivity.this,2,LinearLayoutManager.VERTICAL,false));    // 网格布局，2列，垂直，正序
                                break;
        }
    }
}

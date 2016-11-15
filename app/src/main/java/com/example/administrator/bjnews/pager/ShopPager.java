package com.example.administrator.bjnews.pager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.administrator.bjnews.R;
import com.example.administrator.bjnews.adapter.ShopPagerRecyclerViewAdapter;
import com.example.administrator.bjnews.base.BasePager;
import com.example.administrator.bjnews.bean.ShopPagerBean;
import com.example.administrator.bjnews.utils.CacheUtil;
import com.example.administrator.bjnews.utils.CartProvider;
import com.example.administrator.bjnews.utils.LogUtil;
import com.example.administrator.bjnews.utils.Url;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

//import com.zhy.http.okhttp.OkHttpUtils;

//  http://112.124.22.238:8081/course_api/wares/hot?pageSize=7&curPage=0 // 每页7条，当前页为0

/**
 * Created by Administrator on 2016/9/12 0012.
 * 商城热卖
 */
public class ShopPager extends BasePager {

    private String url;

    private static final int STATE_NORMAL   =   1;      // 默认状态
    private static final int STATE_REFRESH  =   2;      // 下拉刷新
    private static final int STATE_LOADMORE =   3;      // 加载更多

    private int state = STATE_NORMAL;

    /*private TextView txt;*/

    private MaterialRefreshLayout refresh_layout; // 下拉刷新布局
    private RecyclerView recycler_view;
    private ProgressBar pb_loading;
    private int pageSize = 7;   // 每页7个(共4页)
    private int curPage  = 1;   // 当前页为1(首页为0)
    private int totalPage= 4;   // 总页数

    private List<ShopPagerBean.Wares>    datas;     // 商品列表数据
    private ShopPagerRecyclerViewAdapter adapter;   // Recyv适配器

    // 构造器
    public ShopPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        System.out.println("商城热卖的数据被初始化了....");

        /*// 设置内容(子视图)
        txt = new TextView(context); // 在当前页创建一个TextView文本
        txt.setGravity(Gravity.CENTER);
        txt.setText("商城热卖的内容");
        txt.setTextSize(25);
        txt.setTextColor(Color.RED);*/

        // 1 设置标题
        tv_title.setText("商城热卖");
        // 2 创建视图
        View view = View.inflate(context, R.layout.shop_hot_sell,null);
        // 3 实例化
        refresh_layout  = (MaterialRefreshLayout)   view.findViewById(R.id.refresh_layout);
        recycler_view   = (RecyclerView)            view.findViewById(R.id.recycler_view);
        pb_loading      = (ProgressBar)             view.findViewById(R.id.pb_loading);
        // 4 添加view到FrameLayout中
        if (fl_base_content != null) {
            fl_base_content.removeAllViews();   // 消除由addView()导致的重影
        }
        fl_base_content.addView(view);

        initRefreshLayout();

        // 5 设置网址
        setRequestParams();
        // 联网请求与解析数据
        getDataFromNet_OkHttpUtils();
    }

    private void initRefreshLayout() {
        // 设置下拉和上拉刷新
        refresh_layout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            //下拉刷新
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                state=STATE_REFRESH;
                curPage=1;  // 下拉刷新请求的是第1页
                url= Url.SHOP_URL+pageSize+"&curPage="+curPage;
                getDataFromNet_OkHttpUtils();
            }

            @Override
            //加载更多(Ctrl+O)
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);

                if (curPage < totalPage) {
                    state=STATE_LOADMORE;
                    curPage+=1;  // 下拉刷新请求的是第1页
                    url= Url.SHOP_URL+pageSize+"&curPage="+curPage;
                    getDataFromNet_OkHttpUtils();
                }
                else {
                    Toast.makeText(context,"没有更多数据了...",Toast.LENGTH_SHORT).show();
                    refresh_layout.finishRefreshLoadMore(); // 加载更多的UI还原
                }
            }
        });
    }

    private void getDataFromNet_OkHttpUtils() {

        String json =  CacheUtil.getString(context, Url.SHOP_URL);

        if (!TextUtils.isEmpty(json)) {
            processData(json);
        }

        OkHttpUtils
                .get()
                .url(url)
                .id(100)
                .build()
                .execute(new MyStringCallback());
    }

    public class MyStringCallback extends StringCallback
    {
        @Override   // 联网请求成功
        public void onResponse(String response, int id)
        {
            LogUtil.e("使用okhttp联网请求成功==" + response);   // 观察时，输入Info，ShopPager即可(==后面的全部信息)

            //缓存数据
            CacheUtil.putString(context, Url.SHOP_URL, response);

            processData(response);  // 对请求到的数据进行解析

            switch (id)
            {
                case 100:
                        Toast.makeText(context, "http", Toast.LENGTH_SHORT).show();
                        break;

                case 101:
                        Toast.makeText(context, "https", Toast.LENGTH_SHORT).show();
                        break;
            }
        }

        @Override   //  联网失败
        public void onError(Call call, Exception e, int id)
        {
            e.printStackTrace();
            LogUtil.e("使用okhttp联网请求失败==" + e.getMessage());
        }

    }

    private void setRequestParams() {
        state=STATE_NORMAL;
        curPage=1;  // (1~4)
        url= Url.SHOP_URL+pageSize+"&curPage="+curPage;
    }

    private void processData(String response) {         // 数据解析(Alt+S)，先建一个Bean类,再Gson成Bean代码
        ShopPagerBean bean = parsedJson(response);      // 根据response解析Json
        datas = bean.getList();                         // 得到列表
        curPage=bean.getCurrentPage();                  // 得到当前页
        totalPage=bean.getTotalPage();                  // 得到总页数

        LogUtil.e("curPage=="+curPage);
        LogUtil.e("totalPage"+totalPage);
        LogUtil.e("datas(1)"+datas.get(1).getName());

        showData(); // 数据显示
    }

    private void showData() {

        switch (state) {
            case STATE_NORMAL:  // 正常显示
                                adapter = new ShopPagerRecyclerViewAdapter(context,datas);
                                recycler_view.setAdapter(adapter);
                                recycler_view.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
                                break;

            case STATE_REFRESH: // 下拉刷新
                                // 1 清空之前数据
                                adapter.clearData();
                                // 2 添加新数据(刷新)
                                adapter.addData(0,datas);
                                // 3 状态还原
                                refresh_layout.finishRefresh();
                                break;

            case STATE_LOADMORE:// 加载更多
                                // 1 把新的数据添加到原来数据的末尾(刷新)
                                adapter.addData(adapter.getDataCount(),datas);
                                // 2 状态还原
                                refresh_layout.finishRefreshLoadMore();
                                break;

            default:break;
        }

        pb_loading.setVisibility(View.GONE);
    }

    private ShopPagerBean parsedJson(String response) { // 商城热卖数据的解析实现
        return new Gson().fromJson(response,ShopPagerBean.class);
    }
}

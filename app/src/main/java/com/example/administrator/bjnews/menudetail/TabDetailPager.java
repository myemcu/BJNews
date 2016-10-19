package com.example.administrator.bjnews.menudetail;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bjnews.R;
import com.example.administrator.bjnews.base.MenuDetailBasePager;
import com.example.administrator.bjnews.bean.NewsCenterBean_Hand;
import com.example.administrator.bjnews.bean.TabDetailPagerBean;
import com.example.administrator.bjnews.utils.CacheUtil;
import com.example.administrator.bjnews.utils.DensityUtil;
import com.example.administrator.bjnews.utils.LogUtil;
import com.example.administrator.bjnews.utils.Url;
import com.example.administrator.bjnews.view.HorizontalScrollViewPager;

import com.google.gson.Gson;

//import com.example.administrator.bjnews.view.RefreshListView;
import com.myemcu.refreshlistview_lib.RefreshListView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/9/23 0023.
 * 页签详情页面
 */

public class TabDetailPager extends MenuDetailBasePager {

    public static final String READ_ARRAY_ID = "read_array_id"; // ctrl+Alt+C
    private  String url;
    private int prePosition;                // 上一个红点的高亮位置

    @ViewInject(R.id.vp_tabdetail_pager)    // 页签详情页面ViewPager
    private HorizontalScrollViewPager vp_tabdetail_pager;

    @ViewInject(R.id.tv_title)              // 标题
    private TextView tv_title;

    @ViewInject(R.id.ll_point_group)        // 线性布局(水平)装红点用
    private LinearLayout ll_point_group;

    @ViewInject(R.id.lv_tabdetail_pager)    // 下拉刷新列表(在该列表中还要添加顶部轮播图，其后实现)
    private RefreshListView lv_tabdetail_pager;

    private String moreUrl;                 // 用于加载更多的链接

    private final NewsCenterBean_Hand.NewsCenterBean_Data.Children_Data childrenDatas;
    private static final String TAG = TabDetailPager.class.getSimpleName();

    // 顶部新闻对应的数据
    private List<TabDetailPagerBean.DataBean.TopnewsBean> topnews;
    // 下方新闻列表数据
    private List<TabDetailPagerBean.DataBean.NewsBean> newsList;
    private TabDetailPagerBean.DataBean.NewsBean newsData;

    private boolean isLoadMore; // 是否已经加载更多
    private TabDetailPagerAdapter adapter;


    // 构造器
    public TabDetailPager(Context context, NewsCenterBean_Hand.NewsCenterBean_Data.Children_Data data) {
        super(context);
        this.childrenDatas=data;
    }

    @Override
    public View initView() {    // 视图

        View view = View.inflate(context, R.layout.tabdetail_pager,null);
        x.view().inject(this,view);

        View topnewsview = View.inflate(context, R.layout.topnews,null);
        x.view().inject(this,topnewsview);

        // 添加头(顶部轮播图)
        // lv_tabdetail_pager.addHeaderView(topnewsview);

        // 向下拉刷新列表"lv_tabdetail_pager"中添加顶部轮播图，使其整合成一个整体
        // 如果注掉，将不添加顶部轮播图
        lv_tabdetail_pager.addTopNewsView(topnewsview);

        // 设置刷新的监听
        lv_tabdetail_pager.setOnRefreshListener(new myOnRefreshListener());

        // 设置点击ListView_item的监听
        lv_tabdetail_pager.setOnItemClickListener(new myOnItemClickListener());

        return view;
    }

    // 点击ListView中某一条的监听
    private class myOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TabDetailPagerBean.DataBean.NewsBean newsBean = newsList.get(position-1);
            LogUtil.e("newsBean.title=="+newsBean.getTitle()+" newsBean.id=="+newsBean.getId());
            String idArry = CacheUtil.getString(context,READ_ARRAY_ID); // ctrl+Alt+C("read_array_id") (35311,35312,...)
            // 当前这个id没被保存
            if (! idArry.contains(newsBean.getId()+"")) {
                // 保存
                String values = idArry + newsBean.getId()+",";
                CacheUtil.putString(context,READ_ARRAY_ID,values);
                // 适配器刷新
                adapter.notifyDataSetChanged(); // getCount(),getView();
            }
        }
    }

    // 下拉刷新和上拉加载的监听
    private class myOnRefreshListener implements RefreshListView.OnRefreshListener {
        @Override
        public void onPullDownRefresh() {
            Toast.makeText(context,"下拉刷新，被回调",Toast.LENGTH_LONG).show();
            getDataFromNet();
        }

        @Override
        public void onLoadMore() {
//                Toast.makeText(context,"加载更多，被回调",Toast.LENGTH_LONG).show();
            if (TextUtils.isEmpty(moreUrl)) {
                // 没有更多
                Toast.makeText(context,"没有更多数据",Toast.LENGTH_LONG).show();
                lv_tabdetail_pager.OnRefreshFinish(false);
                isLoadMore=false;
            }else {
                // 加载更多
                getMoreDataFromNet();
            }
        }
    }

    // 加载更多
    private void getMoreDataFromNet() {
        RequestParams params = new RequestParams(moreUrl);      // 请求url

        params.setConnectTimeout(2000); //2s断网超时时间

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG,"onSuccess=="+result);
//                CacheUtil.putString(context,url,result);    // 缓存数据
                isLoadMore=true;
                processData(result);                        // 开始解析数据

                lv_tabdetail_pager.OnRefreshFinish(true);   // 更新刷新的时间
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i(TAG,"Throwable=="+ex.getMessage());
                lv_tabdetail_pager.OnRefreshFinish(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.i(TAG,"CancelledException=="+cex.getMessage());
            }

            @Override
            public void onFinished() {
                Log.i(TAG,"==onFinished!==");
            }
        });
    }

    @Override
    public void initData() {    // 数据
        super.initData();
        System.out.println("---------"+childrenDatas.getTitle());
        url= Url.BASE_URL+childrenDatas.getUrl();
        String saveJson = CacheUtil.getString(context,url); // 获取缓存数据
        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);  // 开始解析数据
        }
        System.out.println("url==="+url);
        
        // 开始联网请求数据(使用xUtils3)
        getDataFromNet();
    }

    // Json解析成某一对象(满足控件所需要的数据类型)、将数据绑定到UI
    private void processData(String json) { // 以第一次出现的OnSucess()为解析顺序；
        // 北京为首项解析内容，运行时不易观察，先选中此项，再点旁边的智慧服务，再点新闻中心(之前清调试信息)
        // 北京的信息即再调试窗口的最顶部，通常为一灰条(不太显眼)，点击即可展开
        // 开服务器，点里面的链接"http://192.168.1.200:8080/zhbj/10007/list_1.json"(360IE设置成极速模式)
        // 开HiJson，将IE中的内容(北京的Json信息)全选复制进来后，再点击"格式化Json字符串"

        TabDetailPagerBean tabDetailPagerBean = paseJson(json); // 解析到Bean
        String more = tabDetailPagerBean.getData().getMore();   // 加载更多的链接
        if (TextUtils.isEmpty(more)) {
            moreUrl="";
        }else {
            moreUrl=Url.BASE_URL+more;
        }
        LogUtil.e("moreUrl==="+moreUrl);// 调试时，输入processData进行跟踪(开服务器)

        Log.i(TAG,"解析测试==");
        Log.i(TAG,"解析news[0]成功=="+tabDetailPagerBean.getData().getNews().get(0).getTitle());
        Log.i(TAG,"解析Title成功=="+tabDetailPagerBean.getData().getTitle());


        if (!isLoadMore) {
            // 之前默认
            // 获取顶部新闻的数据
            topnews = tabDetailPagerBean.getData().getTopnews();

            // 设置适配器
            vp_tabdetail_pager.setAdapter(new MyPagerAdapter());

            // 添加红点
            addRedPoint();

            // 更新图片标题
            tv_title.setText(topnews.get(prePosition).getTitle()); // 设置标题

            // 监听ViewPager页面状态
            vp_tabdetail_pager.addOnPageChangeListener(new MyOnPageChangeListener());

            /*设置动图下方的ListView适配器*/
            newsList = tabDetailPagerBean.getData().getNews();          // 准备数据
            adapter = new TabDetailPagerAdapter();
            lv_tabdetail_pager.setAdapter(adapter); // 开始适配

        }else {
            isLoadMore=false;
            // 加载更多
            // 新加的数据(非老数据)
            // List<TabDetailPagerBean.DataBean.NewsBean> newsList = tabDetailPagerBean.getData().getNews();   // 准备数据(直接赋值就覆盖了)
            newsList.addAll(tabDetailPagerBean.getData().getNews());
            // 刷新适配器
            adapter.notifyDataSetChanged();
        }


    }

//==================================================================================================

    private class TabDetailPagerAdapter extends BaseAdapter {//BaseAdapter就是自定义Adapter

        @Override
        public int getCount() {
            return newsList.size();
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

            ViewHolder viewHolder;

            if (convertView==null) {// 若没有转换视图
                convertView=View.inflate(context,R.layout.item_tabdetail_pager,null);   // 就new一个
                viewHolder=new ViewHolder();
                viewHolder.iv_icon  = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_title = (TextView)  convertView.findViewById(R.id.tv_title);
                viewHolder.tv_time  = (TextView)  convertView.findViewById(R.id.tv_time);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // 根据位置得到数据
            newsData = newsList.get(position);
            viewHolder.tv_title.setText(newsData.getTitle());
            viewHolder.tv_time.setText(newsData.getPubdate());
            x.image().bind(viewHolder.iv_icon,newsData.getListimage());

            String idArry = CacheUtil.getString(context,READ_ARRAY_ID); // ctrl+Alt+C("read_array_id") (35311,35312,...)
            if (idArry.contains(newsData.getId()+"")) {
                // 设置成灰色
                viewHolder.tv_title.setTextColor(Color.GRAY);

            }else {
                // 设置成黑色(默认)
                viewHolder.tv_title.setTextColor(Color.BLACK);
            }

            return convertView;
        }
    }

    static class ViewHolder {// 静态类用于直接new
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_time;
    }

    private void addRedPoint() {

        ll_point_group.removeAllViews();    // 布局清显示

        // 添加顶部新闻图片的红点
        for (int i=0;i<topnews.size();i++) {
            ImageView point = new ImageView(context);
            point.setBackgroundResource(R.drawable.point_selector);
            // 设置布局大小(宽，高)
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(context,6),DensityUtil.dip2px(context,6));
            point.setLayoutParams(params);
            if (i==0) {
                point.setEnabled(true); // 高亮显示
            }else {
                point.setEnabled(false);
                params.leftMargin = DensityUtil.dip2px(context,8); // 间距
            }
            // 将红点添加到线性布局中
            ll_point_group.addView(point);
        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override   // 滚动
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            tv_title.setText(topnews.get(position).getTitle()); // 设置标题

            // 把上一次设置为默认
            ll_point_group.getChildAt(prePosition).setEnabled(false);

            // 设置当前高亮
            ll_point_group.getChildAt(position).setEnabled(true);

            // 记录位置
            prePosition=position;
        }

        @Override   // 选择
        public void onPageSelected(int position) {

        }

        @Override   // 状态
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        // Ctrl+O

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.home_scroll_default);

            container.addView(imageView);   // 放到容器中

            // 根据位置得到数据
            TabDetailPagerBean.DataBean.TopnewsBean topnewsBean = topnews.get(position);

            // 联网请求图片
            x.image().bind(imageView,topnewsBean.getTopimage());

            return imageView;
        }

        @Override   // 销毁
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private TabDetailPagerBean paseJson(String json) {
        return new Gson().fromJson(json,TabDetailPagerBean.class);
    }

    // 每个状态的日志都要打
    private void getDataFromNet() {
        RequestParams params = new RequestParams(url);      // 请求url

        //params.setConnectTimeout(4000); //4s超时时间

        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG,"onSuccess=="+result);
                CacheUtil.putString(context,url,result);    // 缓存数据
                processData(result);                        // 开始解析数据
                lv_tabdetail_pager.OnRefreshFinish(true);   // 更新刷新的时间
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i(TAG,"Throwable=="+ex.getMessage());
                lv_tabdetail_pager.OnRefreshFinish(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.i(TAG,"CancelledException=="+cex.getMessage());
            }

            @Override
            public void onFinished() {
                Log.i(TAG,"==onFinished!==");
            }
        });
    }

}

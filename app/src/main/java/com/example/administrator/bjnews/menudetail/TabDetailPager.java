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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.bjnews.R;
import com.example.administrator.bjnews.base.MenuDetailBasePager;
import com.example.administrator.bjnews.bean.NewsCenterBean_Hand;
import com.example.administrator.bjnews.bean.TabDetailPagerBean;
import com.example.administrator.bjnews.utils.CacheUtil;
import com.example.administrator.bjnews.utils.Url;
import com.google.gson.Gson;

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

    private  String url;

    @ViewInject(R.id.vp_tabdetail_pager)    // 页签详情页面ViewPager
    private ViewPager vp_tabdetail_pager;

    @ViewInject(R.id.tv_title)              // 标题
    private TextView tv_title;

    @ViewInject(R.id.ll_point_group)        // 线性布局(水平)装红点用
    private LinearLayout ll_point_group;

    @ViewInject(R.id.lv_tabdetail_pager)    // ListView列表
    private ListView lv_tabdetail_pager;

    private final NewsCenterBean_Hand.NewsCenterBean_Data.Children_Data childrenDatas;
    private static final String TAG = TabDetailPager.class.getSimpleName();

    // 顶部新闻对应的数据
    private List<TabDetailPagerBean.DataBean.TopnewsBean> topnews;


    // 构造器
    public TabDetailPager(Context context, NewsCenterBean_Hand.NewsCenterBean_Data.Children_Data data) {
        super(context);
        this.childrenDatas=data;
    }

    @Override
    public View initView() {    // 视图

        View view = View.inflate(context, R.layout.tabdetail_pager,null);
        x.view().inject(this,view);
        return view;
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
        Log.i(TAG,"解析测试==");
        Log.i(TAG,"解析news[0]成功=="+tabDetailPagerBean.getData().getNews().get(0).getTitle());
        Log.i(TAG,"解析Title成功=="+tabDetailPagerBean.getData().getTitle());

        // 获取顶部新闻的数据
        topnews = tabDetailPagerBean.getData().getTopnews();

        // 设置适配器
        vp_tabdetail_pager.setAdapter(new MyPagerAdapter());
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
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG,"onSuccess=="+result);
                CacheUtil.putString(context,url,result);    // 缓存数据
                processData(result);                        // 开始解析数据
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i(TAG,"Throwable=="+ex.getMessage());
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

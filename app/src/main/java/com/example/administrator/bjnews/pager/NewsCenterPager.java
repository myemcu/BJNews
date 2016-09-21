package com.example.administrator.bjnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.bjnews.MainActivity;
import com.example.administrator.bjnews.base.BasePager;
import com.example.administrator.bjnews.base.MenuDetailBasePager;
//import com.example.administrator.bjnews.bean.NewsCenterBean;
import com.example.administrator.bjnews.bean.NewsCenterBean_Hand;
import com.example.administrator.bjnews.fragment.LeftMenuFragment;
import com.example.administrator.bjnews.menudetail.InteractMenuDetailPager;
import com.example.administrator.bjnews.menudetail.NewsMenuDetailPager;
import com.example.administrator.bjnews.menudetail.PhotosMenuDetailPager;
import com.example.administrator.bjnews.menudetail.TopicMenuDetailPager;
import com.example.administrator.bjnews.utils.Url;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/12 0012.
 * 新闻中心
 */
public class NewsCenterPager extends BasePager {

    private static final String TAG = NewsCenterPager.class.getSimpleName();
    private TextView txt;
    private String url;
    private List<NewsCenterBean_Hand.NewsCenterBean_Data>  leftMenuData;        // 左侧菜单的对应数据
    private ArrayList<MenuDetailBasePager> detailBasePagers;    // 左侧菜单的对应页面(视图)

    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        ib_menu.setVisibility(View.VISIBLE);    // 显示侧滑按钮

        System.out.println("新闻中心的数据被初始化了....");

        // 设置标题
        tv_title.setText("新闻中心");

        // 设置内容(子视图)
        txt = new TextView(context); // 在当前页创建一个TextView文本
        txt.setGravity(Gravity.CENTER);
        txt.setText("新闻中心的内容");
        txt.setTextSize(25);
        txt.setTextColor(Color.RED);

        fl_base_content.addView(txt);   // 把子视图添加到FrameLayout中

        url = Url.NEWSCENTER_URL;

        GetDataFromNet();               // 联网请求数据(联网前开Tomcat服务器，开手机WiFi)
    }

    // 联网请求数据(联网前开Tomcat服务器，开手机WiFi)
    private void GetDataFromNet() {
        RequestParams params = new RequestParams(url); // 联网请求
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {                      // 请求成功

                Log.i(TAG,"联网请求成功=="+result);

                processData(result); // 解析请求结果json
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {   // 请求失败
                Log.i(TAG,"Throwable=="+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {           // 请求取消
                Log.i(TAG,"onCancelled=="+cex.getMessage());
            }

            @Override
            public void onFinished() {                                  // 请求完成
                Log.i(TAG,"完成。");
            }
        });
    }

    // Json的解析与显示
    private void processData(String json) {

        // 手动解析： 1.创建Bean对象(难点)
        //           2.使用系统API

        NewsCenterBean_Hand newsCenterBean = new Gson().fromJson(json,NewsCenterBean_Hand.class); // Gson解析
        Log.i(TAG,newsCenterBean.getData().get(0).getChildren().get(1).getTitle()+"--------------");
        leftMenuData = newsCenterBean.getData(); // 获取所有解析数据给左侧菜单对象，newsCenterBean为解析后的数据对象

        // Gson解析：1.创建Bean对象(CopyJson数据，Alt+Insert（FormatGson）);
        //          2.Gson-API
//        NewsCenterBean newsCenterBean = new Gson().fromJson(json,NewsCenterBean.class); // Gson解析
//        Log.i(TAG,newsCenterBean.getData().get(0).getChildren().get(1).getTitle()+"--------------");
//        leftMenuData = newsCenterBean.getData(); // 获取所有解析数据给左侧菜单对象，newsCenterBean为解析后的数据对象

        // 准备逐级传递
        MainActivity mainActivity = (MainActivity) context;                         // 获取MainActivity上下文
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();     // 获取其上下文中的LeftMenuFragment

        // 添加“新闻、专题、组图、菜单”这四个详情页面
        detailBasePagers = new ArrayList<>();
        detailBasePagers.add(new NewsMenuDetailPager(context));         // 新闻
        detailBasePagers.add(new TopicMenuDetailPager(context));        // 专题
        detailBasePagers.add(new PhotosMenuDetailPager(context));       // 组图
        detailBasePagers.add(new InteractMenuDetailPager(context));     // 菜单

        // 在LeftMenuFragment中显示数据(只能是先特价页面，再显示数据，不然程序要崩)
        leftMenuFragment.setData(leftMenuData);
    }

    // 根据位置切换到对应的菜单详情页面(menudetail)
    public void switchPager(int selectPosition) {

        // 设置标题
        tv_title.setText(leftMenuData.get(selectPosition).getTitle());

        MenuDetailBasePager detailBasePager = detailBasePagers.get(selectPosition);
        View rootView = detailBasePager.rootView;
        // 初始化数据
        detailBasePager.initData();
        fl_base_content.removeAllViews();
        fl_base_content.addView(rootView);
    }
}

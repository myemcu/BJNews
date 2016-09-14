package com.example.administrator.bjnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.example.administrator.bjnews.base.BasePager;
import com.example.administrator.bjnews.bean.NewsCenterBean;
import com.example.administrator.bjnews.utils.Url;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2016/9/12 0012.
 * 新闻中心
 */
public class NewsCenterPager extends BasePager {

    private static final String TAG = NewsCenterPager.class.getSimpleName();
    private TextView txt;
    private String url;

    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

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

        // 联网请求数据(联网前开Tomcat服务器，开手机WiFi)
        GetDataFromNet();
    }

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
        // 手工解析(Android-API)与第三方库解析(Gson，FastJson等)
        // Gson解析：1.创建Bean对象(CopyJson数据，Alt+Insert（FormatGson）);
        //          2.Gson-API
        NewsCenterBean newsCenterBean = new Gson().fromJson(json,NewsCenterBean.class);
        Log.i(TAG,newsCenterBean.getData().get(0).getChildren().get(1).getTitle()+"--------------");
    }
}

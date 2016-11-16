package com.example.administrator.bjnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.example.administrator.bjnews.utils.CacheUtil;
import com.example.administrator.bjnews.utils.Url;
import com.example.administrator.bjnews.volley.VolleyManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
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
    private long startTime; // 联网框架测试，开始时间
    private long endTime;   // 联网框架测试，成功时间
    private long passTime;  // 联网框架测试，耗费时间

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

        /*优先显示本地缓存信息*/
        String saveJson = CacheUtil.getString(context,url); // 获取数据缓存

        if (!TextUtils.isEmpty(saveJson)) { // 如果非空才解析
            processData(saveJson);
        }

        startTime = SystemClock.uptimeMillis();

    // 这两种都可以用，但一次只选一种即可(最终看哪个性能高就选哪个)
    // 通过多次测试，xUtils3相比Volley更快，更稳定(xUtils3完胜)
        GetDataFromNet();   // 联网请求数据(xUtils3)(联网前开Tomcat服务器，开手机WiFi)
//        GetDataFromNet_Volley();   // 联网请求数据(Volley)(联网前开Tomcat服务器，开手机WiFi)
    }

    private void GetDataFromNet_Volley() {

        // 1 定义队列
//        RequestQueue queue =Volley.newRequestQueue(context);
//        Appilcation中初始化后不需要每次都new了

        // 2 队列请求
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {// 联网请求成功

                // 联网性能测试(多测几次看稳定范围)
                endTime = SystemClock.uptimeMillis();
                passTime = endTime-startTime;
                Toast.makeText(context,"联网时间："+passTime,Toast.LENGTH_LONG).show();

                Log.i("Volley","Volley联网请求--成功--"+result);

                // 请求成功后，设置数据缓存
                CacheUtil.putString(context,url,result);

                processData(result); // 解析请求结果json
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {// 联网请求失败
                Log.i("Volley","Volley联网请求--失败--"+error.getMessage());
            }
        }){// 解决乱码
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String parsed = new String(response.data,"UTF-8");
                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return super.parseNetworkResponse(response);
            }
        };

        // 3 向队列添加请求
//        queue.add(request);
        VolleyManager.addRequest(request,"NewsCenterPager");
    }

    // 联网请求数据(联网前开Tomcat服务器，开手机WiFi)
    private void GetDataFromNet() {
        RequestParams params = new RequestParams(url); // 联网请求
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) { // 请求成功

                // 联网性能测试(多测几次看稳定范围)
                endTime = SystemClock.uptimeMillis();
                passTime = endTime-startTime;
                Toast.makeText(context,"联网时间："+passTime,Toast.LENGTH_LONG).show();

                Log.i(TAG,"联网请求成功=="+result);

                // 请求成功后，设置数据缓存
                CacheUtil.putString(context,url,result);

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

        NewsCenterBean_Hand newsCenterBean = paseJson(json);//new Gson().fromJson(json,NewsCenterBean_Hand.class); // Gson解析
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

        // 添加“新闻、专题、组图、菜单”这四个ViewPager
        detailBasePagers = new ArrayList<>();
        detailBasePagers.add(new NewsMenuDetailPager(context,leftMenuData.get(0))); // 新闻，NewsMenuDetailPager是一个ViewPager，这里先获取data[0]根数据——”新闻“(而getChildren()是在该类的构造器中)
        detailBasePagers.add(new TopicMenuDetailPager(context,leftMenuData.get(0)));// 专题
        detailBasePagers.add(new PhotosMenuDetailPager(context));                   // 组图
        detailBasePagers.add(new InteractMenuDetailPager(context));                 // 菜单

        // 在LeftMenuFragment中显示数据(只能是先特价页面，再显示数据，不然程序要崩)
        leftMenuFragment.setData(leftMenuData);
    }

    // 手动解析(Android-API)Json
    // 解析目的：使之变为Bean对象所需要的数据格式
    private NewsCenterBean_Hand paseJson(String json) { // 捕获异常(Ctrl+Alt+t)

        NewsCenterBean_Hand newscenterbean_hand = new NewsCenterBean_Hand();

        // 1 转换为Json对象
        try {
            JSONObject jsonObject = new JSONObject(json);

            int retcode = jsonObject.optInt("retcode"); // 使用optInt，即使有错，也不会崩溃。
            newscenterbean_hand.setRetcode(retcode);

            JSONArray data = jsonObject.optJSONArray("data");
            if (data != null && data.length()>0) {  // List是个接口，只能new其子类
                List<NewsCenterBean_Hand.NewsCenterBean_Data> dataBean = new ArrayList<>();
                newscenterbean_hand.setData(dataBean);
                for (int i =0; i<data.length(); i++) {
                   JSONObject jsonObject1 = (JSONObject) data.get(i);   // 得到从dat0~dat最后
                    if (jsonObject1 != null) {
                        NewsCenterBean_Hand.NewsCenterBean_Data newsCenterData = new NewsCenterBean_Hand.NewsCenterBean_Data();
                        int id = jsonObject1.optInt("id");
                        newsCenterData.setId(id);
                        String title = jsonObject1.optString("title");
                        newsCenterData.setTitle(title);
                        int type = jsonObject1.optInt("type");
                        newsCenterData.setType(type);
                        String url = jsonObject1.optString("url");
                        newsCenterData.setUrl(url);
                        String url1 = jsonObject1.optString("url1");
                        newsCenterData.setUrl(url1);
                        String dayurl = jsonObject1.optString("dayurl");
                        newsCenterData.setDayurl(dayurl);
                        String excurl = jsonObject1.optString("excurl");
                        newsCenterData.setExcurl(excurl);
                        String weekurl = jsonObject1.optString("weekurl");
                        newsCenterData.setWeekurl(weekurl);
                        dataBean.add(newsCenterData);

                        JSONArray childrenData = jsonObject1.optJSONArray("children");
                        if (childrenData != null && childrenData.length()>0) {
                            List<NewsCenterBean_Hand.NewsCenterBean_Data.Children_Data> children = new ArrayList<>(); // 泛型前边已经定了，这里可以不写了。
                            newsCenterData.setChildren(children);
                            for (int j=0; j<childrenData.length(); j++) {
                                JSONObject childrenJson = (JSONObject) childrenData.get(j);
                                if (childrenJson != null) {
                                    NewsCenterBean_Hand.NewsCenterBean_Data.Children_Data childrenData1 = new NewsCenterBean_Hand.NewsCenterBean_Data.Children_Data();
                                    childrenData1.setId(childrenJson.optInt("id"));
                                    childrenData1.setTitle(childrenJson.optString("title"));
                                    childrenData1.setType(childrenJson.optInt("type"));
                                    childrenData1.setUrl(childrenJson.optString("url"));
                                    children.add(childrenData1);
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return newscenterbean_hand;
    }

    // 根据位置，切换到对应的菜单详情页面(menudetail)
    public void switchPager(int selectPosition) {

        // 如果注释掉外层的这个if-else，就可以查看Crash日志，只能在模拟器中看。

        if (selectPosition<detailBasePagers.size()) {
            // 设置标题
            tv_title.setText(leftMenuData.get(selectPosition).getTitle());

            MenuDetailBasePager detailBasePager = detailBasePagers.get(selectPosition);
            View rootView = detailBasePager.rootView;
            // 初始化数据
            detailBasePager.initData();
            fl_base_content.removeAllViews();
            fl_base_content.addView(rootView);

            if (selectPosition==2) { // 如果切换到第3个页面——组图页面
                // 先去BasePager中实例化
                ib_switch_list_grid_view.setVisibility(View.VISIBLE);   // 显示
                ib_switch_list_grid_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhotosMenuDetailPager pager = (PhotosMenuDetailPager) detailBasePagers.get(2);
                        pager.switch_list_grid_view(ib_switch_list_grid_view);

                    }
                });
            }else {
                ib_switch_list_grid_view.setVisibility(View.GONE);      // 隐藏
            }
        }
        else {
            Toast.makeText(context, "该页面还没有启用", Toast.LENGTH_SHORT).show();
        }
    }
}

/**
 * 完整的下拉刷新效果实现：
 * 1. 下拉时，当出现箭头朝下时，是为：下拉刷新
 * 2. 当滑动到箭头朝上时(未松手)，提示:松手刷新
 * 3. 松手时，提示：正在刷新(箭头变圈圈)
 * */

package com.example.administrator.bjnews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.bjnews.R;
import com.example.administrator.bjnews.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/11 0011.
 * 自定义用于下拉刷新的ListView
 */

public class RefreshListView extends ListView{

    private LinearLayout headerView;    // 自定义一个视图(包含下拉刷新控件和顶部轮播图，使其成为一个整体)

    private View ll_pulldown_refresh;   // 下拉刷新控件
    private ImageView iv_header_arrow;
    private ProgressBar pb_header_status;
    private TextView tv_header_status;
    private TextView tv_header_time;

    private int refreshHeight;          // 下拉刷新视图的高
    private int footervHeight;          // 上拉加载视图的高
    private boolean isLoadMore=false;   // 是否加载更多

    private View topnewsview;   // 顶部轮播图

    public static final int PULLDOWN_REFRESH = 0;	    // 下拉状态
    public static final int RELEASE_REFRESH  = 1;		// 松手状态
    public static final int REFRESHING       = 2;		// 刷新状态
    private int currentState = PULLDOWN_REFRESH ;		// 当前状态(下拉刷新)


    private Animation up;           // 箭头朝上
    private Animation down;         // 箭头朝下

    private View refresh_footer_view; // 上拉加载

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView(context);    // 初始化“下拉刷新”视图(代码中创建xml)
        initAnimation();            // 下拉时的箭头动画
        initFootView(context);      // 初始化“上拉加载更多”视图(代码中创建xml)
    }

    // 上拉加载初始化
    private void initFootView(Context context) {
        refresh_footer_view=View.inflate(context,R.layout.refresh_footer,null);
        refresh_footer_view.measure(0,0);                       // 传0代表调它一下，即：调用测量
        footervHeight=refresh_footer_view.getMeasuredHeight();  // 得到上拉加载视图的高

//        refresh_footer_view.setPadding(5,5,5,5);              // 完全显示(android:padding="5dp")
        refresh_footer_view.setPadding(5,5-footervHeight,5,5);  // 隐藏

        addFooterView(refresh_footer_view);                     // 添加

        // 设置滚动监听(滑到底部即最后一条时，出现加载更多)
        setOnScrollListener(new MyOnScrollListener());
    }

    private class MyOnScrollListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // 当静止或惯性滑动到最后一个时
            if (scrollState==SCROLL_STATE_IDLE || scrollState==SCROLL_STATE_FLING) {
                if (getLastVisiblePosition()==getCount()-1) {
                    // 加载更多
                    isLoadMore=true;
                    // 显示加载更多的视图
                    refresh_footer_view.setPadding(5,5,5,5);
                    // 回调接口
                    if (mOnRereshListener != null) {
                        mOnRereshListener.onLoadMore();
                    }
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }


    // 初始化动画
    private void initAnimation() {
        up=new RotateAnimation(0,-180,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        up.setDuration(500);    // 500ms
        up.setFillAfter(true);  // 停留后的状态

        down=new RotateAnimation(-180,-360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        down.setDuration(500);    // 500ms
        down.setFillAfter(true);  // 停留后的状态
    }

    // 初始化下拉刷新视图
    private void initHeaderView(Context context) {

        headerView = (LinearLayout) View.inflate(context, R.layout.refresh_header, null);   // 下拉刷新的列表

        ll_pulldown_refresh =  headerView.findViewById(R.id.ll_pulldown_refresh);           // 下拉刷新的布局
        iv_header_arrow = (ImageView) headerView.findViewById(R.id.iv_header_arrow);        // 向上的红色箭头
        pb_header_status = (ProgressBar) headerView.findViewById(R.id.pb_header_status);    // 环形进度条
        tv_header_status = (TextView) headerView.findViewById(R.id.tv_header_status);       // 上方文字
        tv_header_time = (TextView) headerView.findViewById(R.id.tv_header_time);           // 下方文字

        ll_pulldown_refresh.measure(0,0);                           // 调用系统方法测量
        refreshHeight = ll_pulldown_refresh.getMeasuredHeight();    // 获取测量值
        ll_pulldown_refresh.setPadding(0,-refreshHeight,0,0);       // 隐藏显示(默认)
//        ll_pulldown_refresh.setPadding(0,0,0,0);                    // 完整显示
//        ll_pulldown_refresh.setPadding(0,refreshHeight,0,0);        // 两倍显示

        // 以头的方式添加下拉刷新列表
        addHeaderView(headerView);
    }

    private float startY;   // 下拉时的起始(纵)坐标

    @Override
    public boolean onTouchEvent(MotionEvent ev) {   // 重写该方法，以保证下拉时能看到效果

        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                                        startY=ev.getY();   // 记录起始坐标
                                        break;

            case MotionEvent.ACTION_MOVE:
                                        float endY=ev.getY();           // 记录结束值
                                        float distancey=endY-startY;    // 计算偏移量

                                        // 判断顶部轮播图是否完全显示
                                        boolean isDisplayTopNews = isDisplayTopNews();
                                        if (!isDisplayTopNews) { // 没完全显示
                                          break;
                                        }

                                        // 动态显示下拉刷新
                                        if (distancey>0) {
                                            int padingTop = (int) (-refreshHeight+distancey);

                                            if (padingTop>0 && currentState!=RELEASE_REFRESH) { // 松手刷新
                                                currentState=RELEASE_REFRESH;
                                                LogUtil.e("手势下拉...");
                                                // 更新状态
                                                refreshHeaderStatus();
                                            }else if (padingTop<0 && currentState!=PULLDOWN_REFRESH) { // 下拉刷新
                                                currentState=PULLDOWN_REFRESH;
                                                LogUtil.e("进入下拉刷新状态...");
                                                // 更新状态
                                                refreshHeaderStatus();
                                            }

                                            ll_pulldown_refresh.setPadding(0,padingTop,0,0);
                                        }
                                        break;

            case MotionEvent.ACTION_UP:
                                        startY=0;
                                        if (currentState==PULLDOWN_REFRESH) {   // 隐藏
                                            ll_pulldown_refresh.setPadding(0,-refreshHeight,0,0);
                                        }else if (currentState==RELEASE_REFRESH) {
                                            // 修改成正在刷新
                                            currentState=REFRESHING;
                                            ll_pulldown_refresh.setPadding(10,10,10,10); // 完全显示(没有这句，会影响下拉松手后的显示效果)
                                            refreshHeaderStatus();
                                            // 回调接口
                                            if (mOnRereshListener!=null) {
                                                mOnRereshListener.onPullDownRefresh();
                                            }
                                        }
                                        break;
        }

        return super.onTouchEvent(ev);
    }

    // 状态更新
    private void refreshHeaderStatus() {
        switch (currentState) {
            case PULLDOWN_REFRESH:  // 进入下拉刷新状态
                                    iv_header_arrow.startAnimation(down); // 箭头朝下
                                    iv_header_arrow.setVisibility(View.VISIBLE);
                                    pb_header_status.setVisibility(View.GONE);
                                    tv_header_status.setText("下拉刷新...");
                                    break;

            case RELEASE_REFRESH:   // 手势下拉
                                    iv_header_arrow.startAnimation(up); // 箭头朝上
                                    tv_header_status.setText("松手刷新...");
                                    break;

            case REFRESHING:        // 正在刷新
                                    iv_header_arrow.clearAnimation(); // 停止动画
                                    pb_header_status.setVisibility(View.VISIBLE);
                                    iv_header_arrow.setVisibility(View.GONE);
                                    tv_header_status.setText("正在刷新...");
                                    break;
        }
    }

    // 判断顶部轮播图是否完全显示
    // 原理：当ListView在屏幕上的Y坐标(定值) <= 顶部轮播图在Y轴坐标(动值)的时候，即为完全显示(应对加载更多时候的Bug)。
    // 作用：解决加载更多时候的Bug

    private float mListViewOnScreenY = -1; // 记录ListView在屏幕上的坐标，默认为-1

    private boolean isDisplayTopNews() {

        int[] location = new int[2];

        // 得到ListView在屏幕上的坐标
        if (mListViewOnScreenY==-1) {
            this.getLocationOnScreen(location);
            mListViewOnScreenY=location[1];
        }

        // 得到顶部轮播图在屏幕上的坐标
        topnewsview.getLocationOnScreen(location);
        float mtopnewsViewOnScreeny=location[1];

        /*if (mListViewOnScreenY<=mtopnewsViewOnScreeny) {
            return true;
        }else {
            return false;
        }*/

        return mListViewOnScreenY<=mtopnewsViewOnScreeny;    // 默认显示
    }

    // 添加顶部轮播图，使其与下拉刷新控件refresh_header整合成一个整体
    public void addTopNewsView(View topnewsview) {
        this.topnewsview=topnewsview;

        if (topnewsview != null) {
            headerView.addView(topnewsview);
        }

    }

    // 下拉刷新完成，把状态设置为默认
    // 而最终实现效果是，联网后，可显示系统时间。
    public void OnRefreshFinish(boolean isSuccess) {

        //(若写在这里，则真机与模拟器有差异，真机即使在断网情况下，会隐藏下拉刷新，模拟器会一直显示圈圈)
        /*currentState = PULLDOWN_REFRESH;
        pb_header_status.setVisibility(View.GONE);
        iv_header_arrow.setVisibility(View.VISIBLE);
        ll_pulldown_refresh.setPadding(0,-refreshHeight,0,0);*/

        if (isSuccess) {
            // 写在这里是为了适合真机效果(有网显示系统时间，松手后下拉刷新效果消失，无网则一直圈圈)
            currentState = PULLDOWN_REFRESH;
            pb_header_status.setVisibility(View.GONE);
            iv_header_arrow.setVisibility(View.VISIBLE);
            ll_pulldown_refresh.setPadding(0,-refreshHeight,0,0);

            // 更新刷新时间
            tv_header_time.setText("上次更新时间"+getSystemTime());
        }

    }

    // 得到系统时间
    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    // 自定义接口，回调的时候联网请求
    public interface OnRefreshListener {
        public void onPullDownRefresh();	// 下拉刷新时，回调该方法
        public void onLoadMore();           // 当加载更多时，回调该方法
    }

    private OnRefreshListener mOnRereshListener;

    // 设置刷新的监听：下拉刷新和加载更多
    public void setOnRefreshListener(OnRefreshListener l) {
        mOnRereshListener=l;
    }


}

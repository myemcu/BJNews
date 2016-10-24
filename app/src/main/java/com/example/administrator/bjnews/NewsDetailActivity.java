package com.example.administrator.bjnews;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.Normalizer;

/**
 * Created by Administrator on 2016/10/23 0023.
 * 新闻浏览页面
 */
public class NewsDetailActivity extends Activity{

    private WebView webview;    // webview 可加载网页(包括视屏及图片，同时支持HTML与H5)
    private String url;

    private ProgressBar pb_footer_status;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        findViewById(R.id.tv_title).setVisibility(View.GONE);

        findViewById(R.id.ib_back).setVisibility(View.VISIBLE);
        findViewById(R.id.ib_textsize).setVisibility(View.VISIBLE);
        findViewById(R.id.ib_share).setVisibility(View.VISIBLE);

        webview = (WebView) findViewById(R.id.webview);
        pb_footer_status = (ProgressBar) findViewById(R.id.pb_footer_status); // 圈圈


        // 设置公共点击事件
        MyOnClickListener myOnClickListener = new MyOnClickListener();
        findViewById(R.id.ib_back).setOnClickListener(myOnClickListener);
        findViewById(R.id.ib_textsize).setOnClickListener(myOnClickListener);
        findViewById(R.id.ib_share).setOnClickListener(myOnClickListener);

        // 加载WebSite(Url)
        url = getIntent().getStringExtra("url");
//        url = "http://www.atguigu.com/teacher.shtml";
//        url = "http://www.baidu.com";
        webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);         // 支持JS(WebView必备)
        webSettings.setBuiltInZoomControls(true);       // 添加缩放按钮
        webSettings.setUseWideViewPort(true);           // 设置双击变大变小(与页面有关)
        webview.loadUrl(url);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //Toast.makeText(NewsDetailActivity.this,"网页加载完成",Toast.LENGTH_SHORT).show();
                pb_footer_status.setVisibility(View.GONE);
            }
        });
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.ib_back:
                                        finish();
                                        break;

                case R.id.ib_textsize:
                                        Toast.makeText(NewsDetailActivity.this,"文字大小",Toast.LENGTH_SHORT).show();
                                        showChangeTextSizeDialog();
                                        break;

                case R.id.ib_share:
                                        Toast.makeText(NewsDetailActivity.this,"内容分享",Toast.LENGTH_SHORT).show();
                                        break;
                default:break;
            }
        }
    }

    private int tempSize = 2;
    private int realSize = tempSize;

    private void showChangeTextSizeDialog() {
        String[] items = {"超大字体","大号字体","正常字体","小字体","超小字体"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);// 从this中new出对话框
        builder.setTitle("设置文字大小");
        builder.setSingleChoiceItems(items, realSize, new DialogInterface.OnClickListener() { // 2.正常
            @Override
            public void onClick(DialogInterface dialog, int which) {
               tempSize=which;// 当它一点，就改变
            }
        });// 设置单选项
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                realSize=tempSize;
                ChangeTextSize(realSize);
            }
        });
        builder.show();
    }

    private void ChangeTextSize(int realSize) {
        switch (realSize) {

            /*case 0: webSettings.setTextSize(WebSettings.TextSize.LARGEST);  break;
            case 1: webSettings.setTextSize(WebSettings.TextSize.LARGER);   break;
            case 2: webSettings.setTextSize(WebSettings.TextSize.NORMAL);   break;
            case 3: webSettings.setTextSize(WebSettings.TextSize.SMALLER);  break;
            case 4: webSettings.setTextSize(WebSettings.TextSize.SMALLEST); break;*/

            case 0: webSettings.setTextZoom(200);  break;
            case 1: webSettings.setTextZoom(150);  break;
            case 2: webSettings.setTextZoom(100);  break;
            case 3: webSettings.setTextZoom(75);  break;
            case 4: webSettings.setTextZoom(50);  break;

            default:break;
        }
    }
}

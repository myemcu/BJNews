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

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

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
                                        showShare();
                                        break;
                default:break;
            }
        }
    }

    private void showShare() {

        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();

        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("SharkSDK分享测试");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://image.baidu.com/search/detail?z=0&ipn=d&word=火影&step_word=&hs=0&pn=3&spn=0&di=73630928800&pi=&tn=baiduimagedetail&istype=0&ie=utf-8&oe=utf-8&cs=3811705734%2C4142223178&os=307029396%2C1034527739&simid=0%2C0&adpicid=0&fm=&sme=&cg=&bdtype=0&simics=1466803426%2C2815678637&oriquery=&objurl=http%3A%2F%2Fwww.52tian.net%2Ffile%2Fimage%2F20150610%2F20150610101993489348.png&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bcdptwg_z%26e3BgjpAzdH3FozAzdH3Fd9lAzdH3F&gsm=0&cardserver=1");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("http://image.baidu.com/search/detail?z=0&ipn=d&word=火影&step_word=&hs=0&pn=3&spn=0&di=73630928800&pi=&tn=baiduimagedetail&istype=0&ie=utf-8&oe=utf-8&cs=3811705734%2C4142223178&os=307029396%2C1034527739&simid=0%2C0&adpicid=0&fm=&sme=&cg=&bdtype=0&simics=1466803426%2C2815678637&oriquery=&objurl=http%3A%2F%2Fwww.52tian.net%2Ffile%2Fimage%2F20150610%2F20150610101993489348.png&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bcdptwg_z%26e3BgjpAzdH3FozAzdH3Fd9lAzdH3F&gsm=0&cardserver=1");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://image.baidu.com/search/detail?z=0&ipn=d&word=火影&step_word=&hs=0&pn=3&spn=0&di=73630928800&pi=&tn=baiduimagedetail&istype=0&ie=utf-8&oe=utf-8&cs=3811705734%2C4142223178&os=307029396%2C1034527739&simid=0%2C0&adpicid=0&fm=&sme=&cg=&bdtype=0&simics=1466803426%2C2815678637&oriquery=&objurl=http%3A%2F%2Fwww.52tian.net%2Ffile%2Fimage%2F20150610%2F20150610101993489348.png&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bcdptwg_z%26e3BgjpAzdH3FozAzdH3Fd9lAzdH3F&gsm=0&cardserver=1");

        // 启动分享GUI
        oks.show(this);
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

package com.example.administrator.bjnews;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/9/7 0007.
 * 主页面
 */
public class MainActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView txt = new TextView(this);
        txt.setText("欢迎来到主页面——北京新闻客户端");
        txt.setGravity(Gravity.CENTER);
        txt.setTextSize(20);
        txt.setTextColor(0xcd0000cd);

        setContentView(txt);
    }
}

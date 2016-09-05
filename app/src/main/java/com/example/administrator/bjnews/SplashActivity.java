package com.example.administrator.bjnews;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

public class SplashActivity extends Activity {

    private RelativeLayout rl_splash_root;  // 定义相对布局对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // 三个动画(缩放，渐变，旋转)(也叫补间动画或视图动画)顺序执行


        // 1 缩放动画                           0:很小，1：完整，相对于自身
        ScaleAnimation sa = new ScaleAnimation(0,1,0,1,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);  // Ctrl+P显示括号中的参数，选最长的
        sa.setDuration(2000);   // 动画持续2s
        sa.setFillAfter(true);  // 停留在播放后的状态

        // 2 渐变动画
        AlphaAnimation aa = new AlphaAnimation(0,1);
        aa.setDuration(2000);
        aa.setFillAfter(true);

        // 3 旋转动画
        RotateAnimation ra = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        ra.setDuration(2000);
        ra.setFillAfter(true);

        // 同时播放3个动画
        AnimationSet set = new AnimationSet(false); // false:插入器
        set.addAnimation(sa);
        set.addAnimation(aa);
        set.addAnimation(ra);

        // 实例化相对布局对象
        rl_splash_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
        rl_splash_root.startAnimation(set);
    }
}

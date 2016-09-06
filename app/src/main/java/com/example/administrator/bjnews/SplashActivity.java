package com.example.administrator.bjnews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SplashActivity extends Activity {

    private RelativeLayout rl_splash_root;  // 定义相对布局对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // 三个动画(缩放，渐变，旋转)(也叫补间动画或视图动画)顺序执行


        // 1 缩放动画                           0:很小，1：完整，相对于自身
        ScaleAnimation sa = new ScaleAnimation(0,1,0,1,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);  // Ctrl+P显示括号中的参数，选最长的

        // 2 渐变动画
        AlphaAnimation aa = new AlphaAnimation(0,1);

        // 3 旋转动画
        RotateAnimation ra = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);

        // 同时播放3个动画
        AnimationSet set = new AnimationSet(false); // false:插入器
        set.setDuration(2000);                      // 延时
        set.setFillAfter(true);                     // 停留在播放后的状态
        set.addAnimation(sa);
        set.addAnimation(aa);
        set.addAnimation(ra);

        // 实例化相对布局对象
        rl_splash_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
        rl_splash_root.startAnimation(set);

        //------------------------------------------------------------------------------------------

        // 监听动画播放完成，监听对象：set
        set.setAnimationListener(new MyAnimationListener());
    }

    class MyAnimationListener implements Animation.AnimationListener{

        /*当动画开始播放的时候回调这个方法*/

        @Override
        public void onAnimationStart(Animation animation) {

        }

        /*当动画播放完成的时候回调这方法*/
        @Override
        public void onAnimationEnd(Animation animation) {
            //Toast.makeText(SplashActivity.this,"动画播放完成",Toast.LENGTH_LONG).show();
            Toast.makeText(SplashActivity.this,"动画完成，出现引导页面",Toast.LENGTH_LONG).show();
            // 跳转到引导页面
            Intent intent = new Intent(SplashActivity.this,GuideActivity.class);
            startActivity(intent);
            // 关闭启动(动画)页面
            finish();
        }

        /*当动画重复播放的时候回调这个方法*/
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}

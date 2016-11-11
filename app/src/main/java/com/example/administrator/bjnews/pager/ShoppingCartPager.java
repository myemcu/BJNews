package com.example.administrator.bjnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.example.administrator.bjnews.base.BasePager;
import com.example.administrator.bjnews.bean.ShoppingCart;
import com.example.administrator.bjnews.utils.CartProvider;
import com.example.administrator.bjnews.utils.LogUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/9/12 0012.
 * 购物车
 */
public class ShoppingCartPager extends BasePager {

    private TextView txt;
    private CartProvider cartProvider;  // 定义购物车缓存类

    public ShoppingCartPager(Context context) {
        super(context);
        cartProvider=new CartProvider(context);
    }

    @Override
    public void initData() {
        super.initData();

        System.out.println("购物车的数据被初始化了....");

        // 设置标题
        tv_title.setText("购物车");

        // 设置内容(子视图)
        txt = new TextView(context); // 在当前页创建一个TextView文本
        txt.setGravity(Gravity.CENTER);
        txt.setText("购物车的内容");
        txt.setTextSize(25);
        txt.setTextColor(Color.RED);

        fl_base_content.addView(txt);   // 把子视图添加到FrameLayout中

        showData(); // 显示来自商城热卖中的点击购买的数据
    }

    private void showData() {
        List<ShoppingCart> carts = cartProvider.getAllData();
        for (int i=0;i<carts.size();i++) {
            ShoppingCart cart = carts.get(i);
            LogUtil.e(cart.getName().toString()+" 单价："+cart.getPrice()+"");
            LogUtil.e("数量："+cart.getCount()+"");
        }
    }
}

package com.example.administrator.bjnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.administrator.bjnews.R;
import com.example.administrator.bjnews.adapter.ShoppingPagerAdapter;
import com.example.administrator.bjnews.base.BasePager;
import com.example.administrator.bjnews.bean.ShoppingCart;
import com.example.administrator.bjnews.utils.CartProvider;
import com.example.administrator.bjnews.utils.LogUtil;
import com.example.administrator.bjnews.view.NumberAddSubView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/12 0012.
 * 购物车
 */
public class ShoppingCartPager extends BasePager {

    //private TextView txt;

    private CartProvider cartProvider;  // 定义购物车缓存类

    private RecyclerView recycler_view;
    private CheckBox check_all;
    private TextView tv_total;
    private Button btn_order,btn_delete;
    private TextView tv_result;

    private List<ShoppingCart> datas;   // 购物车数据

    private ShoppingPagerAdapter adapter;

    //private NumberAddSubView number_add_sub_view;

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
        btn_cart_edit.setVisibility(View.VISIBLE);  // 显示编辑按钮

        // 设置内容(子视图)
        /*txt = new TextView(context); // 在当前页创建一个TextView文本
        txt.setGravity(Gravity.CENTER);
        txt.setText("购物车的内容");
        txt.setTextSize(25);
        txt.setTextColor(Color.RED);*/

        View view = View.inflate(context, R.layout.shoppingcart_pager,null);

        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        check_all = (CheckBox) view.findViewById(R.id.check_all);
        tv_total = (TextView) view.findViewById(R.id.tv_total);
        btn_order = (Button) view.findViewById(R.id.btn_order);
        btn_delete = (Button) view.findViewById(R.id.btn_delete);
        tv_result = (TextView) view.findViewById(R.id.tv_result);

        if (fl_base_content != null) {
            fl_base_content.removeAllViews();   // 这个很重要(避免页面重影)
        }
        fl_base_content.addView(view);   // 把子视图添加到FrameLayout中

        showData(); // 显示来自商城热卖中的点击购买的数据
    }

    private void showData() {               // LogCat：Shopping
        datas = cartProvider.getAllData();  // 获取购物车数据
        showCartDatas(datas);               // 打印购物车数据
        if (datas!=null && datas.size()>0) {// 确保非空
            // 有数据
            tv_result.setVisibility(View.GONE);

            // 代码方式设置控件属性
            /*number_add_sub_view.setValue(6);
            number_add_sub_view.setMinValue(2);
            number_add_sub_view.setMaxValue(16);*/

            // 设置适配器
            adapter=new ShoppingPagerAdapter(context,datas,check_all,tv_total);
            recycler_view.setAdapter(adapter);
            recycler_view.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

        }else {
            // 无数据
            tv_result.setVisibility(View.VISIBLE);
        }
    }

    // 打印购物车数据
    private void showCartDatas(List<ShoppingCart> carts) {
        for (int i=0;i<carts.size();i++) {
            ShoppingCart cart = carts.get(i);
            LogUtil.e(cart.getId()+"");
            LogUtil.e(cart.getName().toString()+" 单价："+cart.getPrice()+"");
            LogUtil.e("数量："+cart.getCount()+"");
            LogUtil.e("isChecked:"+cart.isChecked()+"");
        }
    }
}

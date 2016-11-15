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

    private static final int ATION_EDIT  = 0;    // 编辑按钮的编辑状态
    private static final int ATION_FINSH = 1;    // 编辑按钮的完成状态

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

        findViews(view);

        if (fl_base_content != null) {
            fl_base_content.removeAllViews();   // 这个很重要(避免页面重影)
        }

        fl_base_content.addView(view);          // 把子视图添加到FrameLayout中

        tv_total.setText("");                   // 空购物时无价格

        btn_cart_edit.setText("编辑");           // 每次切到该页面时，显示编辑

        // 设置编辑按钮的点击事件
        btn_cart_edit.setTag(ATION_EDIT);
        btn_cart_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int action = (int) btn_cart_edit.getTag();
                switch (action) {
                    case ATION_EDIT:
                        // 变成完成状态
                        showDeleteButton(); // 勾选全消，按钮显示为“删除”
                        break;

                    case ATION_FINSH:       // 全选使能，按钮显示为“去结算”
                        // 变成编辑状态
                        hideDeleteButton();
                        break;

                    default:break;
                }
            }
        });

        // 监听“删除”按钮事件
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.deleteData();
                adapter.showTotalPrice();
                checkData();        // 校验数据
                adapter.checkAll(); // 校验全选
            }
        });

        showData();                     // 显示来自商城热卖中的点击购买的数据
    }

    private void checkData() {
        // 没有这一坨的话，购物车清空的时候会Crash
        if (adapter != null && adapter.getItemCount()>0) {
            tv_result.setVisibility(View.GONE);
        }else {
            tv_result.setVisibility(View.VISIBLE);
        }
    }

    // 显示“删除”按钮
    private void showDeleteButton() {
        // 1 文本设置为完成
        btn_cart_edit.setText("完成");
        // 2 状态设置为完成
        btn_cart_edit.setTag(ATION_FINSH);
        // 3 数据设置非全选
        adapter.checkAllNone(false);    // 设置
        adapter.checkAll();             // 校验
        // 4 删除按钮显示，结算按钮隐藏
        btn_delete.setVisibility(View.VISIBLE);
        btn_order.setVisibility(View.GONE);
        // 5 重新算价
        adapter.showTotalPrice();
    }

    // 隐藏“删除”按钮
    private void hideDeleteButton() {
        // 1 文本设置为完成
        btn_cart_edit.setText("编辑");
        // 2 状态设置为编辑
        btn_cart_edit.setTag(ATION_EDIT);
        // 3 数据设置非全选
        adapter.checkAllNone(true);     // 设置
        adapter.checkAll();             // 校验
        // 4 删除按钮隐藏，结算按钮显示
        btn_delete.setVisibility(View.GONE);
        btn_order.setVisibility(View.VISIBLE);
        // 5 重新算价
        adapter.showTotalPrice();
    }

    private void findViews(View view) {
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        check_all = (CheckBox) view.findViewById(R.id.check_all);
        tv_total = (TextView) view.findViewById(R.id.tv_total);
        btn_order = (Button) view.findViewById(R.id.btn_order);
        btn_delete = (Button) view.findViewById(R.id.btn_delete);
        tv_result = (TextView) view.findViewById(R.id.tv_result);
    }

    private void showData() {               // LogCat：Shopping
        datas = cartProvider.getAllData();  // 获取购物车数据
        showCartDatas(datas);               // 打印购物车数据
        if (datas!=null && datas.size()>0) {// 确保非空
            // 有数据
            tv_result.setVisibility(View.GONE);

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

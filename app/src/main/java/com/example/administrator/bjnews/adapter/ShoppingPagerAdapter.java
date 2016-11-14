package com.example.administrator.bjnews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.bjnews.R;
import com.example.administrator.bjnews.bean.ShoppingCart;
import com.example.administrator.bjnews.view.NumberAddSubView;

import java.util.List;

/**
 * Created by Administrator on 2016/11/14 0014.
 * 购物车适配器
 */

public class ShoppingPagerAdapter extends RecyclerView.Adapter<ShoppingPagerAdapter.ViewHolder>{

    private final Context context;
    private final List<ShoppingCart> datas;
    private final CheckBox check_all;
    private final TextView tv_total;

    public ShoppingPagerAdapter(Context context, List<ShoppingCart> datas, CheckBox check_all, TextView tv_total) {    // 上下文，购物车数据
        this.context=context;
        this.datas=datas;
        this.check_all=check_all;
        this.tv_total=tv_total;

        showTotalPrice();
    }

    // 显示总价
    private void showTotalPrice() {
        tv_total.setText("合计￥"+getTotalPrice());
    }

    // 得到购物车选中商品好的总价格
    private double getTotalPrice() {
        double totalPrice=0;
        if (datas!=null && datas.size()>0) {
            for (int i=0;i<datas.size();i++) {
                ShoppingCart cart = datas.get(i);
                if (cart.isChecked()) {                             // 只有选中才计算总价
                    totalPrice+=cart.getCount()*cart.getPrice();    // 物品数量*单价
                }
            }
        }
        return totalPrice;
    }

    @Override
    // 创建视图并通过ViewHolder返回
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.item_shopping_carts,null);
        return new ViewHolder(itemView);
    }

    @Override
    // 绑定数据
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 1 根据位置得到对应数据
        ShoppingCart cart = datas.get(position);
        // 2 绑定数据
        // 2.1 图片数据
        Glide.with(context)
                .load(cart.getImgUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.home_scroll_default)    // 默认加载图片
                .error(R.drawable.home_scroll_default)          // 失败加载图片
                .into(holder.iv_icon);
        // 2.2 Name数据
        holder.tv_name.setText(cart.getName());
        // 2.3 Price数据
        holder.tv_price.setText("￥"+(int) cart.getPrice());
        // 2.4 加减键当前值
        holder.numberAddSubView.setValue(cart.getCount());
        // 2.5 CheckBox是否为选中状态
        holder.checkbox.setChecked(cart.isChecked());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkbox;
        private ImageView iv_icon;
        private TextView tv_name,tv_price;
        private NumberAddSubView numberAddSubView;

        // 构造器
        public ViewHolder(View itemView) {
            super(itemView);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            numberAddSubView = (NumberAddSubView) itemView.findViewById(R.id.numberAddSubView);
        }
    }
}

package com.example.administrator.bjnews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.bjnews.R;
import com.example.administrator.bjnews.bean.ShopPagerBean;
import com.example.administrator.bjnews.bean.ShoppingCart;
import com.example.administrator.bjnews.utils.CartProvider;

import java.util.List;

/**
 * Created by Administrator on 2016/11/9 0009.
 * 商城热卖适配器
 */

public class ShopPagerRecyclerViewAdapter extends RecyclerView.Adapter<ShopPagerRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final List<ShopPagerBean.Wares> datas;

    private CartProvider cartProvider;  // 购物车缓存工具对象


    // 创建用于接收上下文与商品数据的构造器
    public ShopPagerRecyclerViewAdapter(Context context, List<ShopPagerBean.Wares> datas) {
       this.context=context;
       this.datas=datas;
       cartProvider = new CartProvider(context);   // new出购物车缓存类
    }

    // 清除数据
    public void clearData() {
        datas.clear();
        notifyItemRangeChanged(0,datas.size());
    }

    // 根据指定位置添加数据
    public void addData(int position, List<ShopPagerBean.Wares> data) {
        if (data != null && data.size()>0) {
            datas.addAll(position,data);
            notifyItemRangeChanged(position,datas.size());
        }
    }

    // 得到总条数
    public int getDataCount() {
        return datas.size();
    }

    // 创建适配器自身的ViewHolder方法
    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_icon;
        private TextView tv_name,tv_price;
        private Button btn_buy;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            btn_buy = (Button) itemView.findViewById(R.id.btn_buy);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {  // 创建视图与ViewHolder
        View itemView = View.inflate(context, R.layout.item_shop_pager,null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) { // 绑定数据
        // 1 根据位置，得到数据
        final ShopPagerBean.Wares wares = datas.get(position);
        // 2 绑定数据
        Glide.with(context)
                .load(wares.getImgUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.home_scroll_default)    // 默认加载图片
                .error(R.drawable.home_scroll_default)          // 失败加载图片
                .into(holder.iv_icon);

        holder.tv_name.setText(wares.getName());
        holder.tv_price.setText("￥"+(int) wares.getPrice());

        holder.btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context, "价格："+wares.getPrice(), Toast.LENGTH_SHORT).show();
                // 把商品Wares转换成ShoppingCart
                ShoppingCart cart = cartProvider.conversion(wares);
                cartProvider.addData(cart);
                // Toast.makeText(context,"购买成功",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() { // 返回商品总条数
        return datas.size();
    }
}

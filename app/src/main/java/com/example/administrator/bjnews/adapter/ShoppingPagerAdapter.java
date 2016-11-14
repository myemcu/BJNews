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
import com.example.administrator.bjnews.utils.CartProvider;
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
    private final TextView tv_totalPrice;
    
    private CartProvider cartProvider;

    public ShoppingPagerAdapter(Context context, final List<ShoppingCart> datas, CheckBox check_all, TextView tv_totalPrice) {    // 上下文，购物车数据
        this.context=context;
        this.datas=datas;
        this.check_all=check_all;
        this.tv_totalPrice=tv_totalPrice;
        cartProvider=new CartProvider(context);
        showTotalPrice();
        // 设置Item监听
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 1 得到对应位置的对象
                ShoppingCart cart = datas.get(position);
                // 2 勾选状态取反
                cart.setChecked(!cart.isChecked());
                // 3 状态刷新
                notifyItemChanged(position);
                // 4 校验全选和非全选
                checkAll();
                // 5 显示总价
                showTotalPrice();
            }
        });
        // 校验全选
        checkAll();
    }

    // 校验全选和非全选
    private void checkAll() {
        int number=0;

        if (datas != null && datas.size()>0) {
            for (int i=0;i<datas.size();i++) {
                ShoppingCart cart = datas.get(i);
                if (!cart.isChecked()) {        // 只要有一个没被选，非全选
                    check_all.setChecked(false);// 非勾选
                }else {
                    // 选中
                    number++;
                }
            }
            if (number==datas.size()) {     // 选中的个数和集合总数相同
                check_all.setChecked(true); // 勾选
            }
        }
    }

    // 显示总价
    private void showTotalPrice() {
        tv_totalPrice.setText("合计￥"+getTotalPrice());
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
        final ShoppingCart cart = datas.get(position);
        
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
        
        // 3 设置增加及减少按钮的点击监听
        holder.numberAddSubView.setOnNumClickListener(new NumberAddSubView.OnNumClickListener() {
            @Override
            public void onButtonSub(View view, int value) {
                addSubTotalPrice(value);
            }

            @Override
            public void onButtonAdd(View view, int value) {
                addSubTotalPrice(value);
            }

            private void addSubTotalPrice(int value) {
                // 1 更新数据
                cart.setCount(value);
                // 2 保存数据
                cartProvider.updateData(cart);
                // 3 重新显示价格
                showTotalPrice();
            }
        });
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener!=null) {
                        onItemClickListener.onItemClick(v,getLayoutPosition());
                    }
                }
            });
        }
    }

    // RecyclerView的点击某条时的监听，被点击时回调
    public interface OnItemClickListener {          // 点击接口
        void onItemClick(View view,int position);   // 点击回调
    }

    private OnItemClickListener onItemClickListener;// Alt+Insert-->Setter

    // 设置某条的监听
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener; // 注意：一定要写这个This
    }
}

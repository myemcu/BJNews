package com.example.administrator.bjnews.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/11 0011.
 * 购物车类ShoppingCart继承Wares
 * 功能：用来记录商品在购物车中的状态(有多少个商品，是否被选中，商品相关信息)
 */

public class ShoppingCart extends ShopPagerBean.Wares implements Serializable{  // implements Serializable(为了数据能持久化存储到本地)
    private int count = 1;              // 购物车中的商品数量，默认为1
    private boolean isChecked = true;   // 是否勾选，默认为是

    // Alt+Insert(Getter and Setter and toString)
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "count=" + count +
                ", isChecked=" + isChecked +
                '}';
    }
}

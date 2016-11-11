package com.example.administrator.bjnews.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.example.administrator.bjnews.bean.ShopPagerBean;
import com.example.administrator.bjnews.bean.ShoppingCart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/11 0011.
 * 购物车存取类
 * 功能：存：集合转String(Gson)
 *      取：String转集合(Gson)
 */

public class CartProvider  {

    public static final String JSON_CART = "json_cart"; // json_cart(Ctrl+Alt+C)
    private final Context context;
    private SparseArray<ShoppingCart> datas;    // SparseArray<>：性能优化，是HashMap的替代者(由Android引入，非java)

    public CartProvider(Context context) {      // 构造器
        this.context=context;
        datas=new SparseArray<>(10);            // 10:长度
        listToSparse();                         // 把商城页面的列表数据转到购物车页面中
    }

    private void listToSparse() {               // 商城数据转购物车
        List<ShoppingCart> carts = getAllData();
        if (carts != null && carts.size()>0) {
            for (int i=0;i<carts.size();i++) {
                ShoppingCart cart = carts.get(i);
                datas.put(cart.getId(),cart);
            }
        }
    }

    public List<ShoppingCart> getAllData() {       // 得到所有数据
        return getDataFromLocal();
    }

    private List<ShoppingCart> getDataFromLocal() { // 获取本地json数据，并且通过Gson解析成List列表数据
        List<ShoppingCart> carts = new ArrayList<>();
        // 从本地获取缓存的数据
        String saveJson = CacheUtil.getString(context,JSON_CART);
        // saveJson判空
        if (!TextUtils.isEmpty(saveJson)) { // 通过Gson，将数据转换成List列表
            carts = new Gson().fromJson(saveJson,new TypeToken<List<ShoppingCart>>(){}.getType());  // TypeToken<>:将json数据转化成列表（专用）
        }
        return carts;
    }


    /*数据的adu(添加，删除，更新)*/
    public void addData(ShoppingCart cart) {    // 增加数据
        // 1 添加数据
        ShoppingCart tempcart = datas.get(cart.getId());
        if (tempcart != null) { // 列表中已存在该条数据
            tempcart.setCount(tempcart.getCount()+1);
        }else {
            tempcart = cart;
            tempcart.setCount(1);   // 设置商品数量为1
        }
        datas.put(tempcart.getId(),tempcart);
        // 2 保存数据
        commit();
    }

    public void delData(ShoppingCart cart) {    // 删除数据
        // 1 删除数据
        datas.delete(cart.getId());
        // 2 保存数据
        commit();
    }

    public void updateData(ShoppingCart cart) {    // 更新数据
        // 1 更新数据(count)
        datas.put(cart.getId(),cart);   // 覆盖原理
        // 2 保存数据
        commit();
    }

    private void commit() { // 保存数据
        // 1 把Sparse转换成List
        List<ShoppingCart> carts = parsesToList();
        // 2 把List转换成String(Gson)
        String json = new Gson().toJson(carts);
        // 3 保存数据(Shareprensces)
        CacheUtil.putString(context,JSON_CART,json);
    }

    // 把SparesArray数据转换成List列表数据
    private List<ShoppingCart> parsesToList() {
        List<ShoppingCart> carts = new ArrayList<>();
        if (datas!=null && datas.size()>0) {
            for (int i=0;i<datas.size();i++) {
                ShoppingCart cart = datas.valueAt(i);// SparesArray专用取值方法
                carts.add(cart);
            }
        }
        return carts;
    }

    // 把热卖商品(Wares)，转换成ShoppingCart
    public ShoppingCart conversion(ShopPagerBean.Wares wares) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(wares.getId());
        cart.setName(wares.getName());
        cart.setImgUrl(wares.getImgUrl());
        cart.setDescription(wares.getDescription());
        cart.setPrice(wares.getPrice());
        cart.setSale(wares.getSale());
        return cart;
    }
}

package com.example.administrator.bjnews.pager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.example.administrator.bjnews.R;
import com.example.administrator.bjnews.adapter.ShoppingPagerAdapter;
import com.example.administrator.bjnews.base.BasePager;
import com.example.administrator.bjnews.bean.ShoppingCart;
import com.example.administrator.bjnews.utils.CartProvider;
import com.example.administrator.bjnews.utils.LogUtil;
import com.example.administrator.bjnews.utils.alipay.PayResult;
import com.example.administrator.bjnews.utils.alipay.SignUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

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

    // -支付宝相关-------------------------------------------------------------------------------------
    public static final String PARTNER = "2088911876712776";		// 商户PID
    public static final String SELLER = "chenlei@atguigu.com";		// 商户收款账号
    public static final String RSA_PRIVATE = 						// 商户私钥，pkcs8格式(RSA压缩包)
                    "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMplsAJq4gapyKxH\n" +
                    "36HdORafDUw+k+6xTD2DhpTiAnR8vOsYK+4Q+ap0rPxWJ8V+tbu70YnOENYofbDq\n" +
                    "Wm4Z72FjFttYG68cVXvt+094cKeRMVUKov10McEjBheTWz+ovGtwEcWirADzcLan\n" +
                    "INH+hzygkVl9JNrM8vQ56o5zLIslAgMBAAECgYEAwEh3PPS6I0ZuBW6LAUYglGMq\n" +
                    "yQKynBQIsHAiDZPibTSMbnUaVyP9bXlHVrurMS++C8rVuSvyRuNU9hzPKMau6Y9g\n" +
                    "uT6uNhInmXw2qnRampEzaolxxO4A4cOXkxzvrc3UIrHj+ZeO7bFJ4CS7t0tWqrIL\n" +
                    "ElfcbZk7SxTRmW0X+cECQQDx511LYXXdcunnISSaM6uivY/tTxUweIIqKom6bSN0\n" +
                    "G79zGAUhbgHrryWoAIQVGdFMoMvh41/xWjZZRsW3XcVNAkEA1jD6bgcmTkwAxVpX\n" +
                    "bRKljffYgGjCk/nIlD+DiILVgOhHPodzNSZTCe3JFdbtdubHXlfWHaZTG5IDhTGY\n" +
                    "GzeROQJAGL3QNfiG5JpvP/uM0dS9Fk0LHnt7MFTzAzsMkSu+d46q+yuWwr+MpL1q\n" +
                    "PY+n8ryMQqkjkTv/QSE389OgP0vw9QJBAL1M4jfWB8vRLik9s+DJRxoWvviWHcns\n" +
                    "S5KCI7zFbO6V20lvKqoBqggvaLXtk0evpOpDlQ/1TKQkQwo3j6gWTzkCQQCTBiBu\n" +
                    "9cPPckdj4co1EZCb0wpdJFURjIxVIKDbbrhORVeGReKMf16qnwt9iht1zGERmqCq\n" +
                    "1BDTFumQaY1R3vET";
    public static final String RSA_PUBLIC = 						// 支付宝公钥
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDKZbACauIGqcisR9+h3TkWnw1M\n" +
                    "PpPusUw9g4aU4gJ0fLzrGCvuEPmqdKz8VifFfrW7u9GJzhDWKH2w6lpuGe9hYxbb\n" +
                    "WBuvHFV77ftPeHCnkTFVCqL9dDHBIwYXk1s/qLxrcBHFoqwA83C2pyDR/oc8oJFZ\n" +
                    "fSTazPL0OeqOcyyLJQIDAQAB";

    // 完事后，还必须再在企业支付宝网页的“合作伙伴密钥管理中”重新设置用RSA生成器生成的公钥(手动修改成无空格后保存)
    // 然后再打开企业账户的提现页面，开手机官方支付Demo后支付，支付后再刷新此提现页即可查看新的收入。

    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(context, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    //----------------------------------------------------------------------------------------------

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
                        if (datas != null && datas.size()>0) { // 确保购物车有东东，编辑按钮才有效，没有这个判断，初次进页面点编辑时，App要崩
                            showDeleteButton(); // 勾选全消，按钮显示为“删除”
                        }else {
                            Toast.makeText(context,"亲，购物车还是空的",Toast.LENGTH_SHORT).show();
                        }
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

        // 设置“结算”按钮的点击事件
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datas!=null && datas.size()>0 && adapter.getTotalPrice()>0) {    // 确保有货
                    pay(v); // 调用支付宝SDK进行支付
                }
                else {
                    Toast.makeText(context,"请先购买!",Toast.LENGTH_SHORT).show();
                }
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

    /**
     * call alipay sdk pay. 调用SDK支付(代码来自于支付宝Demo)
     *
     */
    public void pay(View v) {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(context).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            // finish();
                        }
                    }).show();
            return;
        }

        // String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01"); // 官方原版

        // String orderInfo = getOrderInfo("我做的商城热卖客户端", "双11剁手党专属", adapter.getTotalPrice()+"");
        String orderInfo = getOrderInfo("双11剁手党专属", "我做的商城热卖客户端", "0.01");

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((Activity) context);   // context强转
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * create the order info. 创建订单信息
     *
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }
}

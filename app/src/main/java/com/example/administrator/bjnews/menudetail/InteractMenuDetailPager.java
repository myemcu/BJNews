package com.example.administrator.bjnews.menudetail;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.bjnews.R;
import com.example.administrator.bjnews.base.MenuDetailBasePager;
import com.example.administrator.bjnews.bean.PhotosMenuDetailPagerBean;
import com.example.administrator.bjnews.utils.BitmapUtils;
import com.example.administrator.bjnews.utils.CacheUtil;
import com.example.administrator.bjnews.utils.LogUtil;
import com.example.administrator.bjnews.utils.NetCacheUtils;
import com.example.administrator.bjnews.utils.Url;
import com.example.administrator.bjnews.volley.VolleyManager;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/20 0020.
 * 互动菜单详情页面
 */

public class InteractMenuDetailPager extends MenuDetailBasePager{

    private final DisplayImageOptions options;

    private final BitmapUtils bitmapUtils;  // 三级缓存，工具类
    private String url= Url.PHOTOS_URL;

    // private TextView txt;

//    private ImageLoader imageLoader;// 这个是volley中的imageLoader
    protected com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();

    private boolean isShowList = true;  // true显示列表，隐藏网格(默认)

    @ViewInject(R.id.list_view)
    private ListView list_view;
    @ViewInject(R.id.grid_view)
    private GridView grid_view;
    private List<PhotosMenuDetailPagerBean.DataBean.NewsBean> news; // 互动的数据

    /*private TextView txt;

    public InteractMenuDetailPager(Context context) {
        super(context);
    }

    @Override

    public View initView() {    // 视图
        // 设置内容(子视图)
        txt = new TextView(context); // 在当前页创建一个TextView文本
        txt.setGravity(Gravity.CENTER);
        txt.setText("互动菜单详情页面");
        txt.setTextSize(25);
        txt.setTextColor(Color.RED);

        return txt;
    }

    @Override

    public void initData() {    // 数据
        super.initData();
        txt.setText("互动菜单详情页面");
        System.out.println("互动菜单详情页面的数据初始化了..");
    }*/

    // Copy互动中的内容
    private Handler handler = new Handler() {   // 注意里面的findViewWithTag()，而不是findViewById()
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case NetCacheUtils.SUCESS:
                    //Toast.makeText(context,"图片联网请求成功",Toast.LENGTH_SHORT).show();
                    Bitmap bitmap = (Bitmap) msg.obj;                                       // obj就是Bitmap
                    int position = msg.arg1;                                                // arg1就是位置
                    LogUtil.e("图片联网请求成功"+position);

                    // 失败
                    if (list_view != null  && list_view.isShown()) {
                        ImageView imageView  = (ImageView) list_view.findViewWithTag(position); // position就是Tag
                        if (imageView!=null && bitmap!=null) {
                            imageView.setImageBitmap(bitmap);    // 显示从联网请求到的图片

                        }

                    }

                    if (grid_view != null  && grid_view.isShown()) {
                        ImageView imageView  = (ImageView) grid_view.findViewWithTag(position); // position就是Tag
                        if (imageView!=null && bitmap!=null) {
                            imageView.setImageBitmap(bitmap);    // 显示从联网请求到的图片

                        }

                    }
                    break;

                case NetCacheUtils.FAILED:
                    position = msg.arg1;                                                // arg1就是位置
                    LogUtil.e("图片联网请求失败"+position);
                    break;

                default:break;
            }
        }
    };

    public InteractMenuDetailPager(Context context) {
        super(context);
//        imageLoader = VolleyManager.getImageLoader();
        bitmapUtils = new BitmapUtils(handler);

        // 从ImageLoader开源项目中的ImageListAcyivity中拷入
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.pic_item_list_default)   // 默认加载时，显示的图片
                .showImageForEmptyUri(R.drawable.pic_item_list_default) // 加载为空时，显示的图片
                .showImageOnFail(R.drawable.pic_item_list_default)      // 加载错误时，显示的图片
                .cacheInMemory(true)                                    // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                                      // 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)                               // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .displayer(new RoundedBitmapDisplayer(40))              // 设置图片圆角
                .build();
    }

    @Override

    public View initView() {    // 视图

        /*// 设置内容(子视图)
        txt = new TextView(context); // 在当前页创建一个TextView文本
        txt.setGravity(Gravity.CENTER);
        txt.setText("互动菜单详情页面");
        txt.setTextSize(25);
        txt.setTextColor(Color.RED);*/

        View view = View.inflate(context, R.layout.photos_menu_detail_pager,null);     // 加载布局
        x.view().inject(this,view);                             // 使用xUtils3，对刚加载的布局进行注解

        return view;
    }

    @Override

    public void initData() {    // 数据

        super.initData();
        // txt.setText("互动菜单详情页面");
        System.out.println("互动菜单详情页面的数据初始化了..");

        // 默认显示ListView的数据

        url = Url.PHOTOS_URL;

        String saveJson = CacheUtil.getString(context,url);
        if (! TextUtils.isEmpty(saveJson)) {
            processData(saveJson); // 解析数据
        }

        // 1 Volley请求文本
        getDataFromNet_Volley();

    }

    private void processData(String json) {
        // 解析数据(先要生成一个Bean对象)
        PhotosMenuDetailPagerBean bean = new Gson().fromJson(json,PhotosMenuDetailPagerBean.class);
        String title = bean.getData().getNews().get(0).getTitle();
        Toast.makeText(context,"标题："+title,Toast.LENGTH_LONG).show();   // 此步说明，联网解析成功
        // list数据(设置ListView适配器)(ListView需要一个集合：bean.getData().getNews())
        news = bean.getData().getNews();
        // list适配器
        list_view.setAdapter(new MyPhotosAdapter());
    }


    private void getDataFromNet_Volley() {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {  // 联网成功
                // 1 打印吐司
                // Toast.makeText(context,"互动数据-联网成功："+s,Toast.LENGTH_LONG).show();
                // 2 解析数据
                processData(s);
                // 3 缓存数据(文本)
                CacheUtil.putString(context,url,s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 1 打印吐司
                Toast.makeText(context,"互动数据-联网失败："+error,Toast.LENGTH_LONG).show();
            }
        }){
            @Override   // 解决乱码
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String parsed = new String(response.data,"UTF-8");
                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                }
                catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return super.parseNetworkResponse(response);
            }
        };

        VolleyManager.addRequest(request,"PhotosMenuDetailPager");
    }

    // 完成list(默认)与grid的切换
    /*public void switch_list_grid_view(ImageButton iv_switch) {
        if (isShowList) {

            isShowList=false;

            grid_view.setVisibility(View.VISIBLE);
            list_view.setVisibility(View.GONE);
            grid_view.setAdapter(new MyPhotosAdapter());
            iv_switch.setImageResource(R.drawable.icon_pic_list_type);
        }
        else {

            isShowList=true;

            list_view.setVisibility(View.VISIBLE);
            grid_view.setVisibility(View.GONE);
            list_view.setAdapter(new MyPhotosAdapter());
            iv_switch.setImageResource(R.drawable.icon_pic_grid_type);
        }
    }*/

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);//显示图片的过程有一个渐变动画
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    private class MyPhotosAdapter extends BaseAdapter {
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PhotosMenuDetailPager.ViewHolder viewHolder;
            if (convertView==null) {
                convertView=View.inflate(context,R.layout.item_photos,null);
                viewHolder = new PhotosMenuDetailPager.ViewHolder();
                viewHolder.iv_photos_icon= (ImageView) convertView.findViewById(R.id.iv_photos_icon);
                viewHolder.tv_photos_title= (TextView) convertView.findViewById(R.id.tv_photos_title);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (PhotosMenuDetailPager.ViewHolder) convertView.getTag();
            }

            // 根据位置取数据
            PhotosMenuDetailPagerBean.DataBean.NewsBean newsBean = news.get(position);  // getView()中不要抽取变量
            viewHolder.tv_photos_title.setText(newsBean.getTitle());

            /*请求图片(各种方式)**/

            /*loaderImager(viewHolder,newsBean.getListimage());    // Volley方式请求图片*/

            // 自定义三级缓存请求图片
            String imageUrl = newsBean.getListimage();
            /*viewHolder.iv_photos_icon.setTag(position);             // 因为列表中的图片id都一样，要区分不同的加载位置，就只能设置Tag
            Bitmap bitmap = bitmapUtils.getBitmapFromUrl(imageUrl,position);
            if (bitmap!=null) {                                     // 内存和本地
                viewHolder.iv_photos_icon.setImageBitmap(bitmap);
            }*/

            // 集成ImageLoader开源项目请求图片
            /* 第一个参数是：图片的地址，第二个参数是要绑定的图片，第三个参数处理图片的配置，第四个参数*/
            imageLoader.displayImage(imageUrl, viewHolder.iv_photos_icon, options, animateFirstListener);

            return convertView;
        }
    }

    // 这里选择用testVolley-Volley-imagerlist-ImagerListAdapter2中的这个方法来请求图片
    private void loaderImager(final PhotosMenuDetailPager.ViewHolder viewHolder, String imageurl) {

        viewHolder.iv_photos_icon.setTag(imageurl);
        //直接在这里请求会乱位置
        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer != null) {

                    if (viewHolder.iv_photos_icon != null) {
                        if (imageContainer.getBitmap() != null) {
                            viewHolder.iv_photos_icon.setImageBitmap(imageContainer.getBitmap());
                        } else {
                            viewHolder.iv_photos_icon.setImageResource(R.drawable.pic_item_list_default);
                        }
                    }
                }
            }
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //如果出错，则说明都不显示（简单处理），最好准备一张出错图片
                viewHolder.iv_photos_icon.setImageResource(R.drawable.pic_item_list_default);
            }
        };
//        imageLoader.get(imageurl, listener);
    }

    static class ViewHolder {
        ImageView iv_photos_icon;
        TextView  tv_photos_title;
    }
}

package com.myemcu.recyclerviewdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myemcu.recyclerviewdemo.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/7 0007.
 * 自定义RecyclerView适配器
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<String> datas;

    public RecyclerViewAdapter(Context context, ArrayList<String> datas) {
        this.context=context;
        this.datas=datas;
    }

    public void addData(int position, String data) {
        datas.add(position,data);       // 添加到集合中的第0个位置
        notifyItemInserted(position);   // 插入一条数据的更新
    }

    public void delData(int position) {
        datas.remove(position);
        notifyItemRemoved(position);    // 移除数据的刷新
    }

    public void delAllData() {
        datas.clear();
        notifyItemRangeChanged(0,datas.size());
    }

    public void addAllData(ArrayList<String> data) {
        datas.addAll(data);
        notifyItemRangeChanged(0,datas.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {   // 必须为public，要么不写

        private ImageView iv_icon;
        private TextView tv_title;

        public ViewHolder(View itemView) {  // ViewHolder类构造器
            super(itemView);

            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);

            // 设置点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v,getLayoutPosition(),datas.get(getLayoutPosition()));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        // RecyclerView点击事件方法(当点击某Item的时候，回调该方法)
        void onItemClick(View view,int position,String data);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener l) { // 设置Item的点击监听
        this.onItemClickListener=l;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {  // 先创建item_xml
        View itemView = View.inflate(context, R.layout.item, null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) { // 再绑定
        // 1 根据位置得到数据
        String data = datas.get(position);
        // 2 绑定数据
        holder.tv_title.setText(data);
    }

    @Override
    public int getItemCount() { // 获取item总条目数
        return datas.size();
    }

}

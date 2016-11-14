package com.example.administrator.bjnews.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.bjnews.R;

/**
 * Created by Administrator on 2016/11/10 0010.
 * 自定义数字添加减少键
 */

public class NumberAddSubView extends LinearLayout implements View.OnClickListener {

    private int value = 1;          // 当前值
    private int minValue = 1;       // 最小值
    private int maxValue = 10;      // 最大值(库存)

    // Alt+Insert(赋值取值)

    public int getValue() {
        String valueStr = tv_value.getText().toString().trim();   // trim()删除多余空格
        if (!TextUtils.isEmpty(valueStr)) {
            value=Integer.valueOf(valueStr);
        }
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        tv_value.setText(value+""); // int转string
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    private Button btn_sub,btn_add;
    private TextView tv_value;

    public NumberAddSubView(Context context) {
        this(context,null);
    }

    public NumberAddSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberAddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 让布局与当前类形成一个整体
        View.inflate(context, R.layout.number_add_sub_view,this);    // this:NumberAddSubView
        btn_sub = (Button) findViewById(R.id.btn_sub);
        btn_add = (Button) findViewById(R.id.btn_add);
        tv_value = (TextView) findViewById(R.id.tv_value);
        getValue(); // 取值
        btn_sub.setOnClickListener(this);
        btn_add.setOnClickListener(this);

        /*if (attrs != null) {    // attrs:基于反射机制的属性传递，将xml中的控件属性传递过来(传递到对应类中)，简单说来就是在xml中设置控件属性的方法
            // 这个是v7包方法，如果没有，则：compile 'com.android.support:appcompat-v7:25.0.0'
            TintTypedArray typedArray = TintTypedArray.obtainStyledAttributes(context,attrs,R.styleable.NumberAddSubView);

            // 默认值
            int value = typedArray.getInt(R.styleable.NumberAddSubView_value,0);        // 如果取不到，则传默认值0
            if (value > 0) {
                setValue(value);
            }

            // 最小值
            int minValue = typedArray.getInt(R.styleable.NumberAddSubView_minValue,0);  // 如果取不到，则传默认值0
            if (minValue > 0) {
                setMinValue(minValue);
            }

            // 最大值
            int maxValue = typedArray.getInt(R.styleable.NumberAddSubView_maxValue,0);  // 如果取不到，则传默认值0
            if (maxValue > 0) {
                setMaxValue(maxValue);
            }

            // 加减键总背景
            Drawable numberAddSubBackground = typedArray.getDrawable(R.styleable.NumberAddSubView_numberAddSubBackground);
            if (numberAddSubBackground != null) {
                setBackground(numberAddSubBackground);
            }

            // "-"键背景
            Drawable numberSubBackground = typedArray.getDrawable(R.styleable.NumberAddSubView_numberSubBackground);
            if (numberSubBackground != null) {
                btn_sub.setBackground(numberSubBackground);
            }

            // "+"键背景
            Drawable numberAddBackground = typedArray.getDrawable(R.styleable.NumberAddSubView_numberAddBackground);
            if (numberAddBackground != null) {
                btn_add.setBackground(numberAddBackground);
            }
        }*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sub:  subNum();
                                if (listener != null) { // 接口判空
                                    listener.onButtonSub(view,value);
                                }
                                break;

            case R.id.btn_add:  addNum();
                                if (listener != null) { // 接口判空
                                    listener.onButtonAdd(view,value);
                                }
                                break;
            default:break;
        }
    }

    private void subNum() {
        if (value > minValue) {
            value-=1;
        }
        setValue(value);
    }

    private void addNum() {
        if (value < maxValue) {
            value+=1;
        }
        setValue(value);
    }

    // 定义：数字键点击监听接口(因为要随时回传数据，故用接口)(回调——在主MainActivity中设置接口监听时调)
    public interface OnNumClickListener {
        void onButtonSub(View view, int value);  // 接口中需要声明方法("-"键点击时的回调方法)
        void onButtonAdd(View view, int value);  // 接口中需要声明方法("+"键点击时的回调方法)
    }

    // 定义接口对象
    private OnNumClickListener listener;

    // 数字键的设置监听Alt+Insert(Setter)
    public void setOnNumClickListener(OnNumClickListener listener) {
        this.listener = listener;
    }
}

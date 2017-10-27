package com.jaydenxiao.common.commonwidget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaydenxiao.common.R;

@SuppressWarnings("ALL")
public class Normal2NavTabbar extends LinearLayout {

    private TextView tvPage1, tvPage2;
    private RelativeLayout rlCommonPage1, rlCommonPage2;
    private Context context;

    public Normal2NavTabbar(Context context) {
        super(context, null);
        this.context = context;
    }

    public Normal2NavTabbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View.inflate(context, R.layout.view_navigationbar, this);
        tvPage1 = (TextView) findViewById(R.id.tv_page1);
        tvPage2 = (TextView) findViewById(R.id.tv_page2);
        rlCommonPage1 = (RelativeLayout) findViewById(R.id.ly_page1);
        rlCommonPage2 = (RelativeLayout) findViewById(R.id.ly_page2);
    }

    /**
     * 设置标题栏左侧字符串
     *
     * @param tvLeftText
     */
    public void setTvLeft(String tvLeftText) {
        tvPage1.setText(tvLeftText);
    }

    /**
     * 设置标题栏右侧字符串
     *
     * @param tvRightText
     */
    public void setTvRight(String tvRightText) {
        tvPage2.setText(tvRightText);
    }

    /**
     * 点击事件
     */
    public void setOnLeftListener(OnClickListener listener) {
        rlCommonPage1.setOnClickListener(listener);
    }

    public void setOnRightListener(OnClickListener listener) {
        rlCommonPage2.setOnClickListener(listener);
    }

    /**
     * 设置选中
     */
    public void selectNavTab(int position) {
        switch (position) {
            case 0: {
                rlCommonPage1.setBackgroundResource(R.drawable.rectangle_left_select);
                tvPage1.setTextColor(Color.parseColor("#ffffff"));
                rlCommonPage2.setBackgroundResource(R.drawable.rectangle_right);
                tvPage2.setTextColor(Color.parseColor("#435354"));
                break;
            }
            default:
            case 1: {
                rlCommonPage1.setBackgroundResource(R.drawable.rectangle_left);
                tvPage1.setTextColor(Color.parseColor("#435354"));
                rlCommonPage2.setBackgroundResource(R.drawable.rectangle_right_select);
                tvPage2.setTextColor(Color.parseColor("#ffffff"));
                break;
            }
        }
    }
}

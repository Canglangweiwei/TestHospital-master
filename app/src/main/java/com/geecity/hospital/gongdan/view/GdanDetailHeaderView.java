package com.geecity.hospital.gongdan.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.geecity.hospital.R;
import com.geecity.hospital.widget.ExpandableTextView;

/**
 * <p>
 * 我的工单详情头部页面
 * </p>
 * Created by Administrator on 2017/9/6 0006.
 */
@SuppressWarnings("ALL")
public class GdanDetailHeaderView extends LinearLayout {

    private ExpandableTextView wdgd_detail_miaoshu;

    private TextView tv_gdcode;
    private TextView tv_keshi;
    private TextView tv_wupin;
    private TextView tv_chuliren;
    private TextView tv_slsj;

    public GdanDetailHeaderView(Context context) {
        super(context);
        initView();
    }

    public GdanDetailHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public GdanDetailHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GdanDetailHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        View mRootView = inflate(getContext(), R.layout.activity_gongdan_detail_topview, null);

        LinearLayout ll_header = (LinearLayout) mRootView.findViewById(R.id.ll_header);

        LinearLayout ll_01 = (LinearLayout) mRootView.findViewById(R.id.ll_01);
        ImageView image01 = (ImageView) mRootView.findViewById(R.id.image01);
        tv_gdcode = (TextView) mRootView.findViewById(R.id.tv_gdcode);

        LinearLayout ll_001 = (LinearLayout) mRootView.findViewById(R.id.ll_001);
        TextView fangjian = (TextView) mRootView.findViewById(R.id.keshi);
        tv_keshi = (TextView) mRootView.findViewById(R.id.tv_keshi);

        View view = mRootView.findViewById(R.id.view);

        LinearLayout ll_02 = (LinearLayout) mRootView.findViewById(R.id.ll_02);
        TableRow table_01 = (TableRow) mRootView.findViewById(R.id.table_01);
        TextView xingming = (TextView) mRootView.findViewById(R.id.wupin);
        tv_wupin = (TextView) mRootView.findViewById(R.id.tv_wupin);
        TableRow table_02 = (TableRow) mRootView.findViewById(R.id.table_02);
        TextView dianhua = (TextView) mRootView.findViewById(R.id.chuliren);
        tv_chuliren = (TextView) mRootView.findViewById(R.id.tv_chuliren);

        LinearLayout ll_03 = (LinearLayout) mRootView.findViewById(R.id.ll_03);
        TextView slsj = (TextView) mRootView.findViewById(R.id.slsj);
        tv_slsj = (TextView) mRootView.findViewById(R.id.tv_tjsj);

        wdgd_detail_miaoshu = (ExpandableTextView) mRootView.findViewById(R.id.wdgd_detail_miaoshu);

        addView(mRootView);
    }

    /**
     * 订单号
     */
    public void setTv_gdcode(String gdcode) {
        this.tv_gdcode.setText(gdcode);
    }

    /**
     * 房间
     */
    public void setTv_keshi(String keshi) {
        this.tv_keshi.setText(keshi);
    }

    /**
     * 姓名
     */
    public void setTv_wupin(String wupin) {
        this.tv_wupin.setText(wupin);
    }

    /**
     * 电话
     */
    public void setTv_chuliren(String clr) {
        this.tv_chuliren.setText(clr);
    }

    /**
     * 受理时间
     */
    public void setTv_slsj(String slsj) {
        this.tv_slsj.setText(slsj);
    }

    /**
     * 描述
     */
    public void setMiaoshu(String txt) {
        this.wdgd_detail_miaoshu.setText(txt);
    }
}

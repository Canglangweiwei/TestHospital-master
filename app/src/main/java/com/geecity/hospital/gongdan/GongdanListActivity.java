package com.geecity.hospital.gongdan;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.geecity.hospital.R;
import com.geecity.hospital.base.AbsBaseActivity;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import butterknife.Bind;

/**
 * <p>
 * 工单列表
 * </p>
 * Created by Administrator on 2017/9/6 0006.
 */
@SuppressWarnings("ALL")
public class GongdanListActivity extends AbsBaseActivity {

    @Bind(R.id.ntb)
    NormalTitleBar ntb;

    @Bind(R.id.viewpager_main)
    FrameLayout viewpagerMain;

    private String[] titles = {"待处理", "待接收", "处理中", "已完成"};

    private int currentTabIndex = 0;
    public static final String CURRENT_TAB_INDEX = "current_tab_index";

    @Override
    protected int initLayoutResID() {
        return R.layout.activity_gongdan_list;
    }

    @Override
    protected void parseIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            currentTabIndex = bundle.getInt(CURRENT_TAB_INDEX);
    }

    @Override
    protected void initUi() {
        ntb.setTitleText(titles[currentTabIndex]);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected void initListener() {
        ntb.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        addFragment(currentTabIndex);
    }

    /**
     * 加载列表页面
     *
     * @param mFragment
     */
    private void addFragment(int index) {
        FragmentGongdanList mFragment = FragmentGongdanList.newInstance(titles[index]);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.viewpager_main, mFragment);
        transaction.commit();
    }
}

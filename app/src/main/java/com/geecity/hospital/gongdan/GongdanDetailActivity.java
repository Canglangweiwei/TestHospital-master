package com.geecity.hospital.gongdan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.geecity.hospital.R;
import com.geecity.hospital.adapter.JiedianAdapter;
import com.geecity.hospital.base.AbsBaseActivity;
import com.geecity.hospital.bean.GongdanDetailBean;
import com.geecity.hospital.bean.GongdanDetailResultBean;
import com.geecity.hospital.bean.JiedianBean;
import com.geecity.hospital.gongdan.api.ApiServiceProtocol;
import com.geecity.hospital.gongdan.api.DoProgressApiService;
import com.geecity.hospital.gongdan.view.GdanDetailHeaderView;
import com.geecity.hospital.util.rxbus.RxBus;
import com.geecity.hospital.util.rxbus.RxEventIndicator;
import com.geecity.hospital.xgohttp.WebAPIListener;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding.view.RxView;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;
import com.wevey.selector.dialog.DialogOnClickListener;
import com.wevey.selector.dialog.MDAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * <p>
 * 工单详情
 * </p>
 * Created by Administrator on 2017/9/5 0005.
 */
public class GongdanDetailActivity extends AbsBaseActivity
        implements OnRefreshListener {

    @Bind(R.id.ntb)
    NormalTitleBar ntb;
    @Bind(R.id.irc)
    IRecyclerView irc;

    @Bind(R.id.wdgd_btn_operate_paidan)
    Button btn_paidan;// 派单
    @Bind(R.id.wdgd_btn_operate_jiedan)
    Button btn_jiedan;// 接单
    @Bind(R.id.wdgd_btn_operate_confirm)
    Button btn_confirm;// 确认
    @Bind(R.id.wdgd_btn_operate_finish)
    Button btn_finish;// 完成

    private JiedianAdapter mAdapter;

    private String gdid;               // 工单id

    private GdanDetailHeaderView headerView;

    private GongdanDetailBean detailBean;
    private ApiServiceProtocol apiServiceProtocol;
    // 观察者
    private Subscription subscription;

    @Override
    protected int initLayoutResID() {
        return R.layout.activity_gongdan_detail;
    }

    @Override
    protected void parseIntent() {
        // 获取页面传值
        if (getIntent().getExtras() != null) {
            gdid = getIntent().getExtras().getString("id");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent == null)
            return;
        final Bundle bundle = intent.getExtras();
        if (bundle != null) {
            gdid = bundle.getString("id");
            requestData();
        }
    }

    @Override
    protected void initUi() {
        ntb.setTitleText("工单详情");
        headerView = new GdanDetailHeaderView(this);
        apiServiceProtocol = new ApiServiceProtocol();
        // 通知页面刷新详情信息
        subscription = RxBus.getInstance().toObservable(RxEventIndicator.class)
                .subscribe(new Action1<RxEventIndicator>() {

                    @Override
                    public void call(RxEventIndicator event) {
                        if (event == null)
                            return;
                        requestData();
                    }
                });
    }

    @Override
    protected void initDatas() {
        irc.addHeaderView(headerView);

        mAdapter = new JiedianAdapter(this);
        mAdapter.openLoadAnimation(new ScaleInAnimation());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        irc.setLayoutManager(linearLayoutManager);
        irc.setAdapter(mAdapter);
        /**
         * 获取详情信息
         */
        onRefresh();
    }

    @Override
    protected void initListener() {
        ntb.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // 点击操作
        notMoreClick();
        // 设置下拉刷新监听事件
        irc.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        mAdapter.getPageBean().setRefresh(true);
        // 发起请求
        irc.setRefreshing(true);
        requestData();
    }

    /**
     * 请求数据
     */
    private void requestData() {
        TreeMap<String, Object> params = new TreeMap<>();
        params.put("gdid", gdid);
        apiServiceProtocol.detail(params)
                .compose(this.<String>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {

                    @Override
                    public void call(String dataJson) {
                        // 解析首页信息
                        if (TextUtils.isEmpty(dataJson)) {
                            ToastUitl.showShort("首页信息加载失败");
                            if (mAdapter.getPageBean().isRefresh()) {
                                irc.setRefreshing(false);
                            }
                            return;
                        }
                        try {
                            JSONObject json = new JSONObject(dataJson);
                            if (!json.getBoolean("success")) {
                                ToastUitl.showShort(json.getString("message"));
                                return;
                            }
                            String result = json.getString("data");
                            Type type = new TypeToken<GongdanDetailResultBean>() {
                            }.getType();
                            GongdanDetailResultBean resultBean = mGson.fromJson(result, type);
                            if (resultBean != null) {
                                if (mAdapter.getPageBean().isRefresh()) {
                                    irc.setRefreshing(false);
                                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
                                    updateUi(resultBean);
                                }
                            }
                        } catch (JSONException e) {
                            e.getMessage();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (mAdapter.getPageBean().isRefresh()) {
                            irc.setRefreshing(false);
                        }
                    }
                });
    }

    /**
     * 更新页面显示
     */
    private void updateUi(GongdanDetailResultBean resultBean) {
        if (resultBean == null)
            return;
        detailBean = resultBean.getDetail();
        if (detailBean != null) {
            headerView.setTv_gdcode(detailBean.getCode());// 订单号
            headerView.setTv_keshi(detailBean.getKs());// 科室
            headerView.setTv_chuliren(detailBean.getCzr());
            headerView.setTv_wupin(detailBean.getWp());// 运送物品
            headerView.setTv_slsj(detailBean.getTjtime());// 接单时间
            headerView.setMiaoshu(detailBean.getSm());// 接单描述
        }
        List<JiedianBean> mList = resultBean.getJiedian();
        if (mList == null || mList.size() == 0)
            return;
        mAdapter.reset(mList);
    }

    /**
     * 3秒内不允许按钮多次点击
     */
    private void notMoreClick() {
        // 派单
        RxView.clicks(btn_paidan)
                .throttleFirst(5, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {

                    @Override
                    public void call(Void aVoid) {
                        // 派单
                        detailLastNewState(1);
                    }
                });
        // 接单
        RxView.clicks(btn_jiedan)
                .throttleFirst(5, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {

                    @Override
                    public void call(Void aVoid) {
                        // 接单
                        detailLastNewState(2);
                    }
                });
        // 确认
        RxView.clicks(btn_confirm)
                .throttleFirst(5, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {

                    @Override
                    public void call(Void aVoid) {
                        // 确认
                        detailLastNewState(3);
                    }
                });
        // 完成
        RxView.clicks(btn_finish)
                .throttleFirst(5, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {

                    @Override
                    public void call(Void aVoid) {
                        // 完成
                        detailLastNewState(4);
                    }
                });
    }

    /**
     * 获取工单最新详情信息
     */
    private void detailLastNewState(final int idx) {
        TreeMap<String, Object> params = new TreeMap<>();
        params.put("gdid", gdid);
        apiServiceProtocol.detailLastNewState(params)
                .compose(this.<String>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {

                    @Override
                    public void call(String dataJson) {
                        // 解析首页信息
                        if (TextUtils.isEmpty(dataJson)) {
                            ToastUitl.showShort("首页信息加载失败");
                            return;
                        }
                        try {
                            JSONObject json = new JSONObject(dataJson);
                            if (!json.getBoolean("success")) {
                                ToastUitl.showShort(json.getString("message"));
                                return;
                            }
                            String result = json.getString("data");
                            Type type = new TypeToken<GongdanDetailBean>() {
                            }.getType();
                            detailBean = mGson.fromJson(result, type);
                            if (detailBean != null) {
                                headerView.setTv_gdcode(detailBean.getCode());// 订单号
                                headerView.setTv_keshi(detailBean.getKs());// 科室
                                headerView.setTv_chuliren(detailBean.getCzr());
                                headerView.setTv_wupin(detailBean.getWp());// 运送物品
                                headerView.setTv_slsj(detailBean.getTjtime());// 接单时间
                                headerView.setMiaoshu(detailBean.getSm());// 接单描述
                            }
                            // 当前登录人
                            String username = mBaseUserBean.getUserName();
                            if (TextUtils.isEmpty(detailBean.getCzr()) && idx == 1) {
                                if (!"待处理".equals(detailBean.getZt())) {
                                    ToastUitl.showShort("当前节点不允许派单");
                                    return;
                                }
                                startNextActivityForResult(null, MemberSelectorActivity.class, 1001);
                                return;
                            }

                            if (!username.equals(detailBean.getCzr())) {
                                ToastUitl.showShort("当前登录人权限不足");
                                return;
                            }
                            switch (idx) {
                                case 1:// 派单
                                    if (detailBean == null) {
                                        ToastUitl.showShort("数据同步异常，请稍后处理");
                                        return;
                                    }
                                    if (!"待处理".equals(detailBean.getZt())) {
                                        ToastUitl.showShort("当前节点不允许派单操作");
                                        return;
                                    }
                                    startNextActivityForResult(null, MemberSelectorActivity.class, 1001);
                                    return;
                                case 2:// 接单
                                    if (detailBean == null) {
                                        ToastUitl.showShort("数据同步异常，请稍后处理");
                                        return;
                                    }
                                    if (!"待接收".equals(detailBean.getZt())) {
                                        ToastUitl.showShort("当前节点不允许接单操作");
                                        return;
                                    }
                                    doProgress("接单", mBaseUserBean.getUserName());
                                    return;
                                case 3:// 确认
                                    if (detailBean == null) {
                                        ToastUitl.showShort("数据同步异常，请稍后处理");
                                        return;
                                    }
                                    if (!"处理中".equals(detailBean.getZt())) {
                                        ToastUitl.showShort("当前节点不允许确认操作");
                                        return;
                                    }
                                    Bundle bundle1 = new Bundle();
                                    bundle1.putString("gdid", gdid);
                                    bundle1.putString("type", "确认");
                                    startNextActivity(bundle1, GongdanOperateActivity.class);
                                    return;
                                case 4:// 完成
                                    if (detailBean == null) {
                                        ToastUitl.showShort("数据同步异常，请稍后处理");
                                        return;
                                    }
                                    if (!"处理中".equals(detailBean.getZt())) {
                                        ToastUitl.showShort("当前节点不允许完成操作");
                                        return;
                                    }
                                    if (TextUtils.isEmpty(detailBean.getIfsure())
                                            || !Boolean.parseBoolean(detailBean.getIfsure())) {
                                        ToastUitl.showShort("请先确认操作，确认无误后再执行完成操作");
                                        return;
                                    }
                                    Bundle bundle2 = new Bundle();
                                    bundle2.putString("gdid", gdid);
                                    bundle2.putString("type", "完成");
                                    startNextActivity(bundle2, GongdanOperateActivity.class);
                                    break;
                            }
                        } catch (JSONException e) {
                            e.getMessage();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    private MDAlertDialog mdAlertDialog;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 1001:// 选择受理人
                    final String slr = data.getStringExtra("name");
                    doProgress("派单", slr);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 派单、接单、完成
     *
     * @param type 派单、接单、完成、回访
     * @param slr  受理人
     */
    private void doProgress(final String type, final String slr) {
        String desc = "";
        if ("派单".equals(type)) {
            desc = "您要对【" + slr + "】进行派单吗？";
        } else if ("接单".equals(type)) {
            desc = "【" + slr + "】,您确定要接单吗？";
        } else if ("完成".equals(type)) {
            desc = "【" + slr + "】,您的任务确定已经完成？";
        }

        mdAlertDialog = new MDAlertDialog.Builder(this)
                .setHeight(0.25f)  //屏幕高度*0.3
                .setWidth(0.7f)  //屏幕宽度*0.7
                .setTitleVisible(true)
                .setTitleText("温馨提示")
                .setTitleTextColor(R.color.black_light)
                .setContentText(desc)
                .setContentTextColor(R.color.black_light)
                .setLeftButtonText("取消")
                .setLeftButtonTextColor(R.color.black_light)
                .setRightButtonText("确认")
                .setRightButtonTextColor(R.color.gray)
                .setTitleTextSize(16)
                .setContentTextSize(14)
                .setButtonTextSize(14)
                .setOnclickListener(new DialogOnClickListener() {

                    @Override
                    public void clickLeftButton(View view) {
                        mdAlertDialog.dismiss();
                    }

                    @Override
                    public void clickRightButton(View view) {
                        mdAlertDialog.dismiss();
                        submit(type, slr);
                    }
                })
                .build();
        if (!isFinishing())
            mdAlertDialog.show();
    }

    /**
     * 执行汇报进展操作
     *
     * @param type 类型
     * @param slr  受理人
     */
    private void submit(String type, String slr) {
        startProgressDialog();
        final DoProgressApiService mApiService = new DoProgressApiService(this);
        mApiService.setGdid(gdid);
        mApiService.setGdzt(type);
        mApiService.setSlr(slr);
        mApiService.setUsername(mBaseUserBean.getUserName());
        mApiService.setListener(new WebAPIListener() {
            @Override
            public void onLoadSuccess(int successCode) {
                stopProgressDialog();
                ToastUitl.showShort(mApiService.getPromptMsg());
                requestData();
            }

            @Override
            public void onLoadFail(int errorCode, String errorMessage) {
                stopProgressDialog();
                ToastUitl.showShort(errorMessage);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }
}
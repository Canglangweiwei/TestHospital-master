package com.geecity.hospital;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.aspsine.irecyclerview.widget.CustomPtrHeader;
import com.geecity.hospital.account.LoginActivity;
import com.geecity.hospital.base.AbsBaseActivity;
import com.geecity.hospital.base.AbsBaseApplication;
import com.geecity.hospital.bean.TopAdBean;
import com.geecity.hospital.bean.UserBean;
import com.geecity.hospital.gongdan.GongdanDetailActivity;
import com.geecity.hospital.gongdan.GongdanListActivity;
import com.geecity.hospital.gongdan.api.ApiServiceProtocol;
import com.geecity.hospital.util.BannerGlideImageLoader;
import com.jaydenxiao.common.commonutils.DataCleanManager;
import com.jaydenxiao.common.commonutils.NetUtil;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;
import com.wevey.selector.dialog.DialogOnClickListener;
import com.wevey.selector.dialog.MDAlertDialog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 主页
 */
public class MainActivity extends AbsBaseActivity implements OnBannerListener {

    @Bind(R.id.ntb)
    NormalTitleBar ntb;

    @Bind(R.id.ptrFrameLayout)
    PtrClassicFrameLayout mPtrFrame;

    @Bind(R.id.homeFrg_banner)
    Banner mBanner;

    @Bind(R.id.home_tv_zhaohu)
    TextView mTvzhaohu;

    @Bind(R.id.am_tv_paidan)
    TextView mTvdpdNum;// 待派单
    @Bind(R.id.am_tv_djs)
    TextView mTvdjsNum;// 待接收
    @Bind(R.id.am_tv_clz)
    TextView mTvclzNum;// 处理中
    @Bind(R.id.am_tv_ywc)
    TextView mTvywcNum;// 已完成

    private MDAlertDialog mdAlertDialog;

    @Override
    protected int initLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void parseIntent() {
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent i = new Intent(MainActivity.this, GongdanDetailActivity.class);
                    i.putExtra("id", bundle.getString("id"));// 传入id(任务id和接待id)
                    startActivity(i);
                }
            }, 1400);
        }
    }

    /**
     * 版本检测
     *
     * @param update 是否显示对话框提示
     */
    private void checkVersion(boolean update) {
        VerCheckTools tools = new VerCheckTools(this);
        tools.checkVersionRequest(update);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NetUtil.getNetworkType() != NetUtil.NETWORK_TYPE_NONE) {
            loadData();
        }
        if (intent == null)
            return;
        final Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Intent i = new Intent(MainActivity.this, GongdanDetailActivity.class);
            i.putExtra("id", bundle.getString("id"));// 传入id(任务id和接待id)
            startActivity(i);
        }
    }

    @Override
    protected void initUi() {
        ntb.setTitleText("配送中心");
        ntb.setBackVisibility(false);
        mTvzhaohu.setText(mBaseUserBean == null ? "您好" : mBaseUserBean.getZhaohu());
        protocol = new ApiServiceProtocol();
    }

    @Override
    protected void initDatas() {
        // 版本检测
        checkVersion(false);

        List<TopAdBean> imgUrls = new ArrayList<>();

        TopAdBean topAdBean = new TopAdBean();
        topAdBean.setId(1);
        topAdBean.setTitle("大桶水");
        topAdBean.setImage("http://0.89892528.cn:8877/common/logo/logo1.jpg");

        imgUrls.add(topAdBean);

        mBanner.setIndicatorGravity(BannerConfig.RIGHT)
                .setBannerStyle(BannerConfig.NUM_INDICATOR)// 显示数字指示器
                .setImages(imgUrls)
                .setImageLoader(new BannerGlideImageLoader())
                .start();

        CustomPtrHeader header = new CustomPtrHeader(this, 1);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (NetUtil.getNetworkType() != NetUtil.NETWORK_TYPE_NONE) {
                    loadData();
                }
            }
        });
        mPtrFrame.setEnabledNextPtrAtOnce(true);
        mPtrFrame.setOffsetToRefresh(200);
        mPtrFrame.autoRefresh(true);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
    }

    @Override
    protected void initListener() {
        // 右侧按钮
        ntb.setRightImagSrc(R.drawable.main_ic_logout);
        ntb.setOnRightImagListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mdAlertDialog = new MDAlertDialog.Builder(MainActivity.this)
                        .setHeight(0.25f)  //屏幕高度*0.3
                        .setWidth(0.7f)  //屏幕宽度*0.7
                        .setTitleVisible(true)
                        .setTitleText("温馨提示")
                        .setTitleTextColor(R.color.black_light)
                        .setContentText("是否要注销登录？\n\n注销后会清空用户名和密码")
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
                                // 重置账户信息
                                resetAccountInfo();
                            }
                        })
                        .build();
                if (!isFinishing())
                    mdAlertDialog.show();
            }
        });
        mBanner.setOnBannerListener(this);
    }

    /**
     * 轮播图点击事件
     *
     * @param position 图片位置
     */
    @Override
    public void OnBannerClick(int position) {

    }

    @Override
    public void onStart() {
        super.onStart();
        // 开始轮播
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        // 结束轮播
        mBanner.stopAutoPlay();
    }

    @OnClick({R.id.am_ll_dpaidan, R.id.am_ll_djieshou
            , R.id.am_ll_clz, R.id.am_ll_finish})
    void onClick(View view) {
        int currentTabIdx = 0;
        switch (view.getId()) {
            case R.id.am_ll_dpaidan:// 待接收
                currentTabIdx = 0;
                break;
            case R.id.am_ll_djieshou:// 待接收
                currentTabIdx = 1;
                break;
            case R.id.am_ll_clz:// 处理中
                currentTabIdx = 2;
                break;
            case R.id.am_ll_finish:// 已完成
                currentTabIdx = 3;
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(GongdanListActivity.CURRENT_TAB_INDEX, currentTabIdx);
        startNextActivity(bundle, GongdanListActivity.class);
    }

    private long newTime;

    /**
     * 监听返回键
     */
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - newTime > 2000) {
            newTime = System.currentTimeMillis();
            ToastUitl.showShort(getString(R.string.press_twice_exit));
        } else {
            AbsBaseApplication.get(getApplicationContext()).finishAllActivity();
        }
    }

    private ApiServiceProtocol protocol;
    private Subscription mSubscription;

    /**
     * 获取首页更新数据
     */
    private void loadData() {
        if (mBaseUserBean == null)
            return;
        TreeMap<String, Object> params = new TreeMap<>();
        params.put("glc", mBaseUserBean.getGlc());
        params.put("username", mBaseUserBean.getUserName());
        mSubscription = protocol.homeFunc(params)
                .compose(this.<String>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {

                    @Override
                    public void call(String dataJson) {
                        mPtrFrame.refreshComplete();
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
                            JSONObject o = new JSONObject(result);
                            Map<String, Integer> resultMap = new HashMap<>();
                            resultMap.put("dcl", o.getInt("dcl"));
                            resultMap.put("djs", o.getInt("djs"));
                            resultMap.put("clz", o.getInt("clz"));
                            resultMap.put("ywc", o.getInt("ywc"));
                            updateUi(resultMap);
                        } catch (JSONException e) {
                            e.getMessage();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mPtrFrame.refreshComplete();
                    }
                });
    }

    /**
     * 页面更新
     *
     * @param resultMap 查询结果
     */
    private void updateUi(Map<String, Integer> resultMap) {
        if (resultMap == null
                || resultMap.size() == 0)
            return;

        // 待处理
        if (resultMap.get("dcl") == 0) {
            mTvdpdNum.setVisibility(View.INVISIBLE);
        } else {
            mTvdpdNum.setVisibility(View.VISIBLE);
            mTvdpdNum.setText(String.valueOf(resultMap.get("dcl")));
        }
        // 待接收
        if (resultMap.get("djs") == 0) {
            mTvdjsNum.setVisibility(View.INVISIBLE);
        } else {
            mTvdjsNum.setVisibility(View.VISIBLE);
            mTvdjsNum.setText(String.valueOf(resultMap.get("djs")));
        }
        // 处理中
        if (resultMap.get("clz") == 0) {
            mTvclzNum.setVisibility(View.INVISIBLE);
        } else {
            mTvclzNum.setVisibility(View.VISIBLE);
            mTvclzNum.setText(String.valueOf(resultMap.get("clz")));
        }
        // 已完成
        if (resultMap.get("ywc") == 0) {
            mTvywcNum.setVisibility(View.INVISIBLE);
        } else {
            mTvywcNum.setVisibility(View.VISIBLE);
            mTvywcNum.setText(String.valueOf(resultMap.get("ywc")));
        }
    }

    /**
     * 重置账户信息
     */
    private void resetAccountInfo() {
        JPushInterface.stopPush(this);
        DataCleanManager.clearAllCache(this);
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        DataSupport.deleteAll(UserBean.class);
        AbsBaseApplication.sApp.setUserInfo(null);
        AbsBaseApplication.sApp.finishAllActivity();
    }

    @Override
    protected void onDestroy() {
        if (mSubscription != null
                && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        super.onDestroy();
    }
}

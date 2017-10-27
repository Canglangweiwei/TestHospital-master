package com.geecity.hospital.gongdan;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.geecity.hospital.R;
import com.geecity.hospital.adapter.GdListAdapter;
import com.geecity.hospital.base.AbsBaseApplication;
import com.geecity.hospital.base.AbsBaseFragment;
import com.geecity.hospital.bean.GongdanDetailBean;
import com.geecity.hospital.bean.UserBean;
import com.geecity.hospital.gongdan.api.ApiServiceProtocol;
import com.google.gson.reflect.TypeToken;
import com.jaydenxiao.common.commonutils.ToastUitl;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.TreeMap;

import butterknife.Bind;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * <p>
 * 工单列表
 * </p>
 * Created by Administrator on 2017/9/6 0006.
 */
public class FragmentGongdanList extends AbsBaseFragment
        implements
        OnRefreshListener,
        OnLoadMoreListener {

    @Bind(R.id.gongdan_toast_info)
    TextView mTvtoast;
    @Bind(R.id.irc)
    IRecyclerView irc;

    private GdListAdapter mAdapter;

    private ApiServiceProtocol apiServiceProtocol;
    private Subscription mSubscription;
    private int mStartPage = 1;

    private String type;

    public static FragmentGongdanList newInstance(String type) {
        FragmentGongdanList fragment = new FragmentGongdanList();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int initContentView() {
        return R.layout.activity_gongdan_list_frg;
    }

    @Override
    protected void initUi() {
        apiServiceProtocol = new ApiServiceProtocol();

        mAdapter = new GdListAdapter(getActivity());
        mAdapter.openLoadAnimation(new ScaleInAnimation());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        irc.setLayoutManager(linearLayoutManager);
        irc.setAdapter(mAdapter);
        // 设置下拉刷新监听事件
        irc.setOnRefreshListener(this);
        irc.setOnLoadMoreListener(this);
    }

    @Override
    protected void initDatas() {
        if (getArguments() != null) {
            type = getArguments().getString("type");
            // 数据加载
            onRefresh();
        }
    }

    @Override
    protected void initListener() {

    }

    private void requestData(int page) {
        final UserBean mBaseUserBean = AbsBaseApplication.sApp.getUserInfo();
        if (mBaseUserBean == null)
            return;
        TreeMap<String, Object> params = new TreeMap<>();
        params.put("zt", type);// 工单状态
        params.put("page", page);//  查询第几页
        params.put("glc", mBaseUserBean.getGlc());//  管理处
        params.put("username", mBaseUserBean.getUserName());// 当前登录人
        mSubscription = apiServiceProtocol.list(params)
                .compose(this.<String>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {

                    @Override
                    public void call(String dataJson) {
                        // 信息
                        if (TextUtils.isEmpty(dataJson)) {
                            ToastUitl.showShort("首页信息加载失败");
                            return;
                        }
                        try {
                            JSONObject json = new JSONObject(dataJson);
                            if (!json.getBoolean("success")) {
                                ToastUitl.showShort(json.getString("message"));
                                mStartPage = 0;
                                if (mAdapter.getPageBean().isRefresh()) {
                                    irc.setRefreshing(false);
                                    mTvtoast.setVisibility(View.VISIBLE);
                                } else {
                                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.ERROR);
                                }
                                return;
                            }
                            String result = json.getString("data");
                            Type type = new TypeToken<List<GongdanDetailBean>>() {
                            }.getType();
                            List<GongdanDetailBean> rList = mBaseFrgGson.fromJson(result, type);
                            if (rList != null) {
                                mStartPage += 1;
                                if (mAdapter.getPageBean().isRefresh()) {
                                    irc.setRefreshing(false);
                                    mAdapter.reset(rList);
                                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                                    // 添加无相关数据提醒
                                    if (rList.size() == 0)
                                        mTvtoast.setVisibility(View.VISIBLE);
                                    else
                                        mTvtoast.setVisibility(View.GONE);
                                } else {
                                    if (rList.size() > 0) {
                                        irc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                                        mAdapter.addAll(rList);
                                    } else {
                                        irc.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
                                    }
                                }
                            } else {
                                mTvtoast.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            ToastUitl.showShort("用户信息获取失败");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUitl.showShort("请检查网络连接是否正常");
                        mStartPage = 0;
                        if (mAdapter.getPageBean().isRefresh()) {
                            irc.setRefreshing(false);
                            mTvtoast.setVisibility(View.VISIBLE);
                        } else {
                            irc.setLoadMoreStatus(LoadMoreFooterView.Status.ERROR);
                        }
                    }
                });
    }

    @Override
    public void onRefresh() {
        mAdapter.getPageBean().setRefresh(true);
        // 发起请求
        irc.setRefreshing(true);
        mStartPage = 1;
        requestData(mStartPage);
    }

    @Override
    public void onLoadMore(View loadMoreView) {
        mAdapter.getPageBean().setRefresh(false);
        // 发起请求
        irc.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
        requestData(mStartPage);
    }

    @Override
    public void onDestroy() {
        if (mSubscription != null
                && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        super.onDestroy();
    }
}

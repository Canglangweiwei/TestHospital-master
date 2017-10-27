package com.geecity.hospital.gongdan;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.geecity.hospital.R;
import com.geecity.hospital.adapter.KeyValueAdapter;
import com.geecity.hospital.base.AbsBaseActivity;
import com.geecity.hospital.bean.KeyValueBean;
import com.geecity.hospital.gongdan.api.ApiServiceProtocol;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 成员选择
 */
public class MemberSelectorActivity extends AbsBaseActivity {

    @Bind(R.id.ntb)
    NormalTitleBar ntb;
    @Bind(R.id.ams_spn_position)
    Spinner spnPosition;
    @Bind(R.id.ams_et_username)
    EditText etUsername;

    // 记录当前选中的部门
    private String mSelectedPositionId;

    private List<KeyValueBean> positionList = new ArrayList<>();

    @Override
    protected int initLayoutResID() {
        return R.layout.activity_member_selector;
    }

    @Override
    protected void parseIntent() {

    }

    @Override
    protected void initUi() {
        // 初始化标题栏
        ntb.setTitleText("成员选择");
    }

    @Override
    protected void initDatas() {
        startProgressDialog();
        ApiServiceProtocol apiServiceProtocol = new ApiServiceProtocol();
        apiServiceProtocol.positionList()
                .compose(this.<String>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {

                    @Override
                    public void call(String dataJson) {
                        stopProgressDialog();
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
                            JSONArray data = new JSONArray(result);
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject o = data.getJSONObject(i);
                                KeyValueBean kvb = new KeyValueBean();
                                kvb.setKey(o.getString("id"));
                                kvb.setValue(o.getString("name"));
                                positionList.add(kvb);
                            }
                            if (positionList == null
                                    || positionList.size() == 0)
                                return;
                            fillPosition();
                        } catch (JSONException e) {
                            e.getMessage();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        stopProgressDialog();
                    }
                });
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

    /**
     * 填充职位列表
     */
    private void fillPosition() {
        KeyValueAdapter posAdapter = new KeyValueAdapter();
        posAdapter.getList().clear();
        posAdapter.getList().add(new KeyValueBean("", "全部部门"));
        posAdapter.getList().addAll(positionList);
        spnPosition.setAdapter(posAdapter);
        spnPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                KeyValueBean kv = (KeyValueBean) parent.getSelectedItem();
                mSelectedPositionId = kv.getKey();
                loadData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 获取成员列表
     */
    private void loadData() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        MemberSelectorFragment fragment = MemberSelectorFragment.
                newInstance(mSelectedPositionId, etUsername.getText().toString());
        transaction.replace(R.id.ams_fl, fragment);
        transaction.commit();
    }

    /**
     * 执行搜索
     */
    @OnClick({R.id.button8})
    void onSearch(View view) {
        loadData();
    }
}

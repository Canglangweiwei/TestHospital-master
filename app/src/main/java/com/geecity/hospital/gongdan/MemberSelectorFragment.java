package com.geecity.hospital.gongdan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aspsine.irecyclerview.view.ListPagingFragment;
import com.geecity.hospital.R;
import com.geecity.hospital.adapter.MemberAdapter;
import com.geecity.hospital.base.AbsBaseApplication;
import com.geecity.hospital.bean.MemberBean;
import com.geecity.hospital.bean.UserBean;
import com.geecity.hospital.gongdan.api.GetMembersApiService;
import com.geecity.hospital.xgohttp.WebAPIListener;
import com.wevey.selector.dialog.DialogOnClickListener;
import com.wevey.selector.dialog.MDAlertDialog;

import java.util.List;

/**
 * <p>
 * 功能描述：成员选择fragment
 * </p>
 * Created by LiuPeng on 2015/11/9.
 */
@SuppressWarnings("ALL")
public class MemberSelectorFragment extends ListPagingFragment
        implements
        MemberAdapter.OnSeletedListener {

    private GetMembersApiService apiService;

    private String positionId, searchName;
    // 用户信息
    private UserBean userBean;

    private MDAlertDialog mdAlertDialog;

    public static MemberSelectorFragment newInstance(String positionId, String searchName) {
        MemberSelectorFragment fragment = new MemberSelectorFragment();
        Bundle bundle = new Bundle();
        bundle.putString("positionId", positionId);
        bundle.putString("searchName", searchName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        positionId = getArguments().getString("positionId");
        searchName = getArguments().getString("searchName");
        userBean = AbsBaseApplication.sApp.getUserInfo();
    }

    @Override
    public void loadData(int pageIndex) {
        apiService = new GetMembersApiService(getActivity());
        apiService.setPositionId(positionId);
        apiService.setSearchName(searchName);
        apiService.setUserName(userBean.getUserName());
        apiService.setPage(pageIndex);
        apiService.setListener(new WebAPIListener() {

            @Override
            public void onLoadSuccess(int successCode) {
                onLoaded(true);
            }

            @Override
            public void onLoadFail(int errorCode, String errorMessage) {
                onLoaded(false);
            }
        });
    }

    @Override
    public List getDataList() {
        return apiService.getMembers();
    }

    @Override
    public RecyclerView.Adapter getAdapter(List list) {
        return new MemberAdapter(list, this);
    }

    @Override
    public void onSelected(final MemberBean memberBean, int position) {
        mdAlertDialog = new MDAlertDialog.Builder(getActivity())
                .setHeight(0.25f)  //屏幕高度*0.3
                .setWidth(0.7f)  //屏幕宽度*0.7
                .setTitleVisible(true)
                .setTitleText("温馨提示")
                .setTitleTextColor(R.color.black_light)
                .setContentText("是否要选择操作员？")
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
                        // 选中一个员工，返回
                        Intent intent = new Intent();
                        intent.putExtra("name", memberBean.getName());
                        intent.putExtra("contactWay", memberBean.getContactWay());
                        getActivity().setResult(Activity.RESULT_OK, intent);
                        getActivity().finish();
                    }
                })
                .build();
        if (!getActivity().isFinishing())
            mdAlertDialog.show();
    }
}

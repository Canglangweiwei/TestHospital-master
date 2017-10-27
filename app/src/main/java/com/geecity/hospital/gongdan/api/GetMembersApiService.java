package com.geecity.hospital.gongdan.api;

import android.content.Context;

import com.geecity.hospital.bean.MemberBean;
import com.geecity.hospital.xgohttp.IHttpPostApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 获取成员列表
 * Created by LiuPeng on 2015/10/30.
 */
public class GetMembersApiService extends IHttpPostApi {

    private String userName;
    private String positionId = "";
    private String searchName = "";
    private int page = 1;

    private List<MemberBean> members = new ArrayList<>();

    public GetMembersApiService(Context context) {
        super(context);
    }

    @Override
    protected void getInputParam(HashMap<String, Object> params) {
        params.put("searchName", searchName);
        params.put("positionId", positionId);
        params.put("page", page);
        params.put("userName", userName);
    }

    @Override
    protected boolean analysisOutput(String result) throws JSONException {
        members.clear();
        JSONArray data = new JSONArray(result);
        for (int i = 0; i < data.length(); i++) {
            JSONObject o = data.getJSONObject(i);
            MemberBean mb = new MemberBean();
            mb.setId(o.getString("id"));
            mb.setName(o.getString("name"));
            mb.setDepart(o.getString("depart"));
            mb.setPosition(o.getString("position"));
            mb.setContactWay(o.getString("contactWay"));
            members.add(mb);
        }
        return true;
    }

    @Override
    protected String getMethodName() {
        return "task/members.php";
    }

    public List<MemberBean> getMembers() {
        return members;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }
}

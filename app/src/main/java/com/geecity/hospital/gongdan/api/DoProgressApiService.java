package com.geecity.hospital.gongdan.api;

import android.content.Context;

import com.geecity.hospital.xgohttp.IHttpPostApi;

import org.json.JSONException;

import java.util.HashMap;

/**
 * <p>
 * 派单、接单、完成、回访
 * </P>
 * Created by Administrator on 2017/9/11 0011.
 */
public class DoProgressApiService extends IHttpPostApi {

    private String gdid;               // 工单id
    private String gdzt;            // 工单操作
    private String slr;             // 受理人
    private String username;        //当前登录人

    /**
     * 提示信息
     */
    private String promptMsg;

    /**
     * 构造器
     *
     * @param context 上下文环境
     */
    public DoProgressApiService(Context context) {
        super(context);
    }

    @Override
    protected void getInputParam(HashMap<String, Object> params) {
        params.put("gdid", gdid);
        params.put("gdzt", gdzt);
        params.put("slr", slr);
        params.put("username", username);
    }

    @Override
    protected boolean analysisOutput(String result) throws JSONException {
        setPromptMsg(result);
        return true;
    }

    @Override
    protected String getMethodName() {
        return "gongdan/doProgress.php";
    }

    public String getPromptMsg() {
        return promptMsg;
    }

    public void setPromptMsg(String promptMsg) {
        this.promptMsg = promptMsg;
    }

    public void setGdid(String gdid) {
        this.gdid = gdid;
    }

    public void setGdzt(String gdzt) {
        this.gdzt = gdzt;
    }

    public void setSlr(String slr) {
        this.slr = slr;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

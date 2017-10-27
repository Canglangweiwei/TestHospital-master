package com.geecity.hospital.bean;

import android.text.TextUtils;

/**
 * <p>
 * 工单详情信息
 * </p>
 * Created by Administrator on 2017/9/6 0006.
 */
public class GongdanDetailBean {


    /**
     * id : 21
     * ks : 脑科
     * wp : 青霉素
     * sm : 2
     * tjtime : 2017-10-23 15:11:16
     * zt : 待处理
     * code : 20171023151111894
     */

    private int id;
    private String ks;
    private String wp;
    private String sm;
    private String tjtime;
    private String zt;
    private String code;
    private String czr;
    private String ifsure;

    public GongdanDetailBean() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKs() {
        return ks;
    }

    public void setKs(String ks) {
        this.ks = ks;
    }

    public String getWp() {
        return wp;
    }

    public void setWp(String wp) {
        this.wp = wp;
    }

    public String getSm() {
        return sm;
    }

    public void setSm(String sm) {
        this.sm = sm;
    }

    public String getTjtime() {
        return tjtime;
    }

    public void setTjtime(String tjtime) {
        this.tjtime = tjtime;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCzr() {
        return czr;
    }

    public void setCzr(String czr) {
        this.czr = czr;
    }

    public String getIfsure() {
        if (TextUtils.isEmpty(ifsure))
            ifsure = "false";
        return ifsure;
    }

    public void setIfsure(String ifsure) {
        this.ifsure = ifsure;
    }
}

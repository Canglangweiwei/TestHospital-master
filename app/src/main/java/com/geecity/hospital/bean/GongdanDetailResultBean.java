package com.geecity.hospital.bean;

import java.util.List;

/**
 * <p>
 * 工单详情信息
 * </p>
 * Created by Administrator on 2017/9/8 0008.
 */
public class GongdanDetailResultBean {

    private GongdanDetailBean detail;
    private List<JiedianBean> jiedian;

    public GongdanDetailResultBean() {
        super();
    }

    public GongdanDetailBean getDetail() {
        return detail;
    }

    public void setDetail(GongdanDetailBean detail) {
        this.detail = detail;
    }

    public List<JiedianBean> getJiedian() {
        return jiedian;
    }

    public void setJiedian(List<JiedianBean> jiedian) {
        this.jiedian = jiedian;
    }
}

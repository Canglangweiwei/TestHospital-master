package com.geecity.hospital.version;

/**
 * <p>
 * 功能描述：版本实体类
 * </p>
 * Created by weiwei on 2016-10-13 17:03:53
 */
@SuppressWarnings("ALL")
public class AppUpdateInfo {

    private String verName;//版本名称
    private int verCode;//版本号
    private String verInfo;//版本信息
    private String url;//下载路径
    private String updateFlag;//是否强制更新

    public String getVerName() {
        return verName;
    }

    public void setVerName(String verName) {
        this.verName = verName;
    }

    public int getVerCode() {
        return verCode;
    }

    public void setVerCode(int verCode) {
        this.verCode = verCode;
    }

    public String getVerInfo() {
        return verInfo;
    }

    public void setVerInfo(String verInfo) {
        this.verInfo = verInfo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
    }

    public AppUpdateInfo() {
        super();
    }
}

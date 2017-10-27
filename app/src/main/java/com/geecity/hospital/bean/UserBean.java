package com.geecity.hospital.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * <p>
 * 用户信息
 * </p>
 * Created by Administrator on 2017/10/13 0013.
 */
@SuppressWarnings("ALL")
public class UserBean extends DataSupport implements Serializable {


    /**
     * userid : 237
     * departId : 42
     * bumenName : A项目服务中心
     * realName : 魏巍
     * position : 系统管理员
     * pic : http://0.89892528.cn:8700/photos/user_image/237_1501727803.png
     * glc : 1
     * glc_name : A项目管理处
     * copyright : 聚城管家
     * company : 聚城物业
     * server : http://0.89892528.cn:8877
     * areaId : 1
     * auth : 0
     * zhaohu : 中午好, 魏巍( 系统管理员 )
     */

    private int userid;
    private int departId;
    private String bumenName;
    private String realName;
    private String userName;        // 用户名
    private String password;        // 密码
    private String position;
    private String pic;
    private String glc;
    private String glc_name;
    private String copyright;
    private String company;
    private String server;
    private String areaId;
    private int auth;
    private String zhaohu;

    public UserBean() {
        super();
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getDepartId() {
        return departId;
    }

    public void setDepartId(int departId) {
        this.departId = departId;
    }

    public String getBumenName() {
        return bumenName;
    }

    public void setBumenName(String bumenName) {
        this.bumenName = bumenName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getGlc() {
        return glc;
    }

    public void setGlc(String glc) {
        this.glc = glc;
    }

    public String getGlc_name() {
        return glc_name;
    }

    public void setGlc_name(String glc_name) {
        this.glc_name = glc_name;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public String getZhaohu() {
        return zhaohu;
    }

    public void setZhaohu(String zhaohu) {
        this.zhaohu = zhaohu;
    }
}

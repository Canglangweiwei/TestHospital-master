package com.geecity.hospital.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.geecity.hospital.base.AbsBaseApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 节点
 * </p>
 * Created by Administrator on 2017/9/6 0006.
 */
@SuppressWarnings("ALL")
public class JiedianBean implements Parcelable {

    /**
     * khfwid : 5
     * zt : 处理中
     * czr : 系统管理员
     * cztime : 2017-09-07 17:57:54
     * czcontent : [系统管理员]已经进行接受任务。
     * img : null
     */
    private int khfwid;
    private String zt;
    private String title;
    private String czr;
    private String cztime;
    private String czcontent;
    private String img;

    public JiedianBean() {
        super();
    }

    public int getKhfwid() {
        return khfwid;
    }

    public void setKhfwid(int khfwid) {
        this.khfwid = khfwid;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCzr() {
        return czr;
    }

    public void setCzr(String czr) {
        this.czr = czr;
    }

    public String getCztime() {
        return cztime;
    }

    public void setCztime(String cztime) {
        this.cztime = cztime;
    }

    public String getCzcontent() {
        return czcontent;
    }

    public void setCzcontent(String czcontent) {
        this.czcontent = czcontent;
    }

    public Object getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<String> getPhotos() {
        if (img == null || img.length() == 0) {
            return null;
        }
        String[] strs = img.split(",");
        if (strs == null || strs.length == 0) {
            return null;
        }
        List<String> photos = new ArrayList<>();
        for (String p : strs) {
            if (TextUtils.isEmpty(p))
                continue;
            photos.add(AbsBaseApplication.sApp.getUserInfo().getServer() + p);
        }
        return photos;
    }

    protected JiedianBean(Parcel in) {
        khfwid = in.readInt();
        zt = in.readString();
        title = in.readString();
        czr = in.readString();
        cztime = in.readString();
        czcontent = in.readString();
        img = in.readString();
    }

    public static final Creator<JiedianBean> CREATOR = new Creator<JiedianBean>() {

        @Override
        public JiedianBean createFromParcel(Parcel in) {
            return new JiedianBean(in);
        }

        @Override
        public JiedianBean[] newArray(int size) {
            return new JiedianBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(khfwid);
        dest.writeString(zt);
        dest.writeString(title);
        dest.writeString(czr);
        dest.writeString(cztime);
        dest.writeString(czcontent);
        dest.writeString(img);
    }
}

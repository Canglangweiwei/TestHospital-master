package com.geecity.hospital.message;

/**
 * <p>
 * 推送实体类
 * </p>
 * Created by weiwei on 2017/2/4 10:39.
 */
@SuppressWarnings("ALL")
public class MessageBean {

    private int type;
    private String id;
    private String desc;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public MessageBean() {
        super();
    }
}

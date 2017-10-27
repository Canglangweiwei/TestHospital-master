package com.geecity.hospital.bean;

/**
 * <p>
 * 广告
 * </p>
 * Created by Administrator on 2017/9/25 0025.
 */
public class TopAdBean {

    private int id;
    private String image;
    private String title;

    public TopAdBean() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

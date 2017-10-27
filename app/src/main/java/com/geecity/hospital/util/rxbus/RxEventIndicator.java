package com.geecity.hospital.util.rxbus;

@SuppressWarnings("ALL")
public class RxEventIndicator {

    private int id;
    private String name;
    private String desc;
    private String imageUrl;

    public RxEventIndicator() {
        super();
    }

    public RxEventIndicator(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public RxEventIndicator(int id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

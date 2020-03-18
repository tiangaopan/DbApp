package com.tgp.dbapp.bean;

/**
 * 类的大体描述放在这里
 * @author 田高攀
 * @since 2020/3/18 11:01 AM
 */
public class UserImg {
    private String time;
    private String imgPath;

    public UserImg(String time, String imgPath) {
        this.time = time;
        this.imgPath = imgPath;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}

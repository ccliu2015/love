package com.wisedu.scc.love.sqlite.model;

import java.io.Serializable;

/**
 * Created by JZ on 2015/3/12.
 */
public class Login implements Serializable {

    private String userId;
    private String avatar;
    private String phone;
    private String psw;
    private String status; // 登录状态，on代表已登录，off代表未登录
    private String lastTime; // 最后一次登录时间

    public Login(){}

    public Login(String userId, String avatar, String phone, String psw,
                 String status, String lastTime){
        this.userId = userId;
        this.avatar = avatar;
        this.phone = phone;
        this.psw = psw;
        this.status = status;
        this.lastTime = lastTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

}
package com.wisedu.scc.love.sqlite.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by JZ on 2015/3/9.
 */
public class User implements Serializable{

    private String id;
    private String avatar;
    private String nickName;
    private String location;
    private String phone;
    private String psw;

    public User(){}

    public User(String avatar, String nickName, String location,
                String phone, String psw ){
        this.id = UUID.randomUUID().toString();
        this.avatar = avatar;
        this.nickName = nickName;
        this.location = location;
        this.phone = phone;
        this.psw = psw;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

}

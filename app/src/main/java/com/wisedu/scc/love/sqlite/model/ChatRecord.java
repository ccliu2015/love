package com.wisedu.scc.love.sqlite.model;

import java.io.Serializable;

/**
 * Created by JZ on 2015/3/11.
 */
public class ChatRecord implements Serializable {

    private String id;
    private String avatar;
    private String nickName;
    private String content;
    private String time;
    private String urNumber;


    public ChatRecord(){}

    public ChatRecord(String id, String avatar, String nickName, String content,
                String time, String urNumber ){
        this.id = id;
        this.avatar = avatar;
        this.nickName = nickName;
        this.content = content;
        this.time = time;
        this.urNumber = urNumber;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrNumber() {
        return urNumber;
    }

    public void setUrNumber(String urNumber) {
        this.urNumber = urNumber;
    }

}

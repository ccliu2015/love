package com.wisedu.scc.love.sqlite;

/**
 * 模型工厂
 * Created by JZ on 2015/3/12.
 */
public class ModelFactory {

    /**
     * 用户表名
     * @return
     */
    public static String getUserTableName(){
        return "User";
    }

    /**
     * 聊天登录表名
     * @return
     */
    public static String getLoginTableName(){
        return "Login";
    }

    /**
     * 聊天记录表名
     * @return
     */
    public static String getChatTableName(){
        return "ChatRecord";
    }

}

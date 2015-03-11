package com.wisedu.scc.love.sqlite.entity;

/**
 * Created by JZ on 2015/3/11.
 */
public class EntityFactory {

    private static String PACKAGE = "com.wisedu.scc.love.sqlite.entity.";
    private static String[] TABLES = new String[]{
            "User", "ChatRecord"
    };

    /**
     * 创建表语句
     * @return
     */
    public static String[] CreateSql(){

        // SQL语句
        String sql = "CREATE TABLE "+User.TABLE_NAME+" ("+
                ID + " text not null, "+
                AVATAR + " text not null, "+
                NICKNAME + " text not null, "+
                LOCATION + " text not null, "+
                PHONE + " text not null, "+
                PSW + " text not null);";
        return sql;
    }

}

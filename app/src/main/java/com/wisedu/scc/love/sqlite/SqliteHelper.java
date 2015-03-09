package com.wisedu.scc.love.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wisedu.scc.love.model.User;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by JZ on 2015/3/9.
 */
@EBean
public class SqliteHelper {

    /*常量*/
    private static final String DATABASE_NAME = "LOVE";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "USER";
    private static final String ID = "ID";
    private static final String AVATAR = "AVATAR";
    private static final String NICKNAME = "NICKNAME";
    private static final String LOCATION = "LOCATION";
    private static final String PHONE = "PHONE";
    private static final String PSW = "PSW";

    private DatabaseHelper mOpenHelper;

    public SqliteHelper(Context c){
        mOpenHelper = new DatabaseHelper(c);
    }

    /**
     * 插入一条数据
     * @param avatar
     * @param nickName
     * @param location
     * @param phone
     * @param psw
     * @return
     */
    public boolean insert(String avatar, String nickName, String location, String phone, String psw) {
        try {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ID, UUID.randomUUID().toString());
            values.put(AVATAR, avatar);
            values.put(NICKNAME, nickName);
            values.put(LOCATION, location);
            values.put(PHONE, phone);
            values.put(PSW, psw);
            db.insert(TABLE_NAME, null, values);
            Log.i("插入语句：", TABLE_NAME);
            return true;
        } catch (Exception e) {
            Log.i("插入语句：", TABLE_NAME);
            return false;
        }
    }

    /**
     * 修改一条数据
     * @param id
     * @param avatar
     * @param nickName
     * @param location
     * @param phone
     * @param psw
     * @return
     */
    public boolean update(String id, String avatar, String nickName, String location, String phone, String psw) {
        try {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(AVATAR, avatar);
            values.put(NICKNAME, nickName);
            values.put(LOCATION, location);
            values.put(PHONE, phone);
            values.put(PSW, psw);
            db.update(TABLE_NAME, values, ID + "=?", new String[]{id});
            Log.i("修改语句：", TABLE_NAME);
            return true;
        } catch (Exception e) {
            Log.i("修改语句：", TABLE_NAME);
            return false;
        }
    }

    /**
     * 删除一条数据
     * @param id
     * @return
     */
    public boolean delete(String id) {
        try {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            db.delete(TABLE_NAME, ID+"=?", new String[]{id});
            Log.i("删除语句：", TABLE_NAME);
            return true;
        } catch (Exception e) {
            Log.i("删除语句：", TABLE_NAME);
            return false;
        }
    }

    /**
     * 取出所有数据
     * @return
     */
    public List<User> getAll() {
        try {
            List<User> users = new ArrayList<User>();
            User user;

            // 取出数据
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);
            // 遍历数据
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String avatar = cursor.getString(1);
                String nickName = cursor.getString(2);
                String location = cursor.getString(3);
                String phone = cursor.getString(4);
                String psw = cursor.getString(5);
                user = new User(id, avatar, nickName, location, phone, psw);
                users.add(user);
            }
            cursor.close();
            db.close();
            return users;
        } catch (Exception e) {
            Log.i("查询语句：", TABLE_NAME);
            return null;
        }
    }

    /**
     * 删除一张表
     * @return
     */
    public boolean dropTable() {
        try {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            String sql = "drop table " + TABLE_NAME;
            db.execSQL(sql);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 重新创建一张表
     * @return
     */
    public boolean reCreateTable() {
        try {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            String dropSql = "drop table if exists "+TABLE_NAME;
            String createSql = createSql();
            db.execSQL(dropSql);
            db.execSQL(createSql);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 创建表语句
     * @return
     */
    private static String createSql(){
        // SQL语句
        String sql = "CREATE TABLE "+TABLE_NAME+" ("+
                ID + " text not null, "+
                AVATAR + " text not null, "+
                NICKNAME + " text not null, "+
                LOCATION + " text not null, "+
                PHONE + " text not null, "+
                PSW + " text not null);";
        return sql;
    }

    public static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // SQL语句
            String sql = createSql();
            //执行SQL语句
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO
        }
    }
}

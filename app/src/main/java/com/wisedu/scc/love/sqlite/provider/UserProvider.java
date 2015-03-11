package com.wisedu.scc.love.sqlite.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wisedu.scc.love.sqlite.SqliteHelper;
import com.wisedu.scc.love.sqlite.entity.User;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by JZ on 2015/3/9.
 */
@EBean
public class UserProvider extends BaseProvider {

    /*常量*/
    private static final String TABLE_NAME = "USER";
    private static final String ID = "ID";
    private static final String AVATAR = "AVATAR";
    private static final String NICKNAME = "NICKNAME";
    private static final String LOCATION = "LOCATION";
    private static final String PHONE = "PHONE";
    private static final String PSW = "PSW";

    @Bean
    public SqliteHelper mOpenHelper;

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
     * 根据用户名称和密码取得用户
     * @param phone
     * @param psw
     * @return
     */
    public User getByPhoneAndPsw(String phone, String psw) {
        try {
            User user = null;
            // 取出数据
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            Cursor cursor = db.query(TABLE_NAME, null, PHONE+"=? and "+PSW+"=?", new String[]{phone, psw}, null, null, null, null);
            // 遍历数据
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String avatar = cursor.getString(1);
                String nickName = cursor.getString(2);
                String location = cursor.getString(3);
                user = new User(id, avatar, nickName, location, phone, psw);
            }
            cursor.close();
            db.close();
            return user;
        } catch (Exception e) {
            Log.i("查询语句：", TABLE_NAME);
            return null;
        }
    }

}

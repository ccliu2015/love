package com.wisedu.scc.love.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JZ on 2015/3/9.
 */
@EBean
public class SqliteHelper {

    /*常量*/
    private static final String DATABASE_NAME = "LOVE";
    private static final int DATABASE_VERSION = 1;

    private DatabaseHelper mOpenHelper;

    public SqliteHelper(Context c){
        mOpenHelper = new DatabaseHelper(c);
    }

    /**
     * 插入一条数据
     * @param tableName
     * @return
     */
    public boolean insert(String tableName, Object obj) {
        try {
            // 先处理表
            dealTableFirst(tableName);

            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            ContentValues values = SqlBuilder.geneValues(tableName, obj);
            db.insert(tableName, null, values);
            db.close();
            Log.i("插入语句：", tableName);
            return true;
        } catch (Exception e) {
            Log.i("插入语句：", tableName);
            return false;
        }
    }

    /**
     * 修改一条数据
     * @param tableName
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public boolean update(String tableName, Object obj, String whereClause,
                          String[] whereArgs) {
        try {
            // 先处理表
            dealTableFirst(tableName);

            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            ContentValues values = SqlBuilder.geneValues(tableName, obj);
            db.update(tableName, values, whereClause, whereArgs);
            db.close();
            Log.i("修改语句：", tableName);
            return true;
        } catch (Exception e) {
            Log.i("修改语句：", tableName);
            return false;
        }
    }

    /**
     * 删除数据
     * @param tableName
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public boolean delete(String tableName, String whereClause, String[] whereArgs) {
        try {
            // 先处理表
            dealTableFirst(tableName);

            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            db.delete(tableName, whereClause, whereArgs);
            Log.i("删除语句：", tableName);
            db.close();
            return true;
        } catch (Exception e) {
            Log.i("删除语句：", tableName);
            return false;
        }
    }

    /**
     * 查找数据
     * @return
     */
    public <T> List<T> get(String tableName, String[] columns, String whereClause,
                                 String[] whereArgs, String groupBy, String having,
                                 String orderBy, String limit) {
        // 先处理表
        dealTableFirst(tableName);

        // 取出数据
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(tableName, columns, whereClause,
                whereArgs, groupBy, having, orderBy, limit);
        try {
            List<T> list = new ArrayList<T>();
            while (cursor.moveToNext()) {
                T t = SqlBuilder.cursor2Entity(cursor, tableName);
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (db != null)
                db.close();
        }
        return null;
    }

    /**
     * 检查存在
     * @param tableName
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public boolean check(String tableName, String whereClause, String[] whereArgs) {
        // 先处理表
        dealTableFirst(tableName);

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(tableName,null, whereClause, whereArgs, null, null, null);
        try {
            return (null!=cursor&&cursor.getCount()>0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null)
                cursor.close();
            if(null!=db)
                db.close();
        }
    }

    /**
     * 删除一张表
     * @return
     */
    public boolean dropTable(String tableName) {
        try {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            String sql = "drop table " + tableName;
            db.execSQL(sql);
            db.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 重新创建一张表
     * @return
     */
    public boolean reCreateTable(String tableName) {
        try {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            String dropSql = "drop table if exists "+tableName;
            String createSql = SqlBuilder.geneSql(tableName);
            db.execSQL(dropSql);
            db.execSQL(createSql);
            db.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查表是否存在，不存在则创建
     */
    private void dealTableFirst(String table){
        if(null!=table) {
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            try {
                String sql = "select count(0) from sqlite_master"
                        + " where type ='table' and name ='" + table.trim() + "' ";
                Cursor cursor = db.rawQuery(sql, null);
                if (null == cursor || cursor.getCount() == 0) {
                    String createSql = SqlBuilder.geneSql(table);
                    db.execSQL(createSql);
                } else {
                    cursor.close();
                }
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                if(null!=db)
                    db.close();
            }
        }
    }

    /**
     * 内部静态类
     * SQLite使用帮助
     */
    public static class DatabaseHelper extends SQLiteOpenHelper{

        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /*数据库第一次创建时调用，可以在此建表及做一些初始化*/
        @Override
        public void onCreate(SQLiteDatabase db) {
            // 初始化所有表
            for(String sql : SqlBuilder.allCreateSql()){
                db.execSQL(sql);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO
        }
    }

}

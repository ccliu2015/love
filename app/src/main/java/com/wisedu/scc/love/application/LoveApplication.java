package com.wisedu.scc.love.application;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * 自定义Application
 */
public class LoveApplication extends Application {

    private List<Activity> mList = new LinkedList<>();  // 用于存放Activity的列表
    private static LoveApplication instance;  // 单例

    // 配置信息
    public String userId;
    public String phone;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LoveApplication() {
    }

    /**
     * 获取单例
     * @return
     */
    public synchronized static LoveApplication getInstance() {
        if (null == instance) { 
            instance = new LoveApplication();
        } 
        return instance; 
    }

    /**
     * 将Activity添加至列表
     * @param activity
     */
    public void addActivity(Activity activity) { 
        mList.add(activity); 
    }

    /**
     * 退出应用
     */
    public void exit() { 
        try { 
            for (Activity activity : mList) { 
                if (activity != null) 
                    activity.finish(); 
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally { 
            System.exit(0); 
        } 
    }

    /**
     * 低内存时自动回收垃圾
     */
    public void onLowMemory() {
        super.onLowMemory();     
        System.gc(); 
    }

}
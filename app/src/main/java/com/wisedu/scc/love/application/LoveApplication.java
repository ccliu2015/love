package com.wisedu.scc.love.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import java.util.LinkedList;
import java.util.List;

/**
 * 自定义Application
 */
public class LoveApplication extends Application {

    private List<Activity> mList = new LinkedList<>();  // 用于存放Activity的列表
    private static LoveApplication instance;  // 单例
    public static final String DEFAULT_AVATAR = "avatar_default.jpg";

    // 配置当前用户信息
    public String userId;
    public String avatar;
    public String phone;

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

    /**
     * 初始化ImageLoader
     */
    public static void initImageLoader(Context context) {
        // ImageLoader配置参数
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800)  // default = device screen dimensions
                .threadPoolSize(3) // 线程池大小，默认为3
                .threadPriority(Thread.NORM_PRIORITY - 1) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config); // 必须先初始化ImageLoader
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(getApplicationContext());
    }

}
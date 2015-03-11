package com.wisedu.scc.love.base;

import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.wisedu.scc.love.R;
import com.wisedu.scc.love.utils.CommonUtil;

/**
 * Created by JZ on 2015/3/9.
 */
public class BaseFragmentActivity extends FragmentActivity {

    // 退出系统字段
    protected long exitTime = 0;

    /*图片下载类*/
    public ImageLoader imageLoader = ImageLoader.getInstance();

    /**
     * 初始化图片下载类
     */
    public void initImageLoader(){
        // ImageLoader配置参数
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
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
        imageLoader.init(config); // 必须先初始化ImageLoader
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 在两秒钟内连续按两次返回，则退出
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                CommonUtil.shortToast(getApplicationContext(), "再按一次退出系统");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 当用户按HOME键时的事件
     */
    @Override
    protected void onUserLeaveHint() {
    }

}

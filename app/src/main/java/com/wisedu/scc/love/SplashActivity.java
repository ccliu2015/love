package com.wisedu.scc.love;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wisedu.scc.love.base.BaseActivity;
import com.wisedu.scc.love.config.Define;
import com.wisedu.scc.love.sqlite.ModelFactory;
import com.wisedu.scc.love.sqlite.SqlBuilder;
import com.wisedu.scc.love.sqlite.SqliteHelper;
import com.wisedu.scc.love.utils.CommonUtil;
import com.wisedu.scc.love.utils.TerminalHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by JZ on 2015/3/5.
 */
@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity{

    @Bean
    public TerminalHelper terminalHelper;

    /*定义消息常量*/
    private static final int LOAD_SPLASH_IMAGE = 0;
    private static final int DO_INIT_TASK = 1;
    private static final int TO_LOGIN_ACTIVITY = 2;
    private static final int TO_MAIN_ACTIVITY = 3;
    private String splashUri = "drawable://" + R.drawable.splash;

    @Bean
    public Define define;

    @Bean
    public SqliteHelper sqliteHelper;

    @ViewById(R.id.splashImage)
    public ImageView splashImage;

    // 处理器
    private Handler splashHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_SPLASH_IMAGE:
                    loadSplash();
                    break;
                case DO_INIT_TASK:
                    initTask();
                    break;
                case TO_LOGIN_ACTIVITY:
                    toLoginActivity();
                    break;
                case TO_MAIN_ACTIVITY:
                    toMainActivity();
                    break;
            }
        }
    };

    @AfterViews
    public void doAfterViews(){
        splashHandler.sendEmptyMessage(LOAD_SPLASH_IMAGE);
    }

    /*加载图片*/
    public void loadSplash(){
        initImageLoader(); // 初始化图片下载类
        imageLoader.displayImage(
                splashUri, // 加载图片地址
                splashImage, // 目标图片控件
                Define.NORMAL_OPTIONS,
                new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                        CommonUtil.shortToast(SplashActivity.this, "网络不通，请检查网络设置");
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        if (loadedImage != null) {
                            splashImage.setImageBitmap(loadedImage);
                            splashImage.setScaleType(ImageView.ScaleType.FIT_XY);
                            splashHandler.sendEmptyMessage(DO_INIT_TASK);
                        }
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                    }
                });
    }

    /*初始化资源*/
    public void initTask(){
        // TODO 在此初始化资源
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    splashHandler.sendEmptyMessage(TO_LOGIN_ACTIVITY);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*跳转至登录页*/
    public void toLoginActivity(){
        if(checkLogin()){
            splashHandler.sendEmptyMessage(TO_MAIN_ACTIVITY);
        } else {
            startActivity(new Intent(this, LoginActivity_.class));
            SplashActivity.this.finish();
        }
    }

    /*跳转至主页*/
    public void toMainActivity(){
        startActivity(new Intent(this, MainActivity_.class));
        SplashActivity.this.finish();
    }

    /**
     * 检查是否已登录
     * @return
     */
    private boolean checkLogin(){
        return sqliteHelper.check(ModelFactory.getLoginTableName()
        , SqlBuilder.geneWhere("=", "status"), new String[]{"on"});
    }

}

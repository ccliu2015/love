package com.wisedu.scc.love;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by JZ on 2015/3/5.
 */
@EActivity(R.layout.activity_splash)
public class SplashActivity extends Activity{

    @ViewById(R.id.splashImage)
    public ImageView splashImage;

    @Click(R.id.splashImage)
    public void clickSplash(){
        startActivity(new Intent(SplashActivity.this, MainActivity_.class));
    }

    @AfterViews
    public void doAfterViews(){
        splashImage.setImageResource(R.drawable.splash);

        /*3秒钟之后，进入主页面*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    startActivity(new Intent(SplashActivity.this, MainActivity_.class));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}

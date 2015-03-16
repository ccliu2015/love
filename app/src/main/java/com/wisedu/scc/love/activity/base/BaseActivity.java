package com.wisedu.scc.love.activity.base;

import android.app.Activity;
import android.os.Bundle;

import com.wisedu.scc.love.application.LoveApplication;

/**
 * Created by JZ on 2015/3/9.
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoveApplication.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
    }

}

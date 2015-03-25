package com.wisedu.scc.love.activity;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;

import com.wisedu.scc.love.R;
import com.wisedu.scc.love.utils.CommonUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.ViewById;

/**
 * Created by JZ on 2015/3/17.
 */
@EActivity(R.layout.activity_feed)
public class FeedActivity extends Activity {

    @ViewById(R.id.back)
    protected ImageView back;

    @ViewById(R.id.feed)
    protected MultiAutoCompleteTextView feed;

    @ViewById(R.id.email)
    protected EditText email;

    @ViewById(R.id.submit)
    protected Button submit;

    @AfterViews
    protected void doAfterViews(){
    }

    @FocusChange(R.id.feed)
    protected void dealFeedChange(){
        feed.setHint("");
    }

    @FocusChange(R.id.email)
    protected void dealEmailChange(){
        email.setHint("");
    }

    @Click(R.id.submit)
    protected void dealSubmit(){
        CommonUtil.shortToast(getApplicationContext(), "提交成功！");
    }

    @Click(R.id.back)
    protected void dealBack(){
        this.finish();
    }

}

package com.wisedu.scc.love;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.wisedu.scc.love.utils.CommonUtil;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by JZ on 2015/3/9.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity{

    @ViewById
    public EditText phoneEdit;

    @ViewById
    public EditText pswEdit;

    @ViewById
    public Button loginButton;

    @ViewById
    public Button registerButton;

    @Click(R.id.loginButton)
    public void doLogin(){
        if(CommonUtil.IsEmpty(phoneEdit.getText().toString())||
                CommonUtil.IsEmpty(pswEdit.getText().toString())){
            CommonUtil.shortToast(LoginActivity.this, "请填写完整再登录！");
        } else {

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            LoginActivity.this.finish();
        }
    }

    @Click(R.id.registerButton)
    public void doRegister(){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        LoginActivity.this.finish();
    }

}

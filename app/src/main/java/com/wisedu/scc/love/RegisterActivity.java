package com.wisedu.scc.love;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wisedu.scc.love.utils.CommonUtil;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by JZ on 2015/3/9.
 */
@EActivity(R.layout.activity_register)
public class RegisterActivity extends Activity {

    @ViewById
    public ImageView avatar;

    @ViewById
    public EditText nickEdit;

    @ViewById
    public EditText locationEdit;

    @ViewById
    public EditText phoneEdit;

    @ViewById
    public EditText pswEdit;

    @ViewById
    public Button registerButton;

    @Click(R.id.registerButton)
    public void doLogin(){
        if(null == avatar.getDrawable()){
            CommonUtil.shortToast(RegisterActivity.this, "请设置头像！");
        } else if(CommonUtil.IsEmpty(nickEdit.getText().toString())||
                CommonUtil.IsEmpty(locationEdit.getText().toString())||
                CommonUtil.IsEmpty(phoneEdit.getText().toString())||
                CommonUtil.IsEmpty(pswEdit.getText().toString())){
            CommonUtil.shortToast(RegisterActivity.this, "请填写完整再登录！");
        } else {
            CommonUtil.shortToast(RegisterActivity.this, "注册成功！请登录。");
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            RegisterActivity.this.finish();
        }
    }

}

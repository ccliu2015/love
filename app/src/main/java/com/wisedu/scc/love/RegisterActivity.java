package com.wisedu.scc.love;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wisedu.scc.love.base.BaseActivity;
import com.wisedu.scc.love.sqlite.SqliteHelper;
import com.wisedu.scc.love.utils.CommonUtil;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by JZ on 2015/3/9.
 */
@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {

    @Bean
    public SqliteHelper sqliteHelper;

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
        String nickName = nickEdit.getText().toString();
        String location = locationEdit.getText().toString();
        String phone = phoneEdit.getText().toString();
        String psw = pswEdit.getText().toString();
        /*if(null == avatar.getDrawable()){
            CommonUtil.shortToast(RegisterActivity.this, "请设置头像！");
        } else */if(CommonUtil.IsEmpty(nickName)||
                CommonUtil.IsEmpty(location)||
                CommonUtil.IsEmpty(phone)||
                CommonUtil.IsEmpty(psw)){
            CommonUtil.shortToast(RegisterActivity.this, "请填写完整再登录！");
        } else {
            // 存储填写值
            storeUser("", nickName, location, phone, psw);
            CommonUtil.shortToast(RegisterActivity.this, "注册成功！请登录。");
            startActivity(new Intent(RegisterActivity.this, LoginActivity_.class));
            RegisterActivity.this.finish();
        }
    }

    /**
     * 插入用户数据
     * @param avatar
     * @param nickName
     * @param location
     * @param phone
     * @param psw
     */
    private void storeUser(String avatar, String nickName, String location, String phone, String psw) {
        sqliteHelper.insert(avatar, nickName, location, phone, psw);
    }

}

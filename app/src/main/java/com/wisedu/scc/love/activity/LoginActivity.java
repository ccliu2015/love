package com.wisedu.scc.love.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wisedu.scc.love.R;
import com.wisedu.scc.love.activity.base.BaseExitActivity;
import com.wisedu.scc.love.application.LoveApplication;
import com.wisedu.scc.love.sqlite.ModelFactory;
import com.wisedu.scc.love.sqlite.SqlBuilder;
import com.wisedu.scc.love.sqlite.SqliteHelper;
import com.wisedu.scc.love.sqlite.model.Login;
import com.wisedu.scc.love.sqlite.model.User;
import com.wisedu.scc.love.utils.CommonUtil;
import com.wisedu.scc.love.utils.DecodeUtil;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.List;

/**
 * Created by JZ on 2015/3/9.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseExitActivity {

    @Bean
    public SqliteHelper sqliteHelper;

    @ViewById
    public ImageView avatar;

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
        String phone = phoneEdit.getText().toString();
        String psw = pswEdit.getText().toString();

        if(CommonUtil.isEmpty(phone)||
                CommonUtil.isEmpty(psw)){
            CommonUtil.shortToast(LoginActivity.this, "请填写完整再登录！");
        } else if(!checkUser(phone, psw)){
            CommonUtil.shortToast(LoginActivity.this, "用户不存在或密码错误！");
        } else {
            CommonUtil.shortToast(LoginActivity.this, "登录成功！");
            /*登录成功处理*/
            dealLoginSuccess(phone, psw);
            startActivity(new Intent(LoginActivity.this, MainActivity_.class));
            LoginActivity.this.finish();
        }
    }

    @Click(R.id.registerButton)
    public void doRegister(){
        startActivity(new Intent(LoginActivity.this, RegisterActivity_.class));
    }


    @AfterTextChange(R.id.phoneEdit)
    public void dealPhoneChange(){
        String phone = phoneEdit.getText().toString();
        // 取出用户
        List<User> users = sqliteHelper.get(ModelFactory.getUserTableName(), null,
                SqlBuilder.geneWhere("=","phone"), new String[]{phone}
                , null,  null, null, null);
        if(null!=users&&users.size()>0) {
            String avatarPath = users.get(0).getAvatar();
            if(!CommonUtil.isEmpty(avatarPath)) {
                File file = new File(avatarPath);
                Uri uri = Uri.fromFile(file);
                Bitmap bitmap = DecodeUtil.decode(getApplicationContext(), uri, 400, 400);
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                avatar.setImageDrawable(drawable);
            }
        } else {
            avatar.setImageResource(R.drawable.avatar_default);
        }
    }

    @AfterViews
    public void doAfterViews(){
        dealLogin();
    }

    /**
     * 检查用户是否存在
     * @param phone
     * @param psw
     * @return true 存在，false 不存在
     */
    private boolean checkUser(String phone, String psw) {
        return sqliteHelper.check(ModelFactory.getUserTableName(),
                SqlBuilder.geneWhere("=","phone", "psw"), new String[]{phone, psw});
    }

    /**
     * 选出最近登陆的第一个账户
     */
    private void dealLogin() {
        String avatarPath = "", phone = "";

        // 判断是否从登录页面跳转并有附带数据
        if(CommonUtil.isEmpty(getIntent().getStringExtra("curAvatar"))){
            // 判断登录表中是否有已登录信息
            List<Login> logins = sqliteHelper.get(ModelFactory.getLoginTableName(), new String[]{"userId", "avatar", "phone"},
                    null, null, null,  null, " lastTime desc ", null);
            if(null!=logins&&logins.size()>0) {
                Login login = logins.get(0);
                avatarPath = login.getAvatar();
                phone = login.getPhone();
            }
        } else {
            avatarPath = getIntent().getStringExtra("curAvatar");
            phone = getIntent().getStringExtra("curPhone");
            avatarPath = CommonUtil.isEmpty(avatarPath) ? "" : avatarPath;
            phone = CommonUtil.isEmpty(phone) ? "" : phone;
        }

        // 设置头像、手机
        phoneEdit.setText(phone);
        if(!CommonUtil.isEmpty(avatarPath)) {
            File file = new File(avatarPath);
            Uri uri = Uri.fromFile(file);
            Bitmap bitmap = DecodeUtil.decode(getApplicationContext(), uri, 400, 400);
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            avatar.setImageDrawable(drawable);
        }
    }

    /**
     * 登录成功处理
     */
    private void dealLoginSuccess(String phone, String psw){
        String userId = "", avatar = "";
        // 缓存当前用户信息
        List<User> users = sqliteHelper.get(ModelFactory.getUserTableName(), new String[]{"id", "avatar"},
                SqlBuilder.geneWhere("=","phone", "psw"), new String[]{phone, psw}
                , null,  null, null, null);
        if(null!=users&&users.size()>0){
            User user = users.get(0);
            if(null!=user) {
                userId = user.getId();
                avatar = user.getAvatar();
                LoveApplication.getInstance().setUserId(userId);
                LoveApplication.getInstance().setAvatar(avatar);
            }
        }
        LoveApplication.getInstance().setPhone(phone);
        // 缓存登录信息
        sqliteHelper.insert(ModelFactory.getLoginTableName(),
                new Login(userId, avatar, phone, psw, "on", CommonUtil.getCurrentTime()));
    }

}

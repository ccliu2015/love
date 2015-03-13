package com.wisedu.scc.love;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.wisedu.scc.love.base.BaseActivity;
import com.wisedu.scc.love.sqlite.ModelFactory;
import com.wisedu.scc.love.sqlite.SqlBuilder;
import com.wisedu.scc.love.sqlite.SqliteHelper;
import com.wisedu.scc.love.sqlite.model.Login;
import com.wisedu.scc.love.sqlite.model.User;
import com.wisedu.scc.love.utils.CommonUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by JZ on 2015/3/9.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @Bean
    public SqliteHelper sqliteHelper;

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
            CommonUtil.shortToast(LoginActivity.this, "用户不存在，请先注册！");
        } else {
            CommonUtil.shortToast(LoginActivity.this, "登录成功！");
            // 缓存登录信息
            sqliteHelper.insert(ModelFactory.getLoginTableName(),
                    new Login(phone, psw, "on", CommonUtil.getCurrentTime()));
            startActivity(new Intent(LoginActivity.this, MainActivity_.class));
            LoginActivity.this.finish();
        }
    }

    @Click(R.id.registerButton)
    public void doRegister(){
        startActivity(new Intent(LoginActivity.this, RegisterActivity_.class));
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
        List<Login> logins = sqliteHelper.get(ModelFactory.getLoginTableName(), new String[]{"phone"},
               null, null, null,  null, "lastTime desc", null);
        if(null!=logins&&logins.size()>0){
            Login login = logins.get(0);
            if(null!=login)
                phoneEdit.setText(login.getPhone());
        }
    }

}

package com.wisedu.scc.love;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wisedu.scc.love.base.BaseActivity;
import com.wisedu.scc.love.sqlite.ModelFactory;
import com.wisedu.scc.love.sqlite.SqlBuilder;
import com.wisedu.scc.love.sqlite.SqliteHelper;
import com.wisedu.scc.love.sqlite.model.User;
import com.wisedu.scc.love.utils.CommonUtil;
import com.wisedu.scc.love.utils.RegExpUtil;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JZ on 2015/3/9.
 */
@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {

    private String mPhotoPath;
    private File mPhotoFile;
    public final static int CAMERA_RESULT = 8888;

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
        } else */if(CommonUtil.isEmpty(nickName)||
                CommonUtil.isEmpty(location)||
                CommonUtil.isEmpty(phone)||
                CommonUtil.isEmpty(psw)){
            CommonUtil.shortToast(RegisterActivity.this, "请填写完整再登录！");
        } else if(!RegExpUtil.validatePhone(phone)){
            CommonUtil.shortToast(RegisterActivity.this, "请正确填写手机号码！");
        } else if(sqliteHelper.check(ModelFactory.getUserTableName(),
                SqlBuilder.geneWhere("=", "phone"), new String[]{phone})){
            CommonUtil.shortToast(RegisterActivity.this, "该手机号码已存在！");
        } else {
            // TODO 头像信息处理
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
        sqliteHelper.insert(ModelFactory.getUserTableName(),
                new User(avatar, nickName, location, phone, psw));
    }

    @Click(R.id.avatar)
    public void captureAvatar(){
        try {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            mPhotoPath = "mnt/sdcard/DCIM/Camera/" + getPhotoFileName();
            mPhotoFile = new File(mPhotoPath);
            if (!mPhotoFile.exists()) {
                mPhotoFile.createNewFile();
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(mPhotoFile));
                startActivityForResult(intent, CAMERA_RESULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用时间戳生成照片名称
     * @return
     */
    private String getPhotoFileName() {
         return "avatar".concat(String.valueOf(System.currentTimeMillis())).concat("jpg");
    }

    /**
     * 调用拍照Activity后的返回事件
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_RESULT) {
            Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath, null);
            avatar.setImageBitmap(bitmap);
        }
    }

}
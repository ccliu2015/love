package com.wisedu.scc.love;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wisedu.scc.love.base.BaseActivity;
import com.wisedu.scc.love.config.Constants;
import com.wisedu.scc.love.sqlite.ModelFactory;
import com.wisedu.scc.love.sqlite.SqlBuilder;
import com.wisedu.scc.love.sqlite.SqliteHelper;
import com.wisedu.scc.love.sqlite.model.User;
import com.wisedu.scc.love.utils.CommonUtil;
import com.wisedu.scc.love.utils.EnvironmentUtil;
import com.wisedu.scc.love.utils.RegExpUtil;
import com.wisedu.scc.love.utils.file.FileHelper;
import com.wisedu.scc.love.widget.window.SelectPicPopupWindow;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;

/**
 * Created by JZ on 2015/3/9.
 */
@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {

    private String mPhotoPath;
    private File mPhotoFile;
    private SelectPicPopupWindow popWindow;
    public final static int CAMERA_RESULT = 8888;
    public final static int PHOTO_RESULT = 9999;

    @Bean
    public SqliteHelper sqliteHelper;

    @ViewById(R.id.main)
    public RelativeLayout layout;

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

    /**
     * 拍照和选择图片的底部弹出框
     */
    @Click(R.id.avatar)
    public void captureAvatar(){
        // 实例化SelectPicPopupWindow
        popWindow = new SelectPicPopupWindow(RegisterActivity.this, itemsOnClick);
        // 设置PopupWindow在layout中显示的位置
        popWindow.showAtLocation(layout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     *  为弹出窗口实现监听类
     */
    private OnClickListener itemsOnClick = new OnClickListener(){
        @Override
        public void onClick(View v) {
            popWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo:
                    takePhoto();
                    break;
                case R.id.btn_pick_photo:
                    pickPhoto();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 打开拍照界面
     */
    private void takePhoto(){
        try {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            mPhotoPath = EnvironmentUtil.getExternalPhotoSavePath(
                    FileHelper.IMG_DIR).concat(getPhotoFileName());
            mPhotoFile = new File(mPhotoPath);
            if (!mPhotoFile.exists()) {
                    mPhotoFile.createNewFile();
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(mPhotoFile));
            startActivityForResult(intent, CAMERA_RESULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 选择照片
     */
    private void pickPhoto(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PHOTO_RESULT);
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
        try{
            Intent intent = new Intent(RegisterActivity.this, ImageCutActivity_.class);
            Bundle mBundle = new Bundle();
            if (requestCode == CAMERA_RESULT) {
                mBundle.putString(Constants.TYPE, Constants.TAKE);
                mBundle.putString("path", mPhotoPath);
            } else if(requestCode == PHOTO_RESULT){
                // 读取uri所在的图片
                Uri uri = data.getData();
                mBundle.putString(Constants.TYPE, Constants.PICK);
                mBundle.putParcelable("uri", uri);
            }
            intent.putExtras(mBundle);
            startActivity(intent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
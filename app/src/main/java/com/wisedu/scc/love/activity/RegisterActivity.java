package com.wisedu.scc.love.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import com.wisedu.scc.love.R;
import com.wisedu.scc.love.activity.base.BaseActivity;
import com.wisedu.scc.love.application.LoveApplication;
import com.wisedu.scc.love.sqlite.ModelFactory;
import com.wisedu.scc.love.sqlite.SqlBuilder;
import com.wisedu.scc.love.sqlite.SqliteHelper;
import com.wisedu.scc.love.sqlite.model.User;
import com.wisedu.scc.love.utils.BitmapUtil;
import com.wisedu.scc.love.utils.CommonUtil;
import com.wisedu.scc.love.utils.RegExpUtil;
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

    public final static int CAMERA_RESULT = 8888;
    public final static int PHOTO_RESULT = 9999;
    public final static int CROP_RESULT = 7777;

    private File mPhotoFile;
    private SelectPicPopupWindow popWindow;
    private String avatarUri = "";
    private String nickName = "";
    private String location = "";
    private String phone = "";
    private String psw = "";

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
        nickName = nickEdit.getText().toString();
        location = locationEdit.getText().toString();
        phone = phoneEdit.getText().toString();
        psw = pswEdit.getText().toString();
        /*if(null == avatar.getDrawable()){
            CommonUtil.shortToast(RegisterActivity.this, "请设置头像！");
        } else */if(CommonUtil.isEmpty(nickName)||
                CommonUtil.isEmpty(location)||
                CommonUtil.isEmpty(phone)||
                CommonUtil.isEmpty(psw)){
            CommonUtil.shortToast(RegisterActivity.this, "请填写完整再注册！");
        } else if(!RegExpUtil.validatePhone(phone)){
            CommonUtil.shortToast(RegisterActivity.this, "请正确填写手机号码！");
        } else if(sqliteHelper.check(ModelFactory.getUserTableName(),
                SqlBuilder.geneWhere("=", "phone"), new String[]{phone})){
            CommonUtil.shortToast(RegisterActivity.this, "该手机号码已存在！");
        } else {
            // 保存用户
            if(CommonUtil.isEmpty(avatarUri)){
                avatarUri = BitmapUtil.getImagePath(getApplicationContext(), LoveApplication.DEFAULT_AVATAR);
            }
            storeUser(avatarUri, nickName, location, phone, psw);
            CommonUtil.shortToast(RegisterActivity.this, "注册成功！请登录。");
            Intent intent = new Intent(RegisterActivity.this, LoginActivity_.class);
            intent.putExtra("curAvatar", avatarUri);
            intent.putExtra("curPhone", phone);
            startActivity(intent);
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
            mPhotoFile = BitmapUtil.getImageFile(getApplicationContext());
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
     * 调用拍照Activity后的返回事件
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if (requestCode == CAMERA_RESULT) {
                Uri uri = Uri.fromFile(mPhotoFile);
                startPhotoZoom(uri);
            } else if(requestCode == PHOTO_RESULT){
                // 读取uri所在的图片
                Uri uri = data.getData();
                startPhotoZoom(uri);
            } else if(requestCode == CROP_RESULT){
                dealImage(data);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 图片裁剪
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_RESULT);
    }

    /**
     * 处理裁剪之后的图片数据
     */
    private void dealImage(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            // 取得处理后的图片
            Bitmap photo = extras.getParcelable("data");
            // 保存图片
            File image = BitmapUtil.saveBitmap(getApplicationContext(), photo);
            // 设置头像地址
            avatarUri = image.getAbsolutePath();
            // 设置图片给头像
            Drawable drawable = new BitmapDrawable(getResources(), photo);
            avatar.setImageDrawable(drawable); // 如果网络请求成功后才赋值好点，这里本地版先打开
        }
    }

}
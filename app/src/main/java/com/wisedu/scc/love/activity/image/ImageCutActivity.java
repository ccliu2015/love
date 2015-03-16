package com.wisedu.scc.love.activity.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.RelativeLayout;

import com.wisedu.scc.love.R;
import com.wisedu.scc.love.activity.base.BaseActivity;
import com.wisedu.scc.love.config.Constants;
import com.wisedu.scc.love.utils.CommonUtil;
import com.wisedu.scc.love.widget.image.ClipImageLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;

/**
 * Created by JZ on 2015/3/9.
 */
@EActivity(R.layout.activity_image_cut)
public class ImageCutActivity extends BaseActivity {

    @ViewById(R.id.imageCutLayout)
    public RelativeLayout imageCutLayout;

    @AfterViews
    public void doAfterViews(){
        ClipImageLayout clipImageLayout = new ClipImageLayout(getApplicationContext(), null);
        Drawable drawable = getDrawable();
        clipImageLayout.setDrawable(drawable);
        android.view.ViewGroup.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        imageCutLayout.addView(clipImageLayout, layoutParams);
    }

    /**
     * 获取图片
     * @return
     */
    private Drawable getDrawable(){
        Bitmap bitmap = null;
        // 取得参数
        Bundle bundle = getIntent().getExtras();
        String type = bundle.getString(Constants.TYPE);
        if(type.equals(Constants.TAKE)){
            String path = bundle.getString("path");
            bitmap = BitmapFactory.decodeFile(path, null);
        } else if(type.equals(Constants.PICK)){
            Uri uri = bundle.getParcelable("uri");
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            CommonUtil.shortToast(getApplicationContext(), "获取图片来源失败");
        }

        if (bitmap != null) {
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            return drawable;
        } else {
            return null;
        }
    }

}
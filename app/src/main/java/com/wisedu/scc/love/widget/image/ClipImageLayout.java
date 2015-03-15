package com.wisedu.scc.love.widget.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.wisedu.scc.love.R;

/**
 * 裁剪图片布局
 */
public class ClipImageLayout extends RelativeLayout {

    /**缩放图片控件*/
	private ClipZoomImageView mZoomImageView;
    /**图片边框控件*/
	private ClipImageBorderView mClipImageView;
    /**要处理的图片*/
    private Drawable mDrawable = null;
	/** 水平内边距, 默认为20 */
	private int mHorizontalPadding = 20;

	public ClipImageLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
        /**初始化VIEW*/
		mZoomImageView = new ClipZoomImageView(context);
		mClipImageView = new ClipImageBorderView(context);
		android.view.ViewGroup.LayoutParams layoutParams = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        /**设置图片*/
		mZoomImageView.setImageDrawable(getDrawable());

        /**将VIEW加入Layout*/
		this.addView(mZoomImageView, layoutParams);
		this.addView(mClipImageView, layoutParams);

		/**计算padding的px*/
		mHorizontalPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
						.getDisplayMetrics());
		mZoomImageView.setHorizontalPadding(mHorizontalPadding);
		mClipImageView.setHorizontalPadding(mHorizontalPadding);
	}

	/**
	 * 对外公布设置边距的方法,单位为dp
	 */
	public void setHorizontalPadding(int mHorizontalPadding) {
		this.mHorizontalPadding = mHorizontalPadding;
	}

    /**
     * 对外公布设置边距的方法,单位为dp
     */
    public void setDrawable(Drawable drawable) {
        this.mDrawable = drawable;
    }

    /**
     * 对外公布设置边距的方法,单位为dp
     */
    public Drawable getDrawable() {
        return this.mDrawable;
    }

	/**
	 * 裁切图片
	 * @return
	 */
	public Bitmap clip() {
		return mZoomImageView.clip();
	}

}
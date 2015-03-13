package com.wisedu.scc.love.widget.image;

import android.content.Context;
import android.graphics.Bitmap;
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

	/**
     * 水平内边距
	 * TODO 这里测试，直接写死了大小，真正使用过程中，可以提取为自定义属性
	 */
	private int mHorizontalPadding = 20;

	public ClipImageLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mZoomImageView = new ClipZoomImageView(context);
		mClipImageView = new ClipImageBorderView(context);
		android.view.ViewGroup.LayoutParams layoutParams = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		/**
		 * TODO 这里测试，直接写死了图片，真正使用过程中，可以提取为自定义属性
		 */
		mZoomImageView.setImageDrawable(getResources().getDrawable(
				R.drawable.ic_launcher));
		
		this.addView(mZoomImageView, layoutParams);
		this.addView(mClipImageView, layoutParams);

		// 计算padding的px
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
	 * 裁切图片
	 * @return
	 */
	public Bitmap clip() {
		return mZoomImageView.clip();
	}

}
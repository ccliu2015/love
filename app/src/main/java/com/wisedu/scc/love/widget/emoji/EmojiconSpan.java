package com.wisedu.scc.love.widget.emoji;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;

public class EmojiconSpan extends DynamicDrawableSpan {

	private final Context mContext;
	private final int mResourceId;
	private final int mSize;
	private Drawable mDrawable;

	public EmojiconSpan(Context context, int resourceId, int size) {
		super();
		mContext = context;
		mResourceId = resourceId;
		mSize = size;
	}
	
	public EmojiconSpan(Context context, int resourceId, int size, int verticalAlignment) {
		super(verticalAlignment);
		mContext = context;
		mResourceId = resourceId;
		mSize = size;
	}

	public Drawable getDrawable() {
		if (mDrawable == null) {
			try {
				mDrawable = mContext.getResources().getDrawable(mResourceId);
				int size = mSize;
				if (size == 0) {
					mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(),
							mDrawable.getIntrinsicHeight());
				} else {
					mDrawable.setBounds(0, 0, size, size);
				}
			} catch (Exception e) {
				// swallow
			}
		}
		return mDrawable;
	}
}

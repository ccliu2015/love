/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

public class PullToRefreshScrollView extends PullToRefreshBase<ScrollView> {

	public PullToRefreshScrollView(Context context) {
		super(context);
	}

	public PullToRefreshScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshScrollView(Context context, Mode mode) {
		super(context, mode);
	}

	public PullToRefreshScrollView(Context context, Mode mode,
			AnimationStyle style) {
		super(context, mode, style);
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

	@Override
	protected ScrollView createRefreshableView(Context context,
			AttributeSet attrs) {
		//ScrollView scrollView;
		// if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
		ScrollView scrollView = new InternalScrollViewSDK(context, attrs);
		// } else {
		// scrollView = new ScrollView(context, attrs);
		// }

		scrollView.setId(R.id.scrollview);
		return scrollView;
	}

	@Override
	protected boolean isReadyForPullStart() {
		return mRefreshableView.getScrollY() == 0;
	}

	@Override
	protected boolean isReadyForPullEnd() {
		View scrollViewChild = mRefreshableView.getChildAt(0);
		if (null != scrollViewChild) {
			return mRefreshableView.getScrollY() >= (scrollViewChild
					.getHeight() - getHeight());
		}
		return false;
	}

	@Override
	public void requestChildFocus(View child, View focused) {
		// TODO nothing
	}

	private void doOnBorderListener() {
		boolean loadMore = isReadyForPullEnd();
		
		if (DEBUG) {
			Log.d(LOG_TAG, "loadMore:" + loadMore);
		}
		if (isRefreshing()) {
			return;
		}

		if (isAutoLoadMore() && hasMore()
				&& (getMode() == Mode.BOTH || getMode() == Mode.PULL_FROM_END)
				&& isTouchEvent()) {
			if (loadMore) {
				if (DEBUG) {
					Log.d(LOG_TAG, "load...");
				}
				onLoadMore();
			}
		}
	}

	final class InternalScrollViewSDK extends ScrollView {

		public InternalScrollViewSDK(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public void requestChildFocus(View child, View focused) {
			// TODO nothing
		}

		@Override
		protected void onScrollChanged(int x, int y, int oldx, int oldy) {
			super.onScrollChanged(x, y, oldx, oldy);
			doOnBorderListener();
		}

		// @Override
		// protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
		// int scrollY, int scrollRangeX,
		// int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean
		// isTouchEvent) {
		//
		// final boolean returnValue = super.overScrollBy(deltaX, deltaY,
		// scrollX, scrollY, scrollRangeX,
		// scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
		//
		// // Does all of the hard work...
		// // OverscrollHelper.overScrollBy(PullToRefreshScrollView.this,
		// deltaX, scrollX, deltaY, scrollY,
		// // getScrollRange(), isTouchEvent);
		//
		// return returnValue;
		// }

		/**
		 * Taken from the AOSP ScrollView source
		 */
		private int getScrollRange() {
			int scrollRange = 0;
			if (getChildCount() > 0) {
				View child = getChildAt(0);
				scrollRange = Math.max(0, child.getHeight()
						- (getHeight() - getPaddingBottom() - getPaddingTop()));
			}
			return scrollRange;
		}
	}
}

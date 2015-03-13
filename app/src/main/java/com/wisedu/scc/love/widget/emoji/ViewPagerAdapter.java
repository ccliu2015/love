package com.wisedu.scc.love.widget.emoji;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private List<View> pageViews = new ArrayList<View>();

    public ViewPagerAdapter(List<View> pageViews) {
        super();
        this.pageViews.addAll(pageViews);
    }

    // 显示数目
    @Override
    public int getCount() {
        return pageViews.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager)arg0).removeView(pageViews.get(arg1));
    }

    /***
     * 获取每一个item�?类于listview中的getview
     */
    @Override
    public Object instantiateItem(View arg0, int arg1) {
    	View view = pageViews.get(arg1);
    	if (view.getParent() == null) {
    		((ViewPager)arg0).addView(view);
		}
        return view;
    }
}
package com.wisedu.scc.love;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;

import com.wisedu.scc.love.base.BaseFragmentActivity;
import com.wisedu.scc.love.fragment.ContactTabFragment;
import com.wisedu.scc.love.fragment.FindTabFragment;
import com.wisedu.scc.love.fragment.MainTabFragment;
import com.wisedu.scc.love.fragment.MeTabFragment;
import com.wisedu.scc.love.widget.icon.ChangeColorIcon;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseFragmentActivity implements
        ViewPager.OnPageChangeListener, View.OnClickListener {

    @ViewById(R.id.id_viewpager)
    protected ViewPager mViewPager;

    @ViewById(R.id.id_indicator_main)
    protected ChangeColorIcon main;

    @ViewById(R.id.id_indicator_contact)
    protected ChangeColorIcon contact;

    @ViewById(R.id.id_indicator_find)
    protected ChangeColorIcon find;

    @ViewById(R.id.id_indicator_me)
    protected ChangeColorIcon me;

    private List<Fragment> mTabs = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private List<ChangeColorIcon> mTabIndicator = new ArrayList<>();

    @AfterViews
    public void doAfterViews() {
        setOverflowShowingAlways(); // 设置ActionBar
        getActionBar().setDisplayShowHomeEnabled(false); // 左上角图标不显示
        initData(); // 初始化数据
    }

    /**
     * 设置ActionBar的overflow一直显示
     */
    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化ViewPager相关数据
     */
    private void initData() {
        // 设置ViewPager的Fragment
        MainTabFragment mainTabFragment = new MainTabFragment();
        Bundle args = new Bundle();
        args.putString("param", "参数");
        mainTabFragment.setArguments(args);
        mTabs.add(mainTabFragment);
        ContactTabFragment contactTabFragment = new ContactTabFragment();
        mTabs.add(contactTabFragment);
        FindTabFragment findTabFragment = new FindTabFragment();
        mTabs.add(findTabFragment);
        MeTabFragment meTabFragment = new MeTabFragment();
        mTabs.add(meTabFragment);

        // 初始化ViewPager适配器
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTabs.size();
            }
            @Override
            public Fragment getItem(int arg0) {
                return mTabs.get(arg0);
            }
        };

        // 设置ViewPager的适配器及页面切换事件
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);

        // 初始化TAB指示器
        initTabIndicator();
    }

    /**
     * 初始化TAB指示器
     */
    private void initTabIndicator() {
        // 在TAB指示中添加四个Fragment
        mTabIndicator.add(main);
        mTabIndicator.add(contact);
        mTabIndicator.add(find);
        mTabIndicator.add(me);

        // 设置监听器
        main.setOnClickListener(this);
        contact.setOnClickListener(this);
        find.setOnClickListener(this);
        me.setOnClickListener(this);

        // 将main设置为活跃状态
        main.setIconAlpha(1.0f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0) {
            ChangeColorIcon left = mTabIndicator.get(position);
            ChangeColorIcon right = mTabIndicator.get(position + 1);
            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public void onPageSelected(int arg0) {}

    @Override
    public void onClick(View v) {
        resetOtherTabs();
        switch (v.getId()) {
            case R.id.id_indicator_main:
                mTabIndicator.get(0).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.id_indicator_contact:
                mTabIndicator.get(1).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.id_indicator_find:
                mTabIndicator.get(2).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.id_indicator_me:
                mTabIndicator.get(3).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(3, false);
                break;
        }
    }

    /**
     * 重置其他的Tab
     */
    private void resetOtherTabs() {
        for (int i = 0; i < mTabIndicator.size(); i++) {
            mTabIndicator.get(i).setIconAlpha(0);
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

}

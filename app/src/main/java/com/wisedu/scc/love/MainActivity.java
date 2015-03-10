package com.wisedu.scc.love;

import android.widget.TextView;

import com.wisedu.scc.love.base.BaseActivity;
import com.wisedu.scc.love.utils.CommonUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends BaseActivity {

    @ViewById
    public TextView textView1;

    @AfterViews
    protected void doAfterViews(){
        textView1.setText("哈哈");
        CommonUtil.shortToast(this, "这是一个很好的起步");
    }

    @OptionsItem(R.id.action_settings)
    protected void settings(){
        CommonUtil.shortToast(this, "你点击了设置");
    }

}

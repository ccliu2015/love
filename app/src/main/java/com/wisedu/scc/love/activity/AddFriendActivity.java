package com.wisedu.scc.love.activity;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.wisedu.scc.love.R;
import com.wisedu.scc.love.utils.CommonUtil;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by JZ on 2015/3/17.
 */
@EActivity(R.layout.activity_add_friend)
public class AddFriendActivity extends Activity {

    @ViewById(R.id.searchFriends)
    SearchView searchFriends;

    @ViewById(R.id.myInfo)
    RelativeLayout myInfo;

    @ViewById(R.id.friendContainer)
    RelativeLayout friendContainer;

    @ViewById(R.id.friendList)
    ListView friendList;

    @AfterViews
    public void doAfterViews(){
        searchFriends.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
            public boolean onQueryTextChange(String newText) {
                myInfo.setVisibility(View.GONE);
                friendContainer.setVisibility(View.VISIBLE);
                CommonUtil.shortToast(getApplicationContext(), "未找到相关用户");
                return true;
            }
        });
    }

}

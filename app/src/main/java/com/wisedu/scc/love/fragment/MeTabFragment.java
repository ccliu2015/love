package com.wisedu.scc.love.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wisedu.scc.love.LoginActivity_;
import com.wisedu.scc.love.R;
import com.wisedu.scc.love.sqlite.ModelFactory;
import com.wisedu.scc.love.sqlite.SqliteHelper;
import com.wisedu.scc.love.sqlite.model.Login;

import org.androidannotations.annotations.EFragment;

@EFragment
public class MeTabFragment extends Fragment {

    private TextView textView;

    public MeTabFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_me, container, false);
        textView = (TextView)view.findViewById(R.id.meText);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 先删除登录缓存信息
                SqliteHelper sqliteHelper = new SqliteHelper(MeTabFragment.this.getActivity());
                Login login = new Login();
                login.setStatus("off");
                sqliteHelper.update(ModelFactory.getLoginTableName(), login, null, null);
                // 跳转至登录页面
                MeTabFragment.this.getActivity().finish();
                startActivity(new Intent(MeTabFragment.this.getActivity(), LoginActivity_.class));
            }
        });
        return view;
    }

}
package com.wisedu.scc.love.activity.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wisedu.scc.love.R;
import com.wisedu.scc.love.sqlite.model.ChatRecord;
import com.wisedu.scc.love.utils.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainTabFragment extends Fragment {

    private View view;
    private PullToRefreshListView mPullRefreshListView;
    private SimpleAdapter mAdapter;
    private ArrayList<HashMap<String, Object>> itemsData;

    public MainTabFragment() {
    }

    /**
     * 在onCreate之后调用
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        mPullRefreshListView = (PullToRefreshListView)view.findViewById(R.id.messageList);
        // 设置刷新模式
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        // 设置列表内容
        ListView actualListView = mPullRefreshListView.getRefreshableView();
        //生成动态数组，并且转入数据
        itemsData = getMessages();
        mAdapter= new SimpleAdapter(MainTabFragment.this.getActivity(), //没什么解释
                itemsData,//数据来源
                R.layout.item_list_fragment_main,//XML实现
                new String[]{"Avatar", "UserName", "Message", "Time", "Number"}, //动态数据KEY
                new int[]{R.id.avatar, R.id.userName, R.id.message, R.id.time, R.id.number});
        actualListView.setAdapter(mAdapter);
        /*定义事件*/
        mPullRefreshListView.setOnItemClickListener(new CustomOnItemClickListener());
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // 异步获取消息任务
                new GetMessagesTask().execute();
            }
        });
        return view;
    }

    /**
     * 自定义列表项单击事件
     */
    private class CustomOnItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String, Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);
            CommonUtil.shortToast(MainTabFragment.this.getActivity(), "该用户是：".concat(item.get("UserName").toString()));
        }
    }

    /**
     * 自定义异步任务
     */
    private class GetMessagesTask extends AsyncTask<Void, Void, String[]> {

        /**
         * 后台任务
         * @param params
         * @return
         */
        @Override
        protected String[] doInBackground(Void... params) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        /**
         * 在doInBackground之后执行
         * @param result
         */
        @Override
        protected void onPostExecute(String[] result) {
            mAdapter.notifyDataSetChanged();
            // 必须调用，不然会一直处于刷新状态
            mPullRefreshListView.onRefreshComplete();
            super.onPostExecute(result);
        }
    }

    /**
     * 获得聊天数据
     * @return
     */
    private ArrayList<HashMap<String, Object>> getMessages(){
        //生成动态数组，并且转入数据
        ArrayList<HashMap<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("Avatar", R.drawable.avatar_default);//添加图像资源的ID
            map.put("UserName", "婷子");
            map.put("Message", "好想你，你在哪儿？");
            map.put("Time", "17:10");
            map.put("Number", i);
            data.add(map);
        }
        return data;
    }

}

package com.wisedu.scc.love.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.wisedu.scc.love.R;
import java.util.Collections;

public class ContactTabFragment extends Fragment {

    /**
     * 页面内控件
     */
    private View layout;

	public ContactTabFragment() {
	}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_contact, container, false);
        return layout;
    }


    private SideBar mSideBar;

    private TextView mDialog;

    private ListView mListView;

    private EditText mSearchInput;

    private CharacterParser characterParser;// 汉字转拼音

    private PinyinComparator pinyinComparator;// 根据拼音来排列ListView里面的数据类

    private List<PersonDto> sortDataList = new ArrayList<PersonDto>();

    private SchoolFriendMemberListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.school_friend_member);
        mSideBar = (SideBar) findViewById(R.id.school_friend_sidrbar);
        mDialog = (TextView) findViewById(R.id.school_friend_dialog);
        mSearchInput = (EditText) findViewById(R.id.school_friend_member_search_input);

        mSideBar.setTextView(mDialog);
        mSideBar.setOnTouchingLetterChangedListener(this);

        initData();

    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();

        PersonDto cuser1 = new PersonDto();
        cuser1.setName("阿东");
        cuser1.setSortLetters("a");

        PersonDto cuser2 = new PersonDto();
        cuser2.setName("贝贝");
        cuser2.setSortLetters("b");

        PersonDto cuser3 = new PersonDto();
        cuser3.setName("西西");
        cuser3.setSortLetters("x");

        PersonDto cuser4 = new PersonDto();
        cuser4.setName("楠楠");
        cuser4.setSortLetters("n");

        PersonDto cuser5 = new PersonDto();
        cuser5.setName("君君");
        cuser5.setSortLetters("j");

        PersonDto cuser6 = new PersonDto();
        cuser6.setName("陈诚");
        cuser6.setSortLetters("c");

        PersonDto cuser7 = new PersonDto();
        cuser7.setName("美美");
        cuser7.setSortLetters("m");

        PersonDto cuser8 = new PersonDto();
        cuser8.setName("花园");
        cuser8.setSortLetters("h");

        PersonDto cuser9 = new PersonDto();
        cuser9.setName("天天");
        cuser9.setSortLetters("t");

        PersonDto cuser10 = new PersonDto();
        cuser10.setName("八戒");
        cuser10.setSortLetters("b");

        sortDataList.add(cuser1);
        sortDataList.add(cuser2);
        sortDataList.add(cuser3);
        sortDataList.add(cuser4);
        sortDataList.add(cuser5);
        sortDataList.add(cuser6);
        sortDataList.add(cuser7);
        sortDataList.add(cuser8);
        sortDataList.add(cuser9);
        sortDataList.add(cuser10);

        fillData(sortDataList);
        // 根据a-z进行排序源数据
        Collections.sort(sortDataList, pinyinComparator);
        mAdapter = new SchoolFriendMemberListAdapter(this, sortDataList);
        mListView.setAdapter(mAdapter);
        mSearchInput.addTextChangedListener(this);

    }

    @Override
    public void onTouchingLetterChanged(String s) {
        int position = 0;
        // 该字母首次出现的位置
        if(mAdapter != null){
            position = mAdapter.getPositionForSection(s.charAt(0));
        }
        if (position != -1) {
            mListView.setSelection(position);
        }
    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
        filterData(s.toString(), sortDataList);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr, List<PersonDto> list) {
        List<PersonDto> filterDateList = new ArrayList<PersonDto>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = list;
        } else {
            filterDateList.clear();
            for (PersonDto sortModel : list) {
                String name = sortModel.getName();
                String suoxie = sortModel.getSuoxie();
                if (name.indexOf(filterStr.toString()) != -1 || suoxie.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        mAdapter.updateListView(filterDateList);
    }

    /**
     * 填充数据
     *
     * @param list
     */
    private void fillData(List<PersonDto> list) {
        for (PersonDto cUserInfoDto : list) {
            if (cUserInfoDto != null && cUserInfoDto.getName() != null) {
                String pinyin = characterParser.getSelling(cUserInfoDto.getName());
                String suoxie = CharacterParser.getFirstSpell(cUserInfoDto.getName());

                cUserInfoDto.setSuoxie(suoxie);
                String sortString = pinyin.substring(0, 1).toUpperCase();

                if ("1".equals(cUserInfoDto.getUtype())) {// 判断是否是管理员
                    cUserInfoDto.setSortLetters("☆");
                } else if (sortString.matches("[A-Z]")) {// 正则表达式，判断首字母是否是英文字母
                    cUserInfoDto.setSortLetters(sortString);
                } else {
                    cUserInfoDto.setSortLetters("#");
                }
            }
        }
    }


}
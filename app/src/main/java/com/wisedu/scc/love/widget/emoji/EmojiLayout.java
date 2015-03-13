package com.wisedu.scc.love.widget.emoji;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wisedu.scc.love.R;

import java.util.ArrayList;
import java.util.List;

public class EmojiLayout extends RelativeLayout implements OnItemClickListener, OnClickListener {
	
	private static final int EMOJI_COLUMN = 8;

	private Context context;

	/** 显示表情页的viewpager */
	private ViewPager vpContainer;
	
	/**
	 * 显示颜表情的viewpager
	 */
	private ViewPager textVpContainer;

	/** 表情页界面集合 */
	private ArrayList<View> pageViews;
	
	/**
	 * 颜表情界面集合
	 */
	private ArrayList<View> textPageViews;

	/** 游标显示布局 */
	private LinearLayout llDots;

	/** 游标点集合 */
	private ArrayList<ImageView> pointViews;

	/** 表情集合 */
	private List<List<Emoji>> emojis;

	/** 表情数据填充器 */
	private List<EmojiAdapter> emojiAdapters;
	
	//added by lmyu for 颜表情
	private List<List<String>> textEmojis;
	private List<TextEmojiAdapter> textEmojiAdapters;

	/** 当前表情页 */
	private int current = 0;

	/** 表情页的监听事件 */
	private OnCorpusSelectedListener mListener;
	
	private ViewPagerAdapter adapter;
	
	private ViewPagerAdapter textAdapter;
	
	private TextView btnDefault, btnTextEmoji;
	
	enum EmojiType{
		/**
		 * 图片表情
		 */
		EMOJI,
		/**
		 * 文字颜表情
		 */
		TEXT_EMOJI
	}
	
	EmojiType emojiType = EmojiType.EMOJI;

	public EmojiLayout(Context context) {
		this(context, null);
	}

	public EmojiLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public EmojiLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initLayout(context, attrs);
	}

	private void initLayout(Context context, AttributeSet attrs) {
		this.context = context;
		emojis = EmojiConversionUtil.getInstace(context).emojiLists;
		textEmojis = EmojiConversionUtil.getInstace(context).textEmojiLists;
		LayoutInflater factory = LayoutInflater.from(context);
		factory.inflate(R.layout.widget_emoji_container, this);

		initView();
		initViewPager();
		initPoint();
		initData();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		btnDefault = (TextView)findViewById(R.id.btn_default);
		btnTextEmoji = (TextView)findViewById(R.id.btn_text_emoji);
		vpContainer = (ViewPager) findViewById(R.id.vp_contains);
		textVpContainer = (ViewPager)findViewById(R.id.text_vp_contains);
		llDots = (LinearLayout) findViewById(R.id.ll_dots);
		btnDefault.setOnClickListener(this);
		btnTextEmoji.setOnClickListener(this);
	}

	/**
	 * 初始化显示表情的viewpager
	 */
	private void initViewPager() {
		pageViews = new ArrayList<View>();
		textPageViews = new ArrayList<View>();
		// 左侧添加空页
		View nullView1 = new View(context);
		// 设置透明背景
		nullView1.setBackgroundColor(Color.TRANSPARENT);
		pageViews.add(nullView1);
		textPageViews.add(nullView1);

		// 中间添加表情页

		emojiAdapters = new ArrayList<EmojiAdapter>();
		textEmojiAdapters = new ArrayList<TextEmojiAdapter>();
		for (int i = 0; i < emojis.size(); i++) {
			GridView view = new GridView(context);
			EmojiAdapter adapter = new EmojiAdapter(context, emojis.get(i));
			view.setAdapter(adapter);
			emojiAdapters.add(adapter);
			view.setOnItemClickListener(this);
			view.setNumColumns(EMOJI_COLUMN);
			view.setBackgroundColor(Color.TRANSPARENT);
			view.setHorizontalSpacing(1);
			view.setVerticalSpacing(1);
			view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
			view.setCacheColorHint(0);
			view.setHorizontalSpacing(2);
			view.setVerticalSpacing(11);
			view.setSelector(new ColorDrawable(Color.TRANSPARENT));
			view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
			view.setGravity(Gravity.CENTER);
			pageViews.add(view);
		}
		for (int i = 0; i < textEmojis.size(); i++) {
			GridView view = new GridView(context);
			TextEmojiAdapter adapter = new TextEmojiAdapter(context, textEmojis.get(i));
			view.setAdapter(adapter);
			textEmojiAdapters.add(adapter);
			view.setOnItemClickListener(textEmojiOnitemClick);
			view.setNumColumns(3);
			view.setBackgroundColor(Color.TRANSPARENT);
			view.setHorizontalSpacing(1);
			view.setVerticalSpacing(1);
			view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
			view.setCacheColorHint(0);
			view.setHorizontalSpacing(2);
			view.setVerticalSpacing(11);
			view.setSelector(new ColorDrawable(Color.TRANSPARENT));
			view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
			view.setGravity(Gravity.CENTER);
			textPageViews.add(view);
		}

		// 右侧添加空页面
		View nullView2 = new View(context);
		// 设置透明背景
		nullView2.setBackgroundColor(Color.TRANSPARENT);
		pageViews.add(nullView2);
		textPageViews.add(nullView2);
	}

	/**
	 * 初始化游标
	 */
	private void initPoint() {

		pointViews = new ArrayList<ImageView>();
		ImageView imageView;
		for (int i = 0; i < pageViews.size(); i++) {
			imageView = new ImageView(context);
			imageView.setBackgroundResource(R.drawable.expression_page_normal);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 10;
			layoutParams.rightMargin = 10;
			layoutParams.width = LayoutParams.WRAP_CONTENT;
			layoutParams.height = LayoutParams.WRAP_CONTENT;
			llDots.addView(imageView, layoutParams);
			if (i == 0 || i == pageViews.size() - 1) {
				imageView.setVisibility(View.GONE);
			}
			if (i == 1) {
				imageView.setBackgroundResource(R.drawable.expression_page_selected);
			}
			pointViews.add(imageView);

		}
	}

	/**
	 * 填充数据
	 */
	private void initData() {
		adapter = new ViewPagerAdapter(pageViews);
		textAdapter = new ViewPagerAdapter(textPageViews);
		vpContainer.setAdapter(adapter);
		textVpContainer.setAdapter(textAdapter);

		vpContainer.setCurrentItem(1);
		textVpContainer.setCurrentItem(1);
		current = 0;
		vpContainer.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				current = arg0 - 1;
				// 描绘分页点
				drawPoint(arg0);
				// 如果是第一屏或者是最后一屏禁止滑动，其实这里实现的是如果滑动的是第一屏则跳转至第二屏，如果是最后一屏则跳转到倒数第二屏.
				if (arg0 == pointViews.size() - 1 || arg0 == 0) {
					if (arg0 == 0) {
						vpContainer.setCurrentItem(arg0 + 1);// 第二屏
																// 会再次实现该回调方法实现跳转.
						pointViews.get(1).setBackgroundResource(
								R.drawable.expression_page_normal);
					} else {
						vpContainer.setCurrentItem(arg0 - 1);// 倒数第二屏
						pointViews.get(arg0 - 1).setBackgroundResource(
								R.drawable.expression_page_selected);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		textVpContainer.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				current = arg0 - 1;
				// 描绘分页点
				drawPoint(arg0);
				// 如果是第一屏或者是最后一屏禁止滑动，其实这里实现的是如果滑动的是第一屏则跳转至第二屏，如果是最后一屏则跳转到倒数第二屏.
				if (arg0 == pointViews.size() - 1 || arg0 == 0) {
					if (arg0 == 0) {
						textVpContainer.setCurrentItem(arg0 + 1);// 第二屏
																// 会再次实现该回调方法实现跳转.
						pointViews.get(1).setBackgroundResource(
								R.drawable.expression_page_normal);
					} else {
						textVpContainer.setCurrentItem(arg0 - 1);// 倒数第二屏
						pointViews.get(arg0 - 1).setBackgroundResource(
								R.drawable.expression_page_selected);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	/**
	 * 绘制游标背景
	 */
	public void drawPoint(int index) {
		for (int i = 1; i < pointViews.size(); i++) {
			if (index == i) {
				pointViews.get(i).setBackgroundResource(
						R.drawable.expression_page_selected);
			} else {
				pointViews.get(i).setBackgroundResource(
						R.drawable.expression_page_normal);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Emoji emoji = (Emoji) emojiAdapters.get(current).getItem(arg2);
		if (!TextUtils.isEmpty(emoji.getCharacter())) {
			if (mListener != null)
				mListener.onCorpusSelected(emoji);
		}
	}
	
	private OnItemClickListener textEmojiOnitemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String emoji = (String) textEmojiAdapters.get(current).getItem(position);
			if (mListener != null) {
				mListener.onTextEmojiSelected(emoji);
			}
		}
	};
	
	public void setOnCorpusSelectedListener(OnCorpusSelectedListener listener) {
		mListener = listener;
	}

	/**
	 * 表情选择监听
	 * 
	 * @author naibo-liao
	 * @时间： 2013-1-15下午04:32:54
	 */
	public interface OnCorpusSelectedListener {

		void onCorpusSelected(Emoji emoji);

		void onCorpusDeleted();
		
		void onTextEmojiSelected(String emoji);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_default) {
			if (emojiType == EmojiType.EMOJI) {
				return;
			}
			emojiType = EmojiType.EMOJI;
			vpContainer.setVisibility(View.VISIBLE);
			textVpContainer.setVisibility(View.GONE);
			vpContainer.setCurrentItem(1);
			drawPoint(1);
			btnDefault.setBackgroundColor(getResources().getColor(R.color.emoji_tab_selected));
			btnTextEmoji.setBackgroundColor(getResources().getColor(R.color.emoji_tab_unselected));
		}else if (v.getId() == R.id.btn_text_emoji) {
			if (emojiType == EmojiType.TEXT_EMOJI) {
				return;
			}
			emojiType = EmojiType.TEXT_EMOJI;
			vpContainer.setVisibility(View.GONE);
			textVpContainer.setVisibility(View.VISIBLE);
			textVpContainer.setCurrentItem(1);
			drawPoint(1);
			btnDefault.setBackgroundColor(getResources().getColor(R.color.emoji_tab_unselected));
			btnTextEmoji.setBackgroundColor(getResources().getColor(R.color.emoji_tab_selected));
		}
	}

}

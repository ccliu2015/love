package com.wisedu.scc.love.widget.emoji;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;

import com.wisedu.scc.love.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiConversionUtil {

	/** 每一页表情的个数 */
	private static final int PAGE_SIZE = 24;
	
	private static final int TEXT_EMOJI_PAGE_SIZE = 9;

	private static EmojiConversionUtil mFaceConversionUtil;

	/** 保存于内存中的表情HashMap */
	private HashMap<String, String> emojiMap = new HashMap<String, String>();

	/** 保存于内存中的表情集合 */
	private List<Emoji> emojis = new ArrayList<Emoji>();

	/** 表情分页的结果集合 */
	public List<List<Emoji>> emojiLists = new ArrayList<List<Emoji>>();
	
	private List<String> textEmojis = new ArrayList<String>();
	public List<List<String>> textEmojiLists = new ArrayList<List<String>>();
	
	private Context mContext;

	private EmojiConversionUtil(Context context) {
		getFileText(context);
		mContext = context;
		initTextEmojis(context);
	}

	private void initTextEmojis(Context context) {
		Collections.addAll(textEmojis, context.getResources().getStringArray(R.array.text_emojis));
		int pageCount = (int) Math.ceil(textEmojis.size() / TEXT_EMOJI_PAGE_SIZE + 0.1);

		for (int i = 0; i < pageCount; i++) {
			textEmojiLists.add(getTextEmojiData(i));
		}
	}

	public static EmojiConversionUtil getInstace(Context context) {
		if (mFaceConversionUtil == null) {
			mFaceConversionUtil = new EmojiConversionUtil(context);
		}
		return mFaceConversionUtil;
	}

	/**
	 * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
	 * @param str
	 * @return
	 */
	public SpannableString getExpressionString(CharSequence str) {
		return getExpressionString(str, 29);
	}

	public SpannableString getExpressionString(CharSequence str,
			int size) {
		SpannableString spannableString = new SpannableString(str);
		// 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
		String zhengze = "\\[[^\\]]+\\]";
		// 通过传入的正则表达式来生成一个pattern
		Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
		try {
			size += 10;// 用EditText的size会使得表情添加上去略小，这边hack一下
			dealExpression(spannableString, sinaPatten, 0, size);
		} catch (Exception e) {
			Log.e("dealExpression", e.getMessage());
		}
		return spannableString;
	}

	/**
	 * 添加表情
	 * 
	 * @param context
	 * @param imgId
	 * @param spannableString
	 * @return
	 */
	public SpannableString addFace(Context context, int imgId,
			String spannableString, int size) {
		if (TextUtils.isEmpty(spannableString)) {
			return null;
		}
		size += 10;// 用EditText的size会使得表情添加上去略小，这边hack一下
		EmojiconSpan emojiconSpan = new EmojiconSpan(context, imgId, size);
		SpannableString spannable = new SpannableString(spannableString);
		spannable.setSpan(emojiconSpan, 0, spannableString.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	/**
	 * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
	 * 
	 * @param spannableString
	 * @param patten
	 * @param start
	 * @throws Exception
	 */
	private void dealExpression(SpannableString spannableString, Pattern patten, int start, int size)
			throws Exception {
		Matcher matcher = patten.matcher(spannableString);
		while (matcher.find()) {
			String key = matcher.group();
			// 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
			if (matcher.start() < start) {
				continue;
			}
			String value = emojiMap.get(key);
			if (TextUtils.isEmpty(value)) {
				continue;
			}
			// 通过上面匹配得到的字符串来生成图片资源id
			int resId = mContext.getResources().getIdentifier(value, "drawable",
					mContext.getPackageName());
			if (resId != 0) {
				EmojiconSpan emojiconSpan = new EmojiconSpan(mContext, resId,
						size);
				// int end = matcher.start() + key.length();
				// 将该图片替换字符串中规定的位置中
				spannableString.setSpan(emojiconSpan, matcher.start(),
						matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				// if (end < spannableString.length()) {
				// // 如果整个字符串还未验证完，则继续。。
				// //dealExpression(context, spannableString, patten, end);
				// }
				// break;
			}
		}
	}

	public EmojiconSpan dealExpression(String emoji, int size) {
		String value = emojiMap.get(emoji);
		if (TextUtils.isEmpty(value)) {
			return null;
		}
		int resId = mContext.getResources().getIdentifier(value, "drawable",
				mContext.getPackageName());
		EmojiconSpan emojiconSpan = null;
		if (resId != 0) {
			emojiconSpan = new EmojiconSpan(mContext, resId, size, EmojiconSpan.ALIGN_BASELINE);
		}
		return emojiconSpan;
	}

	public void getFileText(Context context) {
		parseData(FileUtils.getEmojiFile(context), context);
	}

	/**
	 * 解析字符
	 * 
	 * @param data
	 */
	private void parseData(List<String> data, Context context) {
		if (data == null) {
			return;
		}
		Emoji emojEentry;
		try {
			for (String str : data) {
				String[] text = str.split(",");
				String fileName = text[0]
						.substring(0, text[0].lastIndexOf("."));
				emojiMap.put(text[1], fileName);
				int resID = context.getResources().getIdentifier(fileName,
						"drawable", context.getPackageName());

				if (resID != 0) {
					emojEentry = new Emoji();
					emojEentry.setId(resID);
					emojEentry.setCharacter(text[1]);
					emojEentry.setFaceName(fileName);
					emojis.add(emojEentry);
				}
			}
			int pageCount = (int) Math.ceil(emojis.size() / PAGE_SIZE + 0.1);

			for (int i = 0; i < pageCount; i++) {
				emojiLists.add(getData(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取分页数据
	 * 
	 * @param page
	 * @return
	 */
	private List<Emoji> getData(int page) {
		int startIndex = page * PAGE_SIZE;
		int endIndex = startIndex + PAGE_SIZE;

		if (endIndex > emojis.size()) {
			endIndex = emojis.size();
		}
		// 不这么写，会在viewpager加载中报集合操作异常，我也不知道为什么
		List<Emoji> list = new ArrayList<Emoji>();
		list.addAll(emojis.subList(startIndex, endIndex));
		if (list.size() < PAGE_SIZE) {
			for (int i = list.size(); i < PAGE_SIZE; i++) {
				Emoji object = new Emoji();
				list.add(object);
			}
		}
		return list;
	}
	
	private List<String> getTextEmojiData(int page){
		int startIndex = page * TEXT_EMOJI_PAGE_SIZE;
		int endIndex = startIndex + TEXT_EMOJI_PAGE_SIZE;

		if (endIndex > textEmojis.size()) {
			endIndex = textEmojis.size();
		}
		List<String> list = new ArrayList<String>();
		list.addAll(textEmojis.subList(startIndex, endIndex));
		if (list.size() < TEXT_EMOJI_PAGE_SIZE) {
			for (int i = list.size(); i < TEXT_EMOJI_PAGE_SIZE; i++) {
				list.add(new String());
			}
		}
		return list;
	}
}
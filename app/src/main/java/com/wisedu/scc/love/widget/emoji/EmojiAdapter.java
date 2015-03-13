package com.wisedu.scc.love.widget.emoji;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.wisedu.scc.love.R;

import java.util.List;

public class EmojiAdapter extends BaseAdapter {

	private List<Emoji> data;

	private LayoutInflater inflater;

	public EmojiAdapter(Context context, List<Emoji> list) {
		this.inflater = LayoutInflater.from(context);
		this.data = list;
	}

	@Override
	public int getCount() {
		return this.data.size();
	}

	@Override
	public Emoji getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Emoji emoji = data.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.widget_emoji, null);
			viewHolder.ivEmoji = (ImageView) convertView
					.findViewById(R.id.iv_emoji);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if (TextUtils.isEmpty(emoji.getCharacter())) {
			viewHolder.ivEmoji.setImageDrawable(null);
		} else {
			viewHolder.ivEmoji.setTag(emoji);
			viewHolder.ivEmoji.setImageResource(emoji.getId());
		}

		return convertView;
	}

	class ViewHolder {
		public ImageView ivEmoji;
	}
}
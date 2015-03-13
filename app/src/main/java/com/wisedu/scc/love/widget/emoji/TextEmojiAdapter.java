package com.wisedu.scc.love.widget.emoji;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wisedu.scc.love.R;

import java.util.ArrayList;
import java.util.List;

public class TextEmojiAdapter extends BaseAdapter{

	List<String> data = new ArrayList<String>();
	
	private LayoutInflater inflater;
	
	public TextEmojiAdapter(Context context, List<String> list) {
		this.inflater = LayoutInflater.from(context);
		data.addAll(list);
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return data.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.widget_text_emoji_item, null);
			viewHolder.tvEmoji = (TextView) convertView
					.findViewById(R.id.tv_emoji);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tvEmoji.setText(data.get(position));

		return convertView;
	}

	class ViewHolder {
		public TextView tvEmoji;
	}
}

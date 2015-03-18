package com.wisedu.scc.love.widget.sortlist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.wisedu.scc.love.R;

import java.util.List;

/**
 * 成员列表适配器
 */
public class SortListAdapter extends BaseAdapter implements SectionIndexer {

	private LayoutInflater inflater;

	private Activity mActivity;

	private List<PersonDto> list;

	public SortListAdapter(Activity mActivity, List<PersonDto> list) {
		this.mActivity = mActivity;
		this.list = list;
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<PersonDto> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.widget_sortlist_item, null);
			holder = new ViewHolder();
			holder.ivHead = (ImageView) convertView.findViewById(R.id.head);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.title);
			holder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
			holder.tvLine = (TextView) convertView.findViewById(R.id.line);
			holder.tvContent = (LinearLayout) convertView.findViewById(R.id.content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final PersonDto dto = list.get(position);

		if (dto != null) {
			// 根据position获取分类的首字母的Char ascii值
			int section = getSectionForPosition(position);
			// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
			if (position == getPositionForSection(section)) {
				holder.tvLetter.setVisibility(View.VISIBLE);
				holder.tvLetter.setText("☆".equals(dto.getSortLetters()) ? dto.getSortLetters() + "(管理员)" : dto.getSortLetters());
				holder.tvLine.setVisibility(View.VISIBLE);
			} else {
				holder.tvLetter.setVisibility(View.GONE);
				holder.tvLine.setVisibility(View.GONE);
			}
			holder.tvTitle.setText(dto.getName());
		}
		return convertView;
	}

	class ViewHolder {
		ImageView ivHead;
		TextView tvLetter;
		TextView tvTitle;
		TextView tvLine;
		LinearLayout tvContent;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

}

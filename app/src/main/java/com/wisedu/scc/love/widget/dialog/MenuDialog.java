package com.wisedu.scc.love.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.wisedu.scc.love.R;

/**
 * 菜单对话框
 */
public class MenuDialog extends Dialog implements
		View.OnClickListener {

	private Context mContext;
	private LinearLayout mLlDialogContainer;

	private OnMenuClick mOnMenuClick;

	public MenuDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public MenuDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	public MenuDialog(Context context) {
		super(context, R.style.dialog);
		init(context);
	}
	
	private void init(Context context) {
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mContext = context;
		mLlDialogContainer = linearLayout;

		setContentView(linearLayout);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Window dialogWindow = getWindow();
		DisplayMetrics metrics = getContext().getResources()
				.getDisplayMetrics();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = (int) (metrics.widthPixels * 0.7);
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.CENTER;
		dialogWindow.setAttributes(lp);
	}

	public void setArray(int arrayRes) {
		String[] datas = mContext.getResources().getStringArray(arrayRes);
		setArray(datas);
	}
	
	public void setArray(String[] datas) {
		for (int i = 0; i < datas.length; i++) {
			Button btn = (Button) LayoutInflater.from(mContext).inflate(
					R.layout.menu_dialog_item, null);
			btn.setText(datas[i]);
			btn.setTag(i);
			btn.setOnClickListener(this);
			mLlDialogContainer.addView(btn, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, (int) mContext.getResources()
							.getDimension(R.dimen.menu_dialog_item_height)));
			if (i == 0) {
				btn.setBackgroundResource(R.drawable.menu_dialog_item_bg_up);
			} else if (i == datas.length - 1) {
				btn.setBackgroundResource(R.drawable.menu_dialog_item_bg_down);
			} else {
				btn.setBackgroundResource(R.drawable.menu_dialog_item_bg_middle);
			}
		}
	}

	@Override
	public void onClick(View v) {
		this.dismiss();
		if (mOnMenuClick != null) {
			mOnMenuClick.onMenuClickChanged(this, (Integer) v.getTag());
		}
	}

	public void setOnMenuClick(OnMenuClick mOnMenuClick) {
		this.mOnMenuClick = mOnMenuClick;
	}

	public static interface OnMenuClick {
		public void onMenuClickChanged(MenuDialog dialog, int index);
	}
}

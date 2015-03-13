package com.wisedu.scc.love.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wisedu.scc.love.R;

/**
 * 
 * 提供升级与强制升级的对话框
 * 
 * @author DOGN Shengdong
 */
public class UpdaterDialog extends Dialog {

	public UpdaterDialog(Context context, int theme) {
		super(context, theme);
		setCancelable(false);
	}

	public UpdaterDialog(Context context) {
		super(context);
		setCancelable(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window = getWindow();

		final DisplayMetrics metrics = getContext().getResources()
				.getDisplayMetrics();
		final int[] location = new int[2];
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = (int) (metrics.widthPixels * 0.78);
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		// Put window on top of input method
		lp.flags |= WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
		lp.gravity = Gravity.CENTER;
		lp.x = location[0];
		lp.y = location[1];
		window.setAttributes(lp);
	}

	/**
	 * Helper class for creating a custom dialog
	 */
	public static class Builder {

		private Context context;
		private String title;
		private String message;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private boolean isProgress;

		private int gravity = Gravity.LEFT;

		private OnClickListener positiveButtonClickListener,
				negativeButtonClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		/**
		 * Set the Dialog message from String
		 *
		 * @param message
		 * @return
		 */
		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		/**
		 * Set the Dialog message from resource
		 *
		 * @param message
		 * @return
		 */
		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		public Builder setMessageGravity(int gravity) {
			this.gravity = gravity;
			return this;
		}

		/**
		 * Set the Dialog title from resource
		 *
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 *
		 * @param title
		 * @return
		 */
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setProgess(boolean isProgress) {
			this.isProgress = isProgress;
			return this;
		}

		/**
		 * Set a custom content view for the Dialog. If a message is set, the
		 * contentView is not added to the Dialog...
		 *
		 * @param v
		 * @return
		 */
		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 *
		 * @param positiveButtonText
		 * @param listener
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,
				OnClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		/**
		 * Set the positive button text and it's listener
		 *
		 * @param positiveButtonText
		 * @param listener
		 * @return
		 */
		public Builder setPositiveButton(String positiveButtonText,
				OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		/**
		 * Set the negative button resource and it's listener
		 *
		 * @param negativeButtonText
		 * @param listener
		 * @return
		 */
		public Builder setNegativeButton(int negativeButtonText,
				OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		/**
		 * Set the negative button text and it's listener
		 *
		 * @param negativeButtonText
		 * @param listener
		 * @return
		 */
		public Builder setNegativeButton(String negativeButtonText,
				OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		/**
		 * Create the custom dialog
		 */
		public UpdaterDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final UpdaterDialog dialog = new UpdaterDialog(context,
					R.style.dialog);
			View layout = inflater
					.inflate(R.layout.updater_widget_dialog, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			((TextView) layout.findViewById(R.id.updater_widget_dialog_title))
					.setText(title);
			// set the confirm button
			if (positiveButtonText != null) {
				((Button) layout
						.findViewById(R.id.updater_widget_dialog_positiveButton))
						.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					((Button) layout
							.findViewById(R.id.updater_widget_dialog_positiveButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							}); 
				} else {
					// if no confirm button just set the visibility to GONE
					layout.findViewById(R.id.updater_widget_dialog_positiveButton)
							.setVisibility(View.GONE);
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.updater_widget_dialog_positiveButton)
						.setVisibility(View.GONE);
			}

			// set the cancel button
			if (negativeButtonText != null) {
				((Button) layout
						.findViewById(R.id.updater_widget_dialog_negativeButton))
						.setText(negativeButtonText);
				Log.v("ddd", "negativeButtonClickListener:" + negativeButtonClickListener);
				if (negativeButtonClickListener != null) {
					((Button) layout
							.findViewById(R.id.updater_widget_dialog_negativeButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				} else {
					// if no confirm button just set the visibility to GONE
					layout.findViewById(R.id.updater_widget_dialog_negativeButton)
							.setVisibility(View.GONE);
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.updater_widget_dialog_negativeButton)
						.setVisibility(View.GONE);
				final int padding = (int)context.getResources().getDimension(
						R.dimen.dialog_button_padding);
				layout.findViewById(R.id.updater_widget_dialog_btn_container)
						.setPadding(
								padding, 0,
								padding, 0);
			}
			if (title != null) {
				((TextView) layout
						.findViewById(R.id.updater_widget_dialog_title))
						.setText(title);
			} else {
				((TextView) layout
						.findViewById(R.id.updater_widget_dialog_title))
						.setText(R.string.common_widget_dialog_default_title);
			}
			// set the content message
			if (message != null) {
				((TextView) layout
						.findViewById(R.id.updater_widget_dialog_message))
						.setText(message);
				((TextView) layout
						.findViewById(R.id.updater_widget_dialog_message))
						.setGravity(gravity);
			} else if (contentView != null) {
				// if no message set
				// add the contentView to the dialog body
				((LinearLayout) layout
						.findViewById(R.id.updater_widget_dialog_message_container))
						.removeAllViews();
				((LinearLayout) layout
						.findViewById(R.id.updater_widget_dialog_message_container))
						.addView(contentView, new LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT));
			}

			// set the progress
			if (isProgress) {
				layout.findViewById(
						R.id.updater_widget_dialog_progress_container)
						.setVisibility(View.VISIBLE);
				layout.findViewById(
						R.id.updater_widget_dialog_message_container)
						.setVisibility(View.GONE);
				
				((ProgressBar) layout.findViewById(R.id.updater_widget_dialog_progress))
						.setIndeterminate(true);
			} else {
				layout.findViewById(
						R.id.updater_widget_dialog_message_container)
						.setVisibility(View.VISIBLE);
				layout.findViewById(
						R.id.updater_widget_dialog_progress_container)
						.setVisibility(View.GONE);
			}
			dialog.setContentView(layout);
			return dialog;
		}
	}

	public void setTitle(String title) {
		TextView tvTitle = (TextView) findViewById(R.id.updater_widget_dialog_title);
		tvTitle.setText(title);
	}

	public void setMessage(String message) {
		TextView tvMessage = (TextView) findViewById(R.id.updater_widget_dialog_message);
		tvMessage.setText(message);
	}

	public void setPositiveButton(String text) {
		Button btnPositive = (Button) findViewById(R.id.updater_widget_dialog_positiveButton);
		btnPositive.setText(text);
	}

	public void setNegativeButton(String text) {
		Button btnNegative = (Button) findViewById(R.id.updater_widget_dialog_negativeButton);
		btnNegative.setText(text);
	}

	public void setPositiveButtonBackground(int resBackground) {
		Button btnPositive = (Button) findViewById(R.id.updater_widget_dialog_positiveButton);
		btnPositive.setBackgroundResource(resBackground);
	}

	public void setNegativeButtonBackground(int resBackground) {
		Button btnNegative = (Button) findViewById(R.id.updater_widget_dialog_negativeButton);
		btnNegative.setBackgroundResource(resBackground);
	}

	public void setProgress(int progress) {
		TextView tv = (TextView) findViewById(R.id.updater_widget_dialog_progress_desc);
		tv.setText(progress + "%");
		ProgressBar pb = (ProgressBar) findViewById(R.id.updater_widget_dialog_progress);
		pb.setIndeterminate(false);
		pb.setProgress(progress);
	}
	
	public void resetProgress() {
		TextView tv = (TextView) findViewById(R.id.updater_widget_dialog_progress_desc);
		tv.setText("");
		ProgressBar pb = (ProgressBar) findViewById(R.id.updater_widget_dialog_progress);
		pb.setIndeterminate(true);
	}
}

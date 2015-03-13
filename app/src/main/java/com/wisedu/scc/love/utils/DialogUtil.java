package com.wisedu.scc.love.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.wisedu.scc.love.widget.dialog.MenuDialog;
import com.wisedu.scc.love.widget.dialog.MenuDialog.OnMenuClick;
import com.wisedu.scc.love.widget.dialog.UpdaterDialog;

public class DialogUtil {

	static Dialog mDialog = null;

	public static void showProgressDialog(Context context) {
		ProgressUtil.showProgress(context);
	}

	public static void showProgressDialog(Context context, int stringRes) {
		ProgressUtil.showProgress(context, stringRes);
	}

	public static void hideProgressDialog() {
		ProgressUtil.hideProgress();
	}

	public static void showMenuDialog(Context context, int arrayRes,
			OnMenuClick listener) {
		mDialog = new MenuDialog(context);
		((MenuDialog) mDialog).setArray(arrayRes);
		((MenuDialog) mDialog).setOnMenuClick(listener);
		mDialog.show();
	}
	
	public static void showMenuDialog(Context context, int arrayRes,
			final OnMenuClick listener,
			DialogInterface.OnCancelListener cancelListener) {
		mDialog = new MenuDialog(context);
		((MenuDialog) mDialog).setArray(arrayRes);
		((MenuDialog) mDialog).setOnMenuClick(listener);
		mDialog.setOnCancelListener(cancelListener);
		mDialog.show();
	}

	public static UpdaterDialog showUpdaterDialog(Context context,
			String title, String message, String positiveButtonText,
			DialogInterface.OnClickListener positiveListener) {
		UpdaterDialog dialog = new UpdaterDialog.Builder(context)
				.setTitle(title).setMessage(message)
				.setPositiveButton(positiveButtonText, positiveListener)
				.setProgess(false).create();
		dialog.show();
		return dialog;
	}

	public static UpdaterDialog showUpdaterDialog(Context context,
			String title, String message, String positiveButtonText,
			String negativeButtonText,
			DialogInterface.OnClickListener positiveListener,
			DialogInterface.OnClickListener negativeListener) {
		UpdaterDialog dialog = new UpdaterDialog.Builder(context)
				.setTitle(title).setMessage(message)
				.setPositiveButton(positiveButtonText, positiveListener)
				.setNegativeButton(negativeButtonText, negativeListener)
				.setProgess(false).create();
		dialog.show();
		return dialog;
	}

	public static UpdaterDialog showUpdaterDialog(Context context,
			String title, String positiveButtonText,
			DialogInterface.OnClickListener positiveListener) {
		UpdaterDialog dialog = new UpdaterDialog.Builder(context)
				.setTitle(title)
				.setPositiveButton(positiveButtonText, positiveListener)
				.setProgess(true).create();
		dialog.show();
		return dialog;
	}

	public static void dismissMenuDialog() {
		dismissListDialog();
	}

	public static void dismissListDialog() {
		try {
			if (mDialog != null) {
				mDialog.dismiss();
				mDialog = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

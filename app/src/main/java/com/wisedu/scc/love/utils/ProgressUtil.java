package com.wisedu.scc.love.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.wisedu.scc.love.R;

public class ProgressUtil {

	public static ProgressDialog mDialog = null;

	public static void showProgress(Context context) {
        showProgress(context, R.string.default_progress_dialog_message, true);
	}

	public static void showProgress(Context context, int textRes) {
		showProgress(context, context.getString(textRes), true);
	}

    public static void showProgress(Context context, String text) {
        showProgress(context, text, true);
    }

    public static void showProgress(Context context, boolean cancelable) {
        showProgress(context, R.string.default_progress_dialog_message, cancelable);
    }

    public static void showProgress(Context context, int textRes, boolean cancelable) {
        showProgress(context, context.getString(textRes), cancelable);
    }

    public static void showProgress(Context context, String textRes, boolean cancelable) {
        try {
            if (mDialog == null) {
                mDialog = new ProgressDialog(context);
            }
            mDialog.setCancelable(cancelable);
            mDialog.setMessage(textRes);
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public static void hideProgress() {
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

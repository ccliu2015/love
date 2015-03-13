package com.wisedu.scc.love.widget.dialog;

import android.app.DatePickerDialog;
import android.content.Context;

/**
 * Created by denny on 1/20/15.
 */
public class DatePickDialogCancel extends DatePickerDialog {

    public DatePickDialogCancel(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
    }

    public DatePickDialogCancel(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context, theme, callBack, year, monthOfYear, dayOfMonth);
    }

    @Override
    protected void onStart() {
    }

    @Override
    protected void onStop() {
    }

}

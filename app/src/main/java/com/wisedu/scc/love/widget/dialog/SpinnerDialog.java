/*
package com.wisedu.scc.love.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

*/
/**
 * Created by chenupt@gmail.com on 12/5/14.
 * Description :
 *//*

public class SpinnerDialog extends Dialog{

    WheelVerticalView wheelVerticalView;
    Builder builder;
    ImageView cancelBtn;
    ImageView okBtn;
    TextView titleTextView;

    public SpinnerDialog(Context context) {
        super(context);
    }

    public SpinnerDialog(Context context, int theme, Builder builder) {
        super(context, theme);
        this.builder = builder;
    }

    protected SpinnerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_widget_dialog_spinner);
        initWindow();
        findViews();
        initView();
        loadData();
    }

    private void initWindow(){
        WindowManager.LayoutParams para = getWindow().getAttributes();
        para.width = WindowManager.LayoutParams.MATCH_PARENT;
        para.windowAnimations = R.style.SpinnerDialogAnim;
        para.gravity = Gravity.BOTTOM;
    }

    private void findViews(){
        wheelVerticalView = (WheelVerticalView) findViewById(R.id.common_vertical_wheel);
        okBtn = (ImageView) findViewById(R.id.common_ok_btn);
        cancelBtn = (ImageView) findViewById(R.id.common_cancel_btn);
        titleTextView = (TextView) findViewById(R.id.common_title_text);
    }

    private void initView(){
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.onSelectListener.onSelect(
                        wheelVerticalView.getCurrentItem(),
                        builder.stringList.get(wheelVerticalView.getCurrentItem()));
                dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void loadData(){
        String[] arr = builder.stringList.toArray(new String[builder.stringList.size()]) ;
        ArrayWheelAdapter adapter = new ArrayWheelAdapter<String>(getContext(), arr);
        wheelVerticalView.setViewAdapter(adapter);
        wheelVerticalView.setCurrentItem(builder.currentItem);
        if(!TextUtils.isEmpty(builder.title)){
            titleTextView.setText(builder.title);
        }
    }

    public interface OnSelectListener{
        public void onSelect(int index, String name);
    }

    public static Builder from(Context context){
        return new Builder(context);
    }

    public static class Builder {
        private String title;
        private List<String> stringList;
        private int currentItem;
        private String currentItemName;
        private Context context;
        private OnSelectListener onSelectListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setStringList(List<String> stringList) {
            this.stringList = stringList;
            return this;
        }

        public Builder setOnSelectListener(OnSelectListener onSelectListener) {
            this.onSelectListener = onSelectListener;
            return this;
        }

        public Builder setCurrentItem(int currentItem) {
            this.currentItem = currentItem;
            return this;
        }

        public Builder setCurrentItem(String stringName) {
            this.currentItemName = stringName;
            return this;
        }

        public void show(){
            if(!TextUtils.isEmpty(currentItemName)){
                int index = stringList.indexOf(currentItemName);
                if(index > 0){
                    currentItem = index;
                }
            }
            Dialog dialog = new SpinnerDialog(context, R.style.dialog, this);
            dialog.show();
        }
    }
}
*/

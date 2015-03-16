package com.wisedu.scc.love.widget.dialog;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.wisedu.scc.love.R;

public class PhotoSelectDialog extends Activity implements OnClickListener{
    
	Button camera,album,cancel;
	
	public static int CAMERA_CODE = 110;
	public static int ALBUM_CODE = 111;
	public static int CANCEL_CODE = 112;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_select_dialog);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);//（全屏）需要添加的语句  
		initView();
		initListener();
	}

	/**
	 * 初始化控件
	 */
	private void initView(){
		this.camera = (Button)findViewById(R.id.btn_take_photo);
		this.album = (Button)findViewById(R.id.btn_pick_photo);
		this.cancel = (Button)findViewById(R.id.btn_cancel);
	}
	
	/**
	 * 注册事件
	 */
	private void initListener(){
		this.camera.setOnClickListener(this);
		this.album.setOnClickListener(this);
		this.cancel.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_take_photo:
			setResult(CAMERA_CODE);
			finish();
			break;
		case R.id.btn_pick_photo:
			setResult(ALBUM_CODE);
			finish();
			break;
		case R.id.btn_cancel:			setResult(CANCEL_CODE);
			finish();
			break;
		default:
			break;
		}
	}

}
package com.wisedu.scc.love;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_show_image)
public class ShowImageActivity extends Activity {

    @ViewById(R.id.id_showImage)
	public ImageView mImageView;

	@AfterViews
	protected void doAfterViews() {
		byte[] b = getIntent().getByteArrayExtra("bitmap");
		Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
		if (bitmap != null) {
			mImageView.setImageBitmap(bitmap);
		}
	}

}
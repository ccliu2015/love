package com.wisedu.scc.love.widget.gallery;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wisedu.scc.love.R;
import com.wisedu.scc.love.utils.BitmapUtil;

import java.util.List;

public class PhotoActivity extends Activity implements OnClickListener,
        ViewPager.OnPageChangeListener {

	private static final String TAG = "PhotoActivity";

	public static final String IMAGES = "IMAGES";
	
	public static final String IMAGE_POSITION = "IMAGE_POSITION";
	
	private static final String STATE_POSITION = "STATE_POSITION";

	private static final int PAGER_MARGIN_DP = 30;

	private ImageLoader mImageLoader = ImageLoader.getInstance();
	private DisplayImageOptions mOptions;

	private ViewPager mViewPager;
	private int mPosition;
	private boolean mPaused;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(onReturnContentLayout());

		Bundle bundle = getIntent().getExtras();
		
		String[] imageUrls = null;
			
		try {
			imageUrls = bundle.getStringArray(IMAGES);
			List<String> list = bundle.getStringArrayList(IMAGES);
			if(list != null) {
				imageUrls = new String[list.size()];
				list.toArray(imageUrls);
			}
		} catch(Exception e) {
			//INGORE
		}
		int pagerPosition = bundle.getInt(IMAGE_POSITION, 0);

		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		mOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		mViewPager.setPageMargin(PAGER_MARGIN_DP);
		mViewPager.setPageMarginDrawable(new ColorDrawable(Color.BLACK));
		mViewPager.setAdapter(new SamplePagerAdapter(getApplicationContext(),
				mImageLoader, mOptions, imageUrls));
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setCurrentItem(pagerPosition);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_POSITION, mPosition);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mPosition = savedInstanceState.getInt(STATE_POSITION);
		mViewPager.setCurrentItem(mPosition);
	}

	protected int onReturnContentLayout() {
		return R.layout.viewpager;
	}

	@Override
	public void onStart() {
		super.onStart();
		mPaused = false;
	}

	@Override
	public void onStop() {
		super.onStop();
		mPaused = true;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent m) {
		if (mPaused)
			return true;
		return super.dispatchTouchEvent(m);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {

	}

	static class SamplePagerAdapter extends PagerAdapter implements
            ImageLoadingListener {

		private Context context;
		private ImageLoader imageLoader;
		private DisplayImageOptions options;
		private LayoutInflater inflater;
		private String[] imageUrls;
		private Handler handler = new Handler();

		public SamplePagerAdapter(Context context, ImageLoader imageLoader,
				DisplayImageOptions options, String[] imageUrls) {
			this.context = context;
			this.imageLoader = imageLoader;
			this.options = options;
			this.imageUrls = imageUrls;
			this.inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return imageUrls.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			View imageLayout = inflater.inflate(R.layout.viewimage, container,
					false);
			PhotoView photoView = (PhotoView) imageLayout
					.findViewById(R.id.image);
			ProgressBar spinner = (ProgressBar) imageLayout
					.findViewById(R.id.loading);
			photoView.setTag(spinner);

	/*		imageLoader.decoderImage(imageUrls[position], photoView, options,
					this);*/

			container.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			if(view == null) return;
			ProgressBar spinner = (ProgressBar) view.getTag();
			spinner.setVisibility(View.VISIBLE);
			view.setVisibility(View.GONE);
		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			String message = null;
			switch (failReason.getType()) {
			case IO_ERROR:
				message = "Input/Output error";
				break;
			case DECODING_ERROR:
				message = "Image can't be decoded";
				break;
			case NETWORK_DENIED:
				message = "Downloads are denied";
				break;
			case OUT_OF_MEMORY:
				message = "Out Of Memory error";
				break;
			case UNKNOWN:
				message = "Unknown error";
				break;
			}
			Log.w(TAG, "imageUri = " + imageUri + " message = " + message);
			ProgressBar spinner = (ProgressBar) view.getTag();
			spinner.setVisibility(View.GONE);
			view.setVisibility(View.VISIBLE);
		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if(view == null) return;
			PhotoView photoView = ((PhotoView) view);
			ProgressBar spinner = (ProgressBar) photoView.getTag();
			spinner.setVisibility(View.GONE);
			view.setVisibility(View.VISIBLE);
		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			if(view == null) return;
			ProgressBar spinner = (ProgressBar) view.getTag();
			spinner.setVisibility(View.GONE);
			view.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		this.mPosition = position;
	}

	static class Decode extends Thread {

		private View view;
		private Object object;
		private Handler handler;

		public Decode(View view, Object object, Handler handler) {
			this.view = view;
			this.object = object;
			this.handler = handler;
		}

		@Override
		public void run() {
			BitmapFactory.Options options = new BitmapFactory.Options();
			BitmapUtil.configOptions(options, Bitmap.Config.ARGB_8888);
			BitmapUtil.sampleOptions(options, object);
			Bitmap[] bitmaps = BitmapUtil.decodeBitmaps(object, options);
			handler.post(new Display(view, bitmaps));
		}
	}

	static class Display implements Runnable {

		private View view;
		private Bitmap[] bitmaps;

		public Display(View view, Bitmap[] bitmaps) {
			this.view = view;
			this.bitmaps = bitmaps;
		}

		@Override
		public void run() {
			PhotoView photoView = ((PhotoView) view);
			ProgressBar spinner = (ProgressBar) photoView.getTag();
			photoView.setSplitedBitmaps(bitmaps);
			spinner.setVisibility(View.GONE);
			view.setVisibility(View.VISIBLE);
		}

	}

}

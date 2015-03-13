package com.wisedu.scc.love.widget.gif;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.TextView;

import java.text.DecimalFormat;

public class NumAnimation extends Animation implements AnimationListener {
	Context context;
	TextView textView;
	double num;

	public NumAnimation(Context context, TextView textView, float num) {
		this.context = context;
		this.textView = textView;
		this.num = num;
		setAnimationListener(this);
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		textView.setText(formatFloat(num * interpolatedTime));
	}

	public void startAnimation() {
		textView.startAnimation(this);
	}

	public final static String formatFloat(double value) {
		DecimalFormat df = new DecimalFormat("#.00");
		return df.format(value);
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		textView.setText(formatFloat(num));
	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

}

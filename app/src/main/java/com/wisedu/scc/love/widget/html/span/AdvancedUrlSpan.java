package com.wisedu.scc.love.widget.html.span;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.view.View;

public class AdvancedUrlSpan extends URLSpan {

	public AdvancedUrlSpan(Parcel src) {
		super(src);
	}

	public AdvancedUrlSpan(String url) {
		super(url);
	}

	@Override
	public void onClick(View widget) {
        try {
            Uri uri = Uri.parse(getURL());
            Context context = widget.getContext();
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if(TextUtils.isEmpty(uri.getScheme())
                    || "http".equalsIgnoreCase(uri.getScheme())
                    || "https".equalsIgnoreCase(uri.getScheme())) {
                intent.setPackage(context.getPackageName());
            }
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                //跳外部浏览器
                final Uri lastUri = Uri.parse(getURL());
                Intent lastIntent = new Intent(Intent.ACTION_VIEW, lastUri);
                context.startActivity(lastIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	@Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }

}

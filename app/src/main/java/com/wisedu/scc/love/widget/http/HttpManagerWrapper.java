package com.wisedu.scc.love.widget.http;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import com.wisedu.scc.love.utils.AppUtil;
import com.wisedu.scc.love.widget.http.HttpManager.HttpConfig;
import java.io.File;
import java.io.Serializable;
import java.util.Map;

/**
 * 成绩查询对外接口
 * 
 * @author DONG Shengdong
 * 
 */
public class HttpManagerWrapper implements RequestInterpator {

	private static final String TAG = "HttpManagerWrapper";

    public static final String DATA = "DATA";

	private static HttpManagerWrapper mHttpManagerWrapper = null;

	private static HttpManager.HttpConfig httpConfig = null;

    public static String FS_URL;

	private Context context;

	private HttpManagerWrapper(Context context) {
		this.context = context;
		httpConfig = new HttpConfig();
		final String prefix = AppUtil
				.getMetaString(context, Platform.API_URL);
        FS_URL = AppUtil
                .getMetaString(context, Platform.FS_URL);

		httpConfig.setPrefix(prefix);
		httpConfig.setRequestInterpator(this);
		httpConfig.setHeadMap(AppUtil.getConfigMap(context, "props"));
	}

	public static synchronized HttpManagerWrapper getInstance(Context context) {
		if (mHttpManagerWrapper == null || httpConfig == null) {
			mHttpManagerWrapper = new HttpManagerWrapper(context);
		}
		return mHttpManagerWrapper;
	}

	public HttpConfig getHttpConfig() {
		return httpConfig;
	}

	public void post(final String url, final ICallback callback,
			final Object... objs) {
		this.post(url, callback, null, objs);
	}

	public void post(final String url, final ICallback callback,
			final Map<String, Object> pararms, final Object... objs) {
		Log.v(TAG, "post url=" + url);
        HttpManager.create(httpConfig).postScc(url, new AutoHttpResponseHandler() {

            @Override
            protected void onSuccess(Header[] headers, int state, int status,
                                     String data, String msg) {
                if (state == 0) {
                    if (callback != null) {
                        callback.onSuccess(url, data, objs);
                    }
                } else {
                    if (callback != null) {
                        callback.onFailed(url, state, msg, objs);
                    }
                }
            }

        }, pararms);
	}

	public void postZ(final String url, final ICallback callback,
			final Object... objs) {
		Log.v(TAG, "postZ url=" + url);
        HttpManager.create(httpConfig).postScc(url, new AutoHttpResponseHandler() {

            @Override
            protected void onSuccess(Header[] headers, int state, int status,
                                     String data, String msg) {
                if (state == 0) {
                    if (callback != null) {
                        callback.onSuccess(url, data, objs);
                    }
                } else {
                    if (callback != null) {
                        callback.onFailed(url, state, msg, objs);
                    }
                }
            }

        }, objs);
	}

    public String postSync(final String url, final Map<String, Object> pararms) {
        return HttpManager.create(httpConfig).postServiceSync(url, pararms);
    }

    public String uploadSync(String url, final File file, final String bizKey) {
        try {
            RequestParams params = new RequestParams();
            params.put("file", file);
            params.put("bizKey", bizKey);
            return HttpManager.create(httpConfig).postSync(url, params);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	@Override
	public boolean intercept(String url, int status) {
		Log.v(TAG, "intercept url=" + url + " status=" + status);
		if (status == 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean loginOffline() {
		Log.v(TAG, "loginOffline...");
        return Platform.getPlatform(context).login(HttpManager.create(httpConfig));
	}

	@Override
	public void requestStart() {
        Platform.getPlatform(context).attachHeader(HttpManager.create(httpConfig));
    }

    public void startActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    public void startActivity(Context context, Class<?> cls, Serializable data) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(DATA, data);
        context.startActivity(intent);
    }

    public void startActivity(Context context, Class<?> cls, Bundle data) {
        Intent intent = new Intent(context, cls);
        intent.putExtras(data);
        context.startActivity(intent);
    }

}

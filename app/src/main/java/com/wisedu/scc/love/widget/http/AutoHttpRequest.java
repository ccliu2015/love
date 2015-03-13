package com.wisedu.scc.love.widget.http;

import android.util.Log;

import com.loopj.android.http.AsyncHttpRequest;
import com.loopj.android.http.ResponseHandlerInterface;
import com.wisedu.scc.love.utils.ReflectUtil;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AutoHttpRequest extends AsyncHttpRequest {

	private final static String TAG = "AutoHttpRequest";
	private final static boolean DEBUG = true;

	private final ResponseHandlerInterface responseHandler;
	private final HttpManager.HttpConfig mHttpConfig;

	private static Field IS_FINISH_FIELD;
	private static Method MAKE_REQUEST_WITH_RETRIES_ENTITY;

	public AutoHttpRequest(AbstractHttpClient client, HttpContext context,
			HttpUriRequest request, ResponseHandlerInterface responseHandler,
			HttpManager.HttpConfig httpConfig) {
		super(client, context, request, responseHandler);
		this.responseHandler = responseHandler;
		this.mHttpConfig = httpConfig;
		IS_FINISH_FIELD = ReflectUtil.getField(getClass(), "isFinished");
		IS_FINISH_FIELD.setAccessible(true);
		MAKE_REQUEST_WITH_RETRIES_ENTITY = ReflectUtil.getMethod(getClass(),
                "makeRequestWithRetries");
		MAKE_REQUEST_WITH_RETRIES_ENTITY.setAccessible(true);
	}

	public void setFinished(boolean value) {
		try {
			IS_FINISH_FIELD.set(this, value);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public void invokemakeRequestWithRetries() {
		try {
			MAKE_REQUEST_WITH_RETRIES_ENTITY.invoke(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void run() {
		final HttpManager.HttpConfig config = mHttpConfig;
		Log.v(TAG, config.getPrefix());

		if (responseHandler instanceof AutoHttpResponseHandler) {
			AutoHttpResponseHandler handler = ((AutoHttpResponseHandler) responseHandler);
			handler.setRetryRequest(true);
			handler.setHttpConfig(config);
		}
		
		if (isCancelled()) {
			return;
		}
		if (responseHandler != null) {
			responseHandler.sendStartMessage();
		}
		if (isCancelled()) {
			return;
		}

		boolean isInterrupt = true;
		while (isInterrupt) {
			Log.v(TAG, "run...");
			try {
				invokemakeRequestWithRetries();
			} catch (Exception e) {
				e.printStackTrace();
				if (e.getCause() != null
						&& e.getCause().getCause() != null
						&& e.getCause().getCause().getMessage() != null
						&& e.getCause().getCause().getMessage()
								.contains(AutoHttpResponseHandler.MSG)) {
					if (DEBUG)
						Log.v(TAG, "ready try again...");
					if (isCancelled()) {
						return;
					}
					continue;
				} else {
					if (!isCancelled() && responseHandler != null) {
						responseHandler.sendFailureMessage(0, null, null, e);
					} else {
						Log.e("AsyncHttpRequest",
								"makeRequestWithRetries returned error, but handler is null",
								e);
					}
				}
			}
			isInterrupt = false;
			if (isCancelled()) {
				return;
			}
		}

		if (responseHandler != null) {
			responseHandler.sendFinishMessage();
		}

		setFinished(true);
	}

}

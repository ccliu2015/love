package com.wisedu.scc.love.widget.http;

import android.os.Message;
import android.util.Log;

import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

public class AutoHttpResponseHandler extends TextHttpResponseHandler {
	
	private static final String LOG_TAG = "AutoHttpResponseHandler";
	private final static boolean DEBUG = true;
	
	public final static String MSG = "session is out date!";
	
	private HttpManager.HttpConfig httpConfig;
	
	private boolean isRetry = false;
	
	public void setHttpConfig(HttpManager.HttpConfig config) {
		httpConfig = config;
	}
	
	public void setRetryRequest(boolean retry) {
		isRetry = retry;
	}

	protected void sendMessage(Message msg) {
		if (DEBUG)
			Log.v(LOG_TAG, "sendMessage..." + msg.what);
		if (msg.what == START_MESSAGE) {
			final RequestInterpator interpator = httpConfig
					.getRequestInterpator();
			if (interpator != null) {
				interpator.requestStart();
			}
		} else if (msg.what == SUCCESS_MESSAGE) {
			if (isRetry && httpConfig != null) {
				isRetry = false;
				final RequestInterpator interpator = httpConfig
						.getRequestInterpator();
				if (interpator != null) {
					Object[] response = (Object[]) msg.obj;
					try {
						final String content = new String((byte[]) response[2],
								getCharset());

						final int status = JsonParams.fromJson(content)
								.getStatus();

						if (interpator.intercept(getRequestURI().toString(),
								status) && interpator.loginOffline()) {
							Log.v(LOG_TAG, "throw new HttpRetryException...");
							throw new HttpRetryException(MSG);
						}

					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (DEBUG)
			Log.v(LOG_TAG, "sendMessage goon...");
		super.sendMessage(msg);
	}

    /**
     * Fired when a request fails to complete, override to handle in your own code
     *
     * @param statusCode   the status code of the response
     * @param headers      HTTP response headers
     * @param responseBody the response body, if any
     * @param error        the underlying cause of the failure
     */
    @Override
    final public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
		final HttpManager.HttpConfig httpConfig = this.httpConfig;
		if (DEBUG)
			Log.v(LOG_TAG, "onFailure statusCode = " + statusCode
					+ " responseBody = " + responseBody);
		if (httpConfig != null) {
			ErrorCodeHandler errorCodeHandler = httpConfig.getErrorCodeHandler();
			if(errorCodeHandler != null) {
				final String content = errorCodeHandler.processError(statusCode);
				processResult(headers, content);
			} else {
				processResult(headers, responseBody);
			}
		}
    }

    /**
     * Fired when a request returns successfully, override to handle in your own code
     *
     * @param statusCode   the status code of the response
     * @param headers      HTTP response headers
     * @param responseBody the body of the HTTP response from the server
     */
    @Override
    final public void onSuccess(int statusCode, Header[] headers, String responseBody) {
    	if (DEBUG)
			Log.v(LOG_TAG, "onSuccess statusCode = " + statusCode
					+ " responseBody = " + responseBody);
    	processResult(headers, responseBody);
    } 
    
    private void processResult(Header[] headers, String content) {
		final int state = JsonParams.fromJson(content).getState();
		final int status = JsonParams.fromJson(content).getStatus();
		final String data = JsonParams.fromJson(content).getData();
		final String msg = JsonParams.fromJson(content).getMsg();
		onSuccess(headers, state, status, data, msg);
    }
    
    protected void onSuccess(Header[] headers, int state, int status, String data, String msg) {
    	
    };      

}

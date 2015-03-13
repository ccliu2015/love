package com.wisedu.scc.love.widget.http;

public interface ICallback {

	public void onSuccess(String url, String data, Object... objs);

	public void onFailed(String url, int state, String msg, Object... objs);

}

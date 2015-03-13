package com.wisedu.scc.love.widget.http;

public interface RequestInterpator {
	
	//开始请求
	public void requestStart();

	//根据url和网络请求状态，确定是否拦载，如果返回true，则表示拦截
	public boolean intercept(String url, int status);
	
	//重新登录
	public boolean loginOffline();
	
}

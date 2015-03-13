package com.wisedu.scc.love.widget.http;

public interface ErrorCodeHandler {
	
	/**
	 * 
	 * @param code error status code
	 * @return error desc
	 */
	public String processError(int code);
	
}

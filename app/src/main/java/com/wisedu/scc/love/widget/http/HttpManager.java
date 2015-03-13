/**
 * Copyright (c) 2012-2013.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wisedu.scc.love.widget.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpRequest;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.wisedu.scc.love.utils.ReflectUtil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpManager extends AsyncHttpClient {

	private static final String TAG = "HttpManager";

	private HttpConfig mHttpConfig;

	private static Field CLIENT_HEADER_FIELD;

	private static Method PARAMS_TO_ENTITY;

	private static Method ADD_ENTITY_TO_REQUEST_ENTITY;

	private static HashMap<String, HttpManager> mHttpMap = new HashMap<String, HttpManager>();

	private HttpManager(HttpConfig config) {
		super();
		CLIENT_HEADER_FIELD = ReflectUtil.getField(getClass(),
                "clientHeaderMap");
		CLIENT_HEADER_FIELD.setAccessible(true);
		PARAMS_TO_ENTITY = ReflectUtil.getMethod(getClass(), "paramsToEntity",
                RequestParams.class, ResponseHandlerInterface.class);
		PARAMS_TO_ENTITY.setAccessible(true);
		ADD_ENTITY_TO_REQUEST_ENTITY = ReflectUtil.getMethod(getClass(),
                "addEntityToRequestBase", HttpEntityEnclosingRequestBase.class,
                HttpEntity.class);
		ADD_ENTITY_TO_REQUEST_ENTITY.setAccessible(true);
		this.mHttpConfig = config;
		final Map<String, String> headMap = config.getHeadMap();
		if (headMap != null) {
			getHeaders().putAll(headMap);
		}
	}

	public static HttpManager create() {
		Log.i(TAG, "HttpManager create.");
		HttpConfig config = new HttpConfig();
		config.setPrefix(HttpConfig.DEFAULT);
		return create(config);
	}

	public static HttpManager create(String prefix) {
		Log.i(TAG, "HttpManager create prefix = " + prefix);
		HttpConfig config = new HttpConfig();
		config.setPrefix(prefix);
		return create(config);
	}

	public static HttpManager create(HttpConfig config) {
		return config != null ? getInstance(config) : create();
	}

	private synchronized static HttpManager getInstance(HttpConfig config) {
		HttpManager http = null;
		synchronized (mHttpMap) {
			Log.i(TAG, "HttpManager getInstance prefix = " + config.getPrefix());
			http = mHttpMap.get(config.getPrefix());
			if (http == null) {
				http = new HttpManager(config);
				mHttpMap.put(config.getPrefix(), http);
			}
		}
		return http;
	}

	public static void shutdown() {
		synchronized (mHttpMap) {
			mHttpMap.clear();
		}
	}

	public HttpConfig getHttpConfig() {
		return mHttpConfig;
	}

	public String generateUrl(String url) {
		return this.mHttpConfig.getUrl(url);
	}

	public Object getSync(String url) {
		return getSync(url, null);
	}

	public Object getSync(String url, RequestParams params) {
		HttpUriRequest request = new HttpGet(getUrlWithQueryString(
				isUrlEncodingEnabled(), generateUrl(url), params));
		return sendSyncRequest(getHttpClient(), getHttpContext(), request, null);
	}

	public Object getSync(String url, Header[] headers, RequestParams params) {
		HttpUriRequest request = new HttpGet(getUrlWithQueryString(
				isUrlEncodingEnabled(), generateUrl(url), params));
		if (headers != null)
			request.setHeaders(headers);
		return sendSyncRequest(getHttpClient(), getHttpContext(), request, null);
	}

	public String postSync(String url) {
		return postSync(url, null);
	}

	public String postSync(String url, RequestParams params) {
		return postSync(url, invokeParamsToEntity(params, new DefaultHttpResponseHandler()), null);
	}

	public String postSync(String url, HttpEntity entity, String contentType) {
		return sendSyncRequest(getHttpClient(), getHttpContext(),
				invokeAddEntityToRequestBase(new HttpPost(generateUrl(url)), entity),
				contentType);
	}

	public String postSync(String url, Header[] headers, RequestParams params,
			String contentType) {
		HttpEntityEnclosingRequestBase request = new HttpPost(generateUrl(url));
		if (params != null)
			request.setEntity(invokeParamsToEntity(params, new DefaultHttpResponseHandler()));
		if (headers != null)
			request.setHeaders(headers);
		return sendSyncRequest(getHttpClient(), getHttpContext(), request,
				contentType);
	}

	protected String sendSyncRequest(HttpClient client,
			HttpContext httpContext, HttpUriRequest uriRequest,
			String contentType) {
		if (contentType != null) {
			uriRequest.addHeader("Content-Type", contentType);
		}
		return new SyncRequestHandler((DefaultHttpClient) client, httpContext,
				AsyncHttpResponseHandler.DEFAULT_CHARSET)
				.sendRequest(uriRequest);
	}

	// thrift post json interface
	public void postScc(String url, ResponseHandlerInterface responseHandler,
			Map<String, Object> pararms) {
		RequestParams params = new RequestParams();
		if (pararms != null) {
			Iterator<Map.Entry<String, Object>> iter = pararms.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iter
						.next();
				final String key = entry.getKey();
				final Object val = entry.getValue();
				if (val instanceof String || val instanceof Integer
						|| val instanceof Long || val instanceof Short) {
					params.put(key, String.valueOf(val));
				} else {
					params.put(key, GsonProvider.getInstance().toJson(val));
				}
			}
		}
		post(url, params, responseHandler);
	}

	// another thrift post json interface
	public void postScc(String url, ResponseHandlerInterface responseHandler,
			Object... objects) {
		RequestParams params = new RequestParams();
		if (objects != null && objects.length != 0) {
			StringBuilder sb = new StringBuilder();
			for (Object object : objects) {
				if (object instanceof String || object instanceof Integer
						|| object instanceof Long || object instanceof Short) {
					sb.append(object);
				} else {
					sb.append(GsonProvider.getInstance().toJson(object));
				}
				sb.append(",");
			}
			params.put("_p", sb.toString());
		}
		post(url, params, responseHandler);
	}

	public String postServiceSync(String url, Map<String, Object> pararms) {
		RequestParams params = new RequestParams();
		if (pararms != null && pararms.entrySet() != null) {
			Iterator<Map.Entry<String, Object>> iter = pararms.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iter
						.next();
				final String key = entry.getKey();
				final Object val = entry.getValue();
				if (val instanceof String || val instanceof Integer
						|| val instanceof Long || val instanceof Short) {
					params.put(key, String.valueOf(val));
				} else {
					params.put(key, new Gson().toJson(val));
				}
			}
		}
		return postSync(url, params);
	}

	public String postServiceSync(String url, Object... objects) {
		RequestParams params = new RequestParams();
		if (objects != null && objects.length != 0) {
			StringBuilder sb = new StringBuilder();
			for (Object object : objects) {
				if (object instanceof String || object instanceof Integer
						|| object instanceof Long || object instanceof Short) {
					sb.append(object);
				} else {
					sb.append(GsonProvider.getInstance().toJson(object));
				}
				sb.append(",");
			}
			params.put("_p", sb.toString());
		}
		return postSync(url, params);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getHeaders() {
		try {
			return (Map<String, String>) CLIENT_HEADER_FIELD.get(this);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public HttpEntity invokeParamsToEntity(RequestParams params,
			ResponseHandlerInterface responseHandler) {
		try {
			return (HttpEntity) PARAMS_TO_ENTITY.invoke(this, params,
					responseHandler);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public HttpEntityEnclosingRequestBase invokeAddEntityToRequestBase(
			HttpEntityEnclosingRequestBase requestBase, HttpEntity entity) {
		try {
			return (HttpEntityEnclosingRequestBase) ADD_ENTITY_TO_REQUEST_ENTITY
					.invoke(this, requestBase, entity);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * check the header
	 * 
	 * @param header
	 *            the name of the header
	 * @return make sure to has the header
	 */
	public boolean hasHeader(String header) {
		return getHeaders().containsKey(header);
	}

	/**
	 * get the header
	 * 
	 * @param header
	 *            the name of the header
	 * @return the contents of the header
	 */
	public String getHeader(String header) {
		return getHeaders().get(header);
	}
	
	/**
	 * put some headers
	 * 
	 * @param headers
	 */
	public void addHeadMap(HashMap<String, String> headers) {
		getHeaders().putAll(headers);
	}

	@Override
	public RequestHandle head(Context context, String url,
			RequestParams params, ResponseHandlerInterface responseHandler) {
		return super.head(context, generateUrl(url), params, responseHandler);
	}

	@Override
	public RequestHandle head(Context context, String url, Header[] headers,
			RequestParams params, ResponseHandlerInterface responseHandler) {
		return super.head(context, generateUrl(url), headers, params,
				responseHandler);
	}

	@Override
	public RequestHandle get(Context context, String url, RequestParams params,
			ResponseHandlerInterface responseHandler) {
		return super.get(context, generateUrl(url), params, responseHandler);
	}

	@Override
	public RequestHandle get(Context context, String url, Header[] headers,
			RequestParams params, ResponseHandlerInterface responseHandler) {
		return super.get(context, generateUrl(url), headers, params,
				responseHandler);
	}

	@Override
	public RequestHandle post(Context context, String url, HttpEntity entity,
			String contentType, ResponseHandlerInterface responseHandler) {
		return super.post(context, generateUrl(url), entity, contentType,
				responseHandler);
	}

	@Override
	public RequestHandle post(Context context, String url, Header[] headers,
			RequestParams params, String contentType,
			ResponseHandlerInterface responseHandler) {
		return super.post(context, generateUrl(url), headers, params,
				contentType, responseHandler);
	}

	@Override
	public RequestHandle post(Context context, String url, Header[] headers,
			HttpEntity entity, String contentType,
			ResponseHandlerInterface responseHandler) {
		return super.post(context, generateUrl(url), headers, entity,
				contentType, responseHandler);
	}

	@Override
	public RequestHandle put(Context context, String url, HttpEntity entity,
			String contentType, ResponseHandlerInterface responseHandler) {
		return super.put(context, generateUrl(url), entity, contentType,
				responseHandler);
	}

	@Override
	public RequestHandle put(Context context, String url, Header[] headers,
			HttpEntity entity, String contentType,
			ResponseHandlerInterface responseHandler) {
		return super.put(context, generateUrl(url), headers, entity,
				contentType, responseHandler);
	}

	@Override
	public RequestHandle delete(Context context, String url,
			ResponseHandlerInterface responseHandler) {
		return super.delete(context, generateUrl(url), responseHandler);
	}

	@Override
	public RequestHandle delete(Context context, String url, Header[] headers,
			ResponseHandlerInterface responseHandler) {
		return super
				.delete(context, generateUrl(url), headers, responseHandler);
	}

	@Override
	public RequestHandle delete(Context context, String url, Header[] headers,
			RequestParams params, ResponseHandlerInterface responseHandler) {
		return super.delete(context, generateUrl(url), headers, params,
				responseHandler);
	}

	/**
	 * Instantiate a new asynchronous HTTP request for the passed parameters.
	 *
	 * @param client
	 *            HttpClient to be used for request, can differ in single
	 *            requests
	 * @param contentType
	 *            MIME body type, for POST and PUT requests, may be null
	 * @param context
	 *            Context of Android application, to hold the reference of
	 *            request
	 * @param httpContext
	 *            HttpContext in which the request will be executed
	 * @param responseHandler
	 *            ResponseHandler or its subclass to put the response into
	 * @param uriRequest
	 *            instance of HttpUriRequest, which means it must be of
	 *            HttpDelete, HttpPost, HttpGet, HttpPut, etc.
	 * @return AsyncHttpRequest ready to be dispatched
	 */
	@Override
	protected AsyncHttpRequest newAsyncHttpRequest(DefaultHttpClient client,
			HttpContext httpContext, HttpUriRequest uriRequest,
			String contentType, ResponseHandlerInterface responseHandler,
			Context context) {
		return new AutoHttpRequest(client, httpContext, uriRequest,
				responseHandler, getHttpConfig());
	}

	/**
	 * make a config for AsyncHttpClient
	 * 
	 * @author DONG Shengdong
	 * 
	 */
	public static class HttpConfig {

		public static final String DEFAULT = "DEFAULT";
		/**
		 * can be url prefix
		 */
		private String prefix;

		private Map<String, String> headMap;
		private RequestInterpator requestInterpator;
		private ErrorCodeHandler errorCodeHandler;

		public String getPrefix() {
			return prefix;
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}

		public Map<String, String> getHeadMap() {
			return headMap;
		}

		public void setHeadMap(Map<String, String> headMap) {
			this.headMap = headMap;
		}

		public RequestInterpator getRequestInterpator() {
			return requestInterpator;
		}

		public void setRequestInterpator(RequestInterpator requestInterpator) {
			this.requestInterpator = requestInterpator;
		}

		public ErrorCodeHandler getErrorCodeHandler() {
			return errorCodeHandler;
		}

		public void setErrorCodeHandler(ErrorCodeHandler errorCodeHandler) {
			this.errorCodeHandler = errorCodeHandler;
		}

		public String getUrl(String url) {
			Log.v(TAG, "getUrl url = " + url);
			if (TextUtils.isEmpty(prefix) || DEFAULT.equals(url)) {
				return url;
			}
			if (url.startsWith("http")) {
				return url;
			} else {
				return prefix + url;
			}
		}

	}
}

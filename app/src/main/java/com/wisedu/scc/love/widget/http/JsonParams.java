package com.wisedu.scc.love.widget.http;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonParams {

	private static final String TAG = "Json";

	private static JsonParams jsonParams;

	private static String mJson;

	private int status;

	private int state;

	private String msg;

	private String data;

	private JsonParams(String json) {
		Log.i(TAG, "from json = " + json);
		try {
			this.status = -1;
			this.state = -1;
			this.msg = "";
			this.data = "";
			JsonElement jsone = new JsonParser().parse(json);
			if (jsone == null || jsone.isJsonNull()) {
				Log.e(TAG, "please check the json data format.");
			} else {
				JsonObject jsonObj = jsone.getAsJsonObject();
				if (jsonObj.has("json")) {
					jsonObj = jsonObj.getAsJsonObject("json");
				}

				JsonElement te;
				if (jsonObj.has("code")) {
					if (jsonObj.has("ret")) {
						te = jsonObj.get("ret");
						if (te.isJsonNull()) {
							this.data = "";
						} else if (te.isJsonPrimitive()) {
							this.data = te.getAsString();
						} else if (te.isJsonArray()) {
							this.data = te.getAsJsonArray().toString();
						} else if (te.isJsonObject()) {
							this.data = te.getAsJsonObject().toString();
						} else {
							this.data = te.getAsString();
						}
					}
					if (jsonObj.has("msg")) {
						this.msg = jsonObj.get("msg").getAsString();
					}
					if (jsonObj.has("code")) {
						this.state = jsonObj.get("code").getAsInt();
					}
					if (202 == state || 216 == state || 205 == state
							|| 203 == state || 49 == state) {
						this.status = 0;
					} else {
						this.status = 1;
					}
				} else {
					if (jsonObj.has("data")) {
						te = jsonObj.get("data");
						if (te.isJsonNull()) {
							this.data = "";
						} else if (te.isJsonPrimitive()) {
							this.data = te.getAsString();
						} else if (te.isJsonArray()) {
							this.data = te.getAsJsonArray().toString();
						} else if (te.isJsonObject()) {
							this.data = te.getAsJsonObject().toString();
						} else {
							this.data = te.getAsString();
						}
					}
					if (jsonObj.has("msg")) {
						this.msg = jsonObj.get("msg").getAsString();
					}
					if (jsonObj.has("state")) {
						this.state = jsonObj.get("state").getAsInt();
					}
					if (jsonObj.has("status")) {
						this.status = jsonObj.get("status").getAsInt();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
	}

	public static synchronized JsonParams fromJson(String json) {
		if (jsonParams == null || mJson == null || !mJson.equals(json)) {
			jsonParams = new JsonParams(json);
		}
		mJson = json;
		return jsonParams;
	}

	public int getState() {
		return state;
	}

	public int getStatus() {
		return status;
	}

	public String getData() {
		return data;
	}

	public String getMsg() {
		return msg;
	}

}

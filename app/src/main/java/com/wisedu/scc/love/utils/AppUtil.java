/*

 Copyright (c) 2013 Wisorg Ltd. All Rights Reserved.

 Copying of this document or code and giving it to others and the
 use or communication of the contents thereof, are forbidden without
 expressed authority. Offenders are liable to the payment of damages.
 All rights reserved in the event of the grant of a invention patent
 or the registration of a utility model, design or code.
 */

package com.wisedu.scc.love.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 应用相关工具方法
 */
public final class AppUtil {

    /**
     * 从AndroidManifest.xml收集到的Application的信息
     * @param context
     * @return
     */
	public static Bundle getMeta(Context context) {
		try {
			ApplicationInfo info = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (info.metaData != null) {
				return info.metaData;
			}
		} catch (NameNotFoundException ignored) {
		}
		return Bundle.EMPTY;
	}

    /**
     * 获取应用某属性的字符串值
     * @param context
     * @param key
     * @return
     */
	public static String getMetaString(Context context, String key) {
		return getMeta(context).getString(key);
	}


	public static String getMetaString(Context context, String key, String def) {
		String value = getMeta(context).getString(key);
		return TextUtils.isEmpty(value) ? def : value;
	}

    /**
     * 获取应用某属性的数字值
     * @param context
     * @param key
     * @return
     */
	public static int getMetaInt(Context context, String key) {
		return getMeta(context).getInt(key);
	}

    /**
     * 获取应用某属性的布尔值
     * @param context
     * @param key
     * @return
     */
	public static boolean getMetaBool(Context context, String key) {
		return getMeta(context).getBoolean(key);
	}

    /**
     * 获取strings.xml中的值
     * @param context
     * @param key
     * @return
     */
	public static String getConfigString(Context context, String key) {
		int id = getResId(context, key, "string");
		return id > 0 ? context.getResources().getString(id) : null;
	}

    /**
     * 获取strings.xml中的值，def为替代值
     * @param context
     * @param key
     * @return
     */
	public static String getConfigString(Context context, String key, String def) {
		int id = getResId(context, key, "string");
		return id > 0 ? context.getResources().getString(id, def) : def;
	}

	public static int getConfigInt(Context context, String key) {
		int id = getResId(context, key, "integer");
		return id > 0 ? context.getResources().getInteger(id) : 0;
	}

	public static int getConfigInt(Context context, String key, int def) {
		int id = getResId(context, key, "integer");
		return id > 0 ? context.getResources().getInteger(id) : def;
	}

	public static boolean getConfigBool(Context context, String key) {
		int id = getResId(context, key, "bool");
		return id > 0 && context.getResources().getBoolean(id);
	}

	public static boolean getConfigBool(Context context, String key, boolean def) {
		int id = getResId(context, key, "bool");
		return id > 0 ? context.getResources().getBoolean(id) : def;
	}

	public static String[] getConfigArr(Context context, String key) {
		int id = getResId(context, key, "array");
		return id > 0 ? context.getResources().getStringArray(id)
				: new String[] {};
	}

	public static Map<String, String> getConfigMap(Context context, String key) {
		final Map<String, String> map = new HashMap<String, String>();
		for (String name : getConfigArr(context, key)) {
			int index = name.indexOf(':');
			if (index > 0) {
				map.put(name.substring(0, index).trim(),
						name.substring(index + 1).trim());
			}
		}
		return map;
	}

   /**
    * 获取设备ID
    */
	public static String getDeviceId(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId().toLowerCase();
	}

    /**
     * 获取资源ID
     * @param context
     * @param key
     * @param type
     * @return
     */
	private static int getResId(Context context, String key, String type) {
		return context.getResources().getIdentifier("app_" + key, type,
				context.getPackageName());
	}

}
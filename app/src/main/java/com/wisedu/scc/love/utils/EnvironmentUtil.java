package com.wisedu.scc.love.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import java.io.File;

@SuppressLint("NewApi")
public class EnvironmentUtil {

	public static final String APP = "/wisedu";

	public static final String APK = "/apk";

	public static final String IMAGE = "/image";

	public static final String AUDIO = "/audio";

	public static final String LOG = "/log";

	public static final String DOWNLOAD = "/download";

	public static boolean checkSDCard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	public static File getAppDirectory(Context context) {
		String path = getRootPath(context) + APK;
		return mkdir(path);
	}

	public static File getImageDirectory(Context context) {
		String path = getRootPath(context) + IMAGE;
		return mkdir(path);
	}

	public static File getAudioDirectory(Context context) {
		String path = getRootPath(context) + AUDIO;
		return mkdir(path);
	}

	public static File getLogDirectory(Context context) {
		String path = getRootPath(context) + LOG;
		return mkdir(path);
	}

	public static File getDownloadDirectory(Context context) {
		String path = getRootPath(context) + DOWNLOAD;
		return mkdir(path);
	}

	public static String getRootPath(Context context) {
		return getSDPath(context) + APP;
	}

	public static File mkdir(String path) {
		final File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	/**
	 * 判断是否有Sd卡
	 */
	private static String getSDPath(Context context) {
		if (checkSDCard()) {
			return Environment.getExternalStorageDirectory().getPath();// 获取根目录
		} else {
			return context.getFilesDir().getPath();
		}
	}

	public static String getExternalPhotoSavePath(String folderName) {
		File path = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		path = new File(path, folderName);
		if (!path.exists()) {
			path.mkdirs();
		}
		return path.getAbsolutePath();
	}
	
	private static final String SDCARD_FOLDER_FORMAT = Environment
			.getExternalStorageDirectory() + "/Android/data/%s/files/";
	public static String getInternalPhotoSavedPath(String packagename){
		String path = String.format(SDCARD_FOLDER_FORMAT, packagename);
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

}

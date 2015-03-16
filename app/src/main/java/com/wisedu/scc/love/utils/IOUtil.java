package com.wisedu.scc.love.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

/**
 * IO工具
 */
public class IOUtil {

    /***/
	public static void closeSilently(Closeable c) {
		if (c == null)
			return;
		try {
			c.close();
		} catch (Throwable t) {
		}
	}

    /***/
	public static void closeSilently(ParcelFileDescriptor c) {
		if (c == null)
			return;
		try {
			c.close();
		} catch (Throwable t) {
		}
	}

    /***/
	public static void closeSilently(Cursor cursor) {
		try {
			if (cursor != null)
				cursor.close();
		} catch (Throwable t) {
		}
	}

    /***/
	public static void copyFile(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int read = 0;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

    /***/
	public static void copyFile(File input, File output) throws IOException {
		if (input.exists()) {
			InputStream inputStream = new FileInputStream(input);
			OutputStream outputStream = new FileOutputStream(output);
			copyFile(inputStream, outputStream);
		}
	}

    /***/
	public static void copyFile(String input, String output) throws IOException {
		File finput = new File(input);
		File foutput = new File(output);
		copyFile(finput, foutput);
	}

    /***/
	public static String getNewFileName(String path) {
		File oldPath = new File(path);
		String ext = ".tmp";
		if (path.indexOf(".") > -1)
			ext = path.substring(path.lastIndexOf("."));
		File directory = new File(oldPath.getParent());

		int x = 0;
		String fileName = oldPath.getName();
		fileName = fileName.substring(0, fileName.lastIndexOf("."));
		while (true) {
			++x;
			String candidate = directory.toString() + "/" + fileName + "-" + x
					+ ext;
			boolean exists = new File(candidate).exists();
			if (!exists)
				break;
		}
		return fileName + "-" + x + "." + ext;
	}

    /***/
	public static int readSystemFileAsInt(String pSystemFile) throws Exception {
		InputStream in = null;
		try {
			Process process = new ProcessBuilder(new String[] {
					"/system/bin/cat", pSystemFile }).start();

			in = process.getInputStream();
			String content = readFully(in);
			return Integer.parseInt(content);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

    /***/
	public static final String readFully(InputStream pInputStream)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		Scanner sc = new Scanner(pInputStream);
		while (sc.hasNextLine()) {
			sb.append(sc.nextLine());
		}
		return sb.toString();
	}

    /***/
	public static void saveFile(File file, String content) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(
				content.getBytes("UTF8"));
		FileOutputStream out = new FileOutputStream(file);
		copyFile(in, out);
	}

    /**
     * 获取文件真实路径
     * */
	public static String getRealFilePath(Context context, Uri uri) {
		if (null == uri)
			return null;

		String scheme = uri.getScheme();
		String data = null;

		if (scheme == null) {
			data = uri.getPath();
		} else if ("file".equals(scheme)) {
			data = uri.getPath();
		} else if ("content".equals(scheme)) {
			Cursor cursor = context.getContentResolver().query(uri,
					new String[] { "_data" }, null, null, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					int index = cursor.getColumnIndex("_data");
					if (index > -1) {
						data = cursor.getString(index);
					}
				}
				cursor.close();
			}
		}
		return data;
	}

    /***/
	public static ByteArrayInputStream fromStream(InputStream stream)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		copyFile(stream, baos);
		baos.flush();
		return new ByteArrayInputStream(baos.toByteArray());
	}

}
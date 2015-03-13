package com.wisedu.scc.love.utils;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.wisedu.scc.love.utils.file.IOUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DecodeUtils {

	public static final int MAX_WIDTH_SIZE = 800;

	public static final int MAX_HEIGHT_SIZE = 1280;
	
	public static int getWidth(Object object) {
		try {
			int width = ((Integer) reflect(object, "getWidth", new Object[0]))
					.intValue();
			return width;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static int getHeight(Object object) {
		try {
			int height = ((Integer) reflect(object, "getHeight", new Object[0]))
					.intValue();
			return height;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static Bitmap decodeRegion(Object paramObject, Rect paramRect,
			BitmapFactory.Options paramOptions) {
		return (Bitmap) reflect(paramObject, "decodeRegion", new Class[] {
				Rect.class, BitmapFactory.Options.class }, new Object[] {
				paramRect, paramOptions });
	}
	
	public static Object reflect(Object object, String method,
			Object[] paramValue) {
		Class<?>[] paramType = new Class<?>[paramValue.length];
		int i = 0;
		int j = paramValue.length;
		while (i < j) {
			paramType[i] = paramValue[i].getClass();
			++i;
		}
		return reflect(object, method, paramType, paramValue);
	}
	
	public static Object reflect(Object object, String method,
			Class<?>[] paramType, Object[] paramValue) {
		try {
			Object ret = object.getClass().getMethod(method, paramType)
					.invoke(object, paramValue);
			return ret;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap decode(Context context, Uri uri, int maxW, int maxH) {
		InputStream stream = openInputStream(context, uri);
		if (null == stream) {
			return null;
		}

		maxW = maxW > MAX_WIDTH_SIZE ? MAX_WIDTH_SIZE : maxW;
		maxH = maxH > MAX_HEIGHT_SIZE ? MAX_HEIGHT_SIZE : maxH;

		Bitmap bitmap = null;
		int[] imageSize = new int[2];

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		boolean decoded = decodeImageBounds(stream, imageSize, options);
		int orientation = defineExifOrientation(uri, options.outMimeType);
		IOUtil.closeSilently(stream);

		Log.v("dsd", "decode orientation:" + orientation + " decoded:"
				+ decoded);

		if (decoded) {
			int sampleSize = computeSampleSize(imageSize[0], imageSize[1],
					(int) (maxW * 1.2D), (int) (maxH * 1.2D), orientation);
			options = getDefaultOptions();

			float maxSampleW = maxW * 1.5F;
			float maxSampleH = maxH * 1.5F;

			if ((imageSize[1] < maxSampleW + 100.0F)
					&& (imageSize[1] < maxSampleH)) {
				sampleSize = 1;
			}

			options.inSampleSize = sampleSize;

			bitmap = decodeBitmap(context, uri, options, maxW, maxH,
					orientation, 0);
		}

		return bitmap;
	}

	static Bitmap decodeBitmap(Context context, Uri uri,
			BitmapFactory.Options options, int maxW, int maxH, int orientation,
			int pass) {
		Bitmap bitmap = null;
		Bitmap newBitmap = null;

		if (pass > 10) {
			return null;
		}

		InputStream stream = openInputStream(context, uri);
		if (null == stream)
			return null;

		try {
			bitmap = BitmapFactory.decodeStream(stream, null, options);
			IOUtil.closeSilently(stream);

			if (bitmap != null) {
				newBitmap = BitmapUtil.resizeBitmap(bitmap, maxW, maxH,
                        orientation);
				if (bitmap != newBitmap) {
					bitmap.recycle();
				}
				bitmap = newBitmap;
			}
		} catch (OutOfMemoryError error) {
			error.printStackTrace();
			IOUtil.closeSilently(stream);
			if (null != bitmap) {
				bitmap.recycle();
				bitmap = null;
			}
			options.inSampleSize += 1;
			bitmap = decodeBitmap(context, uri, options, maxW, maxH,
					orientation, pass + 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public static Bitmap resizeBitmap(Bitmap input, int maxW, int maxH) {
		Bitmap newBitmap = null;
		Bitmap bitmap = input;
		if (input != null) {
			try {
				newBitmap = BitmapUtil.resizeBitmap(input, maxW, maxH, 0);
				if (bitmap != newBitmap) {
					bitmap.recycle();
				}
				bitmap = newBitmap;
			} catch (OutOfMemoryError error) {
				if (null != bitmap) {
					bitmap.recycle();
					bitmap = null;
				}
				bitmap = resizeBitmap(bitmap, maxW - 200, maxH - 200);
			}
		}
		return bitmap;
	}
	
	public static String convertUri(Context context, String strUri) {
		return convertUri(context, Uri.parse(strUri));
	}
	
	public static String convertUri(Context context, Uri uri) {
		String scheme = uri.getScheme();
		InputStream stream = null;
		if ((scheme == null) || ("file".equals(scheme))) {
			return uri.getPath();
		} else {
			String[] projection = { MediaStore.Images.Media.DATA };
			CursorLoader loader = new CursorLoader(context, uri, projection,
					null, null, null);
			Cursor cursor = loader.loadInBackground();
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
	}

	public static InputStream openInputStream(Context context, Uri uri) {
		if (null == uri)
			return null;
		String scheme = uri.getScheme();
		InputStream stream = null;
		if ((scheme == null) || ("file".equals(scheme))) {
			stream = openFileInputStream(uri.getPath());
		} else if ("content".equals(scheme)) {
			stream = openContentInputStream(context, uri);
		} else if (("http".equals(scheme)) || ("https".equals(scheme))) {
			stream = openRemoteInputStream(uri);
		}
		return stream;
	}

	public static boolean decodeImageBounds(InputStream stream, int[] outSize,
			BitmapFactory.Options options) {
		BitmapFactory.decodeStream(stream, null, options);
		if ((options.outHeight > 0) && (options.outWidth > 0)) {
			outSize[0] = options.outWidth;
			outSize[1] = options.outHeight;
			return true;
		}
		return false;
	}

	private static int computeSampleSize(int bitmapW, int bitmapH, int maxW,
			int maxH, int orientation) {
		double h = 0.0;
		double w = 0.0;
		if ((orientation == 0) || (orientation == 180)) {
			w = bitmapW;
			h = bitmapH;
		} else {
			w = bitmapH;
			h = bitmapW;
		}

		Log.v("dsd", "computeSampleSize w:" + w);
		Log.v("dsd", "computeSampleSize h:" + h);
		Log.v("dsd", "computeSampleSize bitmapW:" + bitmapW);
		Log.v("dsd", "computeSampleSize bitmapH:" + bitmapH);
		Log.v("dsd", "computeSampleSize w / maxW:" + w / maxW);
		Log.v("dsd", "computeSampleSize h / maxH:" + h / maxH);
		int sampleSize = (int) Math.ceil(Math.max(w / maxW, h / maxH));
		Log.v("dsd", "computeSampleSize sampleSize:" + sampleSize);
		return sampleSize;
	}

	static InputStream openFileInputStream(String path) {
		try {
			return new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	static InputStream openContentInputStream(Context context, Uri uri) {
		try {
			return context.getContentResolver().openInputStream(uri);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	static InputStream openRemoteInputStream(Uri uri) {
		URL finalUrl;
		try {
			finalUrl = new URL(uri.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) finalUrl.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		connection.setInstanceFollowRedirects(false);
		int code;
		try {
			code = connection.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		if ((code == 301) || (code == 302) || (code == 303)) {
			String newLocation = connection.getHeaderField("Location");
			return openRemoteInputStream(Uri.parse(newLocation));
		}
		try {
			return (InputStream) finalUrl.getContent();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	static ByteArrayInputStream dup(InputStream stream) throws IOException {
		ByteArrayOutputStream remote_stream = new ByteArrayOutputStream();
		IOUtil.copyFile(stream, remote_stream);
		ByteArrayInputStream in_stream = new ByteArrayInputStream(
				remote_stream.toByteArray());
		IOUtil.closeSilently(remote_stream);
		return in_stream;
	}

	public static BitmapFactory.Options getDefaultOptions() {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		options.inDither = false;
		options.inJustDecodeBounds = false;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inTempStorage = new byte[16384];
		return options;
	}

	public static int getSampleSize(double width, double screenWidth) {
		return (int) Math.ceil(width / screenWidth);
	}

	public static int defineExifOrientation(Uri imageUri, String mimeType) {
		int rotation = 0;
		if ("image/jpeg".equalsIgnoreCase(mimeType)
				&& "file".equals(imageUri.getScheme())) {
			try {
				ExifInterface exif = new ExifInterface(imageUri.getPath());
				int exifOrientation = exif.getAttributeInt(
						ExifInterface.TAG_ORIENTATION,
						ExifInterface.ORIENTATION_NORMAL);
				Log.e("dsd", "exifOrientation:" + exifOrientation);
				switch (exifOrientation) {
				case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
				case ExifInterface.ORIENTATION_NORMAL:
					rotation = 0;
					break;
				case ExifInterface.ORIENTATION_TRANSVERSE:
				case ExifInterface.ORIENTATION_ROTATE_90:
					rotation = 90;
					break;
				case ExifInterface.ORIENTATION_FLIP_VERTICAL:
				case ExifInterface.ORIENTATION_ROTATE_180:
					rotation = 180;
					break;
				case ExifInterface.ORIENTATION_TRANSPOSE:
				case ExifInterface.ORIENTATION_ROTATE_270:
					rotation = 270;
					break;
				}
			} catch (IOException e) {
				Log.e("dsd", "Can't read EXIF tags from file [%s]" + imageUri);
			}
		}
		return rotation;
	}

}
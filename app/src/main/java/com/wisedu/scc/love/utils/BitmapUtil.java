package com.wisedu.scc.love.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.wisedu.scc.love.utils.file.IOUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BitmapUtil {
	public static final int COMPRESS_JPEG_QUALITY = 70;

	public final static int MAX_WIDTH = 800;
	public final static int MAX_HEIGHT = 1024;

	public static Bitmap resizeBitmap(Bitmap input, int destWidth,
			int destHeight) throws OutOfMemoryError {
		return resizeBitmap(input, destWidth, destHeight, 0);
	}

	public static void configOptions(Options options,
			Bitmap.Config config) {
		options.inPreferredConfig = config;
	}

	public static void sampleOptions(Options options,
			Object object) {
		int width = DecodeUtils.getWidth(object);
		int sample = BitmapUtil.getSampleSize(width, MAX_WIDTH);
		Log.v("BitmapUtils", "sample:" + sample);
		options.inSampleSize = sample;
	}

	public static int getSampleSize(double width, double screenWidth) {
		return (int) Math.ceil(width / screenWidth);
	}

	public static Bitmap[] decodeBitmaps(Object object, Options options) {
		int width = DecodeUtils.getWidth(object);
		int height = DecodeUtils.getHeight(object);

		final Bitmap[] bitmaps;
		if (height % 1024 == 0) {
			bitmaps = new Bitmap[height / MAX_HEIGHT];
		} else {
			bitmaps = new Bitmap[height / MAX_HEIGHT + 1];
		}

		int index = 0;
		while (true) {
			Bitmap localBitmap = null;
			if (index < bitmaps.length - 1) {
				localBitmap = DecodeUtils.decodeRegion(object, new Rect(0,
						index * MAX_HEIGHT, width, MAX_HEIGHT * (index + 1)),
						options);
				bitmaps[index] = localBitmap;
			} else if (index == bitmaps.length - 1) {
				localBitmap = DecodeUtils.decodeRegion(object, new Rect(0,
						index * MAX_HEIGHT, width, height), options);
				bitmaps[index] = localBitmap;
				break;
			} else {
				break;
			}
			++index;
		}

		return bitmaps;
	}

	static Bitmap decodeBitmap(Context context, Uri uri,
			Options options, int maxW, int maxH, int orientation,
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
			IOUtil.closeSilently(stream);
			if (null != bitmap) {
				bitmap.recycle();
				bitmap = null;
			}
			options.inSampleSize += 1;
			bitmap = decodeBitmap(context, uri, options, maxW, maxH,
					orientation, pass + 1);
		}
		return bitmap;
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

	static InputStream openFileInputStream(String path) {
		try {
			return new FileInputStream(path);
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

	static InputStream openContentInputStream(Context context, Uri uri) {
		try {
			return context.getContentResolver().openInputStream(uri);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap resizeBitmap(Bitmap input, int destWidth,
			int destHeight, int rotation) throws OutOfMemoryError {
		int dstWidth = destWidth;
		int dstHeight = destHeight;
		int srcWidth = input.getWidth();
		int srcHeight = input.getHeight();

		if ((rotation == 90) || (rotation == 270)) {
			dstWidth = destHeight;
			dstHeight = destWidth;
		}

		boolean needsResize = false;

		if ((srcWidth > dstWidth) || (srcHeight > dstHeight)) {
			needsResize = true;

			float ratio1 = (float) srcWidth / dstWidth;
			float ratio2 = (float) srcHeight / dstHeight;
			Log.v("dsd", "ratio1:" + ratio1 + " ratio2:" + ratio2);

			if (ratio1 > ratio2) {
				float p = (float) dstWidth / srcWidth;
				dstHeight = (int) (srcHeight * p);
			} else {
				float p = (float) dstHeight / srcHeight;
				dstWidth = (int) (srcWidth * p);
			}
		} else {
			dstWidth = srcWidth;
			dstHeight = srcHeight;
		}

		Log.v("dsd", "dstWidth:" + dstWidth + " dstHeight:" + dstHeight
				+ " srcWidth:" + srcWidth + " srcHeight:" + srcHeight);
		if ((needsResize) || (rotation != 0)) {
			Bitmap output = null;
			if (rotation == 0) {
				output = Bitmap.createScaledBitmap(input, dstWidth, dstHeight,
						true);
			} else {
				Matrix matrix = new Matrix();
				matrix.postScale((float) dstWidth / srcWidth, (float) dstHeight
						/ srcHeight);
				matrix.postRotate(rotation);
				output = Bitmap.createBitmap(input, 0, 0, srcWidth, srcHeight,
						matrix, true);
			}
			return output;
		}
		return input;
	}

	public static String generalFileName() {
		return String.valueOf(System.currentTimeMillis()) + ".jpg";
	}

	public static File getImageFile(Context context) {
		File dir = EnvironmentUtil.getImageDirectory(context);
		File out = new File(dir, generalFileName());
		return out;
	}

	public static Uri getImageFileUri(Context context) {
		File out = getImageFile(context);
		Uri uri = Uri.fromFile(out);
		return uri;
	}

	public static Uri getImageCropUri(Context context) {
		File file = EnvironmentUtil.getImageDirectory(context);
		File dir = new File(file.getAbsolutePath() + "/" + "crop");
		File out = new File(dir, generalFileName());
		return Uri.fromFile(out);

	}

	public static File saveBitmap(Context context, Bitmap bitmap) {
		File file = getImageFile(context);
		saveBitmap(file, bitmap);
		return file;
	}

	/**
	 * 生成圆形图片
	 *
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getCircularBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		Paint paint = new Paint();

		final int diameter = Math.min(bitmap.getWidth(), bitmap.getHeight());

		Rect rect = new Rect(0, 0, diameter, diameter);
		RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.WHITE);
		canvas.drawOval(rectF, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static Bitmap getCircularBitmap(Bitmap bitmap, int targetWidth, int targetHeight) {
	    Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
	            targetHeight,Bitmap.Config.ARGB_8888);

	    Canvas canvas = new Canvas(targetBitmap);
	    Path path = new Path();
	    path.addCircle(((float) targetWidth - 1) / 2,
	            ((float) targetHeight - 1) / 2,
	            (Math.min(((float) targetWidth),
	                    ((float) targetHeight)) / 2),
	                    Path.Direction.CCW);

	    canvas.clipPath(path);
	    Bitmap sourceBitmap = bitmap;
	    canvas.drawBitmap(sourceBitmap,
	            new Rect(0, 0, sourceBitmap.getWidth(),
	                    sourceBitmap.getHeight()),
	                    new Rect(0, 0, targetWidth, targetHeight), new Paint(Paint.FILTER_BITMAP_FLAG));
		return targetBitmap;
	}



	/**
	 * 保存图片
	 *
	 * @param path
	 * @param bitmap
	 */
	public static File saveBitmap(String path, Bitmap bitmap) {
		File file = new File(path);
		saveBitmap(file, bitmap);
		return file;
	}

	public static File savePngBitmap(String path, Bitmap bitmap) {
		File file = new File(path);
		FileOutputStream fos = null;
		try {
			if (file.exists())
				file.delete();
			fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
			fos.flush();
			fos.close();
			fos = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * 保存图片
	 *
	 * @param file
	 * @param bitmap
	 */
	public static void saveBitmap(File file, Bitmap bitmap) {
		FileOutputStream fos = null;
		try {
			if (file.exists())
				file.delete();
			fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_JPEG_QUALITY,
					fos);
			fos.flush();
			fos.close();
			fos = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载图片
	 *
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static byte[] getImage(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		InputStream inStream = conn.getInputStream();
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return readStream(inStream);
		}
		return null;
	}

	/**
	 * 下载图片
	 * @return
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	public static boolean isSuperLongBitmap(Context context, Uri uri){
		InputStream inputStream = DecodeUtils.openInputStream(context, uri);
		int[] imageSize = new int[2];

		Options options = new Options();
		options.inJustDecodeBounds = true;
		if (DecodeUtils.decodeImageBounds(inputStream, imageSize, options)) {
			int width = imageSize[0];
			int height = imageSize[1];
			float scale = height/width;
			return scale > 3;
		}
		return false;
	}
	
	public static boolean isGifBitmap(String uri){
		String extension = MimeTypeMap.getFileExtensionFromUrl(uri);
		return "gif".equalsIgnoreCase(extension);
	}
}
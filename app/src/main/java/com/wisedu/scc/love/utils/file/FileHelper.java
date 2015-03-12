package com.wisedu.scc.love.utils.file;

import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件辅助类
 */
public class FileHelper {

	private static final int BUFFER = 0x1000; // 缓冲大小，4K
	private static final String CATALOG_DIR = "love"; // 目录路径
	private static final String TMP_DIR = "tmp"; // 缓存路径
    private static final String IMG_DIR = "img"; // 图片路径

    /**
     * 保存文件至目录
     * @param filename
     * @param text
     * @param lines
     * @return
     */
    public static File saveCatelogFile(String filename,
                                         CharSequence text, List<CharSequence> lines) {
        PrintStream out = null;
        try {
            File tempFile = new File(getCatalogDirectory(), filename);
            out = new PrintStream(new BufferedOutputStream(
                    new FileOutputStream(tempFile, false), BUFFER));
            if (text != null) { // 大字符串
                out.print(text);
            } else { // 多行
                for (CharSequence line : lines) {
                    out.println(line);
                }
            }
            return tempFile;
        } catch (FileNotFoundException ex) {
            return null;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 保存文件至缓存
     * @param filename
     * @param text
     * @param lines
     * @return
     */
	public static File saveTemporaryFile(String filename,
			CharSequence text, List<CharSequence> lines) {
        PrintStream out = null;
		try {
			File tempFile = new File(getTempDirectory(), filename);
            out = new PrintStream(new BufferedOutputStream(
					new FileOutputStream(tempFile, false), BUFFER));
			if (text != null) { // 大字符串
				out.print(text);
			} else { // 多行
				for (CharSequence line : lines) {
					out.println(line);
				}
			}
			return tempFile;
		} catch (FileNotFoundException ex) {
			return null;
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

    /**
     * 保存文件至图片文件夹
     * @param filename
     * @param text
     * @param lines
     * @return
     */
    public static File saveImgFile(String filename,
                                         CharSequence text, List<CharSequence> lines) {
        PrintStream out = null;
        try {
            File tempFile = new File(getImgDirectory(), filename);
            out = new PrintStream(new BufferedOutputStream(
                    new FileOutputStream(tempFile, false), BUFFER));
            if (text != null) { // 大字符串
                out.print(text);
            } else { // 多行
                for (CharSequence line : lines) {
                    out.println(line);
                }
            }
            return tempFile;
        } catch (FileNotFoundException ex) {
            return null;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 根据文件名称取得文件
     * @param filename
     * @return
     */
    public static File getCatalogFile(String filename) {
        File dir = getCatalogDirectory();
        File file = new File(dir, filename);
        return file;
    }

    /**
     * 根据文件名称取得文件
     * @param filename
     * @return
     */
    public static File getTmpFile(String filename) {
        File dir = getTempDirectory();
        File file = new File(dir, filename);
        return file;
    }

    /**
     * 根据文件名称取得文件
     * @param filename
     * @return
     */
    public static File getImgFile(String filename) {
        File dir = getImgDirectory();
        File file = new File(dir, filename);
        return file;
    }

    /**
     * 检查SD卡是否存在
     * @return
     */
	public static boolean checkIfSdCardExists() {
		File sdcardDir = Environment.getExternalStorageDirectory();
		return sdcardDir != null && sdcardDir.listFiles() != null;
	}

    /**
     * 取得应用文件目录
     * @return
     */
    public static File getCatalogDirectory() {
        File sdcardDir = Environment.getExternalStorageDirectory();
        File catlogDir = new File(sdcardDir, CATALOG_DIR);
        if (!catlogDir.exists()) {
            catlogDir.mkdir();
        }
        return catlogDir;
    }

    /**
     * 取得缓存目录
     * @return
     */
    public static File getTempDirectory() {
        File catlogDir = getCatalogDirectory();
        File tmpDir = new File(catlogDir, TMP_DIR);
        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }
        return tmpDir;
    }

    /**
     * 取得图片目录
     * @return
     */
    public static File getImgDirectory() {
        File catlogDir = getCatalogDirectory();
        File tmpDir = new File(catlogDir, IMG_DIR);
        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }
        return tmpDir;
    }

    /**
     * 获取URL中的文件名称
     * @param url
     * @return
     */
    public static String getUrlFileName(String url) {
        int first = url.lastIndexOf("/") + 1;
        String filename = url.substring(first, url.length()).toLowerCase();
        return filename;
    }

    /**
     * 保存压缩文件
     * @param filename
     * @param files
     * @return
     */
	public static File saveTemporaryZipFile(String filename, List<File> files) {
		try {
			return saveTemporaryZipFileAndThrow(filename, files);
		} catch (IOException e) {
		}
		return null;
	}

    /**
     * 保存压缩文件
     * @param filename
     * @param files
     * @return
     * @throws IOException
     */
	private static File saveTemporaryZipFileAndThrow(String filename,
			List<File> files) throws IOException {
		File zipFile = new File(getTempDirectory(), filename);
		ZipOutputStream output = null;
		try {
			output = new ZipOutputStream(new BufferedOutputStream(
					new FileOutputStream(zipFile), BUFFER));
			for (File file : files) {
				FileInputStream fi = new FileInputStream(file);
				BufferedInputStream input = null;
				try {
					input = new BufferedInputStream(fi, BUFFER);
					ZipEntry entry = new ZipEntry(file.getName());
					output.putNextEntry(entry);
					copy(input, output);
				} finally {
					if (input != null) {
						input.close();
					}
				}
			}
		} finally {
			if (output != null) {
				output.close();
			}
		}
		return zipFile;
	}

	/**
     * 拷贝流
	 */
	private static long copy(InputStream from, OutputStream to)
			throws IOException {
		byte[] buf = new byte[BUFFER];
		long total = 0;
		while (true) {
			int r = from.read(buf);
			if (r == -1) {
				break;
			}
			to.write(buf, 0, r);
			total += r;
		}
		return total;
	}

}
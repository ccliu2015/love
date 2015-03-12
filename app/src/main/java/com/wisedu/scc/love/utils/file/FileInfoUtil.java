/*
 * Copyright (C) 2013
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
package com.wisedu.scc.love.utils.file;

import java.io.File;
import java.text.DecimalFormat;

/**
 * 文件信息辅助工具
 */
public class FileInfoUtil {

    private static String kB_UNIT_NAME = "KB";
    private static String B_UNIT_NAME = "B";
    private static String MB_UNIT_NAME = "MB";

    /**
	 * 返回自定文件或文件夹的大小
	 */
	public static long getFileSizes(File f) throws Exception {
		long s = 0;
		if (f.exists()) {
			s = f.length();
		} else {
			f.createNewFile();
			System.out.println("文件不存在");
		}
		return s;
	}

    /**
     * 取得文件夹大小(递归)
     */
	public static long getFileSize(File f) throws Exception {
		long size = 0;
		File fList[] = f.listFiles();
		for (int i = 0; i < fList.length; i++) {
			if (fList[i].isDirectory()) {
				size = size + getFileSize(fList[i]);
			} else {
				size = size + fList[i].length();
			}
		}
		return size;
	}

    /**
     * 转换文件大小
     * @param fileS
     * @return
     */
	public static String formetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#0.00");
		String fileSizeString;
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

    /**
     * 转换文件大小，以M为单位
     * @param fileS
     * @return
     */
	public static String formetFileMSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#0.00");
		String fileSizeString = df.format((double) fileS / 1048576) + "M";
		return fileSizeString;
	}

    /**
     * 递归求取目录文件个数
     * @param f
     * @return
     */
	public static long getList(File f) {
		long size = 0;
		File fList[] = f.listFiles();
		size = fList.length;
		for (int i = 0; i < fList.length; i++) {
			if (fList[i].isDirectory()) {
				size = size + getList(fList[i]);
				size--;
			}
		}
		return size;
	}

    /**
     * 组装jpg文件名称
     * @return
     */
	public static String geneFileName() {
		return String.valueOf(System.currentTimeMillis()) + ".jpg";
	}

    /**
     * 取得文件大小
     * @param size
     * @return
     */
    public static String getSizeString(long size) {
        if (size < 1024) {
            return String.valueOf(size) + B_UNIT_NAME;
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            return String.valueOf(size) + kB_UNIT_NAME;
        } else {
            size = size * 100 / 1024;
        }
        return String.valueOf((size / 100)) + "."
                + ((size % 100) < 10 ? "0" : "") + String.valueOf((size % 100))
                + MB_UNIT_NAME;
    }

    /**
     * 以MB为单位保留两位小数
     * @param dirSize
     * @return
     */
    public static String getMbSize(long dirSize) {
        double size = 0;
        size = (dirSize + 0.0) / (1024 * 1024);
        DecimalFormat df = new DecimalFormat("0.00");// 以Mb为单位保留两位小数
        String filesize = df.format(size);
        return filesize;
    }

    /**
     * 以KB为单位保留两位小数
     * @param dirSize
     * @return
     */
    public static String getKBSize(long dirSize) {
        double size = 0;
        size = (dirSize + 0.0) / 1024;
        DecimalFormat df = new DecimalFormat("0.00");// 以KB为单位保留两位小数
        String filesize = df.format(size);
        return filesize;
    }

}
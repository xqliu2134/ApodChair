package com.apod.chair.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.apod.chair.bo.Business;
import com.apod.chair.manager.Config;

public class Storage {
	private final static String TAG = Config.TAG + ".Storage";
	private final static String ROOT_PATH = "apod_chair";
	private final static String DATA_PATH = ROOT_PATH + File.separator + "data";
	private final static String IMG_PATH = ROOT_PATH + File.separator + "img";
	private final static String SAVER_PATH = ROOT_PATH + File.separator + "saver";

	public static String getDataPath() {
		return getRootFilePath() + File.separator + DATA_PATH + File.separator;
	}

	public static String getDataFile() {
		return getDataPath() + Config.DATA_FILE_NAME;
	}

	public static String getImgPath() {
		return getRootFilePath() + File.separator + IMG_PATH + File.separator;
	}

	public static String getRootDirectory() {
		return getRootFilePath() + File.separator + ROOT_PATH + File.separator;
	}

	public static String getSaverPath() {
		return getRootFilePath() + File.separator + SAVER_PATH + File.separator;
	}

	public static String getApk1() {
		return getRootDirectory() + Config.APK1_NAME;
	}

	public static String getApk2() {
		return getRootDirectory() + Config.APK2_NAME;
	}

	public static String getRootFilePath() {
		try {
			File file = Environment.getExternalStorageDirectory();
			return file.getAbsolutePath();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "sdcard";
	}

	public static void initDefaultDirectory(Context context) {
		String root = getRootFilePath();
		String path = root + File.separator + ROOT_PATH;
		File file = new File(path);
		if (!file.exists()) {
			mkdir(file, path, ROOT_PATH);
		}
		path = root + File.separator + File.separator + DATA_PATH;
		file = new File(path);
		if (!file.exists()) {
			mkdir(file, path, DATA_PATH);
		}

		path = root + File.separator + IMG_PATH;
		file = new File(path);
		if (!file.exists()) {
			mkdir(file, path, IMG_PATH);
		}
		path = root + File.separator + SAVER_PATH;
		file = new File(path);
		if (!file.exists()) {
			mkdir(file, path, SAVER_PATH);
		}
		try {
			File apk = new File(getApk1());
			if (!apk.exists()) {
				InputStream in = context.getAssets().open(Config.APK1_NAME);
				copyFile(in, getApk1());
			}

			apk = new File(getApk2());
			if (!apk.exists()) {
				InputStream in = context.getAssets().open(Config.APK2_NAME);
				copyFile(in, getApk2());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void initBusinessDirectory(Business business) {
		String directory = business.getDirectory();
		if (directory != null) {
			File file = new File(directory);
			if (!file.exists()) {
				if (file.mkdir()) {
					Log.d(TAG, "mkdir " + directory + " success.");
				} else {
					Log.d(TAG, "mkdir " + directory + " fail.");
				}
			}
		}

		String doc = business.getDoc();
		if (doc != null) {
			File file = new File(doc);
			if (!file.exists()) {
				if (file.mkdir()) {
					Log.d(TAG, "mkdir " + doc + " success.");
				} else {
					Log.d(TAG, "mkdir " + doc + " fail.");
				}
			}
		}
	}

	private static void mkdir(File file, String path, String dir) {
		Log.d(TAG, path + " not exists to mkdir " + dir);
		if (file.mkdir()) {
			Log.d(TAG, "mkdir " + dir + " success.");
		}
	}

	public static String convertNameByFile(String file) {
		try {
			return file.substring(0, file.indexOf("."));
		} catch (Exception ex) {

		}
		return "";
	}

	public static String convertNameByPath(String path) {
		int start = path.lastIndexOf("/");
		int end = path.lastIndexOf(".");
		if (start != -1 && end != -1) {
			return path.substring(start + 1, end);
		} else {
			return null;
		}

	}

	public static void installApk(Context context, String url) {
		Log.d(TAG, "install apk url is " + url);
		File file = new File(url);
		if (!file.exists()) {
			Log.d(TAG, "apk is not found.");
			return;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(url)), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	public static void copyFile(InputStream in, String target) {
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		Log.d(TAG, "copyFile to " + target);
		try {
			File targetFile = new File(target);
			inBuff = new BufferedInputStream(in);
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			outBuff.flush();
			Log.d(TAG, "copyFile success.");
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, "copyFile fail.");
		} finally {
			if (inBuff != null) {
				try {
					inBuff.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outBuff != null) {
				try {
					outBuff.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

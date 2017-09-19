package com.apod.chair.manager;

import android.util.Log;

public class Config {
	private static String host = "http://215.32.1.12:8001";// "http://120.24.218.119:5396";
	private static String WEB_METHOD = "/router/api.ashx?method=";
	public final static String TAG = "ApodChair";
	public final static String ACCESS_TOKEN_NAME = "access_token";// 授权码名称
	public final static String ACCESS_DEVICE_CODE = "device_code";// 设备码
	public final static String ACCESS_TOKEN_VALUE = "psbc";// 授权码值
	public final static String DATA_FILE_NAME = "apod.xml";// 数据存储文件名
	public final static String DIRECTORY_TAG = "b";// 数据存储文件名
	public final static String APK1_NAME = "ppx.apk";// 游戏APK
	public final static String APK2_NAME = "xmbs.apk";// 游戏APK

	public static String getWebHost() {
		return Config.getHost() + Config.WEB_METHOD;
	}

	public static String getHost() {
		return Config.host;
	}

	public static void setHost(String host) {
		Log.d(TAG, "set host is " + host);
		Config.host = host;
	}
}

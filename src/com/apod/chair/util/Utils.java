package com.apod.chair.util;

import java.util.Calendar;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class Utils {
	public static String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		String mac = info.getMacAddress();
		if (mac != null) {
			mac = mac.toUpperCase();
		}
		return mac;
	}

	public static int[] getDataTime() {
		int[] array = new int[5];
		Calendar calendar = Calendar.getInstance();
		array[0] = calendar.get(Calendar.YEAR);
		array[1] = calendar.get(Calendar.MONTH);
		array[2] = calendar.get(Calendar.DAY_OF_MONTH);
		array[3] = calendar.get(Calendar.HOUR_OF_DAY);
		array[4] = calendar.get(Calendar.MINUTE);
		return array;
	}

	public static int[] getTime() {
		int[] array = new int[2];
		Calendar calendar = Calendar.getInstance();
		array[0] = calendar.get(Calendar.HOUR_OF_DAY);
		array[1] = calendar.get(Calendar.MINUTE);
		return array;
	}

	public static int[] getDate() {
		int[] array = new int[3];
		Calendar calendar = Calendar.getInstance();
		array[0] = calendar.get(Calendar.YEAR);
		array[1] = calendar.get(Calendar.MONTH);
		array[2] = calendar.get(Calendar.DAY_OF_MONTH);
		return array;
	}

	public static boolean isWifiConnected(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager == null) {
			return false;
		}
		int state = wifiManager.getWifiState();
		return WifiManager.WIFI_STATE_ENABLED == state;
	}
}

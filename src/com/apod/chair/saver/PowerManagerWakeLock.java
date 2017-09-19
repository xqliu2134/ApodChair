package com.apod.chair.saver;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class PowerManagerWakeLock {
	private static WakeLock wakeLock;

	public static void acquire(Context context) {
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "PowerManagerWakeLock");
		wakeLock.acquire();
	}

	public static void release() {
		if (wakeLock != null) {
			wakeLock.release();
			wakeLock = null;
		}
	}
}

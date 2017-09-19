package com.apod.chair.saver;

import java.util.List;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.apod.chair.bo.Saver;
import com.apod.chair.bo.Saver.SaverImg;
import com.apod.chair.manager.Config;
import com.apod.chair.saver.SaverDowloadManager.OnDowloadDataChangeListener;
import com.apod.chair.saver.ServerManager.OnDataChangeListener;
import com.apod.chair.util.Share;

public class SaverService extends Service implements OnDataChangeListener, OnDowloadDataChangeListener {
	private final static String TAG = Config.TAG + ".SaverService";
	protected ServerManager mServerManager;
	protected SaverDowloadManager mDowloadManager;

	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");

		mServerManager = ServerManager.getInstance(this);
		mServerManager.setOnDataChangeListener(this);

		mDowloadManager = SaverDowloadManager.getInstance(this);
		mDowloadManager.setOnDowloadDataChangeListener(this);

		KeyguardManager manager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		KeyguardManager.KeyguardLock lock = manager.newKeyguardLock("KeyguardLock");
		lock.disableKeyguard();

		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(screenReceiver, filter);

		mServerManager.execGetSaver();
	}

	private BroadcastReceiver screenReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d(TAG, "receiver action is " + action);
			if (action.equals(Intent.ACTION_SCREEN_ON)) {

			} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
				// 开启屏幕唤醒，常亮
				PowerManagerWakeLock.acquire(SaverService.this);
				Intent intent2 = new Intent(SaverService.this, ScreenActivity.class);
				intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent2);
				PowerManagerWakeLock.release();
			}
		}
	};

	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

	public IBinder onBind(Intent arg0) {
		return null;
	}

	public void onGetSaver(Saver saver) {
		Log.d(TAG, "onGetSaver size is " + saver.getImgs().size());
		Share.getSaverImgs().clear();
		List<SaverImg> imgs = saver.getImgs();
		for (SaverImg img : imgs) {
			mDowloadManager.execDowloadSaverImg(img);
		}
	}

	public void onDowloadSaverImg(SaverImg saverImg) {
		Log.d(TAG, "onDowloadSaverImg size is " + saverImg);
		if (saverImg != null) {
			Share.getSaverImgs().add(saverImg);
		}
	}
}

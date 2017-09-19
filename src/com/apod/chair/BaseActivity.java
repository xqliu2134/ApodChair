package com.apod.chair;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.apod.chair.bo.Business;
import com.apod.chair.bo.CheckIn;
import com.apod.chair.bo.Device;
import com.apod.chair.bo.Feedback;
import com.apod.chair.bo.MsgTemplate;
import com.apod.chair.bo.Network;
import com.apod.chair.bo.Saver;
import com.apod.chair.bo.Saver.SaverImg;
import com.apod.chair.http.WebResponse;
import com.apod.chair.manager.ApodManager;
import com.apod.chair.manager.ApodManager.OnDataChangeListener;
import com.apod.chair.manager.Config;
import com.apod.chair.manager.DowloadManager;
import com.apod.chair.manager.DowloadManager.OnDowloadDataChangeListener;
import com.apod.chair.util.Storage;
import com.apod.chair.view.BizView;
import com.apod.chair.view.PduView;

public class BaseActivity extends Activity implements OnDataChangeListener, OnDowloadDataChangeListener {
	private final static String TAG = Config.TAG + ".BaseActivity";
	protected ApodManager mApodManager;
	protected DowloadManager mDowloadManager;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApodManager = ApodManager.getInstance(this);
		mApodManager.setOnDataChangeListener(this);

		mDowloadManager = DowloadManager.getInstance(this);
		mDowloadManager.setOnDowloadDataChangeListener(this);
	}

	public boolean checkApkExist(Intent intent) {
		try {
			List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
			if (list.size() > 0) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	protected void startMyActivity(String action, String path) {
		Intent intent = new Intent();
		intent.setAction(action);
		if (checkApkExist(intent)) {
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
		} else {
			Storage.installApk(this, path);
		}
	}

	protected void startMyService(Class cls) {
		Intent intent = new Intent(this, cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startService(intent);
		Log.d(TAG, "startMyService");
	}

	protected void startMyActivity(Class cls) {
		Intent intent = new Intent(this, cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public void onGetNetworks(List<Network> list) {

	}

	public void onGetDevices(List<Device> list) {

	}

	public void onCheckIn(CheckIn checkin) {

	}

	public void onGetBusiness(List<Business> business) {

	}

	public void onGetMsgTemplate(WebResponse response, MsgTemplate msgTemplate) {

	}

	public void onDowloadPduImg(PduView pduView) {

	}

	public void onSaveData() {

	}

	public void onReadData(WebResponse response) {

	}

	public void onDowloadBizImg(BizView bizView) {

	}

	public void onGetSaver(Saver saver) {

	}

	public void onDowloadSaverImg(SaverImg saverImg) {

	}

	public void onGetFeedback(WebResponse response, Feedback feedback) {

	}

	public void onCommitedFeedback(Object obj) {

	}

	public void onSendSms(boolean state) {
		Log.d(TAG, "onSendSms state is " + state);
		if (state) {
			Toast.makeText(this, getResources().getString(R.string.hujiao_success), Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, getResources().getString(R.string.hujiao_fail), Toast.LENGTH_LONG).show();
		}
	}

	public void onReadSms() {
		
	}
}

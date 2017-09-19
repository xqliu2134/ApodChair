package com.apod.chair;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apod.chair.bo.Business;
import com.apod.chair.bo.Business.BusinessLevels;
import com.apod.chair.bo.Business.BusinessMode;
import com.apod.chair.bo.CheckIn;
import com.apod.chair.http.WebResponse;
import com.apod.chair.manager.Config;
import com.apod.chair.saver.SaverService;
import com.apod.chair.util.Share;
import com.apod.chair.util.Storage;
import com.apod.chair.view.PduView;
import com.apod.chair.view.PduView.OnBusinessClickListener;

public class MainActivity extends BaseActivity implements OnClickListener, OnBusinessClickListener {
	private final static String TAG = Config.TAG + ".MainActivity";
	private Button btnSmset, btnHujiao, btnYulei, btnFk;
	private TextView txtDeviceName;
	private LinearLayout pduLayout1, pduLayout2, loadingLayout;
	private List<PduView> pduViews = new ArrayList<PduView>();
	private int delaytime = 1000 * 60;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Log.d(TAG, "onCreate");
		findView();
		mApodManager.initDefaultDirectory();
		mApodManager.execReadSms();
		mApodManager.checkIn();
		mHandler.sendEmptyMessageDelayed(MSG_SAVE, delaytime);
	}

	private void findView() {
		pduLayout1 = (LinearLayout) findViewById(R.id.pduLayout1);
		pduLayout2 = (LinearLayout) findViewById(R.id.pduLayout2);
		loadingLayout = (LinearLayout) findViewById(R.id.loadingLayout);
		txtDeviceName = (TextView) findViewById(R.id.txtDeviceName);

		btnSmset = (Button) findViewById(R.id.btnSmset);
		btnSmset.setOnClickListener(this);

		btnHujiao = (Button) findViewById(R.id.btnHujiao);
		btnHujiao.setOnClickListener(this);

		btnYulei = (Button) findViewById(R.id.btnYulei);
		btnYulei.setOnClickListener(this);

		btnFk = (Button) findViewById(R.id.btnFk);
		btnFk.setOnClickListener(this);
	}

	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		mHandler.sendEmptyMessageDelayed(MSG_HEARTBEAT, delaytime);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSmset: {
			startMyActivity(SmsActivity.class);
			break;
		}
		case R.id.btnHujiao: {
			if (mApodManager != null) {
				mApodManager.execSendSms();
			}
			break;
		}
		case R.id.btnYulei: {
			startMyActivity(GameActivity.class);
			break;
		}
		case R.id.btnFk: {
			startMyActivity(CustomActivity.class);
			break;
		}
		}
	}

	public void onCheckIn(CheckIn checkin) {
		Log.d(TAG, "onCheckIn " + checkin);
		refershCheckIn(checkin);
		mApodManager.execReadData();
		loadingLayout.setVisibility(View.VISIBLE);
	}

	private void refershCheckIn(CheckIn checkin) {
		String devicename = null;
		String devicecode = null;
		String mac = getResources().getString(R.string.defalut_mac);
		if (checkin != null) {
			devicename = checkin.getDeviceName();
			mac = checkin.getMac();
			devicecode = checkin.getDeviceCode();
		}
		String macstr = getResources().getString(R.string.mac_title) + ":" + mac;
		String name = getResources().getString(R.string.defalut_checkin);
		if (devicename != null && devicename.trim().length() != 0) {
			name = getResources().getString(R.string.device_name) + ":" + devicename;
		}
		Share.setDeviceCode(devicecode);
		Log.d(TAG, "refershCheckIn devicename = " + devicename + ",devicecode = " + devicecode);
		txtDeviceName.setText(macstr + "/" + name);
		startMyService(SaverService.class);
	}

	public void onGetBusiness(List<Business> business) {
		initBusiness(business);
	}

	private void initBusiness(List<Business> business) {
		loadingLayout.setVisibility(View.GONE);
		if (business == null) {
			return;
		}
		pduLayout1.removeAllViews();
		pduLayout2.removeAllViews();
		pduViews.clear();
		for (int i = 0; i < business.size(); i++) {
			Business bus = business.get(i);
			PduView pduView = new PduView(this);
			pduView.setPduView(bus);
			pduView.setOnBusinessClickListener(this);
			if (i < 3) {
				pduLayout1.addView(pduView.getView());
			} else {
				pduLayout2.addView(pduView.getView());
			}
			pduViews.add(pduView);
			Bitmap bitmap = bus.getDrawable();
			if (bitmap == null) {
				mDowloadManager.execDowloadPduImg(pduView);
			}
			Storage.initBusinessDirectory(bus);
		}
	}

	public void onBusinessClick(Business business) {
		if (business == null) {
			return;
		}
		Share.setBusiness(business);
		List<BusinessLevels> levels = business.getBusinessLevels();
		Log.d(TAG, "onBusinessClick level = " + levels);
		if (levels != null && levels.size() > 0) {
			Intent intent = new Intent(this, LevelActivity.class);
			startActivity(intent);
		} else {
			List<BusinessMode> modes = business.getBusinessModes();
			if (modes != null) {
				Share.setPudName(business.getName());
				Share.setBusinesModes(modes);
				Share.setLevelName("");
				Log.d(TAG, "onBusinessClick mode size = " + modes.size());
				Intent intent = new Intent(this, ProductActivity.class);
				startActivity(intent);
			}
		}
	}

	public void onDowloadPduImg(PduView pduView) {
		Log.d(TAG, "onDowloadPduImg pduView = " + pduView);
		if (pduView != null) {
			pduView.refershPduView();
		}
	}

	public void onReadData(WebResponse response) {
		Log.d(TAG, "onReadData response " + response);
		if (response == null) {
			return;
		}
		initBusiness(response.getBusiness());
		refershCheckIn(response.getCheckin());
		mApodManager.execGetBusiness();
	}

	public void onSaveData() {
		Log.d(TAG, "onSaveData.");
	}

	private final static int MSG_HEARTBEAT = 1;
	private final static int MSG_SAVE = 2;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_HEARTBEAT: {
				if (mApodManager != null) {
					mApodManager.execHeartbeat();
					mHandler.sendEmptyMessageDelayed(MSG_HEARTBEAT, delaytime);
				}
				break;
			}
			case MSG_SAVE: {
				if (mApodManager != null) {
					mApodManager.execSaveData();
					mHandler.sendEmptyMessageDelayed(MSG_SAVE, delaytime);
				}
				break;
			}
			}
		}
	};

	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		mHandler.removeMessages(MSG_HEARTBEAT);
		mHandler.removeMessages(MSG_SAVE);
	}
}

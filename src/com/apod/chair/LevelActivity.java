package com.apod.chair;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apod.chair.bo.Business;
import com.apod.chair.bo.Business.BusinessLevels;
import com.apod.chair.bo.Business.BusinessLevels.ModuleBizAttach;
import com.apod.chair.bo.Business.BusinessMode;
import com.apod.chair.manager.Config;
import com.apod.chair.util.Share;
import com.apod.chair.view.LevelView;
import com.apod.chair.view.LevelView.OnBusinessLevelsClickListener;

public class LevelActivity extends BaseActivity implements OnClickListener, OnBusinessLevelsClickListener {
	private final static String TAG = Config.TAG + ".LevelActivity";
	private TextView txtBackHome, txtBackUp;
	private Button btnHujiao, btnYulei;
	private LinearLayout pduLayout1, pduLayout2;
	private List<LevelView> pduViews = new ArrayList<LevelView>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level);

		pduLayout1 = (LinearLayout) findViewById(R.id.pduLayout1);
		pduLayout2 = (LinearLayout) findViewById(R.id.pduLayout2);

		txtBackHome = (TextView) findViewById(R.id.txtBackHome);
		txtBackHome.setOnClickListener(this);

		txtBackUp = (TextView) findViewById(R.id.txtBackUp);
		txtBackUp.setOnClickListener(this);

		btnHujiao = (Button) findViewById(R.id.btnHujiao);
		btnHujiao.setOnClickListener(this);

		btnYulei = (Button) findViewById(R.id.btnYulei);
		btnYulei.setOnClickListener(this);

		Business business = Share.getBusiness();
		if (business != null) {
			initBusiness(business.getBusinessLevels());
		}
	}

	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
	}

	private void initBusiness(List<BusinessLevels> businessLevels) {
		if (businessLevels == null) {
			return;
		}
		pduLayout1.removeAllViews();
		pduLayout2.removeAllViews();
		pduViews.clear();
		for (int i = 0; i < businessLevels.size(); i++) {
			BusinessLevels bus = businessLevels.get(i);
			LevelView levelView = new LevelView(this);
			levelView.setPduView(bus);
			levelView.setOnBusinessLevelsClickListener(this);
			pduViews.add(levelView);
			if (i < 3) {
				pduLayout1.addView(levelView.getView());
			} else {
				pduLayout2.addView(levelView.getView());
			}
		}
	}

	public void onBusinessLevelsClick(BusinessLevels businessLevels) {
		if (businessLevels == null) {
			return;
		}
		List<ModuleBizAttach> attachs = businessLevels.getModuleBizAttach();
		Log.d(TAG, "onBusinessLevelsClick attachs is " + attachs);
		if (attachs != null && attachs.size() > 0) {
			Share.setModuleBizAttach(attachs);
			Share.setLevelName(businessLevels.getName());
			Intent intent = new Intent(this, AttachActivity.class);
			startActivity(intent);
		} else {
			List<BusinessMode> modes = businessLevels.getBusinessMode();
			Share.setPudName(businessLevels.getName());
			Share.setLevelName(businessLevels.getName());
			Log.d(TAG, "onBusinessLevelsClick modes is " + modes);
			if (modes != null) {
				Share.setBusinesModes(modes);
				Intent intent = new Intent(this, ProductActivity.class);
				startActivity(intent);
			}
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtBackHome: {
			startMyActivity(MainActivity.class);
			break;
		}
		case R.id.txtBackUp: {
			startMyActivity(MainActivity.class);
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
		}
	}

	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
	}
}

package com.apod.chair;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apod.chair.bo.Business.BusinessLevels.ModuleBizAttach;
import com.apod.chair.bo.Business.BusinessLevels.Navigation;
import com.apod.chair.manager.Config;
import com.apod.chair.util.Share;
import com.apod.chair.view.AttachView;
import com.apod.chair.view.AttachView.OnModuleBizAttachClickListener;

public class AttachActivity extends BaseActivity implements OnClickListener, OnModuleBizAttachClickListener {
	private final static String TAG = Config.TAG + ".AttachActivity";
	private TextView txtBackHome, txtBackUp;
	private Button btnHujiao, btnYulei;
	private LinearLayout pduLayout1, pduLayout2;
	private List<AttachView> pduViews = new ArrayList<AttachView>();

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

		initModuleBizAttachs(Share.getModuleBizAttach());
	}

	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
	}

	private void initModuleBizAttachs(List<ModuleBizAttach> moduleBizAttachs) {
		Log.d(TAG, "initModuleBizAttachs moduleBizAttachs = " + moduleBizAttachs);
		if (moduleBizAttachs == null) {
			return;
		}
		pduLayout1.removeAllViews();
		pduLayout2.removeAllViews();
		pduViews.clear();
		for (int i = 0; i < moduleBizAttachs.size(); i++) {
			ModuleBizAttach bus = moduleBizAttachs.get(i);
			AttachView attachView = new AttachView(this);
			attachView.setPduView(bus);
			attachView.setOnModuleBizAttachClickListener(this);
			pduViews.add(attachView);
			if (i < 3) {
				pduLayout1.addView(attachView.getView());
			} else {
				pduLayout2.addView(attachView.getView());
			}
		}
	}

	public void onModuleBizAttachClickListenerClick(ModuleBizAttach moduleBizAttach) {
		Log.d(TAG, "onModuleBizAttachClickListenerClick moduleBizAttach = " + moduleBizAttach);
		if (moduleBizAttach != null) {
			Log.d(TAG, "onModuleBizAttachClickListenerClick moduleBizAttach isModeWord = " + moduleBizAttach.isModeWord());
			if (moduleBizAttach.isModeWord()) {
				Share.setBizAttach(moduleBizAttach);
				startMyActivity(WordActivity.class);
			} else {
				List<Navigation> list = moduleBizAttach.getNavigations();
				Log.d(TAG, "onModuleBizAttachClickListenerClick Navigations is " + list);
				Share.setBizAttach(moduleBizAttach);
				Share.setNavigations(list);
				startMyActivity(MoneyActivity.class);
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
			startMyActivity(LevelActivity.class);
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

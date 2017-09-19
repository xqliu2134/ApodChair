package com.apod.chair;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.apod.chair.manager.Config;
import com.apod.chair.util.Storage;

public class GameActivity extends BaseActivity implements OnClickListener {
	private final static String TAG = Config.TAG + ".GameActivity";
	private Button btnHujiao, btnBack, btnGame1, btnGame2;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		findView();
	}

	private void findView() {
		btnHujiao = (Button) findViewById(R.id.btnHujiao);
		btnHujiao.setOnClickListener(this);

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		btnGame1 = (Button) findViewById(R.id.btnGame1);
		btnGame1.setOnClickListener(this);

		btnGame2 = (Button) findViewById(R.id.btnGame2);
		btnGame2.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnHujiao: {
			if (mApodManager != null) {
				mApodManager.execSendSms();
			}
			break;
		}
		case R.id.btnBack: {
			GameActivity.this.finish();
			break;
		}
		case R.id.btnGame1: {
			startMyActivity("android.intent.action.popbear", Storage.getApk1());
			break;
		}
		case R.id.btnGame2: {
			startMyActivity("android.intent.action.jewels", Storage.getApk2());
			break;
		}
		}
	}

	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
	}

	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

}

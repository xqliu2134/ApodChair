package com.apod.chair;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.apod.chair.manager.ApodManager;
import com.apod.chair.manager.Config;
import com.apod.chair.util.Utils;

public class SmsActivity extends BaseActivity implements OnClickListener {
	private ApodManager mApodManager;
	private LinearLayout passwordLayout, contentLayout;
	private EditText editHost, editPassword;
	private Button btnCommit, btnBack, btnHujiao, btnYulei, btnShowPassword;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.smset);
		mApodManager = ApodManager.getInstance(this);
		findView();
	}

	private void findView() {
		passwordLayout = (LinearLayout) findViewById(R.id.passwordLayout);
		passwordLayout.setVisibility(View.VISIBLE);

		contentLayout = (LinearLayout) findViewById(R.id.contentLayout);
		contentLayout.setVisibility(View.GONE);

		btnCommit = (Button) findViewById(R.id.btnCommit);
		btnCommit.setOnClickListener(this);

		btnShowPassword = (Button) findViewById(R.id.btnShowPassword);
		btnShowPassword.setOnClickListener(this);

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		btnHujiao = (Button) findViewById(R.id.btnHujiao);
		btnHujiao.setOnClickListener(this);

		btnYulei = (Button) findViewById(R.id.btnYulei);
		btnYulei.setOnClickListener(this);

		editHost = (EditText) findViewById(R.id.editHost);
		editHost.setText(Config.getHost());

		editPassword = (EditText) findViewById(R.id.editPassword);
		//editPassword.setText("psbc_95580");
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnCommit: {
			saveValue();
			break;
		}
		case R.id.btnBack: {
			SmsActivity.this.finish();
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
		case R.id.btnShowPassword: {
			showContent();
			break;
		}
		}
	}

	private void showContent() {
		String password = editPassword.getText().toString();
		if (TextUtils.isEmpty(password)) {
			Toast.makeText(this, getResources().getString(R.string.password_failer_msg), Toast.LENGTH_LONG).show();
			return;
		}
		if ("psbc_95580".equals(password)) {
			passwordLayout.setVisibility(View.GONE);
			contentLayout.setVisibility(View.VISIBLE);
		} else {
			Toast.makeText(this, getResources().getString(R.string.password_failer_msg), Toast.LENGTH_LONG).show();
		}
	}

	protected void startMyActivity(Class cls) {
		Intent intent = new Intent(this, cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	private void saveValue() {
		if (!Utils.isWifiConnected(this)) {
			Toast.makeText(this, this.getResources().getString(R.string.send_sms_msg), Toast.LENGTH_LONG).show();
			return;
		}
		String host = editHost.getText().toString();
		Config.setHost(host);
		mApodManager.execSaveData();
		Toast.makeText(this, getResources().getString(R.string.sms_commit_msg), Toast.LENGTH_LONG).show();
	}
}

package com.apod.chair;

import java.io.File;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apod.chair.bo.Business;
import com.apod.chair.bo.Business.BizAttach;
import com.apod.chair.bo.Word;
import com.apod.chair.manager.Config;
import com.apod.chair.manager.WordManager;
import com.apod.chair.manager.WordManager.OnWordListener;
import com.apod.chair.util.Share;

public class WordActivity extends BaseActivity implements OnWordListener, OnClickListener {
	private final static String TAG = Config.TAG + ".WordActivity";
	private WebView docView;
	private WordManager mWordManager;
	private TextView txtWordTitle, txtBackHome, txtWordFailer, txtBackUp;
	private LinearLayout loadingLayout;
	private Button btnHujiao, btnYulei;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word);
		txtWordTitle = (TextView) findViewById(R.id.txtWordTitle);
		txtWordFailer = (TextView) findViewById(R.id.txtWordFailer);
		loadingLayout = (LinearLayout) findViewById(R.id.loadingLayout);
		loadingLayout.setVisibility(View.GONE);

		txtBackHome = (TextView) findViewById(R.id.txtBackHome);
		txtBackHome.setOnClickListener(this);

		txtBackUp = (TextView) findViewById(R.id.txtBackUp);
		txtBackUp.setOnClickListener(this);

		btnHujiao = (Button) findViewById(R.id.btnHujiao);
		btnHujiao.setOnClickListener(this);

		btnYulei = (Button) findViewById(R.id.btnYulei);
		btnYulei.setOnClickListener(this);

		mWordManager = WordManager.getInstance(this);
		mWordManager.setOnWordListener(this);

		docView = (WebView) findViewById(R.id.docView);
		WebSettings webSettings = docView.getSettings();
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);

		initWord();
	}

	private void initWord() {
		BizAttach bizAttach = Share.getBizAttach();
		Log.d(TAG, "bizAttach = " + bizAttach);
		if (bizAttach != null) {
			Business business = bizAttach.getBusiness();
			String str = "";
			if (business != null) {
				str = business.getName() + " > ";
			}
			String levelName = Share.getLevelName();
			if (levelName != null && levelName.length() > 0) {
				str = str + levelName + " > ";
			}
			txtWordTitle.setText(str + bizAttach.getBizName());
			Word word = new Word();
			String doc = bizAttach.getWordDir();
			Log.d(TAG, "doc = " + doc);
			if (doc != null) {
				String filename = bizAttach.getFileName();
				Log.d(TAG, "filename = " + filename);
				if (filename != null && filename.length() > 0) {
					word.setBizAttach(bizAttach);
					word.setDir(doc);
					word.setFilepath(doc + File.separator + filename);
					mWordManager.executeWord(word);
					Log.d(TAG, "execute word.");
				} else {
					showNotFoundMsg();
				}
			} else {
				showNotFoundMsg();
			}
		} else {
			showNotFoundMsg();
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

	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
	}

	public void onLoadingWord(Word word) {
		Log.d(TAG, "onLoadingWord filepath is " + word.getFilepath());
		txtWordFailer.setVisibility(View.GONE);
		loadingLayout.setVisibility(View.VISIBLE);
	}

	public void onLoadedWord(Word word) {
		String url = word.getUrl();
		Log.d(TAG, "onLoadedWord url is " + url);
		if (url == null || url.length() == 0) {
			showNotFoundMsg();
		} else {
			txtWordFailer.setVisibility(View.GONE);
			loadingLayout.setVisibility(View.GONE);
			docView.loadUrl("file://" + word.getUrl());
		}
	}

	public void onDowloadWord(Word word, boolean state) {
		Log.d(TAG, "onDowloadWord state is " + state);
		if (!state) {
			showNotFoundMsg();
		}
	}

	private void showNotFoundMsg() {
		txtWordFailer.setVisibility(View.VISIBLE);
		loadingLayout.setVisibility(View.GONE);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtBackHome: {
			startMyActivity(MainActivity.class);
			WordActivity.this.finish();
			break;
		}
		case R.id.txtBackUp: {
			WordActivity.this.finish();
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
}

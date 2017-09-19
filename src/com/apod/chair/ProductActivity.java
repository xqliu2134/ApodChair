package com.apod.chair;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.apod.chair.bo.Business;
import com.apod.chair.bo.Business.BusinessMode;
import com.apod.chair.manager.Config;
import com.apod.chair.manager.DowloadManager.OnDowloadDataChangeListener;
import com.apod.chair.util.PageEntry;
import com.apod.chair.util.PageEntry.OnStepListener;
import com.apod.chair.util.Share;
import com.apod.chair.view.BizView;
import com.apod.chair.view.BizView.OnBusinessModeClickListener;
import com.apod.chair.view.PduView;

public class ProductActivity extends BaseActivity implements OnClickListener, OnStepListener, OnBusinessModeClickListener, OnDowloadDataChangeListener {
	public final static String TAG = Config.TAG + ".ProductActivity";
	private LinearLayout bizLayout, btnShowWord;
	private PageEntry pageEntry;
	private TextView pduName, txtPrevPage, txtNextPage, bizName;
	private Button btnGridUp, btnGridNext;
	private Button btnHujiao, btnYulei, btnBack;
	private int select;
	private List<BizView> bizViews = new ArrayList<BizView>();
	private ImageView bizShow;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		setContentView(R.layout.product);

		bizName = (TextView) findViewById(R.id.bizName);
		pduName = (TextView) findViewById(R.id.pduName);
		pduName.setText(Share.getPudName());

		bizLayout = (LinearLayout) findViewById(R.id.bizLayout);
		txtPrevPage = (TextView) findViewById(R.id.txtPrevPage);
		txtPrevPage.setOnClickListener(this);

		bizShow = (ImageView) findViewById(R.id.bizShow);
		txtNextPage = (TextView) findViewById(R.id.txtNextPage);
		txtNextPage.setOnClickListener(this);
		pageEntry = new PageEntry(this, Share.getBusinesModes());
		pageEntry.setOnStepListener(this);
		pageEntry.setSelect(select);

		btnShowWord = (LinearLayout) findViewById(R.id.btnShowWord);
		btnShowWord.setOnClickListener(this);

		btnGridUp = (Button) findViewById(R.id.btnGridUp);
		btnGridUp.setOnClickListener(this);

		btnGridNext = (Button) findViewById(R.id.btnGridNext);
		btnGridNext.setOnClickListener(this);

		btnHujiao = (Button) findViewById(R.id.btnHujiao);
		btnHujiao.setOnClickListener(this);

		btnYulei = (Button) findViewById(R.id.btnYulei);
		btnYulei.setOnClickListener(this);

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		initBizView(pageEntry.currentPage());
	}

	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtPrevPage: {
			mHandler.removeMessages(MSG_PREV_PAGE);
			mHandler.sendEmptyMessageDelayed(MSG_PREV_PAGE, 500);
			break;
		}
		case R.id.txtNextPage: {
			mHandler.removeMessages(MSG_NEXT_PAGE);
			mHandler.sendEmptyMessageDelayed(MSG_NEXT_PAGE, 500);
			break;
		}
		case R.id.btnShowWord: {
			if (bizViews != null && bizViews.size() > 0) {
				startMyActivity(WordActivity.class);
			}
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
		case R.id.btnBack: {
			ProductActivity.this.finish();
			break;
		}
		case R.id.btnGridUp: {
			pageEntry.stepUp();
			break;
		}
		case R.id.btnGridNext: {
			pageEntry.stepDown();
			break;
		}
		}
	}

	private void initBizView(List<BusinessMode> datas) {
		bizLayout.removeAllViews();
		bizViews.clear();
		for (int i = 0; i < datas.size(); i++) {
			BusinessMode mode = datas.get(i);
			BizView view = new BizView(this, bizName, bizShow);
			view.setOnBusinessModeClickListener(this);
			view.setBizView(mode);
			view.setSelect(select);
			bizViews.add(view);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			if (i == 0) {
				params.setMargins(0, 20, 0, 0);
			} else {
				params.setMargins(20, 20, 0, 0);
			}
			bizLayout.addView(view.getView(), params);
			Bitmap bitmap = mode.getDrawable();
			if (bitmap == null) {
				mDowloadManager.execDowloadBizImg(view);
			}
		}
	}

	public void onStepUp(int select, boolean change) {
		Log.d(TAG, "onStepUp select = " + select + ",change = " + change);
		this.select = select;
		if (change) {
			pageEntry.prevPage();
			initBizView(pageEntry.getPageEntrys());
		} else {
			for (int i = 0; i < bizViews.size(); i++) {
				bizViews.get(i).setSelect(select);
			}
		}
		pageEntry.setSelect(select);
	}

	public void onStepDown(int select, boolean change) {
		Log.d(TAG, "onStepDown select = " + select + ",change = " + change);
		this.select = select;
		if (change) {
			pageEntry.nextPage();
			initBizView(pageEntry.getPageEntrys());
		} else {
			for (int i = 0; i < bizViews.size(); i++) {
				bizViews.get(i).setSelect(select);
			}
		}
		pageEntry.setSelect(select);
	}

	private final static int MSG_NEXT_PAGE = 1;
	private final static int MSG_PREV_PAGE = 2;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_NEXT_PAGE: {
				pageEntry.nextPage();
				initBizView(pageEntry.getPageEntrys());
				break;
			}
			case MSG_PREV_PAGE: {
				pageEntry.prevPage();
				initBizView(pageEntry.getPageEntrys());
				break;
			}
			}
		}
	};

	public void onBusinessModeChange(BusinessMode mode) {
		if (mode == null) {
			return;
		}
		this.select = mode.getIndex();
		for (int i = 0; i < bizViews.size(); i++) {
			bizViews.get(i).setSelect(select);
		}
	}

	public void onDowloadPduImg(PduView pduView) {

	}

	public void onDowloadBizImg(BizView bizView) {
		if (bizView != null) {
			bizView.refershBizView();
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

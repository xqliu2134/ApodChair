package com.apod.chair;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.apod.chair.bo.Business.BizAttach;
import com.apod.chair.bo.Business.BusinessLevels.Item;
import com.apod.chair.bo.Business.BusinessLevels.ModuleBizAttach;
import com.apod.chair.bo.Business.BusinessLevels.Navigation;
import com.apod.chair.manager.Config;
import com.apod.chair.util.Share;
import com.apod.chair.view.CircleView;
import com.apod.chair.view.MoneyView;
import com.apod.chair.view.MtyView;
import com.apod.chair.view.MtyView.OnMtyClickListener;

public class MoneyActivity extends BaseActivity implements OnClickListener, OnMtyClickListener {
	private final static String TAG = Config.TAG + ".MoneyActivity";
	private TextView txtWordTitle, txtBackHome, txtBackUp;
	private TextView txtTitle1, txtTitle2, txtTitle3;
	private LinearLayout layoutTitle1, layoutTitle2, layoutTitle3;
	private Button btnHujiao, btnYulei;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		setContentView(R.layout.money);
		findViewById();
	}

	private void findViewById() {
		txtWordTitle = (TextView) findViewById(R.id.txtWordTitle);
		txtTitle1 = (TextView) findViewById(R.id.txtTitle1);
		txtTitle2 = (TextView) findViewById(R.id.txtTitle2);
		txtTitle3 = (TextView) findViewById(R.id.txtTitle3);

		layoutTitle1 = (LinearLayout) findViewById(R.id.layoutTitle1);
		layoutTitle2 = (LinearLayout) findViewById(R.id.layoutTitle2);
		layoutTitle3 = (LinearLayout) findViewById(R.id.layoutTitle3);

		txtBackHome = (TextView) findViewById(R.id.txtBackHome);
		txtBackHome.setOnClickListener(this);

		txtBackUp = (TextView) findViewById(R.id.txtBackUp);
		txtBackUp.setOnClickListener(this);

		btnHujiao = (Button) findViewById(R.id.btnHujiao);
		btnHujiao.setOnClickListener(this);

		btnYulei = (Button) findViewById(R.id.btnYulei);
		btnYulei.setOnClickListener(this);
	}

	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		showTextView();
	}

	private void showTextView() {
		layoutTitle1.removeAllViews();
		layoutTitle2.removeAllViews();
		layoutTitle3.removeAllViews();
		List<Navigation> datas = Share.getNavigations();
		Log.d(TAG, "showTextView datas is " + datas);
		if (datas == null) {
			return;
		}
		BizAttach bizAttach = Share.getBizAttach();
		if (bizAttach != null) {
			txtWordTitle.setText(bizAttach.getBizName());
		}
		for (int i = 0; i < datas.size(); i++) {
			Navigation navigation = datas.get(i);
			if (i == 0) {
				txtTitle1.setText(navigation.getName());
				List<Item> items = navigation.getItems();

				LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				CircleView circleView1 = new CircleView(this, items, true);

				layoutTitle1.addView(circleView1, params);
				CircleView circleView2 = new CircleView(this, items, false);
				layoutTitle1.addView(circleView2, params);
			} else if (i == 1) {
				txtTitle2.setText(navigation.getName());
				List<Item> items = navigation.getItems();
				if (items != null) {
					for (int j = 0; j < items.size(); j++) {
						Item item = items.get(j);
						MoneyView moneyView = new MoneyView(this);
						moneyView.setText(item);
						LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						if (i != 0) {
							params.setMargins(0, 5, 0, 0);
						}
						layoutTitle2.addView(moneyView.getView(), params);
					}
				}
			} else if (i == 2) {
				txtTitle3.setText(navigation.getName());
				String products = navigation.getItemProducts();
				Log.d(TAG, "product " + products);
				String[] pdus = products.split(",");
				for (int k = 0; k < pdus.length; k++) {
					String pdu = pdus[k];
					Log.d(TAG, "pdu = " + pdu);
					if (pdu != null) {
						List<ModuleBizAttach> attachs = Share.getTypeDatas();
						if (attachs != null) {
							for (int j = 0; j < attachs.size(); j++) {
								ModuleBizAttach att = attachs.get(j);
								if (pdu.equals(String.valueOf(att.getId()))) {
									MtyView mtyView = new MtyView(this);
									mtyView.setText(att);
									mtyView.setOnMtyClickListener(this);
									LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
									if (k == 0) {
										params.setMargins(0, 0, 0, 0);
									} else {
										params.setMargins(20, 0, 0, 0);
									}
									layoutTitle3.addView(mtyView.getView(), params);
								}
							}
						}
					}
				}
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
			MoneyActivity.this.finish();
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

	public void onMtyChange(ModuleBizAttach mode) {
		Log.d(TAG, "onMtyChange mode is " + mode);
		if (mode != null) {
			if (mode.isModeWord()) {
				Share.setBizAttach(mode);
				startMyActivity(WordActivity.class);
			}
		}
	}

	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
	}

	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}
}

package com.apod.chair;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.apod.chair.adapter.OptionAdapter;
import com.apod.chair.adapter.SexAdapter;
import com.apod.chair.bo.Feedback;
import com.apod.chair.bo.Feedback.Option;
import com.apod.chair.http.WebResponse;
import com.apod.chair.manager.Config;
import com.apod.chair.util.Utils;

public class CustomActivity extends BaseActivity implements OnClickListener {
	private final static String TAG = Config.TAG + ".CustomActivity";
	private Button btnCommit;
	private Button btnHujiao, btnBack;
	private EditText editName, editPhone, editContent;
	private Spinner spinnerSex, spinnerOption;
	private SexAdapter sexAdapter;
	private OptionAdapter optionAdapter;
	private Feedback mCurrentFeedback;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom);
		findView();
		mApodManager.requestFeedback();
	}

	private void findView() {
		btnCommit = (Button) findViewById(R.id.btnCommit);
		btnCommit.setOnClickListener(this);

		btnHujiao = (Button) findViewById(R.id.btnHujiao);
		btnHujiao.setOnClickListener(this);

		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);

		editName = (EditText) findViewById(R.id.editName);
		editPhone = (EditText) findViewById(R.id.editPhone);
		editContent = (EditText) findViewById(R.id.editContent);

		sexAdapter = new SexAdapter(this);
		sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerSex = (Spinner) findViewById(R.id.spinnerSex);
		spinnerSex.setAdapter(sexAdapter);

		optionAdapter = new OptionAdapter(this);
		optionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerOption = (Spinner) findViewById(R.id.spinnerOption);
		spinnerOption.setAdapter(optionAdapter);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnCommit: {
			commitData();
			break;
		}
		case R.id.btnHujiao: {
			if (mApodManager != null) {
				mApodManager.execSendSms();
			}
			break;
		}
		case R.id.btnBack: {
			CustomActivity.this.finish();
			break;
		}
		}
	}

	public void onGetFeedback(WebResponse response, Feedback feedback) {
		Log.d(TAG, "onGetFeedback feedback = " + feedback);
		if (feedback == null) {
			return;
		}
		mCurrentFeedback = feedback;
		optionAdapter.setDatas(feedback.getOptions());
	}

	private void showToastSuccess() {
		Toast.makeText(this, getResources().getString(R.string.feedback_commit_success), Toast.LENGTH_LONG).show();
	}

	private void showToastFailer() {
		Toast.makeText(this, getResources().getString(R.string.feedback_commit_fail), Toast.LENGTH_LONG).show();
	}

	private void commitData() {
		try {
			Feedback feedback = new Feedback();
			String username = editName.getText().toString();
			Log.d(TAG, "commitData username =" + username);
			if (TextUtils.isEmpty(username)) {
				showToastFailer();
				return;
			}
			feedback.setName(username);

			String sex = sexAdapter.getItem(spinnerSex.getSelectedItemPosition());
			Log.d(TAG, "commitData sex =" + sex);
			if (TextUtils.isEmpty(sex)) {
				showToastFailer();
				return;
			}
			feedback.setSex(sex);

			String phone = editPhone.getText().toString();
			Log.d(TAG, "commitData phone =" + phone);
			if (TextUtils.isEmpty(phone)) {
				showToastFailer();
				return;
			}
			feedback.setPhone(phone);

			Option op = optionAdapter.getDatas().get(spinnerOption.getSelectedItemPosition());
			String option = op.getId();
			String opname = op.getName();
			Log.d(TAG, "commitData option =" + option + ",opname = " + opname);
			if (TextUtils.isEmpty(option)) {
				showToastFailer();
				return;
			}
			feedback.setItemName(opname);
			feedback.setItem(option);

			String content = editContent.getText().toString();
			if ("156".equals(option)) {
				feedback.setMsg(content);
			}

			int[] array = Utils.getDataTime();
			StringBuffer buf = new StringBuffer();
			buf.append(array[0]);
			buf.append("-");
			buf.append(array[1]);
			buf.append("-");
			buf.append(array[2]);
			buf.append(" ");
			buf.append(array[3]);
			buf.append(":");
			buf.append(array[4]);
			String time = buf.toString();
			Log.d(TAG, "commitData time =" + time);
			feedback.setTime(time);
			mDowloadManager.execCommitFeedback(feedback);
		} catch (Exception ex) {
			showToastFailer();
		}
	}

	public void onCommitedFeedback(Object obj) {
		Log.d(TAG, "onCommitedFeedback " + obj);
		if (obj != null) {
			showToastSuccess();
			finish();
		} else {
			showToastFailer();
		}
	}
}

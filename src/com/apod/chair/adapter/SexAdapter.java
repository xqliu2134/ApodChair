package com.apod.chair.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SexAdapter extends ArrayAdapter<String> {
	LayoutInflater inflater;
	private String[] datas = new String[] { "男", "女" };

	public SexAdapter(Context context) {
		super(context, android.R.layout.simple_spinner_item);
		inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return datas.length;
	}

	public String getItem(int position) {
		return datas[position];
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View contentView, ViewGroup arg2) {
		HoldView holdView = null;
		if (contentView == null) {
			holdView = new HoldView();
			contentView = inflater.inflate(android.R.layout.simple_spinner_item, null);
			holdView.text1 = (TextView) contentView.findViewById(android.R.id.text1);
			contentView.setTag(holdView);
		} else {
			holdView = (HoldView) contentView.getTag();
		}
		String info = datas[position];
		holdView.text1.setText(info);
		return contentView;
	}

	private class HoldView {
		public TextView text1;
	}

}

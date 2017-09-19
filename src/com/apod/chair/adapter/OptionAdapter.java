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

import com.apod.chair.bo.Feedback.Option;

public class OptionAdapter extends ArrayAdapter<String> {
	LayoutInflater inflater;
	private List<Option> datas = new ArrayList<Option>();

	public OptionAdapter(Context context) {
		super(context, android.R.layout.simple_spinner_item);
		inflater = LayoutInflater.from(context);
	}

	public List<Option> getDatas() {
		return datas;
	}

	public void setDatas(List<Option> options) {
		datas.clear();
		datas.addAll(options);
		notifyDataSetChanged();
	}

	public int getCount() {
		return datas.size();
	}

	public String getItem(int position) {
		return datas.get(position).getName();
	}

	public long getItemId(int position) {
		return position;
	}

	public int getSelectionByName(String str) {
		if (TextUtils.isEmpty(str)) {
			return 0;
		}
		for (int i = 0; i < datas.size(); i++) {
			if (str.equals(datas.get(i).getName())) {
				return i;
			}
		}
		return 0;
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
		Option info = datas.get(position);
		holdView.text1.setText(info.getName());
		return contentView;
	}

	private class HoldView {
		public TextView text1;
	}

}

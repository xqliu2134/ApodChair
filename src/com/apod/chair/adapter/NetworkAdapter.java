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

import com.apod.chair.bo.Network;

public class NetworkAdapter extends ArrayAdapter<String> {
	LayoutInflater inflater;
	private List<Network> datas = new ArrayList<Network>();

	public NetworkAdapter(Context context) {
		super(context, android.R.layout.simple_spinner_item);
		inflater = LayoutInflater.from(context);
	}

	public List<Network> getDatas() {
		return datas;
	}

	public void setDatas(List<Network> datas) {
		this.datas.addAll(datas);
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

	public int getSelectionById(String id) {
		if (TextUtils.isEmpty(id)) {
			return 0;
		}
		for (int i = 0; i < datas.size(); i++) {
			if (id.equals(datas.get(i).getId())) {
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
		Network info = datas.get(position);
		holdView.text1.setText(info.getName());
		return contentView;
	}

	private class HoldView {
		public TextView text1;
	}

}

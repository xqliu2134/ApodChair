package com.apod.chair.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apod.chair.R;
import com.apod.chair.bo.Business.BusinessMode;

public class ProductAdapter extends BaseAdapter {
	private List<BusinessMode> datas = new ArrayList<BusinessMode>();
	private LayoutInflater inflater;
	private boolean showImg;

	public ProductAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	public List<BusinessMode> getDatas() {
		return datas;
	}

	public void setDatas(List<BusinessMode> datas) {
		this.datas = datas;
		notifyDataSetChanged();
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		HoldView holdView = null;
		if (convertView == null) {
			holdView = new HoldView();
			convertView = inflater.inflate(R.layout.biz, null);
			holdView.txtPduTitle = (TextView) convertView.findViewById(R.id.txtPduTitle);
			holdView.imgPduIcon = (ImageView) convertView.findViewById(R.id.imgPduIcon);
			convertView.setTag(holdView);
		} else {
			holdView = (HoldView) convertView.getTag();
		}
		BusinessMode bm = datas.get(position);
		holdView.txtPduTitle.setText(bm.getBizName());
		return convertView;
	}

	private static class HoldView {
		public TextView txtPduTitle;
		public ImageView imgPduIcon;
	}
}
package com.apod.chair.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.apod.chair.R;
import com.apod.chair.bo.Business.BusinessLevels;

public class LevelView implements OnClickListener {
	private View parent;
	private ImageView btnIcon;
	private TextView btnTitle;
	private BusinessLevels businessLevels;

	private OnBusinessLevelsClickListener listener;

	public void setOnBusinessLevelsClickListener(OnBusinessLevelsClickListener listener) {
		this.listener = listener;
	}

	public LevelView(Context context) {
		parent = LayoutInflater.from(context).inflate(R.layout.pdu_btn, null);
		parent.setOnClickListener(this);
		btnIcon = (ImageView) parent.findViewById(R.id.btnIcon);
		btnIcon.setVisibility(View.GONE);
		btnTitle = (TextView) parent.findViewById(R.id.btnTitle);
	}

	public void setPduView(BusinessLevels bus) {
		businessLevels = bus;
		btnTitle.setText(bus.getName());
	}

	public View getView() {
		return parent;
	}

	public void onClick(View v) {
		if (listener != null) {
			listener.onBusinessLevelsClick(businessLevels);
		}
	}

	public interface OnBusinessLevelsClickListener {
		public void onBusinessLevelsClick(BusinessLevels businessLevels);
	}
}

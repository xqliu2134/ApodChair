package com.apod.chair.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.apod.chair.R;
import com.apod.chair.bo.Business.BusinessLevels.ModuleBizAttach;

public class AttachView implements OnClickListener {
	private View parent;
	private ImageView btnIcon;
	private TextView btnTitle;
	private ModuleBizAttach moduleBizAttach;

	private OnModuleBizAttachClickListener listener;

	public void setOnModuleBizAttachClickListener(OnModuleBizAttachClickListener listener) {
		this.listener = listener;
	}

	public AttachView(Context context) {
		parent = LayoutInflater.from(context).inflate(R.layout.pdu_btn, null);
		parent.setOnClickListener(this);
		btnIcon = (ImageView) parent.findViewById(R.id.btnIcon);
		btnIcon.setVisibility(View.GONE);
		btnTitle = (TextView) parent.findViewById(R.id.btnTitle);
	}

	public void setPduView(ModuleBizAttach bus) {
		moduleBizAttach = bus;
		btnTitle.setText(bus.getBizName());
	}

	public View getView() {
		return parent;
	}

	public void onClick(View v) {
		if (listener != null) {
			listener.onModuleBizAttachClickListenerClick(moduleBizAttach);
		}
	}

	public interface OnModuleBizAttachClickListener {
		public void onModuleBizAttachClickListenerClick(ModuleBizAttach moduleBizAttach);
	}
}

package com.apod.chair.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apod.chair.R;
import com.apod.chair.bo.Business.BusinessLevels.ModuleBizAttach;

public class MtyView implements OnClickListener {
	private LinearLayout parent;
	private TextView txtMty;
	private ModuleBizAttach attach;
	private OnMtyClickListener listener;

	public void setOnMtyClickListener(OnMtyClickListener listener) {
		this.listener = listener;
	}

	public MtyView(Context context) {
		parent = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.mty, null);
		parent.setOnClickListener(this);
		txtMty = (TextView) parent.findViewById(R.id.txtMty);
	}

	public void setText(ModuleBizAttach attach) {
		this.attach = attach;
		if (txtMty != null) {
			txtMty.setText(attach.getBizName());
		}
	}

	public View getView() {
		return parent;
	}

	public void onClick(View v) {
		if (listener != null) {
			listener.onMtyChange(attach);
		}
	}

	public interface OnMtyClickListener {
		public void onMtyChange(ModuleBizAttach mode);
	}
}

package com.apod.chair.view;

import com.apod.chair.bo.Business;
import com.apod.chair.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class PduView implements OnClickListener {
	private View parent;
	private ImageView btnIcon;
	private TextView btnTitle;
	private Business business;

	private OnBusinessClickListener listener;

	public void setOnBusinessClickListener(OnBusinessClickListener listener) {
		this.listener = listener;
	}

	public PduView(Context context) {
		parent = LayoutInflater.from(context).inflate(R.layout.pdu_btn, null);
		parent.setOnClickListener(this);
		btnIcon = (ImageView) parent.findViewById(R.id.btnIcon);
		btnTitle = (TextView) parent.findViewById(R.id.btnTitle);
	}

	public void setBusiness(Business bus) {
		business = bus;
	}

	public void refershPduView() {
		if (business == null) {
			return;
		}
		Bitmap bitmap = business.getDrawable();
		if (bitmap == null) {
			btnIcon.setImageResource(R.drawable.pdu_geren);
		} else {
			btnIcon.setImageBitmap(bitmap);
		}
		btnTitle.setText(business.getName());
	}

	public void setPduView(Business bus) {
		business = bus;
		Bitmap bitmap = business.getDrawable();
		if (bitmap == null) {
			btnIcon.setImageResource(R.drawable.pdu_geren);
		} else {
			btnIcon.setImageBitmap(bitmap);
		}
		btnTitle.setText(bus.getName());
	}

	public Business getBusiness() {
		return business;
	}

	public View getView() {
		return parent;
	}

	public void onClick(View v) {
		if (listener != null) {
			listener.onBusinessClick(business);
		}
	}

	public interface OnBusinessClickListener {
		public void onBusinessClick(Business business);
	}
}

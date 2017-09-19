package com.apod.chair.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apod.chair.ProductActivity;
import com.apod.chair.bo.Business.BusinessMode;
import com.apod.chair.util.Share;
import com.apod.chair.R;

public class BizView implements OnClickListener {
	private Context mContext;
	private View parent;
	private ImageView imgPduIcon;
	private Button imgBtn;
	private TextView txtPduTitle, bizName;
	private BusinessMode businessMode;
	private LinearLayout bizIconLayout;
	private OnBusinessModeClickListener listener;
	private ImageView bizShow;
	private int mSelect;

	public void setOnBusinessModeClickListener(OnBusinessModeClickListener listener) {
		this.listener = listener;
	}

	public BizView(Context context, TextView biz, ImageView show) {
		mContext = context;
		bizName = biz;
		bizShow = show;
		parent = LayoutInflater.from(context).inflate(R.layout.biz, null);
		imgPduIcon = (ImageView) parent.findViewById(R.id.imgPduIcon);
		txtPduTitle = (TextView) parent.findViewById(R.id.txtPduTitle);
		bizIconLayout = (LinearLayout) parent.findViewById(R.id.bizIconLayout);
		imgBtn = (Button) parent.findViewById(R.id.imgBtn);
		imgBtn.setOnClickListener(this);
	}

	public void setBusinessMode(BusinessMode mode) {
		this.businessMode = mode;
	}

	public BusinessMode getBusinessMode() {
		return this.businessMode;
	}

	public void refershBizView() {
		Log.d(ProductActivity.TAG, "refershBizView businessMode = " + businessMode);
		if (businessMode == null) {
			return;
		}
		txtPduTitle.setText(businessMode.getBizName());
		Bitmap bitmap = businessMode.getDrawable();
		Log.d(ProductActivity.TAG, "refershBizView bitmap = " + bitmap);
		if (bitmap != null) {
			imgPduIcon.setImageBitmap(bitmap);
		} else {
			imgPduIcon.setImageResource(R.drawable.default_grid_bg);
		}
		if (businessMode.getIndex() == mSelect) {
			if (bitmap != null) {
				bizShow.setImageBitmap(bitmap);
			} else {
				bizShow.setImageResource(R.drawable.defalut_show_bg);
			}
		}
	}

	public void setBizView(BusinessMode mode) {
		this.businessMode = mode;
		if (mode == null) {
			return;
		}
		txtPduTitle.setText(mode.getBizName());
		Bitmap bitmap = mode.getDrawable();
		Log.d("Apod", "bitmap = " + bitmap);
		if (bitmap != null) {
			imgPduIcon.setImageBitmap(bitmap);
		} else {
			bizShow.setImageBitmap(null);
		}
	}

	public void setSelect(int select) {
		mSelect = select;
		if (businessMode.getIndex() == select) {
			bizIconLayout.setBackgroundResource(R.drawable.biz_kuan_bg);
			Share.setBizAttach(businessMode.getBizAttach());
			bizName.setText(businessMode.getBizName());
			txtPduTitle.setTextColor(mContext.getResources().getColor(R.color.text_foucs));
			Bitmap bitmap = businessMode.getDrawable();
			if (bitmap != null) {
				bizShow.setImageBitmap(bitmap);
			} else {
				bizShow.setImageBitmap(null);
			}
		} else {
			bizIconLayout.setBackground(null);
			txtPduTitle.setTextColor(mContext.getResources().getColor(R.color.white));
		}
	}

	public View getView() {
		return parent;
	}

	public void onClick(View v) {
		if (listener != null) {
			listener.onBusinessModeChange(businessMode);
		}
	}

	public interface OnBusinessModeClickListener {
		public void onBusinessModeChange(BusinessMode mode);
	}
}

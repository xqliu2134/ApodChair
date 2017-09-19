package com.apod.chair.saver;

import android.content.Context;

import com.apod.chair.bo.Saver.SaverImg;
import com.apod.chair.http.DowloadRequest;
import com.apod.chair.http.DowloadRequest.OnDowloadRequestListener;
import com.apod.chair.http.Web;
import com.apod.chair.manager.Config;
import com.apod.chair.view.BizView;
import com.apod.chair.view.PduView;

public class SaverDowloadManager implements OnDowloadRequestListener {
	private final static String TAG = Config.TAG + ".DowloadManager";

	private final static int REQUEST_DOWLOAD_SAVEIMG = 3;// 下载屏保图片

	private static SaverDowloadManager mManager = null;
	private static DowloadRequest mRequest;

	private Context mContext;

	private OnDowloadDataChangeListener mListener;

	public void setOnDowloadDataChangeListener(OnDowloadDataChangeListener listener) {
		this.mListener = listener;
	}

	private SaverDowloadManager(Context context) {
		mContext = context;
		mRequest = new DowloadRequest();
		mRequest.setOnRequestListener(this);
	}

	public static SaverDowloadManager getInstance(Context context) {
		if (mManager == null) {
			mManager = new SaverDowloadManager(context);
		}
		return mManager;
	}

	public void execDowloadSaverImg(SaverImg saverImg) {
		mRequest.execute(REQUEST_DOWLOAD_SAVEIMG, saverImg);
	}

	public Object onRequest(int id, Object obj) {
		if (id == REQUEST_DOWLOAD_SAVEIMG) {
			return Web.executeDowloadSaverImg(obj);
		}

		return null;
	}

	public void onResponse(int id, Object obj) {
		if (id == REQUEST_DOWLOAD_SAVEIMG) {
			if (mListener != null) {
				if (obj != null && (obj instanceof SaverImg)) {
					mListener.onDowloadSaverImg((SaverImg) obj);
				} else {
					mListener.onDowloadSaverImg(null);
				}
			}
		}
	}

	public interface OnDowloadDataChangeListener {
		public void onDowloadSaverImg(SaverImg saverImg);
	}
}

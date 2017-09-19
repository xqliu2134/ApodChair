package com.apod.chair.manager;

import android.content.Context;

import com.apod.chair.bo.Feedback;
import com.apod.chair.bo.Saver;
import com.apod.chair.bo.Saver.SaverImg;
import com.apod.chair.http.DowloadRequest;
import com.apod.chair.http.DowloadRequest.OnDowloadRequestListener;
import com.apod.chair.http.Web;
import com.apod.chair.util.Share;
import com.apod.chair.view.BizView;
import com.apod.chair.view.PduView;

public class DowloadManager implements OnDowloadRequestListener {
	private final static String TAG = Config.TAG + ".DowloadManager";

	private final static int REQUEST_DOWLOAD_PDUIMG = 1;// 下载产品图标
	private final static int REQUEST_DOWLOAD_BIZIMG = 2;// 下载模块图标
	private final static int REQUEST_DOWLOAD_SAVEIMG = 3;// 下载屏保图片
	private final static int REQUEST_COMMIT_FEEDBACK = 4;// 提交客户反馈信息

	private static DowloadManager mManager = null;
	private static DowloadRequest mRequest;

	private Context mContext;

	private OnDowloadDataChangeListener mListener;

	public void setOnDowloadDataChangeListener(OnDowloadDataChangeListener listener) {
		this.mListener = listener;
	}

	private DowloadManager(Context context) {
		mContext = context;
		mRequest = new DowloadRequest();
		mRequest.setOnRequestListener(this);
	}

	public static DowloadManager getInstance(Context context) {
		if (mManager == null) {
			mManager = new DowloadManager(context);
		}
		return mManager;
	}

	public void execDowloadPduImg(PduView pduView) {
		mRequest.execute(REQUEST_DOWLOAD_PDUIMG, pduView);
	}

	public void execDowloadBizImg(BizView bizView) {
		mRequest.execute(REQUEST_DOWLOAD_BIZIMG, bizView);
	}

	public void execDowloadSaverImg(SaverImg saverImg) {
		mRequest.execute(REQUEST_DOWLOAD_SAVEIMG, saverImg);
	}

	public void execCommitFeedback(Feedback feedback) {
		mRequest.execute(REQUEST_COMMIT_FEEDBACK, feedback);
	}

	public Object onRequest(int id, Object obj) {
		if (REQUEST_DOWLOAD_PDUIMG == id) {
			return Web.executeDowloadPduImg(obj);
		} else if (id == REQUEST_DOWLOAD_BIZIMG) {
			return Web.executeDowloadBizImg(obj);
		} else if (id == REQUEST_DOWLOAD_SAVEIMG) {
			return Web.executeDowloadSaverImg(obj);
		} else if (id == REQUEST_COMMIT_FEEDBACK) {
			return Web.executeCommitFeedback(obj, Share.getDeviceCode());
		}

		return null;
	}

	public void onResponse(int id, Object obj) {
		if (REQUEST_DOWLOAD_PDUIMG == id) {
			if (mListener != null) {
				if (obj != null && (obj instanceof PduView)) {
					mListener.onDowloadPduImg((PduView) obj);
				}
			}
		} else if (id == REQUEST_DOWLOAD_BIZIMG) {
			if (mListener != null) {
				if (obj != null && (obj instanceof BizView)) {
					mListener.onDowloadBizImg((BizView) obj);
				}
			}
		} else if (id == REQUEST_DOWLOAD_SAVEIMG) {
			if (mListener != null) {
				if (obj != null && (obj instanceof SaverImg)) {
					mListener.onDowloadSaverImg((SaverImg) obj);
				} else {
					mListener.onDowloadSaverImg(null);
				}
			}
		} else if (id == REQUEST_COMMIT_FEEDBACK) {
			if (mListener != null) {
				if (obj != null) {
					mListener.onCommitedFeedback(obj);
				} else {
					mListener.onCommitedFeedback(null);
				}
			}
		}
	}

	public interface OnDowloadDataChangeListener {
		public void onDowloadPduImg(PduView pduView);

		public void onDowloadBizImg(BizView bizView);

		public void onDowloadSaverImg(SaverImg saverImg);

		public void onCommitedFeedback(Object obj);
	}
}

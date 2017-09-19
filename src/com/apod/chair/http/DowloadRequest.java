package com.apod.chair.http;

import android.os.AsyncTask;

import com.apod.chair.bo.Business;

/**
 * 异步访问服务器请求对象 每个执行操作在单独一个线程中 实现请求和返回两个方法
 * 
 * @author ease
 * 
 */
public class DowloadRequest {
	private OnDowloadRequestListener listener;

	public void execute(int id, Object obj) {
		new WebAsyncTask(id).execute(obj);
	}

	public class WebAsyncTask extends AsyncTask<Object, Void, Object> {

		private int id;

		public WebAsyncTask(int id) {
			this.id = id;
		}

		protected Object doInBackground(Object... params) {
			Object obj = params[0];
			if (listener != null) {
				obj = listener.onRequest(id, obj);
			}
			return obj;
		}

		protected void onPostExecute(Object result) {
			if (listener != null) {
				listener.onResponse(id, result);
			}
		}
	}

	public interface OnDowloadRequestListener {
		public Object onRequest(int id, Object obj);

		public void onResponse(int id, Object obj);
	}

	public void setOnRequestListener(OnDowloadRequestListener listener) {
		this.listener = listener;
	}
}

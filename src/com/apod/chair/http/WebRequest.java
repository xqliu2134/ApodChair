package com.apod.chair.http;

import android.os.AsyncTask;
import android.util.Log;

/**
 * 异步访问服务器请求对象 每个执行操作在单独一个线程中 实现请求和返回两个方法
 * 
 * @author ease
 * 
 */
public class WebRequest {
	public static String CUSTID_VALUE = "1";
	private OnRequestListener listener;

	public void execute(int id, boolean show) {
		int isShow = show ? 1 : 0;
		new WebAsyncTask(id).execute(isShow);
	}

	public class WebAsyncTask extends AsyncTask<Integer, Integer, WebResponse> {
		private int id;

		public WebAsyncTask(int id) {
			this.id = id;
		}

		protected WebResponse doInBackground(Integer... params) {
			WebResponse wb = null;
			if (listener != null) {
				boolean show = params[0] == 1 ? true : false;
				wb = listener.onRequest(id, show);
			}
			return wb;
		}

		protected void onPostExecute(WebResponse result) {
			if (listener != null) {
				listener.onResponse(id, result);
			}
		}
	}

	public interface OnRequestListener {
		public WebResponse onRequest(int id, boolean show);

		public void onResponse(int id, WebResponse response);
	}

	public void setOnRequestListener(OnRequestListener listener) {
		this.listener = listener;
	}
}

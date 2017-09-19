package com.apod.chair.bo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.apod.chair.manager.Config;
import com.apod.chair.util.Storage;

public class Saver {
	private final static String TAG = Config.TAG + ".Saver";

	public static String getWebUrl() {
		return Config.getWebHost() + "GetConfScreenSaver";
	}

	private int time;
	private String screenSaver;
	private List<SaverImg> imgs = new ArrayList<SaverImg>();

	public void setImgs(List<SaverImg> imgs) {
		this.imgs = imgs;
	}

	public List<SaverImg> getImgs() {
		return imgs;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getScreenSaver() {
		return screenSaver == null ? "" : screenSaver;
	}

	public void setScreenSaver(String screenSaver, boolean create) {
		this.screenSaver = screenSaver;
		if (screenSaver == null) {
			return;
		}
		imgs.clear();
		String[] str = screenSaver.split(",");
		for (String url : str) {
			SaverImg img = new SaverImg();
			img.setUrl(url, create);
			imgs.add(img);
		}
	}

	public class SaverImg {
		private String url;
		private String name;
		private String path;
		private Bitmap drawable;

		public Bitmap getDrawable() {
			return drawable;
		}

		public String getUrl() {
			return url == null ? "" : url;
		}

		public void setUrl(String url, boolean create) {
			this.url = url;
			if (url == null) {
				return;
			}
			int i = url.lastIndexOf("/");
			if (i > 0) {
				this.name = this.url.substring(i + 1);
			}
			this.path = Storage.getSaverPath() + name;
			if (create) {
				createDrawable();
			}
		}

		public void createDrawable() {
			try {
				File file = new File(this.path);
				if (!file.exists()) {
					Log.d(TAG, path + " file is not exists.");
					return;
				}
				InputStream is = new FileInputStream(file);
				drawable = BitmapFactory.decodeStream(is);
				Log.d(TAG, path + " bitmap create success.");
			} catch (Exception ex) {
				ex.printStackTrace();
				Log.d(TAG, path + " bitmap create failer.");
				drawable = null;
			}
		}

		public String getName() {
			return name == null ? "" : name;
		}

		public String getPath() {
			return path == null ? "" : path;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setPath(String path) {
			this.path = path;
		}

	}

	public class Field {
		public final static String WAIT_TIME = "wait_time";
		public final static String SCREEN_SAVER = "screen_saver";
	}

	public void jsonToObject(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			setTime(jsonObject.getInt(Field.WAIT_TIME));
			setScreenSaver(jsonObject.getString(Field.SCREEN_SAVER), false);
		} catch (JSONException e) {
		}
	}
}

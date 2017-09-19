package com.apod.chair.bo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.apod.chair.manager.Config;

public class Feedback {
	public static String getWebUrl() {
		return Config.getWebHost() + "GetConfFeedback";
	}

	public static String getWebCommitUrl() {
		return Config.getWebHost() + "SendFeedback";
	}

	private String option;
	private String mobiles;
	private String content;

	private String name;
	private String phone;
	private String item;
	private String sex;
	private String devicecode;
	private String time;
	private String msg;
	private List<Option> options = new ArrayList<Option>();
	private String itemName;
	private boolean result;

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public List<Option> getOptions() {
		return options;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getMobiles() {
		return mobiles;
	}

	public void setMobiles(String mobiles) {
		this.mobiles = mobiles;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getDevicecode() {
		return devicecode;
	}

	public void setDevicecode(String devicecode) {
		this.devicecode = devicecode;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public class Field {
		public final static String OPTION = "Option";
		public final static String MOBILES = "Mobiles";
		public final static String CONTENT = "Content";
		public final static String USERNAME = "username";
		public final static String SEX = "sex";
		public final static String MOBILE = "mobile";
		public final static String OPTIONS = "options";
		public final static String TIME = "time";
		public final static String MSG = "content";
	}

	public void jsonToObject(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			option = jsonObject.getString(Field.OPTION);
			convertOption(option);
			mobiles = jsonObject.getString(Field.MOBILES);
			content = jsonObject.getString(Field.CONTENT);
		} catch (JSONException e) {
		}
	}

	private void convertOption(String str) {
		try {
			options.clear();
			if (str == null || str.length() <= 0) {
				return;
			}
			str = str.replace("{", "");
			str = str.replace("}", "");
			Log.d("Apod", "str = " + str);
			String[] strs = str.split(",");
			if (strs == null || strs.length <= 0) {
				return;
			}
			for (String ops : strs) {
				String[] names = ops.split(":");
				if (names != null && names.length == 2) {
					Option option = new Option();
					option.setId(names[0].replace("\"", ""));
					option.setName(names[1].replace("\"", ""));
					options.add(option);
				}
			}
		} catch (Exception ex) {
		}
	}

	public class Option {
		private String id;
		private String name;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}

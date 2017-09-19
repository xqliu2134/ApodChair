package com.apod.chair.bo;

import org.json.JSONException;
import org.json.JSONObject;

import com.apod.chair.manager.Config;

public class MsgTemplate {
	public static String getWebUrl() {
		return Config.getWebHost() + "GetConfCall";
	}
	private int id;
	private String content;
	private String brancheName;
	private String deviceType;
	private String deviceName;
	private String mobile;
	private String msgContent;

	public String getMsgContent() {
		return msgContent;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return covertEmpty(content);
	}

	public void setContent(String content) {
		this.content = content;
		if (content == null || content.length() <= 0) {
			return;
		}
		content = content.replace("#DeviceCode#", getBrancheName());
		content = content.replace("#DeviceName#", getDeviceName());
		content = content.replace("#DeviceType#", getDeviceType());
		msgContent = content;
	}

	public String getBrancheName() {
		return covertEmpty(brancheName);
	}

	public void setBrancheName(String brancheName) {
		this.brancheName = brancheName;
	}

	public String getDeviceType() {
		return covertEmpty(deviceType);
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceName() {
		return covertEmpty(deviceName);
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getMobile() {
		return covertEmpty(mobile);
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
		if (this.mobile == null || this.mobile.length() == 0) {
			return;
		}
		try {
			this.mobile = this.mobile.substring(1, this.mobile.length() - 1);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public class Field {
		public final static String ID = "Id";
		public final static String CONTENT = "Content";
		public final static String BRANCHENAME = "BranchName";
		public final static String DEVICETYPE = "DeviceType";
		public final static String DEVICENAME = "DeviceName";
		public final static String MOBILE = "Mobile";
	}

	public void jsonToObject(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			setId(jsonObject.getInt(Field.ID));
			setBrancheName(jsonObject.getString(Field.BRANCHENAME));
			setDeviceType(jsonObject.getString(Field.DEVICETYPE));
			setDeviceName(jsonObject.getString(Field.DEVICENAME));
			setMobile(jsonObject.getString(Field.MOBILE));
			setContent(jsonObject.getString(Field.CONTENT));
		} catch (JSONException e) {
		}
	}

	private String covertEmpty(String str) {
		return str == null ? "" : str;
	}
}

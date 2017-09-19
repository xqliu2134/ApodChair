package com.apod.chair.bo;

import org.json.JSONException;
import org.json.JSONObject;

import com.apod.chair.manager.Config;

public class CheckIn {
	public static String getWebUrl() {
		return Config.getWebHost() + "CheckIn";
	}

	private String mac;
	private String deviceName;
	private String deviceCode;
	private String accessToken;

	public String getAccessToken() {
		return covertEmpty(accessToken);
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getDeviceCode() {
		return covertEmpty(deviceCode);
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getMac() {
		return covertEmpty(mac);
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getDeviceName() {
		return covertEmpty(deviceName);
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public class Field {
		public final static String ACCESS_TOKEN = "AccessToken";
		public final static String DEVICE_NAME = "DeviceName";
		public final static String DEVICE_CODE = "DeviceCode";
		public final static String MAC = "Mac";
	}

	public void jsonToObject(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			deviceCode = jsonObject.getString(Field.DEVICE_CODE);
			deviceName = jsonObject.getString(Field.DEVICE_NAME);
			accessToken = jsonObject.getString(Field.ACCESS_TOKEN);
		} catch (JSONException e) {
		}
	}

	private String covertEmpty(String str) {
		return str == null ? "" : str;
	}
}

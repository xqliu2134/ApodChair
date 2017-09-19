package com.apod.chair.bo;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.apod.chair.bo.Device.Field;
import com.apod.chair.manager.Config;

public class Sms {
	private String ipcode = "ZHWD";
	private String username = "zhwd";
	private String password = "zhwd123";
	private String ipaddress = "120.236.170.115";
	private String database = "mas";
	private String host = Config.getHost();

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getIpcode() {
		return ipcode;
	}

	public void setIpcode(String ipcode) {
		this.ipcode = ipcode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public static String jsonToObject(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			return jsonObject.getString("result");
		} catch (JSONException e) {
		}
		return null;
	}
}

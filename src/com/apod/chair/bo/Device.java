package com.apod.chair.bo;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.apod.chair.manager.Config;

public class Device {
	public static String getWebUrl() {
		return Config.getWebHost() + "GetDeviceTypes";
	}

	public class Field {
		public final static String ID = "DeviceTypeId";
		public final static String NAME = "DeviceTypeName";
	}

	private int id;
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return covertEmpty(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public static void jsonToObject(String json, List<Device> devices) {
		try {
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				Device device = new Device();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				device.setId(jsonObject.getInt(Field.ID));
				device.setName(jsonObject.getString(Field.NAME));
				devices.add(device);
			}
		} catch (JSONException e) {
		}
	}

	private String covertEmpty(String str) {
		return str == null ? "" : str;
	}
}

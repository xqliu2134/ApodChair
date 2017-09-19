package com.apod.chair.bo;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.apod.chair.manager.Config;

public class Network {
	public final static String WEB_URL = Config.getWebHost() + "GetBranches";

	public class Field {
		public final static String ID = "BranchId";
		public final static String NAME = "BranchName";
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
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static void jsonToObject(String json, List<Network> networks) {
		try {
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				Network network = new Network();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				network.setId(jsonObject.getInt(Field.ID));
				network.setName(jsonObject.getString(Field.NAME));
				networks.add(network);
			}
		} catch (JSONException e) {
		}
	}
}

package com.apod.chair.bo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.apod.chair.manager.Config;
import com.apod.chair.util.Share;
import com.apod.chair.util.Storage;

public class Business {
	private final static String TAG = Config.TAG + ".Business";

	public static String getWebUrl() {
		return Config.getWebHost() + "GetBusinessModule";
	}

	private int id;
	private int depth;
	private String name;
	private String imageurl;
	private String imgName;
	private Bitmap drawable;
	private String directory;
	private String doc;
	private List<BusinessMode> businessModes;
	private List<BusinessLevels> businessLevels;

	public List<BusinessLevels> getBusinessLevels() {
		return businessLevels;
	}

	public void setBusinessLevels(List<BusinessLevels> businessLevels) {
		this.businessLevels = businessLevels;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public String getDoc() {
		return doc == null ? "" : doc;
	}

	public String getDirectory() {
		return directory == null ? "" : directory;
	}

	public Bitmap getDrawable() {
		return drawable;
	}

	public void createDrawable(String path) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				Log.d(TAG, path + " file is not exists.");
				return;
			}
			InputStream is = new FileInputStream(file);
			drawable = BitmapFactory.decodeStream(is);
		} catch (OutOfMemoryError ome) {
			drawable = null;
		} catch (Exception ex) {
			ex.printStackTrace();
			drawable = null;
		}
	}

	public String getImgName() {
		return imgName == null ? "" : imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public void setBusinessModes(List<BusinessMode> businessModes) {
		this.businessModes = businessModes;
	}

	public List<BusinessMode> getBusinessModes() {
		return businessModes;
	}

	public String getImageurl() {
		return imageurl == null ? "" : imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
		if (imageurl == null) {
			return;
		}
		int i = this.imageurl.lastIndexOf("/");
		if (i > 0) {
			imgName = this.imageurl.substring(i + 1);
			if (imgName != null && imgName.length() > 0) {
				createDrawable(Storage.getImgPath() + imgName);
			}
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		directory = Storage.getRootDirectory() + Config.DIRECTORY_TAG + "" + id + File.separator;
		doc = directory + File.separator + "doc" + File.separator;
	}

	public String getName() {
		return name == null ? "" : name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public class Field {
		public final static String ID = "Id";
		public final static String NAME = "Name";
		public final static String IMGURL = "ImgUrl";
		public final static String BUSINESSMODULES = "BusinessModules";
		public final static String BUSINESSLEVELS = "BusinessLevels";
		public final static String DEPTH = "Depth";
	}

	public static void jsonToObject(String json, List<Business> business) {
		try {
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				Business busines = new Business();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				busines.setId(jsonObject.getInt(Field.ID));
				busines.setDepth(jsonObject.getInt(Field.DEPTH));
				busines.setName(jsonObject.getString(Field.NAME));
				busines.setImageurl(jsonObject.getString(Field.IMGURL));

				List<BusinessMode> businessModes = new ArrayList<BusinessMode>();
				BusinessMode.jsonToObject(busines, jsonObject.getJSONArray(Field.BUSINESSMODULES), businessModes);
				busines.setBusinessModes(businessModes);
				Log.d(TAG, "name is " + busines.getName() + ",size is " + businessModes.size());
				if (businessModes.size() == 0) {
					List<BusinessLevels> businessLevels = new ArrayList<BusinessLevels>();
					BusinessLevels.jsonToObject(busines, jsonObject.getJSONArray(Field.BUSINESSLEVELS), businessLevels);
					busines.setBusinessLevels(businessLevels);
				}

				business.add(busines);
			}
		} catch (JSONException e) {
		}
	}

	public static class BusinessMode {
		private final static String TAG = Config.TAG + ".Business.BusinessMode";
		private int id;
		private int levelId;
		private int index;
		private String bizName;
		private String bizImageUrl;
		private BizAttach bizAttach;;
		private Bitmap drawable;
		private String imgName;
		private Business business;

		public Business getBusiness() {
			return business;
		}

		public void setBusiness(Business business) {
			this.business = business;
		}

		public String getImgName() {
			return imgName == null ? "" : imgName;
		}

		public void setImgName(String imgName) {
			this.imgName = imgName;
		}

		public Bitmap getDrawable() {
			return drawable;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public void setBizAttach(BizAttach bizAttach) {
			this.bizAttach = bizAttach;
		}

		public BizAttach getBizAttach() {
			return bizAttach;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getLevelId() {
			return levelId;
		}

		public void setLevelId(int levelId) {
			this.levelId = levelId;
		}

		public String getBizName() {
			return bizName == null ? "" : bizName;
		}

		public void setBizName(String bizName) {
			this.bizName = bizName;
		}

		public String getBizImageUrl() {
			return bizImageUrl == null ? "" : bizImageUrl;
		}

		public void createDrawable(String path) {
			try {
				File file = new File(path);
				if (!file.exists()) {
					Log.d(TAG, path + " file is not exists.");
					return;
				}
				InputStream is = new FileInputStream(file);
				drawable = BitmapFactory.decodeStream(is);
			} catch (OutOfMemoryError ome) {
				drawable = null;
			} catch (Exception ex) {
				ex.printStackTrace();
				drawable = null;
			}
		}

		public void setBizImageUrl(String bizImageUrl) {
			this.bizImageUrl = bizImageUrl;
			if (bizImageUrl == null || business == null) {
				return;
			}
			int i = this.bizImageUrl.lastIndexOf("/");
			if (i > 0) {
				imgName = this.bizImageUrl.substring(i + 1);
				if (imgName != null && imgName.length() > 0) {
					createDrawable(business.getDirectory() + File.separator + imgName);
				}
			}
		}

		public class Field {
			public final static String ID = "Id";
			public final static String BIZNAME = "BizName";
			public final static String INDEX = "Index";
			public final static String BIZIMGURL = "BizImgUrl";
			public final static String BIZATTACH = "BizAttach";
		}

		public static void jsonToObject(Business busines, JSONArray jsonArray, List<BusinessMode> businessModes) {
			try {
				int index = 0;
				for (int i = 0; i < jsonArray.length(); i++, index++) {
					BusinessMode businessMode = new BusinessMode();
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					businessMode.setBusiness(busines);
					businessMode.setId(jsonObject.getInt(Field.ID));
					businessMode.setIndex(index);
					businessMode.setBizName(jsonObject.getString(Field.BIZNAME));
					businessMode.setBizImageUrl(jsonObject.getString(Field.BIZIMGURL));

					BizAttach bizAttach = new BizAttach();
					bizAttach.setBizName(businessMode.getBizName());
					bizAttach.setBusiness(busines);
					bizAttach.jsonToObject(jsonObject.getString(Field.BIZATTACH));
					businessMode.setBizAttach(bizAttach);
					businessModes.add(businessMode);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public static class BizAttach {
		private int id;
		private String fileName;
		private String filePath;
		private Business business;
		private String wordDir;
		private String bizName;

		public String getBizName() {
			return bizName;
		}

		public void setBizName(String bizName) {
			this.bizName = bizName;
		}

		public String getWordDir() {
			if (business != null) {
				wordDir = business.getDoc();
			}
			return wordDir;
		}

		public Business getBusiness() {
			return business;
		}

		public void setBusiness(Business business) {
			this.business = business;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getFileName() {
			return fileName == null ? "" : fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFilePath() {
			return filePath == null ? "" : filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

		public class Field {
			public final static String ID = "Id";
			public final static String FILENAME = "FileName";
			public final static String FILEPATH = "FilePath";
		}

		public void jsonToObject(String json) {
			try {
				JSONArray jsonArray = new JSONArray(json);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					id = jsonObject.getInt(Field.ID);
					fileName = jsonObject.getString(Field.FILENAME);
					filePath = jsonObject.getString(Field.FILEPATH);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public static class BusinessLevels {
		private final static String TAG = Config.TAG + ".Business.BusinessLevel";
		private int id;
		private String name;
		private List<ModuleBizAttach> moduleBizAttachs;
		private List<BusinessMode> businessMode;
		private Business business;

		public List<BusinessMode> getBusinessMode() {
			return businessMode;
		}

		public void setBusinessMode(List<BusinessMode> businessMode) {
			this.businessMode = businessMode;
		}

		public Business getBusiness() {
			return business;
		}

		public void setBusiness(Business business) {
			this.business = business;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name == null ? "" : name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<ModuleBizAttach> getModuleBizAttach() {
			return moduleBizAttachs;
		}

		public void setModuleBizAttach(List<ModuleBizAttach> moduleBizAttachs) {
			this.moduleBizAttachs = moduleBizAttachs;
		}

		public class Field {
			public final static String ID = "Id";
			public final static String NAME = "Name";
			public final static String BUSINESSMODULES = "BusinessModules";
			public final static String BUSINESSLEVELS = "BusinessLevels";
			public final static String BIZNAME = "BizName";
			public final static String FILENAME = "FileName";
			public final static String FILEPATH = "FilePath";
			public final static String BIZATTACH = "BizAttach";
			public final static String DEPTH = "Depth";
			public final static String TYPE = "Type";
			public final static String ITEMVALUE = "itemValue";
			public final static String ITEMNAME = "itemName";
			public final static String PRODUCTS = "Products";
		}

		public static void jsonToObject(Business busines, JSONArray jsonArray, List<BusinessLevels> businessLevels) {
			try {
				for (int i = 0; i < jsonArray.length(); i++) {
					BusinessLevels level = new BusinessLevels();
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					level.setBusiness(busines);
					level.setId(jsonObject.getInt(Field.ID));
					level.setName(jsonObject.getString(Field.NAME));
					String bname = busines.getName();
					Log.d(TAG, "BusinessLevels name is " + level.getName() + ",business name is " + bname);
					if ("财富管理中心".equals(bname)) {
						List<ModuleBizAttach> modes = new ArrayList<ModuleBizAttach>();
						ModuleBizAttach.jsonModeToObject(busines, jsonObject.getJSONArray(Field.BUSINESSMODULES), modes);
						if (modes.size() == 0) {
							ModuleBizAttach.jsonToObject(busines, jsonObject.getJSONArray(Field.BUSINESSLEVELS), modes);
						}

						// 如果是产品推荐，先将该数据保存在共享类中，以便后续调用(风险评估)
						if ("产品推荐".equals(level.getName())) {
							Log.d(TAG, "set type datas.");
							Share.setTypeDatas(modes);
						}
						level.setModuleBizAttach(modes);
					} else {
						List<BusinessMode> modes = new ArrayList<BusinessMode>();
						BusinessMode.jsonToObject(busines, jsonObject.getJSONArray(Field.BUSINESSMODULES), modes);
						level.setBusinessMode(modes);
					}
					businessLevels.add(level);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		public static class ModuleBizAttach extends BizAttach {
			private final static int MODE_WORD = 1;
			private final static int MODE_TEXT = 2;
			private int type = MODE_WORD;
			private int depth;
			private List<Navigation> navigations;

			public List<Navigation> getNavigations() {
				return navigations;
			}

			public void setNavigations(List<Navigation> navigations) {
				this.navigations = navigations;
			}

			public int getDepth() {
				return depth;
			}

			public void setDepth(int depth) {
				this.depth = depth;
			}

			public boolean isModeWord() {
				return type == MODE_WORD;
			}

			public int getType() {
				return type;
			}

			public void setType(int type) {
				this.type = type;
			}

			public static void jsonModeToObject(Business busines, JSONArray jsonArray, List<ModuleBizAttach> modes) {
				try {
					for (int i = 0; i < jsonArray.length(); i++) {
						ModuleBizAttach mode = new ModuleBizAttach();
						mode.setBusiness(busines);
						mode.setType(MODE_WORD);
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						mode.setId(jsonObject.getInt(BusinessLevels.Field.ID));
						mode.setBizName(jsonObject.getString(BusinessLevels.Field.BIZNAME));

						JSONArray bizArray = jsonObject.getJSONArray(BusinessLevels.Field.BIZATTACH);
						for (int j = 0; j < bizArray.length(); j++) {
							JSONObject bizObject = bizArray.getJSONObject(j);
							mode.setFileName(bizObject.getString(BusinessLevels.Field.FILENAME));
							mode.setFilePath(bizObject.getString(BusinessLevels.Field.FILEPATH));
						}
						Log.d(TAG, "ModuleBizAttach name is " + mode.getBizName());

						modes.add(mode);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			public static void jsonToObject(Business busines, JSONArray jsonArray, List<ModuleBizAttach> modes) {
				try {
					for (int i = 0; i < jsonArray.length(); i++) {
						ModuleBizAttach mode = new ModuleBizAttach();
						mode.setBusiness(busines);
						JSONObject jsonObject = jsonArray.getJSONObject(i);

						mode.setId(jsonObject.getInt(BusinessLevels.Field.ID));
						mode.setBizName(jsonObject.getString(BusinessLevels.Field.NAME));
						mode.setDepth(jsonObject.getInt(BusinessLevels.Field.DEPTH));

						Log.d(TAG, "ModuleBizAttach name is " + mode.getBizName());
						jsonModeToObject(jsonObject, mode, mode.getDepth());

						modes.add(mode);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			public static void jsonXunToObject(JSONArray jsonArray, ModuleBizAttach mode, int depth) {
				try {
					if (depth == 3) {
						jsonNavigationToObject(jsonArray, mode);
					} else {
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							jsonModeToObject(jsonObject, mode, jsonObject.getInt(BusinessLevels.Field.DEPTH));
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			public static void jsonNavigationToObject(JSONArray jsonArray, ModuleBizAttach mode) {
				try {
					mode.setType(MODE_TEXT);
					List<Navigation> navs = new ArrayList<Navigation>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject naviObject = jsonArray.getJSONObject(i);
						Navigation nav = new Navigation();
						nav.setMode(mode);
						nav.setIndex(i);
						String name = naviObject.getString(BusinessLevels.Field.NAME);
						Log.d(TAG, "jsonXunToObject name is " + name);
						nav.setName(name);

						JSONArray itemArray = naviObject.getJSONArray(BusinessLevels.Field.BUSINESSLEVELS);
						if (itemArray.length() > 0) {
							List<Item> items = new ArrayList<Item>();
							Navigation.jsonBizToObject(itemArray, items);
							nav.setItems(items);
						} else {
							nav.setItemProducts(naviObject.getString(BusinessLevels.Field.PRODUCTS));
						}
						navs.add(nav);
					}
					mode.setNavigations(navs);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			private static void jsonBizToObject(JSONArray jsonArray, ModuleBizAttach mode) {
				try {
					for (int k = 0; k < jsonArray.length(); k++) {
						JSONObject modeObject = jsonArray.getJSONObject(k);
						JSONArray bizArray = modeObject.getJSONArray(BusinessLevels.Field.BIZATTACH);
						mode.setType(MODE_WORD);
						for (int j = 0; j < bizArray.length(); j++) {
							JSONObject bizObject = bizArray.getJSONObject(j);
							mode.setFileName(bizObject.getString(BusinessLevels.Field.FILENAME));
							mode.setFilePath(bizObject.getString(BusinessLevels.Field.FILEPATH));
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			private static void jsonModeToObject(JSONObject jsonObject, ModuleBizAttach mode, int depth) {
				try {
					JSONArray modeArray = jsonObject.getJSONArray(BusinessLevels.Field.BUSINESSMODULES);
					if (modeArray.length() > 0) {
						jsonBizToObject(modeArray, mode);
					} else if (depth <= 3) {
						JSONArray levelArray = jsonObject.getJSONArray(BusinessLevels.Field.BUSINESSLEVELS);
						jsonXunToObject(levelArray, mode, depth);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		public static class Navigation {
			private String name;
			private List<Item> items = new ArrayList<Item>();
			private int index;
			private ModuleBizAttach mode;
			private String itemProducts;

			public String getItemProducts() {
				return itemProducts == null ? "" : itemProducts;
			}

			public void setItemProducts(String itemProducts) {
				this.itemProducts = itemProducts;
			}

			public ModuleBizAttach getMode() {
				return mode;
			}

			public void setMode(ModuleBizAttach mode) {
				this.mode = mode;
			}

			public int getIndex() {
				return index;
			}

			public void setIndex(int index) {
				this.index = index;
			}

			public List<Item> getItems() {
				return items;
			}

			public void setItems(List<Item> items) {
				this.items = items;
			}

			public String getName() {
				return name == null ? "" : name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public static void jsonBizToObject(JSONArray jsonArray, List<Item> items) {
				try {
					for (int k = 0; k < jsonArray.length(); k++) {
						JSONObject itemObject = jsonArray.getJSONObject(k);
						Item item = new Item();
						item.setItemName(itemObject.getString(BusinessLevels.Field.NAME));
						JSONArray levelArray = itemObject.getJSONArray(BusinessLevels.Field.BUSINESSLEVELS);
						for (int j = 0; j < levelArray.length(); j++) {
							JSONObject levelObject = levelArray.getJSONObject(j);
							item.setItemValue(levelObject.getString(BusinessLevels.Field.NAME));
						}
						items.add(item);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		public static class Item {
			private String itemName;
			private String itemValue;

			public String getItemName() {
				return itemName == null ? "" : itemName;
			}

			public void setItemName(String itemName) {
				this.itemName = itemName;
			}

			public String getItemValue() {
				return itemValue == null ? "" : itemValue;
			}

			public void setItemValue(String itemValue) {
				this.itemValue = itemValue;
			}
		}
	}

}

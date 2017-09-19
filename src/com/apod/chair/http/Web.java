package com.apod.chair.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.apod.chair.bo.Sms;
import android.util.Log;

import com.apod.chair.bo.Business;
import com.apod.chair.bo.Business.BizAttach;
import com.apod.chair.bo.Business.BusinessMode;
import com.apod.chair.bo.CheckIn;
import com.apod.chair.bo.Device;
import com.apod.chair.bo.Feedback;
import com.apod.chair.bo.MsgTemplate;
import com.apod.chair.bo.Network;
import com.apod.chair.bo.Saver;
import com.apod.chair.bo.Saver.SaverImg;
import com.apod.chair.bo.Word;
import com.apod.chair.manager.Config;
import com.apod.chair.util.Storage;
import com.apod.chair.view.BizView;
import com.apod.chair.view.PduView;

/**
 * Http请求服务器方法类
 * 
 * @author ease
 * 
 */
public class Web {
	private final static String TAG = Config.TAG + ".web";

	private static boolean executeDowload(String urlStr, String name, String path, boolean sync) {
		OutputStream output = null;
		InputStream input = null;
		try {
			Log.d(TAG, "executeDowload urlStr is " + urlStr);
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			input = conn.getInputStream();
			String filename = path + name;
			Log.d(TAG, "filename is " + filename);
			File file = new File(filename);
			if (file.exists()) {
				Log.d(TAG, "file is exists the sync is " + sync);
				if (sync) {
					file.delete();
				} else {
					return true;
				}
			}
			if (!file.createNewFile()) {
				Log.d(TAG, "file create fail,so return.");
				return false;
			}
			output = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = input.read(buffer)) != -1) {
				output.write(buffer, 0, count);
			}
		} catch (Exception ex) {
			Log.d(TAG, "file dowload is failer.");
			return false;
		} finally {
			try {
				if (output != null) {
					output.flush();
					output.close();
				}
				if (input != null) {
					input.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	private static String execute(String url, List<NameValuePair> params) {
		HttpPost request = new HttpPost(url);
		// 设置消息头请求内容编码格式
		request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		try {
			Log.d(TAG, "execute url is " + url);
			// 设置访问参数
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			// 设置请求参数
			HttpParams httpParams = new BasicHttpParams();
			// 设置连接超时时间
			HttpConnectionParams.setConnectionTimeout(httpParams, 20 * 1000);
			HttpConnectionParams.setSoTimeout(httpParams, 20 * 1000);
			// 执行请求操作
			HttpResponse response = new DefaultHttpClient(httpParams).execute(request);
			// 获取请求返回对象
			HttpEntity resultEntity = response.getEntity();
			// 获取请求返回内容
			String result = EntityUtils.toString(resultEntity, HTTP.UTF_8);
			Log.d(TAG, "result = " + result);
			return result;
		} catch (Exception e) {
			Log.d(TAG, "request http failer.");
		}
		return null;
	}

	public static List<Network> executeGetNetworks() {
		List<Network> networks = new ArrayList<Network>();
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Config.ACCESS_TOKEN_NAME, Config.ACCESS_TOKEN_VALUE));
			String json = Web.execute(Network.WEB_URL, params);
			if (json != null) {
				Network.jsonToObject(json, networks);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return networks;
	}

	public static Saver executeGetSavers(String deviceCode) {
		Saver saver = new Saver();
		if (deviceCode == null) {
			return saver;
		}
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Config.ACCESS_TOKEN_NAME, Config.ACCESS_TOKEN_VALUE));
			params.add(new BasicNameValuePair(Config.ACCESS_DEVICE_CODE, deviceCode));
			String json = Web.execute(Saver.getWebUrl(), params);
			if (json != null) {
				saver.jsonToObject(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return saver;
	}

	public static List<Device> executeGetDeviceTypes() {
		List<Device> devices = new ArrayList<Device>();
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Config.ACCESS_TOKEN_NAME, Config.ACCESS_TOKEN_VALUE));
			String json = Web.execute(Device.getWebUrl(), params);
			if (json != null) {
				Device.jsonToObject(json, devices);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return devices;
	}

	public static CheckIn executeCheckInDevice(String mac) {
		Log.d(TAG, "executeCheckInDevice mac = " + mac);
		CheckIn checkin = new CheckIn();
		checkin.setMac(mac);
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(CheckIn.Field.MAC, String.valueOf(checkin.getMac())));
			String json = Web.execute(CheckIn.getWebUrl(), params);
			if (json != null) {
				checkin.jsonToObject(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return checkin;
	}

	public static MsgTemplate executeGetMsgTemplate(String deviceCode) {
		MsgTemplate msgTemplate = new MsgTemplate();
		if (deviceCode == null) {
			return msgTemplate;
		}
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Config.ACCESS_TOKEN_NAME, Config.ACCESS_TOKEN_VALUE));
			params.add(new BasicNameValuePair(Config.ACCESS_DEVICE_CODE, deviceCode));
			String json = Web.execute(MsgTemplate.getWebUrl(), params);
			if (json != null) {
				msgTemplate.jsonToObject(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgTemplate;
	}

	public static Feedback executeGetFeedback(String deviceCode) {
		Feedback feedback = new Feedback();
		if (deviceCode == null) {
			return feedback;
		}
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Config.ACCESS_TOKEN_NAME, Config.ACCESS_TOKEN_VALUE));
			params.add(new BasicNameValuePair(Config.ACCESS_DEVICE_CODE, deviceCode));
			String json = Web.execute(Feedback.getWebUrl(), params);
			if (json != null) {
				feedback.jsonToObject(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return feedback;
	}

	public static Object executeCommitFeedback(Object obj, String deviceCode) {
		if (obj == null) {
			return obj;
		}
		Feedback feedback = (Feedback) obj;
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Config.ACCESS_TOKEN_NAME, Config.ACCESS_TOKEN_VALUE));
			params.add(new BasicNameValuePair(Config.ACCESS_DEVICE_CODE, deviceCode));
			params.add(new BasicNameValuePair(Feedback.Field.USERNAME, feedback.getName()));
			params.add(new BasicNameValuePair(Feedback.Field.SEX, feedback.getSex()));
			params.add(new BasicNameValuePair(Feedback.Field.MOBILE, feedback.getPhone()));
			params.add(new BasicNameValuePair(Feedback.Field.OPTIONS, feedback.getItem()));
			params.add(new BasicNameValuePair(Feedback.Field.TIME, feedback.getTime()));
			params.add(new BasicNameValuePair(Feedback.Field.MSG, feedback.getMsg()));
			String json = Web.execute(Feedback.getWebCommitUrl(), params);
			if (json != null) {
				feedback.setResult(true);
				return feedback;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static List<Business> executeGetBusiness(String deviceCode) {
		List<Business> business = new ArrayList<Business>();
		if (deviceCode == null) {
			return business;
		}
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Config.ACCESS_TOKEN_NAME, Config.ACCESS_TOKEN_VALUE));
			params.add(new BasicNameValuePair(Config.ACCESS_DEVICE_CODE, deviceCode));
			String json = Web.execute(Business.getWebUrl(), params);
			if (json != null) {
				Business.jsonToObject(json, business);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return business;
	}

	public static PduView executeDowloadPduImg(Object obj) {
		if (obj == null || !(obj instanceof PduView)) {
			return null;
		}
		PduView pduView = (PduView) obj;
		Business business = pduView.getBusiness();
		if (business == null) {
			return null;
		}
		String imageUrl = business.getImageurl();
		if (imageUrl == null || imageUrl.length() == 0) {
			return null;
		}
		boolean state = executeDowload(Config.getHost() + imageUrl, business.getImgName(), Storage.getImgPath(), true);
		Log.d(TAG, "executeDowloadPduImg state is " + state);
		if (state) {
			business.createDrawable(Storage.getImgPath() + business.getImgName());
			pduView.setBusiness(business);
		}
		return pduView;
	}

	public static BizView executeDowloadBizImg(Object obj) {
		if (obj == null || !(obj instanceof BizView)) {
			return null;
		}
		BizView bizView = (BizView) obj;
		BusinessMode mode = bizView.getBusinessMode();
		if (mode == null) {
			return null;
		}
		String imageUrl = mode.getBizImageUrl();
		if (imageUrl == null || imageUrl.length() == 0) {
			return null;
		}
		Business business = bizView.getBusinessMode().getBusiness();
		if (business == null) {
			return null;
		}
		String directory = business.getDirectory();
		if (directory == null || directory.length() == 0) {
			return null;
		}
		boolean state = executeDowload(Config.getHost() + imageUrl, mode.getImgName(), directory, true);
		Log.d(TAG, "executeDowloadBizImg state is " + state);
		if (state) {
			mode.createDrawable(directory + mode.getImgName());
			bizView.setBusinessMode(mode);
		}
		return bizView;
	}

	public static boolean executeDowloadDocx(Word word) {
		if (word == null) {
			return false;
		}
		BizAttach bizAttach = (BizAttach) word.getBizAttach();
		if (bizAttach == null) {
			return false;
		}
		String url = bizAttach.getFilePath();
		if (url == null || url.length() == 0) {
			return false;
		}
		String doc = bizAttach.getWordDir();
		if (doc == null || doc.length() == 0) {
			return false;
		}
		String filename = bizAttach.getFileName();
		if (filename == null || filename.length() == 0) {
			return false;
		}
		return executeDowload(Config.getHost() + url, filename, doc, true);
	}

	public static SaverImg executeDowloadSaverImg(Object obj) {
		if (obj == null || !(obj instanceof SaverImg)) {
			return null;
		}
		SaverImg saverImg = (SaverImg) obj;
		String imageUrl = saverImg.getUrl();
		if (imageUrl == null) {
			return null;
		}
		boolean state = executeDowload(Config.getHost() + imageUrl, saverImg.getName(), Storage.getSaverPath(), true);
		if (state) {
			saverImg.createDrawable();
			return saverImg;
		}
		return null;
	}

	public static boolean executeSendSms(String deviceCode) {
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Config.ACCESS_TOKEN_NAME, Config.ACCESS_TOKEN_VALUE));
			params.add(new BasicNameValuePair(Config.ACCESS_DEVICE_CODE, deviceCode));
			String result = Sms.jsonToObject(Web.execute(Config.getWebHost() + "CallDuty", params));
			if ("true".equalsIgnoreCase(result)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void executeHeartbeat(String deviceCode) {
		if (deviceCode == null) {
			return;
		}
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Config.ACCESS_TOKEN_NAME, Config.ACCESS_TOKEN_VALUE));
			params.add(new BasicNameValuePair(Config.ACCESS_DEVICE_CODE, deviceCode));
			Web.execute(Config.getWebHost() + "Heartbeat", params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package com.apod.chair.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;

import com.apod.chair.bo.Business;
import com.apod.chair.bo.Business.BizAttach;
import com.apod.chair.bo.Business.BusinessLevels;
import com.apod.chair.bo.Business.BusinessLevels.Item;
import com.apod.chair.bo.Business.BusinessLevels.ModuleBizAttach;
import com.apod.chair.bo.Business.BusinessLevels.Navigation;
import com.apod.chair.bo.Business.BusinessMode;
import com.apod.chair.bo.CheckIn;
import com.apod.chair.bo.MsgTemplate;
import com.apod.chair.bo.Saver;
import com.apod.chair.http.WebResponse;
import com.apod.chair.manager.Config;

public class XmlUtils {
	private final static String TAG = Config.TAG + ".XmlUtils";

	public static void save(String path, WebResponse response) {
		Log.d(TAG, "save file " + path);
		BufferedOutputStream out = null;
		try {
			File file = new File(path);
			if (file.exists()) {
				Log.d(TAG, path + " is exists,so delete it.");
				file.delete();
			}
			FileOutputStream fstr = new FileOutputStream(file);
			out = new BufferedOutputStream(fstr);
			XmlSerializer serializer = Xml.newSerializer();
			serializer.setOutput(out, "UTF-8");
			serializer.startDocument("UTF-8", true);
			serializer.startTag(null, "apod");

			CheckIn checkIn = response.getCheckin();
			if (checkIn != null) {
				String mac = checkIn.getMac();
				if (mac == null || mac.length() == 0) {
					Log.d(TAG, "save mac is null,so not save.");
					return;
				}
				serializer.startTag(null, "checkin");
				wriateValue(serializer, CheckIn.Field.DEVICE_NAME, checkIn.getDeviceName());
				wriateValue(serializer, CheckIn.Field.DEVICE_CODE, checkIn.getDeviceCode());
				wriateValue(serializer, CheckIn.Field.ACCESS_TOKEN, checkIn.getAccessToken());
				wriateValue(serializer, CheckIn.Field.MAC, mac);
				serializer.endTag(null, "checkin");
			}

			Saver saver = Share.getSaver();
			if (saver != null) {
				serializer.startTag(null, "saver");
				wriateValue(serializer, Saver.Field.SCREEN_SAVER, saver.getScreenSaver());
				serializer.endTag(null, "saver");
			}

			String host = Config.getHost();
			if (host != null && host.length() > 0) {
				serializer.startTag(null, "host");
				wriateValue(serializer, "value", host);
				serializer.endTag(null, "host");
			}

			List<Business> business = response.getBusiness();
			if (business != null) {
				for (int i = 0; i < business.size(); i++) {
					serializer.startTag(null, "business");
					Business bus = business.get(i);
					wriateValue(serializer, Business.Field.ID, String.valueOf(bus.getId()));
					wriateValue(serializer, Business.Field.IMGURL, bus.getImageurl());
					wriateValue(serializer, Business.Field.NAME, bus.getName());
					List<BusinessMode> bms = bus.getBusinessModes();
					if (bms != null) {
						for (int j = 0; j < bms.size(); j++) {
							serializer.startTag(null, "businessMode");
							BusinessMode bm = bms.get(j);
							wriateValue(serializer, Business.BusinessMode.Field.ID, String.valueOf(bm.getId()));
							wriateValue(serializer, Business.BusinessMode.Field.INDEX, String.valueOf(bm.getIndex()));
							wriateValue(serializer, Business.BusinessMode.Field.BIZNAME, bm.getBizName());
							wriateValue(serializer, Business.BusinessMode.Field.BIZIMGURL, bm.getBizImageUrl());
							BizAttach ba = bm.getBizAttach();
							if (ba != null) {
								serializer.startTag(null, "bizAttach");
								wriateValue(serializer, BizAttach.Field.ID, String.valueOf(ba.getId()));
								wriateValue(serializer, BizAttach.Field.FILENAME, ba.getFileName());
								wriateValue(serializer, BizAttach.Field.FILEPATH, ba.getFilePath());
								serializer.endTag(null, "bizAttach");
							}
							serializer.endTag(null, "businessMode");
						}
					}
					List<BusinessLevels> bls = bus.getBusinessLevels();
					if (bls != null) {
						for (int j = 0; j < bls.size(); j++) {
							serializer.startTag(null, "businessLevels");
							BusinessLevels bl = bls.get(j);
							wriateValue(serializer, Business.BusinessLevels.Field.ID, String.valueOf(bl.getId()));
							wriateValue(serializer, Business.BusinessLevels.Field.NAME, String.valueOf(bl.getName()));
							List<BusinessMode> modes = bl.getBusinessMode();
							if (modes != null && modes.size() > 0) {
								for (int h = 0; h < modes.size(); h++) {
									serializer.startTag(null, "businessMode");
									BusinessMode bm = modes.get(h);
									wriateValue(serializer, Business.BusinessMode.Field.ID, String.valueOf(bm.getId()));
									wriateValue(serializer, Business.BusinessMode.Field.INDEX, String.valueOf(bm.getIndex()));
									wriateValue(serializer, Business.BusinessMode.Field.BIZNAME, bm.getBizName());
									wriateValue(serializer, Business.BusinessMode.Field.BIZIMGURL, bm.getBizImageUrl());
									BizAttach ba = bm.getBizAttach();
									if (ba != null) {
										serializer.startTag(null, "bizAttach");
										wriateValue(serializer, BizAttach.Field.ID, String.valueOf(ba.getId()));
										wriateValue(serializer, BizAttach.Field.FILENAME, ba.getFileName());
										wriateValue(serializer, BizAttach.Field.FILEPATH, ba.getFilePath());
										serializer.endTag(null, "bizAttach");
									}
									serializer.endTag(null, "businessMode");
								}
							} else {
								List<ModuleBizAttach> mbas = bl.getModuleBizAttach();
								if (mbas != null) {
									for (int k = 0; k < mbas.size(); k++) {
										ModuleBizAttach mba = mbas.get(k);
										serializer.startTag(null, "moduleBizAttach");
										wriateValue(serializer, Business.BusinessLevels.Field.ID, String.valueOf(mba.getId()));
										wriateValue(serializer, Business.BusinessLevels.Field.BIZNAME, mba.getBizName());
										wriateValue(serializer, Business.BusinessLevels.Field.TYPE, String.valueOf(mba.getType()));
										wriateValue(serializer, Business.BusinessLevels.Field.FILENAME, mba.getFileName());
										wriateValue(serializer, Business.BusinessLevels.Field.FILEPATH, mba.getFilePath());

										List<Navigation> navs = mba.getNavigations();
										if (navs != null) {
											for (int h = 0; h < navs.size(); h++) {
												serializer.startTag(null, "navigation");
												Navigation nav = navs.get(h);
												wriateValue(serializer, Business.BusinessLevels.Field.NAME, nav.getName());
												wriateValue(serializer, Business.BusinessLevels.Field.PRODUCTS, nav.getItemProducts());
												wriateValue(serializer, Business.BusinessMode.Field.INDEX, String.valueOf(nav.getIndex()));
												List<Item> items = nav.getItems();
												if (items != null) {
													for (int b = 0; b < items.size(); b++) {
														serializer.startTag(null, "item");
														Item item = items.get(b);
														wriateValue(serializer, Business.BusinessLevels.Field.ITEMNAME, item.getItemName());
														wriateValue(serializer, Business.BusinessLevels.Field.ITEMVALUE, item.getItemValue());
														serializer.endTag(null, "item");
													}
												}
												serializer.endTag(null, "navigation");
											}
										}
										serializer.endTag(null, "moduleBizAttach");
									}
								}
							}
							serializer.endTag(null, "businessLevels");
						}
					}
					serializer.endTag(null, "business");
				}
			}

			serializer.endTag(null, "apod");
			serializer.endDocument();
			out.flush();
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void wriateValue(XmlSerializer serializer, String name, String value) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag(null, name);
		serializer.text(value);
		serializer.endTag(null, name);
	}

	public static void readSms(String path) {
		Log.d(TAG, "readSms file " + path);
		File file = new File(path);
		if (!file.exists()) {
			Log.d(TAG, "[" + path + "] is not exists,so return.");
			return;
		}
		InputStream in = null;
		try {
			in = new FileInputStream(path);
			// 1、获取PULL解析工厂实例对象
			DocumentBuilderFactory mDocumentBuilderFactory = DocumentBuilderFactory.newInstance();

			// 2、调用DocumentBuilderFactory中的newDocumentBuilder()方法创建文档对象构造器
			DocumentBuilder mDocumentBuilder = mDocumentBuilderFactory.newDocumentBuilder();

			// 3、将文件流解析成XML文档对象
			Document mDocument = mDocumentBuilder.parse(in);

			// 4、使用mDocument文档对象得到文档根节点
			Element mElement = mDocument.getDocumentElement();

			// 7、根据名称获取根节点中的Sms数据
			readSms(mElement);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static WebResponse read(String path, WebResponse response) {
		Log.d(TAG, "read file " + path);
		File file = new File(path);
		if (!file.exists()) {
			Log.d(TAG, "[" + path + "] is not exists,so return.");
			return response;
		}
		InputStream in = null;
		try {
			in = new FileInputStream(path);
			// 1、获取PULL解析工厂实例对象
			DocumentBuilderFactory mDocumentBuilderFactory = DocumentBuilderFactory.newInstance();

			// 2、调用DocumentBuilderFactory中的newDocumentBuilder()方法创建文档对象构造器
			DocumentBuilder mDocumentBuilder = mDocumentBuilderFactory.newDocumentBuilder();

			// 3、将文件流解析成XML文档对象
			Document mDocument = mDocumentBuilder.parse(in);

			// 4、使用mDocument文档对象得到文档根节点
			Element mElement = mDocument.getDocumentElement();

			// 5、根据名称获取根节点中的CheckIn数据
			CheckIn checkIn = readCheckIn(mElement);
			response.setCheckin(checkIn);

			// 6、根据名称获取根节点中的Saver数据
			Saver saver = readSaver(mElement);
			response.setSaver(saver);
			Share.setSaverImgs(saver.getImgs());

			// 7、根据名称获取根节点中的Business数据
			List<Business> business = readBusiness(mElement);
			response.setBusiness(business);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	private static List<Business> readBusiness(Element mElement) {
		NodeList mNodeList = mElement.getElementsByTagName("business");
		List<Business> business = new ArrayList<Business>();
		Log.d(TAG, "read business node size is " + mNodeList.getLength());
		for (int i = 0; i < mNodeList.getLength(); i++) {
			Business bus = new Business();
			Element businessElement = (Element) mNodeList.item(i);
			// 获取Business节点中的属性
			if (businessElement.hasChildNodes()) {
				// 获取Business节点的子节点列表
				NodeList mBusinessNodeList = businessElement.getChildNodes();
				// 遍历子节点列表并赋值
				for (int j = 0; j < mBusinessNodeList.getLength(); j++) {
					Node node = mBusinessNodeList.item(j);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						String name = node.getNodeName();
						Node firstNode = node.getFirstChild();
						if (name == null || firstNode == null) {
							continue;
						}
						Log.d(TAG, name + " = " + firstNode.getNodeValue());
						if (Business.Field.ID.equals(node.getNodeName())) {
							bus.setId(Integer.parseInt(node.getFirstChild().getNodeValue()));
						} else if (Business.Field.IMGURL.equals(node.getNodeName())) {
							bus.setImageurl(node.getFirstChild().getNodeValue());
						} else if (Business.Field.NAME.equals(node.getNodeName())) {
							bus.setName(node.getFirstChild().getNodeValue());
						}
					}
				}
				List<BusinessMode> businessModes = new ArrayList<Business.BusinessMode>();
				NodeList mNodeList2 = businessElement.getElementsByTagName("businessMode");
				Log.d(TAG, "read business mode node size is " + mNodeList2.getLength());
				for (int k = 0; k < mNodeList2.getLength(); k++) {
					BusinessMode mode = new BusinessMode();
					mode.setBusiness(bus);
					Element modeElement = (Element) mNodeList2.item(k);
					// 获取mode节点中的属性
					if (modeElement.hasChildNodes()) {
						// 获取mode节点的子节点列表
						NodeList mModeNodeList = modeElement.getChildNodes();
						// 遍历子节点列表并赋值
						for (int l = 0; l < mModeNodeList.getLength(); l++) {
							Node node = mModeNodeList.item(l);
							String name = node.getNodeName();
							Node firstNode = node.getFirstChild();
							if (name == null || firstNode == null) {
								continue;
							}
							Log.d(TAG, name + " = " + firstNode.getNodeValue());
							if (Business.BusinessMode.Field.ID.equals(node.getNodeName())) {
								mode.setId(Integer.parseInt(node.getFirstChild().getNodeValue()));
							} else if (Business.BusinessMode.Field.INDEX.equals(node.getNodeName())) {
								mode.setIndex(Integer.parseInt(node.getFirstChild().getNodeValue()));
							} else if (Business.BusinessMode.Field.BIZNAME.equals(node.getNodeName())) {
								mode.setBizName(node.getFirstChild().getNodeValue());
							} else if (Business.BusinessMode.Field.BIZIMGURL.equals(node.getNodeName())) {
								mode.setBizImageUrl(node.getFirstChild().getNodeValue());
							}
						}
					}

					NodeList mNodeList3 = modeElement.getElementsByTagName("bizAttach");
					Log.d(TAG, "read bizAttach node size is " + mNodeList3.getLength());
					for (int h = 0; h < mNodeList3.getLength(); h++) {
						BizAttach bizAttach = new BizAttach();
						bizAttach.setBusiness(bus);
						bizAttach.setBizName(mode.getBizName());
						Element bizAttachElement = (Element) mNodeList3.item(h);
						// 获取BizAttach节点中的属性
						if (bizAttachElement.hasChildNodes()) {
							// 获取bizAttach节点的子节点列表
							NodeList mBizAttachNodeList = bizAttachElement.getChildNodes();
							// 遍历子节点列表并赋值
							for (int g = 0; g < mBizAttachNodeList.getLength(); g++) {
								Node node = mBizAttachNodeList.item(g);
								String name = node.getNodeName();
								Node firstNode = node.getFirstChild();
								if (name == null || firstNode == null) {
									continue;
								}
								Log.d(TAG, name + " = " + firstNode.getNodeValue());
								if (BizAttach.Field.ID.equals(node.getNodeName())) {
									bizAttach.setId(Integer.parseInt(node.getFirstChild().getNodeValue()));
								} else if (BizAttach.Field.FILENAME.equals(node.getNodeName())) {
									bizAttach.setFileName(node.getFirstChild().getNodeValue());
								} else if (BizAttach.Field.FILEPATH.equals(node.getNodeName())) {
									bizAttach.setFilePath(node.getFirstChild().getNodeValue());
								}
							}
						}
						mode.setBizAttach(bizAttach);
					}
					businessModes.add(mode);
				}
				bus.setBusinessModes(businessModes);

				List<BusinessLevels> businessLevels = new ArrayList<BusinessLevels>();
				NodeList mNodeList3 = businessElement.getElementsByTagName("businessLevels");
				Log.d(TAG, "read business level node size is " + mNodeList3.getLength());
				for (int k = 0; k < mNodeList3.getLength(); k++) {
					BusinessLevels bls = new BusinessLevels();
					bls.setBusiness(bus);
					Element levelElement = (Element) mNodeList3.item(k);
					// 获取mode节点中的属性
					if (levelElement.hasChildNodes()) {
						// 获取mode节点的子节点列表
						NodeList mLevelNodeList = levelElement.getChildNodes();
						// 遍历子节点列表并赋值
						for (int l = 0; l < mLevelNodeList.getLength(); l++) {
							Node node = mLevelNodeList.item(l);
							String name = node.getNodeName();
							Node firstNode = node.getFirstChild();
							if (name == null || firstNode == null) {
								continue;
							}
							Log.d(TAG, name + " = " + firstNode.getNodeValue());
							if (Business.BusinessLevels.Field.ID.equals(node.getNodeName())) {
								bls.setId(Integer.parseInt(node.getFirstChild().getNodeValue()));
							} else if (Business.BusinessLevels.Field.NAME.equals(node.getNodeName())) {
								bls.setName(node.getFirstChild().getNodeValue());
							}
						}
					}

					NodeList mNodeList7 = levelElement.getElementsByTagName("businessMode");
					if (mNodeList7 != null && mNodeList7.getLength() > 0) {
						List<BusinessMode> modes = new ArrayList<Business.BusinessMode>();
						Log.d(TAG, "read levels mode node size is " + mNodeList7.getLength());
						for (int m = 0; m < mNodeList7.getLength(); m++) {
							BusinessMode mode = new BusinessMode();
							mode.setBusiness(bus);
							Element modeElement = (Element) mNodeList7.item(m);
							// 获取mode节点中的属性
							if (modeElement.hasChildNodes()) {
								// 获取mode节点的子节点列表
								NodeList mModeNodeList = modeElement.getChildNodes();
								// 遍历子节点列表并赋值
								for (int l = 0; l < mModeNodeList.getLength(); l++) {
									Node node = mModeNodeList.item(l);
									String name = node.getNodeName();
									Node firstNode = node.getFirstChild();
									if (name == null || firstNode == null) {
										continue;
									}
									Log.d(TAG, name + " = " + firstNode.getNodeValue());
									if (Business.BusinessMode.Field.ID.equals(node.getNodeName())) {
										mode.setId(Integer.parseInt(node.getFirstChild().getNodeValue()));
									} else if (Business.BusinessMode.Field.INDEX.equals(node.getNodeName())) {
										mode.setIndex(Integer.parseInt(node.getFirstChild().getNodeValue()));
									} else if (Business.BusinessMode.Field.BIZNAME.equals(node.getNodeName())) {
										mode.setBizName(node.getFirstChild().getNodeValue());
									} else if (Business.BusinessMode.Field.BIZIMGURL.equals(node.getNodeName())) {
										mode.setBizImageUrl(node.getFirstChild().getNodeValue());
									}
								}
							}

							NodeList mNodeList8 = modeElement.getElementsByTagName("bizAttach");
							Log.d(TAG, "read levels mode bizAttach node size is " + mNodeList8.getLength());
							for (int h = 0; h < mNodeList8.getLength(); h++) {
								BizAttach bizAttach = new BizAttach();
								bizAttach.setBusiness(bus);
								bizAttach.setBizName(mode.getBizName());
								Element bizAttachElement = (Element) mNodeList8.item(h);
								// 获取BizAttach节点中的属性
								if (bizAttachElement.hasChildNodes()) {
									// 获取bizAttach节点的子节点列表
									NodeList mBizAttachNodeList = bizAttachElement.getChildNodes();
									// 遍历子节点列表并赋值
									for (int g = 0; g < mBizAttachNodeList.getLength(); g++) {
										Node node = mBizAttachNodeList.item(g);
										String name = node.getNodeName();
										Node firstNode = node.getFirstChild();
										if (name == null || firstNode == null) {
											continue;
										}
										Log.d(TAG, name + " = " + firstNode.getNodeValue());
										if (BizAttach.Field.ID.equals(node.getNodeName())) {
											bizAttach.setId(Integer.parseInt(node.getFirstChild().getNodeValue()));
										} else if (BizAttach.Field.FILENAME.equals(node.getNodeName())) {
											bizAttach.setFileName(node.getFirstChild().getNodeValue());
										} else if (BizAttach.Field.FILEPATH.equals(node.getNodeName())) {
											bizAttach.setFilePath(node.getFirstChild().getNodeValue());
										}
									}
								}
								mode.setBizAttach(bizAttach);
							}
							modes.add(mode);
						}
						bls.setBusinessMode(modes);
					} else {
						List<ModuleBizAttach> bizAttachs = new ArrayList<ModuleBizAttach>();
						NodeList mNodeList4 = levelElement.getElementsByTagName("moduleBizAttach");
						Log.d(TAG, "read moduleBizAttach node size is " + mNodeList4.getLength());
						for (int h = 0; h < mNodeList4.getLength(); h++) {
							ModuleBizAttach bizAttach = new ModuleBizAttach();
							bizAttach.setBusiness(bus);
							Element bizAttachElement = (Element) mNodeList4.item(h);
							// 获取BizAttach节点中的属性
							if (bizAttachElement.hasChildNodes()) {
								// 获取bizAttach节点的子节点列表
								NodeList mBizAttachNodeList = bizAttachElement.getChildNodes();
								// 遍历子节点列表并赋值
								for (int g = 0; g < mBizAttachNodeList.getLength(); g++) {
									Node node = mBizAttachNodeList.item(g);
									String name = node.getNodeName();
									Node firstNode = node.getFirstChild();
									if (name == null || firstNode == null) {
										continue;
									}
									Log.d(TAG, name + " = " + firstNode.getNodeValue());
									if (Business.BusinessLevels.Field.ID.equals(node.getNodeName())) {
										bizAttach.setId(Integer.parseInt(node.getFirstChild().getNodeValue()));
									} else if (Business.BusinessLevels.Field.BIZNAME.equals(node.getNodeName())) {
										bizAttach.setBizName(node.getFirstChild().getNodeValue());
									} else if (Business.BusinessLevels.Field.TYPE.equals(node.getNodeName())) {
										bizAttach.setType(Integer.parseInt(node.getFirstChild().getNodeValue()));
									} else if (Business.BusinessLevels.Field.FILENAME.equals(node.getNodeName())) {
										bizAttach.setFileName(node.getFirstChild().getNodeValue());
									} else if (Business.BusinessLevels.Field.FILEPATH.equals(node.getNodeName())) {
										bizAttach.setFilePath(node.getFirstChild().getNodeValue());
									}
								}
							}

							List<Navigation> navigations = new ArrayList<Navigation>();
							NodeList mNodeList5 = bizAttachElement.getElementsByTagName("navigation");
							Log.d(TAG, "read navigation node size is " + mNodeList5.getLength());
							for (int j = 0; j < mNodeList5.getLength(); j++) {
								Navigation navigation = new Navigation();
								Element navigationElement = (Element) mNodeList5.item(j);
								// 获取Navigation节点中的属性
								if (navigationElement.hasChildNodes()) {
									// 获取Navigation节点的子节点列表
									NodeList navigationNodeList = navigationElement.getChildNodes();
									// 遍历子节点列表并赋值
									for (int g = 0; g < navigationNodeList.getLength(); g++) {
										Node node = navigationNodeList.item(g);
										String name = node.getNodeName();
										Node firstNode = node.getFirstChild();
										if (name == null || firstNode == null) {
											continue;
										}
										Log.d(TAG, name + " = " + firstNode.getNodeValue());
										if (Business.BusinessLevels.Field.NAME.equals(node.getNodeName())) {
											navigation.setName(node.getFirstChild().getNodeValue());
										} else if (Business.BusinessLevels.Field.PRODUCTS.equals(node.getNodeName())) {
											navigation.setItemProducts(node.getFirstChild().getNodeValue());
										} else if (Business.BusinessMode.Field.INDEX.equals(node.getNodeName())) {
											navigation.setIndex(Integer.parseInt(node.getFirstChild().getNodeValue()));
										}
									}
								}

								List<Item> items = new ArrayList<Item>();
								NodeList mNodeList6 = navigationElement.getElementsByTagName("item");
								for (int t = 0; t < mNodeList6.getLength(); t++) {
									Item item = new Item();
									Element itemElement = (Element) mNodeList6.item(t);
									// 获取Item节点中的属性
									if (itemElement.hasChildNodes()) {
										// 获取Item节点的子节点列表
										NodeList itemNodeList = itemElement.getChildNodes();
										// 遍历子节点列表并赋值
										for (int g = 0; g < itemNodeList.getLength(); g++) {
											Node node = itemNodeList.item(g);
											String name = node.getNodeName();
											Node firstNode = node.getFirstChild();
											if (name == null || firstNode == null) {
												continue;
											}
											Log.d(TAG, name + " = " + firstNode.getNodeValue());
											if (Business.BusinessLevels.Field.ITEMNAME.equals(node.getNodeName())) {
												item.setItemName(node.getFirstChild().getNodeValue());
											} else if (Business.BusinessLevels.Field.ITEMVALUE.equals(node.getNodeName())) {
												item.setItemValue(node.getFirstChild().getNodeValue());
											}
										}
									}
									items.add(item);
								}
								navigation.setItems(items);
								navigations.add(navigation);
							}
							bizAttach.setNavigations(navigations);
							bizAttachs.add(bizAttach);
						}
						// 如果是产品推荐，先将该数据保存在共享类中，以便后续调用(风险评估)
						if ("产品推荐".equals(bls.getName())) {
							Log.d(TAG, "set type datas.");
							Share.setTypeDatas(bizAttachs);
						}
						bls.setModuleBizAttach(bizAttachs);
					}
					businessLevels.add(bls);
				}
				bus.setBusinessLevels(businessLevels);
			}
			business.add(bus);
		}
		Log.d(TAG, "read business size is " + business.size());
		return business;
	}

	private static void readSms(Element mElement) {
		NodeList mNodeList = mElement.getElementsByTagName("host");
		Log.d(TAG, "read host node size is " + mNodeList.getLength());
		for (int i = 0; i < mNodeList.getLength() && i < 1; i++) {
			Element smsElement = (Element) mNodeList.item(i);
			// 获取person节点中的属性
			if (smsElement.hasChildNodes()) {
				// 获取person节点的子节点列表
				NodeList mNodeList2 = smsElement.getChildNodes();
				// 遍历子节点列表并赋值
				for (int j = 0; j < mNodeList2.getLength(); j++) {
					Node mNodeChild = mNodeList2.item(j);
					String name = mNodeChild.getNodeName();
					Node firstNode = mNodeChild.getFirstChild();
					if (name == null || firstNode == null) {
						continue;
					}
					Log.d(TAG, name + " = " + firstNode.getNodeValue());
					if (mNodeChild.getNodeType() == Node.ELEMENT_NODE) {
						if ("value".equals(mNodeChild.getNodeName())) {
							String host = mNodeChild.getFirstChild().getNodeValue();
							Config.setHost(host);
						}
					}
				}
			}
		}
	}

	private static MsgTemplate readMsgTemplate(Element mElement) {
		NodeList mNodeList = mElement.getElementsByTagName("msgtemplate");
		MsgTemplate msgTemplate = new MsgTemplate();
		Log.d(TAG, "read msgtemplate node size is " + mNodeList.getLength());
		for (int i = 0; i < mNodeList.getLength() && i < 1; i++) {
			Element msgtemplateElement = (Element) mNodeList.item(i);
			// 获取person节点中的属性
			if (msgtemplateElement.hasChildNodes()) {
				// 获取person节点的子节点列表
				NodeList mNodeList2 = msgtemplateElement.getChildNodes();
				// 遍历子节点列表并赋值
				for (int j = 0; j < mNodeList2.getLength(); j++) {
					Node mNodeChild = mNodeList2.item(j);
					String name = mNodeChild.getNodeName();
					Node firstNode = mNodeChild.getFirstChild();
					if (name == null || firstNode == null) {
						continue;
					}
					Log.d(TAG, name + " = " + firstNode.getNodeValue());
					if (mNodeChild.getNodeType() == Node.ELEMENT_NODE) {
						if (MsgTemplate.Field.ID.equals(mNodeChild.getNodeName())) {
							msgTemplate.setId(Integer.parseInt(mNodeChild.getFirstChild().getNodeValue()));
						} else if (MsgTemplate.Field.BRANCHENAME.equals(mNodeChild.getNodeName())) {
							msgTemplate.setBrancheName(mNodeChild.getFirstChild().getNodeValue());
						} else if (MsgTemplate.Field.CONTENT.equals(mNodeChild.getNodeName())) {
							msgTemplate.setContent(mNodeChild.getFirstChild().getNodeValue());
						} else if (MsgTemplate.Field.DEVICETYPE.equals(mNodeChild.getNodeName())) {
							msgTemplate.setDeviceType(mNodeChild.getFirstChild().getNodeValue());
						} else if (MsgTemplate.Field.MOBILE.equals(mNodeChild.getNodeName())) {
							msgTemplate.setMobile(mNodeChild.getFirstChild().getNodeValue());
						}
					}
				}
			}
		}
		return msgTemplate;
	}

	private static CheckIn readCheckIn(Element mElement) {
		NodeList mNodeList = mElement.getElementsByTagName("checkin");
		CheckIn checkin = new CheckIn();
		Log.d(TAG, "read checkin node size is " + mNodeList.getLength());
		for (int i = 0; i < mNodeList.getLength() && i < 1; i++) {
			Element checkinElement = (Element) mNodeList.item(i);
			// 获取person节点中的属性
			if (checkinElement.hasChildNodes()) {
				// 获取person节点的子节点列表
				NodeList mNodeList2 = checkinElement.getChildNodes();
				// 遍历子节点列表并赋值
				for (int j = 0; j < mNodeList2.getLength(); j++) {
					Node mNodeChild = mNodeList2.item(j);
					String name = mNodeChild.getNodeName();
					Node firstNode = mNodeChild.getFirstChild();
					if (name == null || firstNode == null) {
						continue;
					}
					Log.d(TAG, name + " = " + firstNode.getNodeValue());
					if (mNodeChild.getNodeType() == Node.ELEMENT_NODE) {
						if (CheckIn.Field.DEVICE_NAME.equals(mNodeChild.getNodeName())) {
							checkin.setDeviceName(mNodeChild.getFirstChild().getNodeValue());
						} else if (CheckIn.Field.DEVICE_CODE.equals(mNodeChild.getNodeName())) {
							checkin.setDeviceCode(mNodeChild.getFirstChild().getNodeValue());
							Share.setDeviceCode(checkin.getDeviceCode());
						} else if (CheckIn.Field.ACCESS_TOKEN.equals(mNodeChild.getNodeName())) {
							checkin.setAccessToken(mNodeChild.getFirstChild().getNodeValue());
						} else if (CheckIn.Field.MAC.equals(mNodeChild.getNodeName())) {
							checkin.setMac(mNodeChild.getFirstChild().getNodeValue());
						}
					}
				}
			}
		}
		return checkin;
	}

	private static Saver readSaver(Element mElement) {
		NodeList mNodeList = mElement.getElementsByTagName("saver");
		Saver saver = new Saver();
		Log.d(TAG, "read saver node size is " + mNodeList.getLength());
		for (int i = 0; i < mNodeList.getLength() && i < 1; i++) {
			Element checkinElement = (Element) mNodeList.item(i);
			// 获取person节点中的属性
			if (checkinElement.hasChildNodes()) {
				// 获取person节点的子节点列表
				NodeList mNodeList2 = checkinElement.getChildNodes();
				// 遍历子节点列表并赋值
				for (int j = 0; j < mNodeList2.getLength(); j++) {
					Node mNodeChild = mNodeList2.item(j);
					String name = mNodeChild.getNodeName();
					Node firstNode = mNodeChild.getFirstChild();
					if (name == null || firstNode == null) {
						continue;
					}
					Log.d(TAG, name + " = " + firstNode.getNodeValue());
					if (mNodeChild.getNodeType() == Node.ELEMENT_NODE) {
						if (Saver.Field.SCREEN_SAVER.equals(mNodeChild.getNodeName())) {
							saver.setScreenSaver(mNodeChild.getFirstChild().getNodeValue(), true);
						}
					}
				}
			}
		}
		return saver;
	}
}

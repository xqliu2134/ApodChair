package com.apod.chair.util;

import java.util.ArrayList;
import java.util.List;

import com.apod.chair.bo.Business;
import com.apod.chair.bo.Business.BizAttach;
import com.apod.chair.bo.Business.BusinessLevels.ModuleBizAttach;
import com.apod.chair.bo.Business.BusinessLevels.Navigation;
import com.apod.chair.bo.Business.BusinessMode;
import com.apod.chair.bo.Saver;
import com.apod.chair.bo.Saver.SaverImg;
import com.apod.chair.bo.Sms;

public class Share {

	private static Business business;
	private static BizAttach bizAttach;
	private static List<SaverImg> saverImgs = new ArrayList<SaverImg>();
	private static List<ModuleBizAttach> moduleBizAttach;
	private static List<Navigation> navigations;
	private static String deviceCode;
	private static List<ModuleBizAttach> typeDatas;
	private static Saver saver;
	private static Sms sms = new Sms();
	private static List<BusinessMode> businesModes = new ArrayList<BusinessMode>();
	private static String pudName = "";
	private static String levelName = "";

	public static String getLevelName() {
		return levelName;
	}

	public static void setLevelName(String levelName) {
		Share.levelName = levelName;
	}

	public static String getPudName() {
		return pudName;
	}

	public static void setPudName(String pudName) {
		Share.pudName = pudName;
	}

	public static List<BusinessMode> getBusinesModes() {
		return businesModes;
	}

	public static void setBusinesModes(List<BusinessMode> businesModes) {
		Share.businesModes = businesModes;
	}

	public static void setSms(Sms sms) {
		Share.sms = sms;
	}

	public static Sms getSms() {
		return sms;
	}

	public static Saver getSaver() {
		return saver;
	}

	public static void setSaver(Saver saver) {
		Share.saver = saver;
	}

	public static void setSaverImgs(List<SaverImg> saverImgs) {
		Share.saverImgs = saverImgs;
	}

	public static List<ModuleBizAttach> getTypeDatas() {
		return typeDatas;
	}

	public static void setTypeDatas(List<ModuleBizAttach> typeDatas) {
		Share.typeDatas = typeDatas;
	}

	public static List<Navigation> getNavigations() {
		return navigations;
	}

	public static void setNavigations(List<Navigation> navigations) {
		Share.navigations = navigations;
	}

	public static String getDeviceCode() {
		return deviceCode;
	}

	public static void setDeviceCode(String deviceCode) {
		Share.deviceCode = deviceCode;
	}

	public static List<ModuleBizAttach> getModuleBizAttach() {
		return moduleBizAttach;
	}

	public static void setModuleBizAttach(List<ModuleBizAttach> moduleBizAttach) {
		Share.moduleBizAttach = moduleBizAttach;
	}

	public static List<SaverImg> getSaverImgs() {
		return saverImgs;
	}

	public static BizAttach getBizAttach() {
		return bizAttach;
	}

	public static void setBizAttach(BizAttach bizAttach) {
		Share.bizAttach = bizAttach;
	}

	public static Business getBusiness() {
		return business;
	}

	public static void setBusiness(Business business) {
		Share.business = business;
	}
}

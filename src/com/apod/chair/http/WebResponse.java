package com.apod.chair.http;

import java.util.List;

import com.apod.chair.bo.Business;
import com.apod.chair.bo.CheckIn;
import com.apod.chair.bo.Device;
import com.apod.chair.bo.Feedback;
import com.apod.chair.bo.MsgTemplate;
import com.apod.chair.bo.Network;
import com.apod.chair.bo.Saver;
import com.apod.chair.bo.Sms;

public class WebResponse {
	private int requestType;
	private List<Network> networks;
	private List<Device> devices;
	private CheckIn checkin;
	private List<Business> business;
	private MsgTemplate msgTemplate;
	private Saver saver;
	private Feedback feedback;
	private boolean smsFlag;

	public boolean isSmsFlag() {
		return smsFlag;
	}

	public void setSmsFlag(boolean smsFlag) {
		this.smsFlag = smsFlag;
	}

	public Feedback getFeedback() {
		return feedback;
	}

	public void setFeedback(Feedback feedback) {
		this.feedback = feedback;
	}

	public Saver getSaver() {
		return saver;
	}

	public void setSaver(Saver saver) {
		this.saver = saver;
	}

	public MsgTemplate getMsgTemplate() {
		return msgTemplate;
	}

	public void setMsgTemplate(MsgTemplate msgTemplate) {
		this.msgTemplate = msgTemplate;
	}

	public List<Business> getBusiness() {
		return business;
	}

	public void setBusiness(List<Business> business) {
		this.business = business;
	}

	public CheckIn getCheckin() {
		return checkin;
	}

	public void setCheckin(CheckIn checkin) {
		this.checkin = checkin;
	}

	public List<Network> getNetworks() {
		return networks;
	}

	public void setNetworks(List<Network> networks) {
		this.networks = networks;
	}

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	public int getRequestType() {
		return requestType;
	}

	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}
}

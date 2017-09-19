package com.apod.chair.bo;

import java.io.File;

import com.apod.chair.bo.Business.BizAttach;

public class Word {
	public static final int READ_SUCCESS = 1;
	public static final int READ_FILE_NOT_FOUND = 2;
	public static final int READ_FILE_NOT_FAIL = 3;

	private String filepath;
	private String filename;
	private String pigDir;
	private String url;
	private int state;
	private String dir;
	private BizAttach bizAttach;

	public BizAttach getBizAttach() {
		return bizAttach;
	}

	public void setBizAttach(BizAttach bizAttach) {
		this.bizAttach = bizAttach;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
		this.url = dir + File.separator + filename + File.separator + filename + ".html";
		this.pigDir = dir + File.separator + filename + File.separator + "library/";
	}

	public String getPigDir() {
		return pigDir;
	}

	public String getUrl() {
		return url;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean success() {
		return state == READ_SUCCESS;
	}

	public boolean fail() {
		return state == READ_FILE_NOT_FAIL;
	}

	public boolean notfound() {
		return state == READ_FILE_NOT_FOUND;
	}
}

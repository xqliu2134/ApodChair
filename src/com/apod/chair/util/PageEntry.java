package com.apod.chair.util;

import java.util.ArrayList;
import java.util.List;

import android.widget.ImageView;

import com.apod.chair.ProductActivity;
import com.apod.chair.R;
import com.apod.chair.bo.Business.BusinessMode;

public class PageEntry {
	private final static int COUNT = 4;
	private List<BusinessMode> datas = new ArrayList<BusinessMode>();
	private List<BusinessMode> pageEntrys = new ArrayList<BusinessMode>();
	private int pageIndex = 1;
	private int pageCount;
	private ImageView imgPageUp, imgPageNext;
	private int select;
	private OnStepListener listener;

	public void setOnStepListener(OnStepListener listener) {
		this.listener = listener;
	}

	public PageEntry(ProductActivity view, List<BusinessMode> datas) {
		pageIndex = 1;
		imgPageUp = (ImageView) view.findViewById(R.id.imgPageUp);
		imgPageNext = (ImageView) view.findViewById(R.id.imgPageNext);
		this.datas = datas;
		if (datas == null || datas.size() == 0) {
			return;
		}
		int size = datas.size();
		pageCount = size / COUNT;
		if ((size % COUNT) > 0) {
			pageCount++;
		}
		showPageImage();
	}
	
	public int getSelect() {
		return select;
	}

	public void setSelect(int select) {
		this.select = select;
	}

	private void showPageImage() {
		if (pageIndex > 1) {
			imgPageUp.setImageResource(R.drawable.page_up_bg_p);
		} else {
			imgPageUp.setImageResource(R.drawable.page_up_bg_n);
		}

		if (pageIndex < pageCount) {
			imgPageNext.setImageResource(R.drawable.page_next_bg_p);
		} else {
			imgPageNext.setImageResource(R.drawable.page_next_bg_n);
		}
	}

	public List<BusinessMode> getPageEntrys() {
		return pageEntrys;
	}

	public int getPageCount() {
		return pageCount;
	}

	public List<BusinessMode> getDatas() {
		return datas;
	}

	public boolean prevPage() {
		if ((pageIndex - 1) < 1) {
			return false;
		}
		pageIndex = pageIndex - 1;
		convertPage(pageIndex);
		showPageImage();
		return true;
	}

	public List<BusinessMode> currentPage() {
		convertPage(pageIndex);
		return pageEntrys;
	}

	public void stepUp() {
		int position = select - 1;
		if (position < 0) {
			return;
		}
		if (listener != null) {
			listener.onStepUp(position, hasDataPage(position));
		}
	}

	public void stepDown() {
		int position = select + 1;
		if (position >= datas.size()) {
			return;
		}
		if (listener != null) {
			listener.onStepDown(position, hasDataPage(position));
		}
	}

	private boolean hasDataPage(int position) {
		for (int i = 0; i < pageEntrys.size(); i++) {
			BusinessMode mode = pageEntrys.get(i);
			if (position == mode.getIndex()) {
				return false;
			}
		}
		return true;
	}

	public boolean nextPage() {
		if ((pageIndex + 1) > pageCount) {
			return false;
		}
		pageIndex = pageIndex + 1;
		convertPage(pageIndex);
		showPageImage();
		return true;
	}

	private void convertPage(int index) {
		pageEntrys.clear();
		int i = (index - 1) * COUNT;
		int count = index * COUNT;
		for (; i < datas.size() && i < count; i++) {
			BusinessMode mode = datas.get(i);
			pageEntrys.add(mode);
		}
	}

	public interface OnStepListener {
		public void onStepUp(int select, boolean change);

		public void onStepDown(int select, boolean change);
	}
}

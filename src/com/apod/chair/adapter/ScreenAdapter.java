package com.apod.chair.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ScreenAdapter extends PagerAdapter {
	private List<ImageView> datas = new ArrayList<ImageView>();

	public ScreenAdapter(List<ImageView> datas) {
		this.datas = datas;
	}

	public void destroyItem(ViewGroup view, int position, Object arg2) {
		ViewPager pViewPager = ((ViewPager) view);
		pViewPager.removeView(datas.get(position));
	}

	public int getCount() {
		return datas.size();
	}

	public Object instantiateItem(ViewGroup container, int position) {
		ImageView child = datas.get(position);
		container.addView(child);
		return child;
	}

	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

}

package com.apod.chair.saver;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.apod.chair.R;
import com.apod.chair.adapter.ScreenAdapter;
import com.apod.chair.bo.Saver.SaverImg;
import com.apod.chair.manager.Config;
import com.apod.chair.saver.transf.DepthPageTransformer;
import com.apod.chair.util.Share;

public class ScreenActivity extends Activity {
	private final static String TAG = Config.TAG + ".ScreenActivity";
	private static PowerManager.WakeLock mWakeLock;
	private boolean isAcquire;
	private ViewPager viewPager;
	private ScreenAdapter mAdapter;
	private List<ImageView> imgViews = new ArrayList<ImageView>();
	private int currentIndex;
	private boolean isNext = true;
	private long delaytime = 10 * 1000;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen);
		Log.d(TAG, "onCreate");
		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
		// PARTIAL_WAKE_LOCK :保持CPU 运转，屏幕和键盘灯有可能是关闭的。
		// SCREEN_DIM_WAKE_LOCK ：保持CPU 运转，允许保持屏幕显示但有可能是灰的，允许关闭键盘灯
		// SCREEN_BRIGHT_WAKE_LOCK ：保持CPU 运转，允许保持屏幕高亮显示，允许关闭键盘灯
		// FULL_WAKE_LOCK ：保持CPU 运转，保持屏幕高亮显示，键盘灯也保持亮度
		mWakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "ApodChair");

		initView();
		mAdapter = new ScreenAdapter(imgViews);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		DepthPageTransformer transform = new DepthPageTransformer();
		viewPager.setPageTransformer(true, transform);
		viewPager.setAdapter(mAdapter);

	}

	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		mWakeLock.acquire();
		isAcquire = true;
		mHandler.sendEmptyMessageDelayed(MSG_ACQUIRE, delaytime);
	}

	private void initView() {
		imgViews.clear();
		List<SaverImg> saverImgs = Share.getSaverImgs();
		for (SaverImg img : saverImgs) {
			ImageView view = new ImageView(this);
			view.setOnTouchListener(new View.OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					Log.d(TAG, "onTouch");
					ScreenActivity.this.finish();
					return false;
				}
			});
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			view.setLayoutParams(params);
			Bitmap bitmap = img.getDrawable();
			Log.d(TAG, "initView bitmap = " + bitmap);
			if (bitmap != null) {
				view.setImageBitmap(bitmap);
			} else {
				view.setBackgroundResource(R.drawable.bg);
			}
			imgViews.add(view);
		}
	}

	private final static int MSG_ACQUIRE = 1;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_ACQUIRE: {
				if (isAcquire) {
					int count = mAdapter.getCount();
					if (currentIndex >= (count - 1)) {
						isNext = false;
					} else if (currentIndex <= 0) {
						isNext = true;
					}
					if (isNext) {
						currentIndex++;
					} else {
						currentIndex--;
					}
					currentIndex = currentIndex < 0 ? 0 : currentIndex;
					currentIndex = currentIndex >= count ? count - 1 : currentIndex;
					viewPager.setCurrentItem(currentIndex);
					mHandler.sendEmptyMessageDelayed(MSG_ACQUIRE, delaytime);
				}
				break;
			}
			}
		}
	};

	public boolean onTouchEvent(MotionEvent event) {
		this.finish();
		return super.onTouchEvent(event);
	}

	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
		try {
			mWakeLock.release();
		} catch (Exception ex) {

		}
		isAcquire = false;
		mHandler.removeMessages(MSG_ACQUIRE);
	}

	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		try {
			mWakeLock.release();
		} catch (Exception ex) {

		}
		isAcquire = false;
		mHandler.removeMessages(MSG_ACQUIRE);
	}
}

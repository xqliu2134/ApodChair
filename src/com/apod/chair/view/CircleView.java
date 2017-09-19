package com.apod.chair.view;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

import com.apod.chair.R;
import com.apod.chair.bo.Business.BusinessLevels.Item;

public class CircleView extends View {
	private int[] colors;
	private Paint circlePaint, textPaint;
	private List<Item> items;
	private boolean left = true;

	public CircleView(Context context, List<Item> items, boolean left) {
		super(context);
		this.items = items;
		this.left = left;
		colors = new int[] { context.getResources().getColor(R.color.circle_color1), context.getResources().getColor(R.color.circle_color2), context.getResources().getColor(R.color.circle_color3) };
		circlePaint = new Paint();
		textPaint = new Paint();
		textPaint.setColor(Color.BLACK);
		textPaint.setStrokeWidth(0);
		textPaint.setTextSize(25);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(450, 150);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int[] location = new int[2];
		getLocationInWindow(location);
		int centre = 67; // 获取圆心的x坐标
		int radius = centre; // 圆环的半径
		circlePaint.setStyle(Paint.Style.FILL); // 设置空心
		circlePaint.setAntiAlias(true); // 消除锯齿
		circlePaint.setColor(Color.WHITE);
		canvas.drawCircle(centre, centre, radius, circlePaint);
		// 设置扇型是实心
		RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius); // 用于定义的圆弧的形状和大小的界限
		circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
		int posotion = 0;
		for (int i = 0; i < items.size() && i < colors.length; i++) {
			Item item = items.get(i);
			circlePaint.setColor(colors[i]);
			int value = covertPrecent(item.getItemValue());
			int precent = 360 * value / 100;
			canvas.drawArc(oval, posotion, precent, true, circlePaint);
			String text = item.getItemName() + "：" + value + "%";
			if (i == 0) {
				canvas.drawText(text, (radius * 2) + 20, 20, textPaint);
			} else {
				canvas.drawText(text, (radius * 2) + 20, 20 * (i + 1) + 30 * i, textPaint);
			}
			posotion = precent + posotion;
		}
	}

	private int covertPrecent(String value) {
		int precent = 0;
		try {
			value = value.replace("%", "");
			if (left) {
				value = value.substring(0, value.indexOf("-"));
			} else {
				value = value.substring(value.indexOf("-") + 1);
			}
			precent = Integer.parseInt(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return precent;
	}

}

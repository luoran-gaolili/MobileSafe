package com.example.uipackage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobilesafe.R;

public class myTextViewTwo extends RelativeLayout {

	CheckBox checkbox;
	TextView viewone, viewtwo;
	String startText;
	String closeText;

	private void initview(Context context) {
		// TODO Auto-generated method stub

		View.inflate(context, R.layout.set_list_layout, myTextViewTwo.this);
		checkbox = (CheckBox) findViewById(R.id.checkbox);
		viewone = (TextView) findViewById(R.id.update_view_two);
		viewtwo = (TextView) findViewById(R.id.update_view);

	}

	public myTextViewTwo(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initview(context);
	}

	/**
	 * 自定义控件的属性
	 * 
	 * @param context
	 * @param attrs
	 */
	public myTextViewTwo(Context context, AttributeSet attrs) {
		super(context, attrs);

		initview(context);
		String title = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/com.example.mobilesafe",
				"title");
		startText = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/com.example.mobilesafe",
				"startText");
		closeText = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/com.example.mobilesafe",
				"closeText");
		viewtwo.setText(title);
		viewone.setText(closeText);

	}

	public myTextViewTwo(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initview(context);
	}

	public boolean getchecked() {
		return checkbox.isChecked();
	}

	public void setChecked(boolean bool) {
		if (bool) {
			viewone.setText(startText);
		} else {
			viewone.setText(closeText);
		}
		checkbox.setChecked(bool);
	}

	public void setText(String str) {
		viewone.setText(str);

	}

}

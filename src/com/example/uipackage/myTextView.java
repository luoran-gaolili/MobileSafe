package com.example.uipackage;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

public class myTextView extends TextView{

	public myTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public myTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public myTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		// TODO Auto-generated method stub
		return true;
	}

}

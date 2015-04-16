package com.example.mobilesafe;

import com.example.mobilesafe.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class SetupActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setup1);
	}

}

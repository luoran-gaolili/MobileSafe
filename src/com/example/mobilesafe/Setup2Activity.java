package com.example.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import com.example.uipackage.myTextViewTwo;

public class Setup2Activity extends BaseActivity {
	myTextViewTwo textview;
	SharedPreferences preferences;
	SharedPreferences.Editor edit;
	TelephonyManager tm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setup2);
		textview = (myTextViewTwo) findViewById(R.id.settingId);
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		preferences = getSharedPreferences("configd", MODE_PRIVATE);
		Boolean bool = preferences.getBoolean("choose", false);
		if (bool) {
			textview.setChecked(true);
		} else {
			textview.setChecked(false);
		}
		textview.setOnClickListener(new OnClickListener() {
			

			public void onClick(View v) {
				edit = preferences.edit();
				if (textview.getchecked()) {
					textview.setChecked(false);
					edit.putBoolean("choose", false);
					edit.putString("SIM", null);
				} else {
					textview.setChecked(true);
					edit.putBoolean("choose", true);
					String str=tm.getSimSerialNumber();
					edit.putString("SIM", str);
				}
				edit.commit();
			}
		});
	}

	@Override
	protected void showNext() {
		// TODO Auto-generated method stub
		Boolean bool = preferences.getBoolean("choose", false);
		if (bool) {
			Intent intent = new Intent(this, Setup3Activity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.right, R.anim.right_move);
		} else {
			Toast.makeText(this, "Çë°ó¶¨SIM¿¨", 0).show();
		}

	}

	@Override
	protected void showPre() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, Setup1Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.left, R.anim.left_move);
	}

}

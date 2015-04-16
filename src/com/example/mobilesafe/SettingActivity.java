package com.example.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.example.service.BlackNumberService;
import com.example.service.ShowLocationAddressService;
import com.example.streamtools.ServiceActive;
import com.example.uipackage.myTextViewThree;
import com.example.uipackage.myTextViewTwo;

public class SettingActivity extends Activity {
	SharedPreferences preference;
	SharedPreferences.Editor edit;
	myTextViewTwo textview, textviewone, textviewtwo;
	myTextViewThree tvt;
	TextView showDes;

	Boolean bool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_layout);
		textview = (myTextViewTwo) findViewById(R.id.settingId);
		textviewone = (myTextViewTwo) findViewById(R.id.settingId_one);
		textviewtwo = (myTextViewTwo) findViewById(R.id.settingId_three);
		tvt = (myTextViewThree) findViewById(R.id.settingId_two);
		showDes = (TextView) tvt.findViewById(R.id.update_view_two);
		preference = getSharedPreferences("config", MODE_PRIVATE);
		bool = preference.getBoolean("update", false);
		if (bool) {
			// 自动更新已开启
			textview.setChecked(true);
		} else {
			// 自动更新已关闭
			textview.setChecked(false);
		}

		textview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edit = preference.edit();
				if (textview.getchecked()) {
					textview.setChecked(false);
					edit.putBoolean("update", false);

				} else {
					textview.setChecked(true);
					edit.putBoolean("update", true);
				}
				edit.commit();

			}
		});
		Boolean bool = ServiceActive.selectServiceActive(this,
				"com.example.service.ShowLocationAddressService");
		if (bool) {
			textviewone.setChecked(true);
		} else {
			textviewone.setChecked(false);
		}
		textviewone.setOnClickListener(new OnClickListener() {
			Intent intent = new Intent(SettingActivity.this,
					ShowLocationAddressService.class);

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (textviewone.getchecked()) {
					textviewone.setChecked(false);
					stopService(intent);
				} else {
					textviewone.setChecked(true);
					startService(intent);
				}

			}
		});
		boolean result = ServiceActive.selectServiceActive(
				SettingActivity.this, "com.example.service.BlackNumberService");
		textviewtwo.setChecked(result);
		textviewtwo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SettingActivity.this,
						BlackNumberService.class);
				if (textviewtwo.getchecked()) {
					textviewtwo.setChecked(false);
					stopService(intent);
				} else {
					textviewtwo.setChecked(true);
					startService(intent);
				}

			}
		});
		final String[] items = { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };
		int which = preference.getInt("key", 0);
		showDes.setText(items[which]);
		tvt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int which = preference.getInt("key", 0);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SettingActivity.this);
				builder.setTitle("提示框风格");
				builder.setSingleChoiceItems(items, which,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								edit = preference.edit();
								edit.putInt("key", which);
								edit.commit();
								showDes.setText(items[which]);
								dialog.dismiss();

							}
						});
				builder.setPositiveButton("取消", null);
				builder.show();

			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Boolean bool = ServiceActive.selectServiceActive(this,
				"com.example.service.ShowLocationAddressService");
		if (bool) {
			textviewone.setChecked(true);
		} else {
			textviewone.setChecked(false);
		}
		boolean result = ServiceActive.selectServiceActive(
				SettingActivity.this, "com.example.service.BlackNumberService");
		textviewtwo.setChecked(result);
	}
}

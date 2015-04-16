package com.example.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Setup4Activity extends BaseActivity {
	SharedPreferences sp;
	SharedPreferences.Editor edit;
	CheckBox check;
	TextView textview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setup4);
		sp = getSharedPreferences("configd", MODE_PRIVATE);
		check = (CheckBox) findViewById(R.id.finish_check);
		textview = (TextView) findViewById(R.id.finish_textview);

		Boolean bool = sp.getBoolean("setupd", false);
		if (bool) {
			textview.setText("您已开启自动防盗");
			check.setChecked(true);
		} else {
			textview.setText("您没有开启自动防盗");
			check.setChecked(false);
		}
		check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				edit = sp.edit();
				if (isChecked) {
					textview.setText("您已开启自动防盗");
					edit.putBoolean("setupd", true);
				} else {
					textview.setText("您没有开启自动防盗");
					edit.putBoolean("setupd", false);
				}
				edit.commit();

			}
		});
	}

	@Override
	protected void showNext() {
		// TODO Auto-generated method stub
		Boolean bool = sp.getBoolean("setupd", false);
		if (bool) {
			edit = sp.edit();
			edit.putBoolean("configed", true);
			edit.commit();
			Intent intent = new Intent(this, LostFindActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.right, R.anim.right_move);
		} else {
			Toast.makeText(this, "您没有设置防盗保护", 0).show();
		}

	}

	@Override
	protected void showPre() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.left, R.anim.left_move);
	}

}

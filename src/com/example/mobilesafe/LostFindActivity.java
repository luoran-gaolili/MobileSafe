package com.example.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {
	SharedPreferences preference;
	TextView textView;
	ImageView image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lostfind);
		textView = (TextView) findViewById(R.id.start_textview);
		image = (ImageView) findViewById(R.id.start_image);
		preference = getSharedPreferences("configd", MODE_PRIVATE);
		Boolean bool = preference.getBoolean("configed", false);

		if (bool) {
			// ������ù��ˣ�ͣ�ڱ�ҳ��

			String str = preference.getString("safenumber", "");
			textView.setText(str + "");
			Boolean real = preference.getBoolean("setupd", false);
			if (real) {
				image.setImageResource(R.drawable.lock);
			} else {
				image.setImageResource(R.drawable.unlock);
			}

		} else {
			// ���û�����ã����������򵼽���
			Intent intent = new Intent(this, Setup1Activity.class);
			startActivity(intent);
			finish();
		}
	}

	public void enterSetup(View view) {
		Intent intent = new Intent(this, Setup1Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.right, R.anim.right_move);
	}

}

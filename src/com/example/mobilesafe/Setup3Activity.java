package com.example.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class Setup3Activity extends BaseActivity {
	SharedPreferences sp;
	SharedPreferences.Editor edit;
	private EditText et_setup3_phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		sp = getSharedPreferences("configd", MODE_PRIVATE);
		setContentView(R.layout.setup3);
		et_setup3_phone = (EditText) findViewById(R.id.setup3_edit);
		et_setup3_phone.setText(sp.getString("safenumber", ""));
	}

	@Override
	protected void showNext() {
		// TODO Auto-generated method stub
		String phone = et_setup3_phone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			Toast.makeText(this, "安全号码还没有设置", 0).show();
			return;
		}

		edit = sp.edit();
		edit.putString("safenumber", phone);
		edit.commit();

		Intent intent = new Intent(this, Setup4Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.right, R.anim.right_move);
	}

	@Override
	protected void showPre() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, Setup2Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.left, R.anim.left_move);

	}

	public void selectContact(View view) {
		Intent intent = new Intent(this, SelectContactActivity.class);
		startActivityForResult(intent, 0);

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			if (resultCode == 0) {
				String phone = data.getStringExtra("phone").replace("-", "");
				et_setup3_phone.setText(phone);
			}
			break;

		default:
			break;
		}
	}

}

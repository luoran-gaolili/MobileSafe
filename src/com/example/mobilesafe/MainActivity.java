package com.example.mobilesafe;

import com.example.streamtools.GetMd5Passsword;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	SharedPreferences preference;
	SharedPreferences.Editor edit;
	AlertDialog alert;

	GridView gridview;
	MyAdapter adapter;
	private static String[] names = { "手机防盗", "通信卫士", "软件管理", "进程管理", "流量统计",
			"手机杀毒", "缓存清理", "高级工具", "设置中心" };
	private static int[] images = { R.drawable.safe, R.drawable.callmsgsafe,
			R.drawable.app, R.drawable.taskmanager, R.drawable.netmanager,
			R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools,
			R.drawable.settings };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		preference = getSharedPreferences("config", MODE_PRIVATE);
		gridview = (GridView) findViewById(R.id.grid_view);
		adapter = new MyAdapter();
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			Intent intent;

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 8:
					intent = new Intent(MainActivity.this,
							SettingActivity.class);
					startActivity(intent);
					break;
				case 7:
					intent = new Intent(MainActivity.this,
							SeniorToolsActivity.class);
					startActivity(intent);
					break;
				case 0:
					showEnterDialog();
					break;
				case 1:
					intent = new Intent(MainActivity.this,
							BlackNumberActivity.class);
					startActivity(intent);
					break;
				case 2:
					intent = new Intent(MainActivity.this,
							AppMangerActivity.class);
					startActivity(intent);
					break;
				case 3:
					intent = new Intent(MainActivity.this,
							TaskMangerActivity.class);
					startActivity(intent);
					break;
				default:
					break;
				}

			}

			private void showEnterDialog() {
				// TODO Auto-generated method stub

				if (isSetPassword()) {
					showIntoDialog();
				} else {
					showSetDialog();
				}

			}

			/**
			 * 判断是否设置密码
			 * 
			 * @return
			 */

			private boolean isSetPassword() {
				// TODO Auto-generated method stub
				String str = preference.getString("password", null);
				if (TextUtils.isEmpty(str)) {
					return false;
				} else {
					return true;
				}

			}

			private void showSetDialog() {
				// TODO Auto-generated method stub
				final AlertDialog.Builder builder = new Builder(
						MainActivity.this);
				View view = View.inflate(MainActivity.this, R.layout.setup,
						null);
				Button buttonone = (Button) view.findViewById(R.id.set_button);
				Button buttontwo = (Button) view
						.findViewById(R.id.set_buttontwo);
				final EditText textone = (EditText) view
						.findViewById(R.id.setup_textone);
				final EditText texttwo = (EditText) view
						.findViewById(R.id.setup_texttwo);
				buttonone.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String str1 = textone.getText().toString().trim();
						String str2 = texttwo.getText().toString().trim();
						System.out.println(str1);
						if (TextUtils.isEmpty(str1) || TextUtils.isEmpty(str2)) {
							Toast.makeText(MainActivity.this, "密码为空", 0).show();
							return;

						}
						if (str1.equals(str2)) {
							edit = preference.edit();
							edit.putString("password",
									GetMd5Passsword.getPassword(str1));
							edit.commit();
							// 进入手机卫士主界面
							Toast.makeText(MainActivity.this, "密码设置成功", 0)
									.show();
							alert.dismiss();
							Intent intent = new Intent(getApplicationContext(),
									LostFindActivity.class);
							startActivity(intent);

						} else {
							Toast.makeText(MainActivity.this, "密码不一致，请重新确认", 0)
									.show();
						}

					}

				});
				buttontwo.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						alert.dismiss();

					}
				});

				builder.setView(view);
				alert = builder.show();

			}

			/**
			 * 
			 * 
			 */

			private void showIntoDialog() {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new Builder(MainActivity.this);
				View view = View.inflate(MainActivity.this, R.layout.confim,
						null);
				Button buttonone = (Button) view
						.findViewById(R.id.confim_button);
				Button buttontwo = (Button) view
						.findViewById(R.id.confim_buttontwo);
				final EditText edit_text = (EditText) view
						.findViewById(R.id.confim_text);
				buttonone.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String str = edit_text.getText().toString().trim();
						String password = preference
								.getString("password", null);
						if (GetMd5Passsword.getPassword(str).equals(password)) {
							Toast.makeText(MainActivity.this, "输入密码正确", 0)
									.show();
							Intent intent = new Intent(getApplicationContext(),
									LostFindActivity.class);
							startActivity(intent);
						} else {
							Toast.makeText(MainActivity.this, "输入密码错误，请重新输入", 0)
									.show();
							edit_text.setText(null);
						}

					}
				});
				buttontwo.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						alert.dismiss();
					}
				});
				builder.setView(view);
				alert = builder.show();

			}
		});
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return names.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = View.inflate(MainActivity.this, R.layout.grid_list,
					null);
			ImageView imageview = (ImageView) view
					.findViewById(R.id.image_view);
			TextView textview = (TextView) view.findViewById(R.id.grid_view);
			imageview.setImageResource(images[position]);
			textview.setText(names[position]);
			return view;
		}

	}

}

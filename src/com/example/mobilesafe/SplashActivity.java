package com.example.mobilesafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.example.receiver.Adminstrator;
import com.example.streamtools.StreamTools;

public class SplashActivity extends Activity {
	protected static final int EQUALS = 0;
	protected static final int DIALOG = 1;
	protected static final int URLERROR = 2;
	protected static final int IOERROR = 3;
	protected static final int JSONERROR = 4;
	TextView sp_view, progress_view;
	String version;
	String decription;
	String appurl;
	SharedPreferences preference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_layout);
		copyDB();
		sp_view = (TextView) findViewById(R.id.splash_view);
		progress_view = (TextView) findViewById(R.id.progress_view);
		preference = getSharedPreferences("config", MODE_PRIVATE);
		Boolean bool = preference.getBoolean("update", false);
		if (bool) {
			checkInfo();
		} else {
			hd.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					enterHome();
				}
			}, 2000);

		}
		/**
		 * 获取版本号
		 */
		sp_view.setText("版本号:" + getVersionName());
		/**
		 * 获取更新信息
		 */

		AlphaAnimation aa = new AlphaAnimation(0.2f, 1f);
		aa.setDuration(500);
		findViewById(R.id.splash_layout).startAnimation(aa);
	}

	private void copyDB() {
		// TODO Auto-generated method stub
		try {
			File file = new File(getFilesDir(), "address.db");
			if (file.exists() && file.length() > 0) {
				InputStream stream = getAssets().open("address.db");
				FileOutputStream fos = new FileOutputStream(file);
				int len = 0;
				byte[] bt = new byte[1024];
				while ((len = stream.read(bt)) != -1) {
					fos.write(bt, 0, len);
				}
				fos.close();
				stream.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Handler hd = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case EQUALS:
				enterHome();

				break;
			case DIALOG:
				showUpdateDialog();

				break;
			case URLERROR:
				enterHome();
				break;
			case JSONERROR:

				break;

			default:
				break;
			}
		}

		private void showUpdateDialog() {
			// TODO Auto-generated method stub
			AlertDialog.Builder alert = new AlertDialog.Builder(
					SplashActivity.this);
			alert.setTitle("发现新版本");
			alert.setMessage(decription);
			alert.setCancelable(false);
			alert.setPositiveButton("点击下载", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 下载apk
					// 如果sdcard卡存在的话
					// 启动下载
					if (Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						FinalHttp finalhttp = new FinalHttp();
						finalhttp.download(appurl, Environment
								.getExternalStorageDirectory()
								.getAbsolutePath()
								+ "/mobilesafe2.0.apk",
								new AjaxCallBack<File>() {

									@Override
									public void onFailure(Throwable t,
											int errorNo, String strMsg) {
										// TODO Auto-generated method stub
										t.printStackTrace();
										Toast.makeText(getApplicationContext(),
												"对不起，文件下载失败", 0).show();
										super.onFailure(t, errorNo, strMsg);
									}

									@Override
									public void onLoading(long count,
											long current) {
										// TODO Auto-generated method stub
										super.onLoading(count, current);
										progress_view
												.setVisibility(View.VISIBLE);
										int progress = (int) (current * 100 / count);
										progress_view.setText("下载进度" + progress
												+ "%");

									}

									@Override
									public void onSuccess(File t) {
										// TODO Auto-generated method stub
										super.onSuccess(t);
										// 开始安装
										Intent intent = new Intent();
										intent.setAction("android.intent.action.VIEW");
										intent.addCategory("android.intent.category.DEFAULT");
										intent.setDataAndType(Uri.fromFile(t),
												"application/vnd.android.package-archive");
										startActivity(intent);

									}

								});

					} else {
						Toast.makeText(getApplicationContext(),
								"识别不出sdcard卡，请检查后重试", Toast.LENGTH_SHORT)
								.show();
					}

				}
			});
			alert.setNegativeButton("下次再说", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					enterHome();
				}
			});
			alert.show();

		}

	};

	private void enterHome() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private void checkInfo() {
		new Thread() {

			@Override
			public void run() {
				HttpURLConnection connection = null;
				Message ms = Message.obtain();
				Long startTime = System.currentTimeMillis();
				try {
					URL url = new URL(getString(R.string.apkurl));
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);

					int responseId = connection.getResponseCode();
					Log.d("哈哈", responseId + "");
					if (responseId == 200) {

						InputStream stream = connection.getInputStream();
						String str = StreamTools.getString(stream);
						Log.d("联网信息", str);
						JSONObject jb = new JSONObject(str);
						version = jb.getString("version");
						decription = jb.getString("description");
						appurl = jb.getString("appurl");
						Log.d("版本号", version);
						Log.d("描述信息", decription);
						Log.d("描述信息", appurl);
						if (getVersionName().equals(version)) {
							// 进入主界面
							ms.what = EQUALS;
						} else {
							// 弹出对话框更新
							ms.what = DIALOG;
						}
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					ms.what = URLERROR;
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					ms.what = IOERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					ms.what = JSONERROR;
					e.printStackTrace();
				} finally {
					Long endTime = System.currentTimeMillis();
					Long time = endTime - startTime;
					if (time < 2000) {
						try {
							Thread.sleep(2000 - time);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (connection != null) {
						connection.disconnect();
					}
					hd.sendMessage(ms);

				}

			}

		}.start();

	}

	/**
	 * 获取apk版本号
	 * 
	 * @return
	 */
	private String getVersionName() {
		// TODO Auto-generated method stub
		String version = null;
		PackageManager pm = getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
			version = pi.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return version;
		}
	}

}

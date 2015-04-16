package com.example.mobilesafe;

import com.example.streamtools.SmsBakup;
import com.example.streamtools.SmsBakup.BakupFace;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SeniorToolsActivity extends Activity {
	ProgressDialog dialog;
	ProgressBar progressbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.seniortool_layout);
		progressbar = (ProgressBar) findViewById(R.id.progressBar1);
	}

	public void queryNumber(View view) {
		Intent intent = new Intent(this, QueryNumberActivity.class);
		startActivity(intent);
	}

	public void SmsBackup(View view) {
		dialog = new ProgressDialog(this);
		dialog.setMessage("正在备份短信");
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.show();
		progressbar.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
					SmsBakup.smsBakup(SeniorToolsActivity.this,
							new BakupFace() {

								public void SmsSetProgress(int progress) {
									// TODO Auto-generated method stub
									dialog.setProgress(progress);
									progressbar.setProgress(progress);

								}

								@Override
								public void SmsSetMax(int max) {
									// TODO Auto-generated method stub
									dialog.setMax(max);
									progressbar.setMax(max);

								}
							});

					runOnUiThread(new Runnable() {

						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(SeniorToolsActivity.this, "短信备份成功",
									Toast.LENGTH_SHORT).show();
							progressbar.setVisibility(View.GONE);
						}
					});

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					dialog.dismiss();

				}
			}
		}.start();

	}

	public void SmsRetore(View view) {
		dialog = new ProgressDialog(this);
		dialog.setMessage("正在还原短信");
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.show();
		progressbar.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				SmsBakup.smsRestore(SeniorToolsActivity.this, true,
						new BakupFace() {

							@Override
							public void SmsSetProgress(int progress) {
								// TODO Auto-generated method stub
								dialog.setProgress(progress);
								progressbar.setProgress(progress);
							}

							@Override
							public void SmsSetMax(int max) {
								// TODO Auto-generated method stub
								dialog.setMax(max);
								progressbar.setMax(max);
							}

						});
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(SeniorToolsActivity.this, "短信还原成功",
								Toast.LENGTH_SHORT).show();
						progressbar.setVisibility(View.GONE);
						dialog.dismiss();
					}
				});
			}
		}).start();

	}

}

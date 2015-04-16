package com.example.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.streamtools.QueryNumberLocation;

public class ShowLocationAddressService extends Service {
	TelephonyManager tm;
	Mylistener listener;
	String numberLocation;
	WindowManager wm;
	TextView tv;
	ShowCallNumberReceiver receiver;
	View view;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private class Mylistener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			// 响铃状态
			case TelephonyManager.CALL_STATE_RINGING:
				numberLocation = QueryNumberLocation
						.queryLocation(incomingNumber);
				/*
				 * Toast.makeText(getApplicationContext(), numberLocation, 1)
				 * .show();
				 */
				myToast(numberLocation);
				break;
			// 挂断状态
			case TelephonyManager.CALL_STATE_IDLE:
				if (view != null) {
					wm.removeView(view);
				}
				break;
			default:
				break;
			}
		}

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new Mylistener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		IntentFilter filter = new IntentFilter();
		receiver = new ShowCallNumberReceiver();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(receiver, filter);
		params = new WindowManager.LayoutParams();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
		unregisterReceiver(receiver);
		receiver = null;
	}

	int startX;
	int startY;
	WindowManager.LayoutParams params;
	SharedPreferences sp;
	long[] mHits = new long[2];

	private void myToast(String location) {
		view = View.inflate(getApplicationContext(), R.layout.address_show,
				null);
		/**
		 * 双击事件1
		 */
		view.setOnClickListener(new OnClickListener() {
			long firstTime = SystemClock.uptimeMillis();

			public void onClick(View v) {
				if (firstTime > 0) {
					long secondTime = SystemClock.uptimeMillis();
					long dTime = secondTime - firstTime;
					if (dTime < 500) {
						params.x = wm.getDefaultDisplay().getWidth() / 2
								- view.getWidth() / 2;
						params.y = wm.getDefaultDisplay().getHeight() / 2
								- view.getHeight() / 2;
						wm.updateViewLayout(view, params);
						return;
					} else {
						firstTime = 0;
					}

				}
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						firstTime = 0;

					}
				});

			}
		});
		/**
		 * 双机事件2
		 */
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();
				if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
					// 双击居中了。。。
					params.x = wm.getDefaultDisplay().getWidth() / 2
							- view.getWidth() / 2;
					params.y = wm.getDefaultDisplay().getHeight() / 2
							- view.getHeight() / 2;
					wm.updateViewLayout(view, params);
				}
			}
		});
		/**
		 * 给view添加一个触发事件
		 * 
		 */
		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					Log.d("手指按下", "没有检测到啊");
					break;
				case MotionEvent.ACTION_MOVE:
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					int dx = newX - startX;
					int dy = newY - startY;
					params.x += dx;
					params.y += dy;
					if (params.x < 0) {
						params.x = 0;
					}
					if (params.y < 0) {
						params.y = 0;
					}
					if (params.x > (wm.getDefaultDisplay().getWidth() - view
							.getWidth())) {
						params.x = wm.getDefaultDisplay().getWidth()
								- view.getWidth();
					}
					if (params.y > (wm.getDefaultDisplay().getHeight() - view
							.getHeight())) {
						params.y = wm.getDefaultDisplay().getHeight()
								- view.getHeight();
					}
					wm.updateViewLayout(view, params);
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();

					break;
				case MotionEvent.ACTION_UP:
					sp = getSharedPreferences("config", MODE_PRIVATE);
					SharedPreferences.Editor edit;
					edit = sp.edit();
					edit.putInt("params.x", (int) event.getRawX());
					edit.putInt("params.y", (int) event.getRawY());
					edit.commit();
					break;
				default:
					break;
				}
				return false;
			}
		});
		TextView tv = (TextView) view.findViewById(R.id.show_address_tv);
		tv.setText(location);
		int[] ids = { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		int id = sp.getInt("key", 0);
		view.setBackgroundResource(ids[id]);
		/*
		 * tv = new TextView(getApplicationContext());
		 * tv.setText(numberLocation); tv.setTextSize(22);
		 * tv.setTextColor(Color.RED);
		 */

		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.TOP + Gravity.LEFT;
		params.x = sp.getInt("params.x", 0);
		params.y = sp.getInt("params.y", 0);

		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		// 电话优先级窗体 记得添加权限
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		wm.addView(view, params);

	}

	class ShowCallNumberReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String number = getResultData();
			String numberLocationAddress = QueryNumberLocation
					.queryLocation(number);
			myToast(numberLocationAddress);
		}

	}

}

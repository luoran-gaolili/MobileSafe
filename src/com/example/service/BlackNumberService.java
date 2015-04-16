package com.example.service;

import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.example.db.BlackListInfo;

public class BlackNumberService extends Service {
	SMSReceiver receiver;
	TelephonyManager manger;
	MyListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		IntentFilter fliter = new IntentFilter();
		fliter.addAction("android.provider.Telephony.SMS_RECEIVED");
		fliter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		receiver = new SMSReceiver();
		registerReceiver(receiver, fliter);
		manger = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		listener = new MyListener();
		manger.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	private class MyListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				System.out.println("你的电话响了");
				BlackListInfo info = new BlackListInfo(getApplicationContext());
				String result = info.findMode(incomingNumber);
				if ("2".equals(result) || "3".equals(result)) {
					// System.out.println("挂断电话");
					// 挂断电话
					Uri uri = Uri.parse("content://call_log/calls");
					getContentResolver().registerContentObserver(uri, true,
							new observer(incomingNumber, new Handler()));
					endCall();

				} else {
					return;
				}
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				break;
			default:
				break;
			}
		}

		private class observer extends ContentObserver {
			private String incomingNumber;

			public observer(String incomingNumber, Handler handler) {
				super(handler);
				// TODO Auto-generated constructor stub
				this.incomingNumber = incomingNumber;
			}

			@Override
			public void onChange(boolean selfChange) {
				// TODO Auto-generated method stub
				super.onChange(selfChange);
				deleteCallLog(incomingNumber);
				getContentResolver().unregisterContentObserver(this);
			}

		}

		/**
		 * 删除通话记录
		 * 
		 * @param incomingNumber
		 */
		private void deleteCallLog(String incomingNumber) {
			// TODO Auto-generated method stub
			ContentResolver resolver = getContentResolver();
			// 呼叫记录uri的路径
			Uri uri = Uri.parse("content://call_log/calls");
			resolver.delete(uri, "number=?", new String[] { incomingNumber });

		}

		/**
		 * 远程加载adil文件来调用系统隐藏的方法
		 */

		private void endCall() {
			// 加载字节码文件
			try {
				Class clazz = BlackNumberService.class.getClassLoader()
						.loadClass("android.os.ServiceManager");
				Method method = clazz.getMethod("getService", String.class);
				IBinder binder = (IBinder) method.invoke(null,
						TELEPHONY_SERVICE);
				ITelephony.Stub.asInterface(binder).endCall();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	class SMSReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Object[] object = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : object) {
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
				String smsAddress = sms.getOriginatingAddress();
				String smsMessage = sms.getMessageBody();
				BlackListInfo info = new BlackListInfo(context);
				String result = info.findMode(smsAddress);
				System.out.println(result);
				if ("1".equals(result) || "3".equals(result)) {
					Log.d("TAG", "拦截短信");
					abortBroadcast();
				}

			}

		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
		receiver = null;
		manger.listen(listener, PhoneStateListener.LISTEN_NONE);
	}

}

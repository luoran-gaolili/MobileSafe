package com.example.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.example.mobilesafe.R;
import com.example.service.LocationService;

public class SMSReceiver extends BroadcastReceiver {

	private static final String TAG = "SMSReceiver";
	private SharedPreferences sp;
	DevicePolicyManager dpm;

	@Override
	public void onReceive(Context context, Intent intent) {
		// 写接收短信的代码
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		sp = context.getSharedPreferences("configd", Context.MODE_PRIVATE);
		for (Object b : objs) {
			// 具体的某一条短信
			SmsMessage sms = SmsMessage.createFromPdu((byte[]) b);
			// 获取发送方的号码
			String sender = sms.getOriginatingAddress();// 15555555556
			// 获取用户设置的安全号码
			String safenumber = sp.getString("safenumber", "");// 5556
			// 获取内容信息
			String body = sms.getMessageBody();
			if (sender.contains(safenumber)) {
				if ("#*location*#".equals(body)) {
					// 启动服务
					Intent i = new Intent(context, LocationService.class);
					context.startService(i);
					SharedPreferences sp = context.getSharedPreferences(
							"config", Context.MODE_PRIVATE);
					String lastlocation = sp.getString("lastlocation", null);
					if (TextUtils.isEmpty(lastlocation)) { // 位置没有得到
						SmsManager.getDefault().sendTextMessage(sender, null,
								"geting loaction.....", null, null);
					} else {
						SmsManager.getDefault().sendTextMessage(sender, null,
								lastlocation, null, null);
					}
					abortBroadcast();
				} else if ("#*alarm*#".equals(body)) {
					// 播放报警影音

					MediaPlayer player = MediaPlayer.create(context,
							R.raw.alarm);
					player.setLooping(false);// 循环播放
					player.setVolume(1.0f, 1.0f);
					player.start();

					abortBroadcast();
				} else if ("#*wipedata*#".equals(body)) {
					// 远程清除数据
					dpm = (DevicePolicyManager) context
							.getSystemService(Context.DEVICE_POLICY_SERVICE);

					abortBroadcast();
				} else if ("#*lockscreen*#".equals(body)) {
					// 远程锁屏
					dpm = (DevicePolicyManager) context
							.getSystemService(Context.DEVICE_POLICY_SERVICE);
					/*
					 * 激活设备管理器
					 */
					Intent intents = new Intent(
							DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
					ComponentName cn = new ComponentName(context,
							Adminstrator.class);
					intents.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cn);
					intents.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
							"请开启一键锁屏");

					intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intents);
					dpm.lockNow();
					dpm.resetPassword("", 1);
					abortBroadcast();
				}
			}

		}

	}

}

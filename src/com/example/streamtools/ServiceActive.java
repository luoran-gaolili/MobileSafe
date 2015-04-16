package com.example.streamtools;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.Context;

/*
 * 检查后台服务是否还存活
 * 
 */
public class ServiceActive {
	public static boolean selectServiceActive(Context context,
			String serviceName) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> rs = am.getRunningServices(100);
		for (RunningServiceInfo info : rs) {
			String activeServiceName = info.service.getClassName();
			if (activeServiceName.equals(serviceName)) {
				return true;
			}
		}
		return false;

	}
}

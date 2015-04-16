package com.example.streamtools;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

import com.example.bean.TaskInfo;
import com.example.mobilesafe.R;

public class AdminTaskInfo {
	public static List<TaskInfo> adminTaskInfo(Context context) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = context.getPackageManager();
		List<RunningAppProcessInfo> info = am.getRunningAppProcesses();
		List<TaskInfo> list = new ArrayList<>();
		for (RunningAppProcessInfo infos : info) {

			TaskInfo taskinfo = new TaskInfo();
			String packageName = infos.processName;
			taskinfo.setPackageName(packageName);
			android.os.Debug.MemoryInfo[] mem = am
					.getProcessMemoryInfo(new int[] { infos.pid });
			long memsize = mem[0].getTotalPrivateDirty() * 1024l;
			taskinfo.setMemSize(memsize);
			try {
				ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
				String appName = appInfo.loadLabel(pm).toString();
				taskinfo.setAppName(appName);
				Drawable icon = appInfo.loadIcon(pm);
				taskinfo.setIcon(icon);

				if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					taskinfo.setUserProcess(true);
				} else {
					taskinfo.setUserProcess(false);
				}

			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				taskinfo.setIcon(context.getResources().getDrawable(
						R.drawable.ic_default));
				taskinfo.setAppName(packageName);

			}
			list.add(taskinfo);

		}

		return list;

	}

}

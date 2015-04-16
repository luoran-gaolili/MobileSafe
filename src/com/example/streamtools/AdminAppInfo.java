package com.example.streamtools;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.bean.App;

public class AdminAppInfo {
	public static List<App> appInfo(Context context) {
		List<App> list = new ArrayList<App>();

		try {

			// 获取包管理器
			PackageManager manager = context.getPackageManager();
			// 获取系统里面的所有安装包信息
			List<PackageInfo> info = manager.getInstalledPackages(0);
			for (PackageInfo infos : info) {
				App app = new App();
				// 获取应用的包名
				String packageName = infos.packageName;
				// 获取应用的名字
				String name = infos.applicationInfo.loadLabel(manager)
						.toString();
				// 获取应用的图标
				Drawable drawable = infos.applicationInfo.loadIcon(manager);
				int flags = infos.applicationInfo.flags;
				if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					// 用户程序
					app.setUserApp(true);
				} else {
					// 系统程序
					app.setUserApp(false);
				}
				if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) {
					// 手机内存
					app.setRom(true);
				} else {
					// 外部存储设备
					app.setRom(false);
				}

				app.setAppName(name);
				app.setIcon(drawable);
				app.setPackName(packageName);
				list.add(app);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}

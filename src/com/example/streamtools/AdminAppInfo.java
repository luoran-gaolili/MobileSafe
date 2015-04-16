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

			// ��ȡ��������
			PackageManager manager = context.getPackageManager();
			// ��ȡϵͳ��������а�װ����Ϣ
			List<PackageInfo> info = manager.getInstalledPackages(0);
			for (PackageInfo infos : info) {
				App app = new App();
				// ��ȡӦ�õİ���
				String packageName = infos.packageName;
				// ��ȡӦ�õ�����
				String name = infos.applicationInfo.loadLabel(manager)
						.toString();
				// ��ȡӦ�õ�ͼ��
				Drawable drawable = infos.applicationInfo.loadIcon(manager);
				int flags = infos.applicationInfo.flags;
				if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					// �û�����
					app.setUserApp(true);
				} else {
					// ϵͳ����
					app.setUserApp(false);
				}
				if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) {
					// �ֻ��ڴ�
					app.setRom(true);
				} else {
					// �ⲿ�洢�豸
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

package com.example.streamtools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

public class AdminRunningProcess {
	private static ActivityManager manager;

	public static int adminProcessCount(Context context) {
		manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> process = manager.getRunningAppProcesses();
		return process.size();
	}

	public static long adminAvaiableMem(Context context) {
		manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo memory = new MemoryInfo();
		manager.getMemoryInfo(memory);
		return memory.availMem;
	}

	public static long adminTotalMem() {
		long data = 0;
		try {
			File file = new File("/proc/meminfo");
			FileInputStream stream = new FileInputStream(file);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(stream));
			String line = br.readLine();
			StringBuilder buileder = new StringBuilder();
			for (char c : line.toCharArray()) {
				if (c >= '0' && c <= '9') {
					buileder.append(c);
				}
			}
			// 返回的是kb，需要装化为byte
			data = Long.parseLong(buileder.toString()) * 1024;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
}

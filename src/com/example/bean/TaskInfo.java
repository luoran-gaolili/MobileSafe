package com.example.bean;

import android.graphics.drawable.Drawable;

public class TaskInfo {
	private String packageName;
	private Drawable icon;
	private String appName;
	boolean userProcess;
	private long memSize;
	boolean isChecked;

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public TaskInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public boolean isUserProcess() {
		return userProcess;
	}

	public void setUserProcess(boolean userProcess) {
		this.userProcess = userProcess;
	}

	public long getMemSize() {
		return memSize;
	}

	public void setMemSize(long memSize) {
		this.memSize = memSize;
	}

	@Override
	public String toString() {
		return "TaskInfo [packageName=" + packageName + ", icon=" + icon
				+ ", appName=" + appName + ", userProcess=" + userProcess
				+ ", memSize=" + memSize + "]";
	}

}

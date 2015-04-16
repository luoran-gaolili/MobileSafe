package com.example.bean;

import android.graphics.drawable.Drawable;

public class App {
	private String appName;
	private Drawable icon;
	private String packName;
	private boolean userApp;
	private boolean Rom;

	public App() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public boolean isUserApp() {
		return userApp;
	}

	public void setUserApp(boolean userApp) {
		this.userApp = userApp;
	}

	public boolean isRom() {
		return Rom;
	}

	public void setRom(boolean rom) {
		Rom = rom;
	}

	@Override
	public String toString() {
		return "App [appName=" + appName + ", icon=" + icon + ", packName="
				+ packName + ", userApp=" + userApp + ", Rom=" + Rom + "]";
	}

}

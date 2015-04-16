package com.example.service;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class LocationService extends Service {
	LocationManager manger;
	String provider;

	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		manger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		List<String> providerList = manger.getProviders(true);
		if (providerList.contains(LocationManager.GPS_PROVIDER)) {
			provider = LocationManager.GPS_PROVIDER;
		} else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
			provider = LocationManager.NETWORK_PROVIDER;
		} else {
			Toast.makeText(getApplicationContext(), "没有找到可用设备",
					Toast.LENGTH_SHORT).show();
		}
		Location location = manger.getLastKnownLocation(provider);
		if (location != null) {
			showLocation(location);
		}
		manger.requestLocationUpdates(provider, 5000, 5, locationListener);

	}

	private LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			showLocation(location);
		}
	};

	private void showLocation(Location location) {
		// TODO Auto-generated method stub
		String longitude = "j:" + location.getLongitude() + "\n";
		String latitude = "w:" + location.getLatitude() + "\n";
		String accuracy = "a" + location.getAccuracy() + "\n";
		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("lastlocation", longitude + latitude + accuracy);
		editor.commit();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		manger.removeUpdates(locationListener);
		locationListener = null;
	}

}

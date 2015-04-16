package com.example.receiver;

import com.example.streamtools.QueryNumberLocation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ShowCallNumberReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		String number = getResultData();
		String numberLocationAddress = QueryNumberLocation
				.queryLocation(number);
		Toast.makeText(context, numberLocationAddress, 1).show();

	}

}

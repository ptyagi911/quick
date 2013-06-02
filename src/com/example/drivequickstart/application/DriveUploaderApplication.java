package com.example.drivequickstart.application;

import com.example.drivequickstart.Constants;
import com.example.drivequickstart.Logger;
import com.example.drivequickstart.WifiMobileDataNetworkMonitor;

import android.app.Application;

public class DriveUploaderApplication extends Application {

	private static WifiMobileDataNetworkMonitor wifiStateMonitor;
	
	@Override
	public void onCreate() {
		if (wifiStateMonitor == null) {
			wifiStateMonitor = new WifiMobileDataNetworkMonitor();
			Logger.debug(Constants.TAG_DriveUploaderApplication, "Initiated wifiMonitor once");
		}
		
		wifiStateMonitor.isNetworkAvailable(getApplicationContext());
		
		wifiStateMonitor.registerWifiStateChangedReceiver(getApplicationContext());
	}
}

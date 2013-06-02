package com.example.drivequickstart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * Monitors wifi state to check when wifi is connected
 * or disconnected
 * @author ptyagi
 *
 */
public class WifiMobileDataNetworkMonitor {

	private static WifiStateChangedReceiver wifiStateChangedReceiver;
	
	public boolean isNetworkAvailable(Context context) {
		boolean isNetworkAvailable = false;
		
		ConnectivityManager cm = 
				(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isMobile = activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
		boolean isWifi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
		
		if (isMobile) {
			Logger.debug(Constants.TAG_WifiMobileDataNetworkMonitor, "Mobile network is connected");
		}
		
		if (isWifi) {
			Logger.debug(Constants.TAG_WifiMobileDataNetworkMonitor, "Wifi is connected");
		}
		
		isNetworkAvailable = (isMobile || isWifi) == true ? true : false;
		
		return isNetworkAvailable;
	}
	
	/**
	 * Register wifiStateChanged receiver.
	 * Main activity register itself with it to get all 
	 * notifications of change in wifi state
	 * @param context
	 */
	public void registerWifiStateChangedReceiver(Context context) {
		if (wifiStateChangedReceiver == null) {
			wifiStateChangedReceiver = new WifiStateChangedReceiver();
		}
		
		context.registerReceiver(wifiStateChangedReceiver,
	              new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
	}
	
	private class WifiStateChangedReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int currentWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 
					WifiManager.WIFI_STATE_UNKNOWN);
			
			String currentState = "";
			
			switch (currentWifiState) {
//				case WifiManager.WIFI_MODE_FULL:
//					currentState = "WIFI_MODE_FULL (does peridoc scans for remembered networks)";
//					break;
//				case WifiManager.WIFI_MODE_FULL_HIGH_PERF:
//					currentState = "WIFI_MODE_FULL_HIGH_PERF (Keeps an active wifi connection. battery drain)";
//					break;
//				case WifiManager.WIFI_MODE_SCAN_ONLY:
//					currentState = "WIFI_MODE_SCAN_ONLY";
//					break;
				case WifiManager.WIFI_STATE_DISABLED:
					currentState = "WIFI_STATE_DISABLED";
					break;
				case WifiManager.WIFI_STATE_DISABLING:
					currentState = "WIFI_STATE_DISABLING";
					break;
				case WifiManager.WIFI_STATE_ENABLED:
					currentState = "WIFI_STATE_ENABLED";
					break;
				case WifiManager.WIFI_STATE_ENABLING:
					currentState = "WIFI_STATE_ENABLING";
					break;
				case WifiManager.WIFI_STATE_UNKNOWN:
					currentState = "WIFI_STATE_UNKNOWN";
					break;
				default:
					currentState = "Default:"+currentWifiState;
			}
			
			Logger.debug(Constants.TAG_WifiMobileDataNetworkMonitor, "Current WIFI State: "+currentState);			
		}
	}
}

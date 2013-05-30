package com.example.drivequickstart;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class UploaderService extends Service {

	private final String TAG = getClass().getSimpleName();
	
	private boolean isActivityForeground = false;
	private boolean isScreenOn = false;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		isActivityForeground = true;
		isScreenOn = true;
		
		scheduler();
	}
	
	public void onDestroy() {
		super.onDestroy();
		
		Log.d(TAG, "App is destroyed");
	}
	
	private void scheduler() {
		Timer activityCheck = new Timer(true);
		activityCheck.schedule(new TimerTask() {
			
			@Override
			public void run() {
				isActivityForeground = isActivityForeground();
				isScreenOn = isScreenOn();
				
				if (isActivityForeground && isScreenOn) {
					Log.d(TAG, "Activity is Foreground");
				} else {
					Log.d(TAG, "Activity is background");
				}
			}
		}, 0, 1 * 1000);
	}
	
	private boolean isActivityForeground() {
		boolean isForeground = false;
		
		ActivityManager activityManager = 
				(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningProcesses = 
				activityManager.getRunningAppProcesses();
		
		for (RunningAppProcessInfo processInfo : runningProcesses) {
			if (processInfo.importance == 
					RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				if (String.valueOf(this.getPackageName()).
						equals(String.valueOf(processInfo.processName))) {
					isForeground = true;
				}
			}
		}
		
		return isForeground;
	}
	
	private boolean isScreenOn() {
		PowerManager powerManager = 
				(PowerManager)getSystemService(Context.POWER_SERVICE);
		return powerManager.isScreenOn();
	}
}

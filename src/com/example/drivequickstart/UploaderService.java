package com.example.drivequickstart;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import com.example.drivequickstart.model.DataModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class UploaderService extends Service {

	private final String TAG = getClass().getSimpleName();
	
	private boolean isActivityForeground = false;
	private boolean isScreenOn = false;

    private static int counter = 1;
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

                if (counter < 100)
                    cacheMediaFiles();

				if (isActivityForeground && isScreenOn) {
					Log.d(TAG, "Activity is Foreground");
				} else {
					Log.d(TAG, "Activity is background");
				}
			}
		}, 0, 1 * 1000);
	}

    private void cacheMediaFiles() {
        HashMap<String, File> files = DataModel.getDCIMCameraRoll(1);
        for(HashMap.Entry<String, File> entry : files.entrySet()) {
            counter++;
            File file = entry.getValue();
            System.out.println("Media File Size:" + file.length());

            long lastModified = file.lastModified();
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd-HH");//dd/MM/yyyy
            Date now = new Date();
            String strDate = sdfDate.format(lastModified);
            System.out.println(strDate);

            System.out.println("Media File last modified:" + strDate);

        }
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

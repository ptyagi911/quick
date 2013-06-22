package com.example.drivequickstart.model;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import com.example.drivequickstart.view.MyListAdapter.ThumbnailTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class UploaderService extends Service {

	private final String TAG = getClass().getSimpleName();
	
	private boolean isActivityForeground = false;
	private boolean isScreenOn = false;

    private Context appContext;
    private FileObserver fileObserver;

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

        appContext = getApplication().getApplicationContext();

        String dirToWatch = DataModel.getCameraRollDir();
        fileObserver = new FileObserver(dirToWatch);
        fileObserver.startWatching();

        cacheMediaFiles();
		//scheduler();
	}
	
	public void onDestroy() {
		super.onDestroy();

        fileObserver.stopWatching();
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
		}, 0, 10 * 1000);
	}

    private void cacheMediaFiles() {
        HashMap<String, File> files = DataModel.getDCIMCameraRoll(1);
        for(HashMap.Entry<String, File> entry : files.entrySet()) {
            counter++;
            File file = entry.getValue();
            System.out.println("Media File Size:" + file.length());

            try {
            storeMediaFileState(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Generates directory based on the last modified date
     */
    public void storeMediaFileState(File file) throws Exception {
        long lastModified = file.lastModified();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd-HH");//dd/MM/yyyy
        String strDate = sdfDate.format(lastModified);
        System.out.println(strDate);

        System.out.println("Media File last modified:" + strDate);
        String[] tokens = strDate.split("-");

        String year = tokens[0];
        String month = tokens[1];
        String day = tokens[2];
        String hour = tokens[3];

        String stateFileName = file.getName().replace(".jpg", ".state");
        //stateFileName = file.getName().replace(".mp4", ".state");
        File stateFile = new File(year+"/"+month+"/"+day+"/"+hour+"/"+stateFileName);

        if (!stateFile.exists()) {
            //stateFile.createNewFile();
            stateFile.mkdirs();
        }

        MediaFile mediaFile = new MediaFile();
        mediaFile.setName(stateFileName);
        mediaFile.setSize(file.length());
        mediaFile.setStateFile(stateFile);

        new ThumbnailTask(-1, null, DataModel.MODE_PICTURES, mediaFile, appContext).execute(file.getAbsolutePath());
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

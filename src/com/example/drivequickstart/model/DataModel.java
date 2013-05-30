package com.example.drivequickstart.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import android.os.Environment;
import android.util.Log;

public class DataModel {

	private static String TAG = "DriveUploader-DataModel";
	
	public static final int MODE_PICTURES = 1;
	public static final int MODE_VIDEOS = 2;
	
	public static ArrayList<String> getItems(int mode) {
		ArrayList<String> items = new ArrayList<String>();
		
		HashMap<String, File> files = getDCIMCameraRoll(mode);
		
		for (Entry<String, File>entry : files.entrySet()) {
			items.add(entry.getKey());
		}
		return items;
	}
	
	public static HashMap<String, File> getDCIMCameraRoll(int mode) {
		HashMap<String, File> mediaFiles = new HashMap<String, File>();
		
		if (!isMediaMounted()) {
			Log.e(TAG, "Media is not mounted");
			return mediaFiles;
		}

		File dcimCameraDir = new File(Environment.getExternalStorageDirectory(),
				"DCIM/Camera");
		
		if (!dcimCameraDir.canRead()) {
			Log.e(TAG, "CameraRoll is not readable");
			return mediaFiles;
		}
		
		File[] files = dcimCameraDir.listFiles();
		
		for(int i=0; i < files.length; i++) {
			String fileName = files[i].getName();
			
			if ((mode == MODE_VIDEOS) && fileName.endsWith(".mp4")) {
				mediaFiles.put(fileName, files[i]);
			} else if ((mode == MODE_PICTURES) && fileName.endsWith(".jpg")) {
				mediaFiles.put(fileName, files[i]);
			}
		}
		
		return mediaFiles;
	}
	
	public static boolean isMediaMounted() {
		boolean mounted = false;
		
		mounted = Environment.MEDIA_MOUNTED.equals(
				Environment.getExternalStorageState()) ?
						true : false;
		
		return mounted;
	}
}

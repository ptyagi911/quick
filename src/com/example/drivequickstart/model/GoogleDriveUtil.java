package com.example.drivequickstart.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;

public class GoogleDriveUtil {

	private static String TAG = "DriveUploader-GoogleDriveUtil";
	
	private static Drive mService;
	
	public static Drive getDriveServiceInstance() {
		if (mService == null) {
			GoogleAccountCredential credentials =
					GoogleAccountManager.getGoogleAccountCredential();
			mService = getDriveService(credentials);
		}
		
		return mService;
	}
	
	/**
	 * Gets google drive service instance
	 * @param credential
	 * @return
	 */
	private static Drive getDriveService(GoogleAccountCredential credential) {
		if (credential instanceof GoogleAccountCredential) {
			Drive.Builder builder = new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential);
	  		builder.setApplicationName("Test Application");
	  		return builder.build();
		}
		
		Log.e(TAG, "GoogleAccountCredential is NULL");
		return null;
	}
	
	/**
	 * Upload given media file to Google Drive
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private static boolean  uploadFileToGoogleDrive(File file) throws IOException {
		boolean success = false;
		
		if (!file.exists()) {
			Log.e(TAG, "Doesn't exist File : "+ file);
			return false;
		}
		
		String fileName = file.getName();
		
		com.google.api.services.drive.model.File driveFile = 
				new com.google.api.services.drive.model.File();
		
		FileContent driveFileContent = null;
		
		if (fileName.startsWith("VID_")) {
			driveFileContent = new FileContent("video/mp4", file);
			driveFile.setMimeType("video/mp4");
		} else if (fileName.startsWith("IMG_")) {
			driveFileContent = new FileContent("image/jpeg", file);
			driveFile.setMimeType("image/jpeg");
		}
		
		driveFile.setTitle(fileName);
		
		if (mService != null) {
			com.google.api.services.drive.model.File uploadedFile =
					mService.files().insert(driveFile, driveFileContent).execute();
			
			if (uploadedFile != null) {
				success = true;
			}
		}
			
		return success;
	}
	
	/**
	 * Pass the array of files to be uploaded
	 * @param mediaFiles
	 */
	public static void saveFilesToDrive(final ArrayList<File> mediaFiles) {
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for (File file : mediaFiles) {
					try {
						uploadFileToGoogleDrive(file);
					} catch (IOException e) {
						Log.e(TAG, "Failed to upload file to google drive: " + 
								file.getAbsolutePath());
						e.printStackTrace();
					}
				}
			}
		});
		
		t.start();
	}
}

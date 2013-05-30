package com.example.drivequickstart.model;

import android.content.Context;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.drive.DriveScopes;

public class GoogleAccountManager {

	private static String TAG = "DriveUploader-GoogleAccountManager";
	
	private static Context mAppContext;
	private static GoogleAccountCredential mCredential;
	
	public static void setGoogleAccountCredential(Context context) {
		mAppContext = context;
		mCredential = GoogleAccountCredential.usingOAuth2(mAppContext, DriveScopes.DRIVE);
	}
	
	public static GoogleAccountCredential getGoogleAccountCredential() {
		return mCredential;
	}
	
	
	
}

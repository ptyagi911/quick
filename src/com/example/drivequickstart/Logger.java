package com.example.drivequickstart;

import android.util.Log;

public class Logger {

	private static boolean isDebug = true;
	
	public static void setDebug(boolean debug) {
		isDebug = debug;
	}
	
	public static void debug(String tag, String message) {
		if (isDebug) {
			Log.d(tag, message);
		}
	}
	
	public static void error(String tag, String message) {
		Log.e(tag, message);
	}
}

package com.example.drivequickstart.model;

import android.content.Context;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
	
	public static void getNowDirectory() {
		StringBuilder dirPath = new StringBuilder();
		
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd-HH");//dd/MM/yyyy
		Date now = new Date();
		String strDate = sdfDate.format(now);
		System.out.println(strDate);
		
		String[] timeStampTokens = strDate.split("-");
		if (timeStampTokens.length != 4) {
			return;
		}
		
		File dirYear = new File(timeStampTokens[0]);
		File dirMonth = new File(timeStampTokens[1]);
		File dirDay = new File(timeStampTokens[2]);
		File dirHour = new File(timeStampTokens[3]);
		
		if (dirYear.exists()) {
			if (dirMonth.exists()) {
				
			}
		}
	}
	
	public static void performFileOps(Context context) throws Exception {
		String storageFile = "priyanka.txt";
		
		JSONObject data = new JSONObject();
		data.put("priyanka", "tyagi");
		writeToInternalStorage(context, data, storageFile);
		JSONObject data2 = new JSONObject();
		data2.put("company", "disney");
		writeToInternalStorage(context, data2, storageFile);
		
		readFromInternalStorage(context, storageFile);
	}
	
	/**
	 * Writes to internal storage
	 * 
	 * @param context
	 * @param data
	 * @param storageFile
	 * @throws java.io.IOException
	 * @throws org.json.JSONException
	 */
	public static void writeToInternalStorage(Context context,
			Object data, String storageFile)
					throws IOException, JSONException {
		File file = new File(context.getFilesDir(), storageFile);
		BufferedWriter bw = null;
		
		try {
			
			FileWriter fileWriter = new FileWriter(file);
			bw = new BufferedWriter(fileWriter);
			String text = data.toString();
			Log.d("PRIYANKA-WRITE", text);
			bw.write(text);
	        bw.newLine();
		} finally {
			if (bw != null) {
				bw.close();
			}
		}
	}
	
	/**
	 * Reads data data from internal storage
	 * @param context
	 * @param storageFile
	 * @return
	 * @throws java.io.IOException
	 * @throws org.json.JSONException
	 */
	public static JSONObject readFromInternalStorage(Context context,
			String storageFile) 
			throws IOException, JSONException {
		
		FileInputStream fin = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			fin = context.openFileInput(storageFile);
			InputStreamReader inputStreamReader = new InputStreamReader(fin);
			BufferedReader bufferedReader = 
					new BufferedReader(inputStreamReader);
			sb = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				Log.d("PRIYANKA-READ", line);
				sb.append(line);
			}
		} finally {
			if (fin != null) {
				fin.close();
			}
		}
		
		return new JSONObject(sb.toString());
	}
}

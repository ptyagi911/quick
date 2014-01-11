package com.example.drivequickstart.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DBController extends SQLiteOpenHelper {
	private static final String LOGCAT = null;

	public DBController(Context applicationcontext) {
        super(applicationcontext, "androidbot1.db", null, 1);
        Log.d(LOGCAT, "Created");
    }
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		String query;
		query = "CREATE TABLE media (mediaId INTEGER PRIMARY KEY, mediaType INTEGER, fileName TEXT, thumbnail BLOB)";
        database.execSQL(query);
        Log.d(LOGCAT, "media record Created");
	}
	@Override
	public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
		String query;
		query = "DROP TABLE IF EXISTS media";
		database.execSQL(query);
        onCreate(database);
	}
	
	public synchronized void insertMediaRow(HashMap<String, String> queryValues) {
        try {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
        values.put("mediaType", queryValues.get("mediaType"));
		values.put("fileName", queryValues.get("fileName"));
        values.put("thumbnail", queryValues.get("thumbnail"));
		database.insert("media", null, values);
		database.close();
        } catch (Exception e) {
            Log.v("quick", "DBException: " + e.getMessage());
        }
	}
	
	public int updateMediaRow(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();
	    ContentValues values = new ContentValues();
        values.put("mediaType", queryValues.get("mediaType"));
	    values.put("fileName", queryValues.get("fileName"));
        values.put("thumbnail", queryValues.get("thumbnail"));
	    return database.update("media", values, "mediaId" + " = ?", new String[] { queryValues.get("mediaId") });
	    //String updateQuery = "Update  words set txtWord='"+word+"' where txtWord='"+ oldWord +"'";
	    //Log.d(LOGCAT,updateQuery);
	    //database.rawQuery(updateQuery, null);
	    //return database.update("words", values, "txtWord  = ?", new String[] { word });
	}
	
	public void deleteMediaRow(String id) {
		Log.d(LOGCAT, "delete");
		SQLiteDatabase database = this.getWritableDatabase();
		String deleteQuery = "DELETE FROM media where mediaId='"+ id +"'";
		Log.d("query", deleteQuery);
		database.execSQL(deleteQuery);
	}
	
	public ArrayList<MediaFile> getAllMediaFiles() {
        ArrayList<MediaFile> mediaList = new ArrayList<MediaFile>();
        try {

		String selectQuery = "SELECT * FROM media";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	        do {
                Object mediaId = cursor.getString(0);
                Object mediaType = cursor.getString(1);
                Object fileName = cursor.getString(2);
                Object thumbnailBlob = cursor.getString(3);

                MediaFile mediaFile = new MediaFile();
                mediaFile.setMediaId(Integer.valueOf((String)mediaId));
	        	mediaFile.setMediaType(Integer.valueOf((String)mediaType));
                mediaFile.setFileName((String) fileName);
                mediaFile.setThumbnailBlob(thumbnailBlob);

                mediaList.add(mediaFile);

	        } while (cursor.moveToNext());
	    }
        } catch (Exception e) {
            Log.v("quick", "DBException: " + e.getMessage());
        }
	    // return contact list
	    return mediaList;
	}
	
	public HashMap<String, String> getMediaInfo(String id) {
		HashMap<String, String> map = new HashMap<String, String>();
		SQLiteDatabase database = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM media where mediaId='"+id+"'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
	        do {
					//HashMap<String, String> map = new HashMap<String, String>();
                map.put("mediaId", cursor.getString(0));
                map.put("mediaType", cursor.getString(1));
                map.put("fileName", cursor.getString(2));
                map.put("thumbnail", cursor.getString(3));
				   //wordList.add(map);
	        } while (cursor.moveToNext());
	    }				    
	return map;
	}	
}

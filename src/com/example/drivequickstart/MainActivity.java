package com.example.drivequickstart;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

public class MainActivity extends Activity {
  static final int REQUEST_ACCOUNT_PICKER = 1;
  static final int REQUEST_AUTHORIZATION = 2;
  static final int CAPTURE_IMAGE = 3;

  private static Uri fileUri;
  private static Drive service;
  private GoogleAccountCredential credential;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
   
    //Start the uploader service
    //this.startService(new Intent(this, UploaderService.class));
    
    //This code should go under uploader service
    credential = GoogleAccountCredential.usingOAuth2(this, DriveScopes.DRIVE);
    startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
  }

  @Override
  protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    switch (requestCode) {
    case REQUEST_ACCOUNT_PICKER:
      if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
        String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        if (accountName != null) {
          credential.setSelectedAccountName(accountName);
          service = getDriveService(credential);
          //startCameraIntent();
          saveFileToDrive();
        }
      }
      break;
    case REQUEST_AUTHORIZATION:
      if (resultCode == Activity.RESULT_OK) {
        saveFileToDrive();
      } else {
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
      }
      break;
    case CAPTURE_IMAGE:
      if (resultCode == Activity.RESULT_OK) {
        saveFileToDrive();
      }
    }
  }

  private void startCameraIntent() {
    String mediaStorageDir = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_PICTURES).getPath();
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
    fileUri = Uri.fromFile(new java.io.File(mediaStorageDir + java.io.File.separator + "IMG_"
        + timeStamp + ".jpg"));

    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
    startActivityForResult(cameraIntent, CAPTURE_IMAGE);
  }

  private void saveFileToDrive() {
    Thread t = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
        	java.io.File path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MOVIES);
            //File file = new File(path, "DemoPicture.jpg");
        	//File file = new File(path, "DemoPicture.jpg");
        	
          // File's binary content
          String filePath = "/storage/sdcard0/Pictures/IMG_20130419_210205.jpg";
          //java.io.File fileContent = new java.io.File(fileUri.getPath());
//          java.io.File fileContent = new java.io.File(Environment.getExternalStorageDirectory(), "DCIM/Camera/VID_20130112_120112.mp4");
          java.io.File fileContent = new java.io.File(Environment.getExternalStorageDirectory(), "DCIM/Camera/VID_20130112_120112.mp4");
          boolean isExternalStorageAvailable = checkExternalStorageAvailabilty();
          
          ///storage/emulated/0/Pictures/Instagram/IMG_20130418_-73048.jpg
    	  if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

    		  java.io.File pictureDir = new java.io.File(Environment.getExternalStorageDirectory(), "DCIM/Camera");
        	  //getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        	  ///storage/sdcard0/Pictures
        	  boolean canRead = pictureDir.canRead();
        	  //false
        	  java.io.File[] picFiles = pictureDir.listFiles();
        	  for (int i=0; i<picFiles.length; i++) {
        		  System.out.println(picFiles[i]);
        		  ///storage/emulated/0/DCIM/Camera/IMG_20121224_091527.JPG
        		  String fileName = picFiles[i].getName();
        		  if (fileName.startsWith("VID_")) {
        			  showToast("Uploading video..."+fileName);
            		  java.io.File vid = picFiles[i];
            		  
            		  if (vid.exists()) {
            			  FileContent mediaContent = new FileContent("video/mp4", vid);
            				
            	          // File's metadata.
            	          File body = new File();
            	          body.setTitle(vid.getName());
            	          body.setMimeType("video/mp4");
            	
            	          //if (GooglePlayServicesUtil.isGooglePlayServicesAvailable()) {
            		          File file = service.files().insert(body, mediaContent).execute();
            		          if (file != null) {
            		            showToast("Video uploaded: " + file.getTitle());
            		            //startCameraIntent();
            		          }
            		  }
        		  }
        	  }
          }
    	  
    	  //uploading photo
          if (fileContent.exists()) {
	          FileContent mediaContent = new FileContent("image/jpeg", fileContent);
	
	          // File's metadata.
	          File body = new File();
	          body.setTitle(fileContent.getName());
	          body.setMimeType("image/jpeg");
	
	          //if (GooglePlayServicesUtil.isGooglePlayServicesAvailable()) {
		          File file = service.files().insert(body, mediaContent).execute();
		          if (file != null) {
		            showToast("Photo uploaded: " + file.getTitle());
		            //startCameraIntent();
		          }
	          //}
          } else {
        	  System.out.println("File doesn't seem to exist: "+path.getAbsolutePath());
          }
        } catch (UserRecoverableAuthIOException e) {
          startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
    t.start();
  }

  private boolean checkExternalStorageAvailabilty() {
	  boolean mExternalStorageAvailable = false;
	  boolean mExternalStorageWriteable = false;
	  String state = Environment.getExternalStorageState();

	  if (Environment.MEDIA_MOUNTED.equals(state)) {
	      // We can read and write the media
	      mExternalStorageAvailable = mExternalStorageWriteable = true;
	  } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	      // We can only read the media
	      mExternalStorageAvailable = true;
	      mExternalStorageWriteable = false;
	  } else {
	      // Something else is wrong. It may be one of many other states, but all we need
	      //  to know is we can neither read nor write
	      mExternalStorageAvailable = mExternalStorageWriteable = false;
	  }
	  
	  return mExternalStorageAvailable;
  }
  
  	private Drive getDriveService(GoogleAccountCredential credential) {
  		Drive.Builder builder = new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential);
  		builder.setApplicationName("Test Application");
  		return builder.build();
  	}

  public void showToast(final String toast) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
      }
    });
  }
}
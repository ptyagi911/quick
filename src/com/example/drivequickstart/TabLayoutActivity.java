package com.example.drivequickstart;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;


public class TabLayoutActivity extends TabActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tablayout_activity);
		
		TabHost tabHost = getTabHost();
		
		//Add tab for photos
		TabSpec photoSpec = tabHost.newTabSpec("photos");
		photoSpec.setIndicator("Photos", 
				getResources().getDrawable(R.drawable.icon_photos_tab));
		Intent photosIntent = new Intent(this, PhotosActivity.class);
		photoSpec.setContent(photosIntent);
		
		//Add Tab For Videos
		TabSpec videoSpec = tabHost.newTabSpec("videos");
		videoSpec.setIndicator("Videos", 
				getResources().getDrawable(R.drawable.icon_videos_tab));
		Intent videosIntent = new Intent(this, VideosActivity.class);
		videoSpec.setContent(videosIntent);
		
		
		tabHost.addTab(photoSpec);
		tabHost.addTab(videoSpec);
	}
}

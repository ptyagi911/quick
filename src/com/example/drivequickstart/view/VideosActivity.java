package com.example.drivequickstart.view;

import android.os.Bundle;
import com.example.drivequickstart.model.DataModel;


public class VideosActivity extends BaseActivity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, DataModel.MODE_VIDEOS);
	}
}

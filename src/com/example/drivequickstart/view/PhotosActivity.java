package com.example.drivequickstart.view;

import com.example.drivequickstart.model.DataModel;
import android.os.Bundle;

public class PhotosActivity extends BaseActivity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, DataModel.MODE_PICTURES);
	}
}
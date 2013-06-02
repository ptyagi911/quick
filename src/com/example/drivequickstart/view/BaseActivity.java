package com.example.drivequickstart.view;

import java.util.ArrayList;
import java.util.List;

import com.example.drivequickstart.R;
import com.example.drivequickstart.model.DataModel;

import com.haarman.listviewanimations.itemmanipulation.AnimateDismissAdapter;
import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class BaseActivity extends Activity {
	
	private List<Integer> mSelectedPositions;
	private MyListAdapter mAdapter;
	
	public void onCreate(Bundle savedInstanceState, int mediaMode) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animateremoval);
		
		mSelectedPositions = new ArrayList<Integer>();
		
		ListView listView = (ListView) findViewById(R.id.activity_animateremoval_listview);
		mAdapter = new MyListAdapter(this, DataModel.getItems(mediaMode), mediaMode);
		
		final AnimateDismissAdapter<String> animateDismissAdapter = 
				new AnimateDismissAdapter<String>(mAdapter, new MyOnDismissCallback());
		animateDismissAdapter.setListView(listView);
		listView.setAdapter(animateDismissAdapter);
		
		Button button = (Button) findViewById(R.id.activity_animateremoval_button);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				animateDismissAdapter.animateDismiss(mSelectedPositions);
				mSelectedPositions.clear();
			}
		});
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				CheckedTextView ctv = (CheckedTextView) ((LinearLayout) view).getChildAt(1);
				ctv.toggle();
				if (ctv.isChecked()) {
					mSelectedPositions.add(position);
				} else {
					mSelectedPositions.remove((Integer) position);
				}				
			}
		});
	}
	
	private class MyOnDismissCallback implements OnDismissCallback {

		@Override
		public void onDismiss(ListView listView, int[] reverseSortedPositions) {
			for (int position : reverseSortedPositions) {
				mAdapter.remove(position); //write code to update the status of the row
			}
		}		
	}
	
	public List<Integer> getSelectedPositions() {
		return mSelectedPositions;
	}
}

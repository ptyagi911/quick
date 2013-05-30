package com.example.drivequickstart;

import java.util.ArrayList;
import java.util.List;


import com.example.drivequickstart.model.DataModel;
import com.haarman.listviewanimations.ArrayAdapter;
import com.haarman.listviewanimations.itemmanipulation.AnimateDismissAdapter;
import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class VideosActivity extends Activity {
	
	private List<Integer> mSelectedPositions;
	private MyListAdapter mAdapter;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animateremoval);
		
		mSelectedPositions = new ArrayList<Integer>();
		
		ListView listView = (ListView) findViewById(R.id.activity_animateremoval_listview);
		mAdapter = new MyListAdapter(DataModel.getItems(DataModel.MODE_VIDEOS));
		
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
				CheckedTextView ctv = (CheckedTextView) view;
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
				mAdapter.remove(position);
			}
		}		
	}
	
	private class MyListAdapter extends ArrayAdapter<String> {
		
		public MyListAdapter(ArrayList<String> items) {
			super(items);
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CheckedTextView ctv = (CheckedTextView)convertView;
			
			if (ctv == null) {
				ctv = (CheckedTextView) LayoutInflater.
						from(VideosActivity.this).
						inflate(R.layout.activity_animateremoval_row, parent, false);
			}
			ctv.setText(getItem(position));
			ctv.setChecked(mSelectedPositions.contains(position));
			return ctv;
		}
	}
}

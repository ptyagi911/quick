/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.drivequickstart.view;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.example.drivequickstart.R;
import com.example.drivequickstart.model.DBController;
import com.example.drivequickstart.model.DataModel;
import com.example.drivequickstart.model.MediaFile;
import com.example.drivequickstart.model.MediaStore;

import java.util.ArrayList;

/**
 * A list view example where the data comes from a custom ListAdapter
 */
public class List4 extends ListActivity {

    DBController dbController = new DBController(this);
    private DataModel dataModel = new DataModel(dbController);
    ArrayList<MediaFile> mediaStore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Fill-in Database
        dataModel.getDCIMCameraRoll(DataModel.MODE_PICTURES);

        mediaStore = MediaStore.getMediaStore(dbController);

        ListView lv = getListView();
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lv.setMultiChoiceModeListener(new ModeCallback());
        // Use our own list adapter
        setListAdapter(new SpeechListAdapter(this));
    }
    private class ModeCallback implements ListView.MultiChoiceModeListener {

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.list_select_menu, menu);
            mode.setTitle("Select Items");
            setSubtitle(mode);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            /*switch (item.getItemId()) {
                case R.id.share:
                    Toast.makeText(List4.this, "Shared " + getListView().getCheckedItemCount() +
                            " items", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    break;
                default:
                    Toast.makeText(List4.this, "Clicked " + item.getTitle(),
                            Toast.LENGTH_SHORT).show();
                    break;
            }*/
            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        public void onItemCheckedStateChanged(ActionMode mode,
                                              int position, long id, boolean checked) {
            setSubtitle(mode);
        }

        private void setSubtitle(ActionMode mode) {
            final int checkedCount = getListView().getCheckedItemCount();
            switch (checkedCount) {
                case 0:
                    mode.setSubtitle(null);
                    break;
                case 1:
                    mode.setSubtitle("One item selected");
                    break;
                default:
                    mode.setSubtitle("" + checkedCount + " items selected");
                    break;
            }
        }
    }

    /**
     * A sample ListAdapter that presents content from arrays of speeches and
     * text.
     * 
     */
    private class SpeechListAdapter extends BaseAdapter {

        public SpeechListAdapter(Context context) {
            mContext = context;
        }

        /**
         * The number of items in the list is determined by the number of speeches
         * in our array.
         * 
         * @see android.widget.ListAdapter#getCount()
         */
        public int getCount() {
            return mediaStore.size();//MediaStore.TITLES.length;
        }

        /**
         * Since the data comes from an array, just returning the index is
         * sufficent to get at the data. If we were using a more complex data
         * structure, we would return whatever object represents one row in the
         * list.
         * 
         * @see android.widget.ListAdapter#getItem(int)
         */
        public Object getItem(int position) {
            return mediaStore.get(position);
        }

        /**
         * Use the array index as a unique id.
         * 
         * @see android.widget.ListAdapter#getItemId(int)
         */
        public long getItemId(int position) {
            return position;
        }

        /**
         * Make a MediaView to hold each row.
         * 
         * @see android.widget.ListAdapter#getView(int, android.view.View,
         *      android.view.ViewGroup)
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            MediaView sv;
            if (convertView == null) {
//                sv = new MediaView(mContext, MediaStore.TITLES[position],
//                        MediaStore.DIALOGUE[position]);
                sv = new MediaView(mContext, mediaStore.get(position).getFileName(),
                        "Test View");
            } else {
                sv = (MediaView) convertView;
                sv.setTitle(mediaStore.get(position).getFileName());
                sv.setDialogue("Test View");
            }

            return sv;
        }

        /**
         * Remember our context so we can use it when constructing views.
         */
        private Context mContext;
    }
    
    /**
     * We will use a MediaView to display each speech. It's just a LinearLayout
     * with two text fields.
     *
     */
    private class MediaView extends LinearLayout {
        public MediaView(Context context, String title, String words) {
            super(context);

            this.setOrientation(VERTICAL);

            // Here we build the child views in code. They could also have
            // been specified in an XML file.

            mTitle = new TextView(context);
            mTitle.setText(title);
            addView(mTitle, new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            mDialogue = new TextView(context);
            mDialogue.setText(words);
            addView(mDialogue, new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            checkedTextView = new CheckedTextView(context);
            checkedTextView.setChecked(true);
            checkedTextView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("Q", "Clicked checkbox");
                }
            });

            RelativeLayout.LayoutParams checkedViewLayoutParams =
                    new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            checkedViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            addView(checkedTextView, checkedViewLayoutParams);

        }

        /**
         * Convenience method to set the title of a MediaView
         */
        public void setTitle(String title) {
            mTitle.setText(title);
        }

        /**
         * Convenience method to set the dialogue of a MediaView
         */
        public void setDialogue(String words) {
            mDialogue.setText(words);
        }

        private TextView mTitle;
        private TextView mDialogue;
        private CheckedTextView checkedTextView;
    }
}

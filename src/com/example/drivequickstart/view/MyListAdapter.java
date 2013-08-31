package com.example.drivequickstart.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.drivequickstart.Logger;
import com.example.drivequickstart.R;
import com.example.drivequickstart.model.MediaFile;
import com.haarman.listviewanimations.ArrayAdapter;

import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter<Object> {
	private BaseActivity mActivity;
	private int dataMode;
    private ArrayList<Object> itemsObject;
	
	public MyListAdapter(BaseActivity activity, ArrayList<Object> items, int dataMode) {
		super(items);
		mActivity = activity;
		this.dataMode = dataMode;
        itemsObject = items;
	}

    public int getCount() {
        return itemsObject.size();
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
        return itemsObject.get(position);
    }

    /**
     * Use the array index as a unique id.
     *
     * @see android.widget.ListAdapter#getItemId(int)
     */
    public long getItemId(int position) {
        return position;
    }

    @Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Logger.debug("Priyanka", "Getting view at position: " + position);
		ViewHolder viewHolder;
		MediaFile mediaFile  = (MediaFile)getItem(position);
        String filePath =  mediaFile.getAbsolutePath();
                String fileName = filePath.substring(filePath.lastIndexOf("/")+1);

		if (convertView == null) {
			convertView = (LinearLayout) LayoutInflater.
					from(mActivity).
					inflate(R.layout.list_item_icon_text, parent, false);
			
			viewHolder = new ViewHolder();
			viewHolder.checkedTextView = (CheckedTextView)convertView.findViewById(R.id.CheckedTextView);//getChildAt(1);
			viewHolder.imageView = (ImageView)convertView.findViewById(R.id.icon);//getChildAt(0);//(ImageView) mActivity.findViewById(R.id.thumbnail_microkind);
            viewHolder.textView = (TextView)convertView.findViewById(R.id.text);

			convertView.setTag(viewHolder);
		} else {
		    viewHolder = (ViewHolder) convertView.getTag();
        }

		viewHolder.position = position;

        viewHolder.textView.setText(mediaFile.getName());
        viewHolder.imageView.setImageBitmap(mediaFile.getThumbnail());

		return convertView;
	}

/*    public View getView1(int position, View convertView, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unneccessary calls
        // to findViewById() on each row.
        ViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_icon_text, null);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);

            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }

        // Bind the data efficiently with the holder.
        holder.text.setText(DATA[position]);
        holder.icon.setImageBitmap((position & 1) == 1 ? mIcon1 : mIcon2);

        return convertView;
    }
*/

	private static class ViewHolder {
		public ImageView imageView;
		public CheckedTextView checkedTextView;
        public TextView textView;
		public int position;
	}
}

/*

	public static Bitmap createPictureThumbnail(String fileName) {
		Bitmap imageBitmap = null;

        try
        {
            final int THUMBNAIL_SIZE = 96;

            FileInputStream fis = new FileInputStream(fileName);
            imageBitmap = BitmapFactory.decodeStream(fis);

            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        } catch (OutOfMemoryError e) {
        	System.out.println("Priyanka-Failed to create picture thumbnail: " + e.getMessage());
        	e.printStackTrace();
        } catch(Exception ex) {
        	ex.printStackTrace();
        }

        return imageBitmap;
	}

	public static class ThumbnailTask extends AsyncTask<String, Void, Bitmap> {
	    private int mPosition;
	    private ViewHolder mHolder;
	    private int mediaType;
        private MediaFile mediaFile;
        private Context context;

	    public ThumbnailTask(int position, ViewHolder holder, int mediaType, MediaFile mediaFile, Context context) {
	        mPosition = position;
	        mHolder = holder;
	        this.mediaType = mediaType;
            this.mediaFile = mediaFile;
            context = context;
	    }

	    @Override
	    protected void onPostExecute(Bitmap bitmap) {
	        if (mHolder != null && mHolder.position == mPosition) {
	            if(mHolder.imageView != null) {
	            	Logger.debug("Priyanka", "Setting image at position: " +
		        			mPosition);
	            	mHolder.imageView.setImageBitmap(bitmap);
	            	//if (this.mediaType == DataModel.MODE_PICTURES) System.out.println("Priyanka-posted image at:  " + mPosition);
	            } else {
	            	//if (this.mediaType == DataModel.MODE_PICTURES)  System.out.println("Priyanka:[Image is null]missed image at:"+mPosition);
	            	Logger.debug("Priyanka", "No Image found for position: " +
		        			mPosition);
	            }
	        } else if (context != null && this.mediaFile != null) {
                 mediaFile.setThumbnail(bitmap);
                try {
                 FileUtil.writeToInternalStorage(context, mediaFile, mediaFile.getStateFile().getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
	        	//if (this.mediaType == DataModel.MODE_PICTURES) System.out.println("Priyanka:[holder null]missed image at:"+mPosition);
	        	Logger.debug("Priyanka", "Holder is Null for position: " +
	        			mPosition);
	        }
	    }

		@Override
		protected Bitmap doInBackground(String... params) {
			String fileName = params[0];
			Bitmap image = null;

			try {
				if (this.mediaType == DataModel.MODE_PICTURES) {
					image = createPictureThumbnail(fileName);
				} else if (this.mediaType == DataModel.MODE_VIDEOS) {
					image = ThumbnailUtils.createVideoThumbnail(fileName,
				    		   Thumbnails.MICRO_KIND);
				}
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
			return image;
		}
	}
	*/
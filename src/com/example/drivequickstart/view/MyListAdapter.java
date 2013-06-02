package com.example.drivequickstart.view;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore.Video.Thumbnails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.drivequickstart.R;
import com.example.drivequickstart.model.DataModel;
import com.haarman.listviewanimations.ArrayAdapter;

public class MyListAdapter extends ArrayAdapter<String> {
	private BaseActivity mActivity;
	private int dataMode;
	
	public MyListAdapter(BaseActivity activity, ArrayList<String> items, int dataMode) {
		super(items);
		mActivity = activity;
		this.dataMode = dataMode;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		String filePath = getItem(position);
		String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
		LinearLayout lview = (LinearLayout)convertView;

		if (lview == null) {
			lview = (LinearLayout) LayoutInflater.
					from(mActivity).
					inflate(R.layout.activity_animateremoval_row, parent, false);
			
			viewHolder = new ViewHolder();
			viewHolder.ctv = (CheckedTextView)lview.getChildAt(1);
			viewHolder.image = (ImageView) mActivity.findViewById(R.id.thumbnail_microkind);
			
			lview.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) lview.getTag();
		}
		
		viewHolder.position = position;
		List<Integer>mSelectedPositions = mActivity.getSelectedPositions();
		
		//String filePath = "/storage/sdcard0/DCIM/Camera/20130518_190623_LLS.jpg";
		//"/storage/sdcard0/DCIM/Camera/20130531_120035.mp4";
		new ThumbnailTask(position, viewHolder, this.dataMode).execute(filePath);
//	    new ThumbnailTask(position, viewHolder, this.dataMode).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, filePath);
		
		if (viewHolder.ctv != null) viewHolder.ctv.setText(fileName);
		if (viewHolder.ctv != null) viewHolder.ctv.setChecked(mSelectedPositions.contains(position));
        
		return lview;
	}
	
	public static Bitmap createPictureThumbnail(String fileName) {
        try     
        {

            final int THUMBNAIL_SIZE = 96;

            FileInputStream fis = new FileInputStream(fileName);
            Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return imageBitmap;
            //imageData = baos.toByteArray();

        }
        catch(Exception ex) {
        	return null;
        }
	}
	
	private static class ThumbnailTask extends AsyncTask<String, Void, Bitmap> {
	    private int mPosition;
	    private ViewHolder mHolder;
	    private int mediaType;

	    public ThumbnailTask(int position, ViewHolder holder, int mediaType) {
	        mPosition = position;
	        mHolder = holder;
	        this.mediaType = mediaType;
	    }

	    @Override
	    protected void onPostExecute(Bitmap bitmap) {
	        if (mHolder != null && mHolder.position == mPosition) {
	            if(mHolder.image != null) {
	            	mHolder.image.setImageBitmap(bitmap);
	            	if (this.mediaType == DataModel.MODE_PICTURES) System.out.println("Priyanka-posted image at:  " + mPosition);
	            } else {
	            	if (this.mediaType == DataModel.MODE_PICTURES)  System.out.println("Priyanka:[Image is null]missed image at:"+mPosition);
	            }
	        } else {
	        	if (this.mediaType == DataModel.MODE_PICTURES) System.out.println("Priyanka:[holder null]missed image at:"+mPosition);
	        }
	    }

		@Override
		protected Bitmap doInBackground(String... params) {
			String fileName = params[0];
			Bitmap image = null;
			
			if (this.mediaType == DataModel.MODE_PICTURES) {
				image = createPictureThumbnail(fileName);
			} else if (this.mediaType == DataModel.MODE_VIDEOS) {
				image = ThumbnailUtils.createVideoThumbnail(fileName, 
			    		   Thumbnails.MICRO_KIND);
			}
			
			return image;
		}
	}
	private static class ViewHolder {
		public ImageView image;
		public CheckedTextView ctv;
		public int position;
	}
}
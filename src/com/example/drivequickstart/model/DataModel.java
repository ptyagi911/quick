package com.example.drivequickstart.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import com.example.drivequickstart.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class DataModel {

	public static final int MODE_PICTURES = 1;
	public static final int MODE_VIDEOS = 2;

    private DBController dbController;

    public DataModel(DBController dbController) {
        this.dbController = dbController;
    }

	public ArrayList<Object> getItems(int mode) {
		return getDCIMCameraRoll(mode);
	}

    public String getCameraRollDir() {
        File dcimCameraDir = new File(Environment.getExternalStorageDirectory(),
                "DCIM/Camera");
        return dcimCameraDir.getAbsolutePath();
    }

	public ArrayList<Object> getDCIMCameraRoll(final int mode) {
        final ArrayList<Object> mediaFiles = new ArrayList < Object >();
		
		if (!isMediaMounted()) {
			Log.e(Constants.TAG_DataModel, "MediaStore is not mounted");
			return mediaFiles;
		}

		File dcimCameraDir = new File(Environment.getExternalStorageDirectory(),
				"DCIM/Camera");
		
		if (!dcimCameraDir.canRead()) {
			Log.e(Constants.TAG_DataModel, "CameraRoll is not readable");
			return mediaFiles;
		}
		
		File[] files = dcimCameraDir.listFiles();

		for(int i=0; i < files.length; i++) {
			final String fileName = files[i].getName();
            final String absolutePath = files[i].getAbsolutePath();

            if ((mode == MODE_VIDEOS) && fileName.endsWith(".mp4")
                    || (mode == MODE_PICTURES) && fileName.endsWith(".jpg")) {


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String, String> row = new HashMap<String, String>();
                        //Bitmap image =  createPictureThumbnail(absolutePath);
                        row.put("mediaType", new Integer(mode).toString());//hack
                        row.put("fileName", fileName);
                        //if (image != null) row.put("thumbnail", image.toString());
                        row.put("thumbnail", "");
                        dbController.insertMediaRow(row);
                    }
                }).start();

                /*ThumbnailTask task = new ThumbnailTask(files[i], mode);
                task.setCallback(new Callback() {
                    @Override
                    public void onSuccess(MediaFile mediaFile) {
                        mediaFiles.add(mediaFile);
                        HashMap<String, String> row = new HashMap<String, String>();

                        row.put("mediaType", ""+mode);//hack
                        row.put("fileName", fileName);
                        row.put("thumbnail", mediaFile.getThumbnail().toString());
                        dbController.insertMediaRow(row);
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        //To change body of implemented methods use File | Settings | File Templates.
                        Log.e("quick", "" + exception.getMessage());
                    }
                });

                task.execute(files[i].getAbsolutePath());
                */
            }
		}
		
		return mediaFiles;
	}
	
	public static boolean isMediaMounted() {
		boolean mounted = false;
		
		mounted = Environment.MEDIA_MOUNTED.equals(
				Environment.getExternalStorageState()) ?
						true : false;
		
		return mounted;
	}


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

    public class ThumbnailTask extends AsyncTask<String, Void, Bitmap> {
        private Callback myCallback;

        private int mediaType;
        private File file;


        public ThumbnailTask(File file, int mediaType) {
            this.file = file;
            this.mediaType = mediaType;
        }

        public void setCallback(Callback callback) {
            myCallback = callback;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (myCallback != null) {
                MediaFile mFile = new MediaFile();
                mFile.setThumbnail(bitmap);
                mFile.setFile(file);
                mFile.setAbsolutePath(file.getAbsolutePath());
                mFile.setName(file.getName());
                myCallback.onSuccess(mFile);
            } else {
                myCallback.onFailure(new Exception("Failed to create media file"));
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
                            MediaStore.Video.Thumbnails.MICRO_KIND);
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
            return image;
        }
    }

    public interface Callback {
        public void onSuccess(MediaFile mediaFile);
        public void onFailure(Exception exception);
    }
}

package com.example.test;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

	private Context mContext;
	private DownloadImageTask downloadImageTask;	
	
	public ImageAdapter(Context c) {
		mContext = c;
	}
	
	@Override
	public int getCount() {
			
		return 2;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
		    if (convertView == null) {  // if it's not recycled, initialize some attributes     
		    	imageView = new ImageView(mContext);
		        imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
		        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		        imageView.setPadding(8, 8, 8, 8);
		    } else {
		        imageView = (ImageView) convertView;
		    }
		    
		    /** 
		     * open database and get an alphabetic order cursor
		     * */
		    DatabaseHelper mDbHelper = new DatabaseHelper(mContext);
		   
		    try {
				mDbHelper.createDataBase();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    mDbHelper.openDataBase();
		    Cursor cursor = mDbHelper.allOverlays();
		    cursor.moveToPosition(position);
		    downloadImageTask = new DownloadImageTask();
		    downloadImageTask.execute(cursor.getString(4));
		    
		    try {
				imageView.setImageBitmap( downloadImageTask.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		    return imageView;
		}

	
	
	
	 public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
			
			private Bitmap loadedImage;
			
			
			@Override
			protected void onPostExecute(Bitmap result) {
				super.onPostExecute(result);
			}
			
			@Override
			protected Bitmap doInBackground(String... imageHttpAddress) {
				URL imageUrl = null;
		        try {
		            imageUrl = new URL(imageHttpAddress[0]);
		            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
		            conn.connect();
		            loadedImage = BitmapFactory.decodeStream(conn.getInputStream());
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
				return loadedImage;
			}

		}
}

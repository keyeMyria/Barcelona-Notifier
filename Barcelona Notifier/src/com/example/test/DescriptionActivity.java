package com.example.test;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class DescriptionActivity extends Activity{
	
	private ImageView imageView;
	private TextView descriptionTv;
    private String imageUrl;
    private String descriptionUrl;
    private ProgressDialog progressDialog = null;
    private DownloadImageTask downloadImageTask = new DownloadImageTask();
    private DownloadStringTask downloadStringTask = new DownloadStringTask();
    private boolean textfinished = false; 
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.description);
	        progressDialog = ProgressDialog.show(this, "Loading", "please wait");
	       
	        ActionBar ab = getActionBar();
	        
	        Bundle extras = getIntent().getExtras();
	        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE
	        					|ActionBar.DISPLAY_SHOW_HOME
	        					|ActionBar.DISPLAY_HOME_AS_UP);
	        ab.setTitle(extras.getString("title"));
	        
	        
	        descriptionUrl = extras.getString("Description_URL");
	        imageUrl = extras.getString("Image_URL");	        
	        
	        imageView = (ImageView) findViewById(R.id.imageView1);
	        TextView titleTv = (TextView) findViewById(R.id.title_text);
	        titleTv.setText(extras.getString("title"));
	        descriptionTv = (TextView) findViewById(R.id.description_text);
	        
	        /**
	         * Asynktask to download description and image
	         * */
	        downloadStringTask.execute(descriptionUrl);	       
	        downloadImageTask.execute(imageUrl);
	     
	        
	 	} 
	 
	 @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId())
		 {
		 	case android.R.id.home:
		 		Intent intent = new Intent(this, MapsActivity.class);
		 		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 		startActivity(intent);
		 		return true;
		 	default:
		 		return super.onOptionsItemSelected(item);
		 }
		 
	 }


	private void setImage(){
		 try {
				imageView.setImageBitmap(downloadImageTask.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
	 }
	 
	 private void setText(){
		 try {
			 descriptionTv.setText(Html.fromHtml(downloadStringTask.get()));
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	 }
	 
	 public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
			
			private Bitmap loadedImage;
			
			
			@Override
			protected void onPostExecute(Bitmap result) {
				
				if(progressDialog != null && textfinished)
				{
					progressDialog.dismiss();
				}
				setImage();
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
	 
	 public class DownloadStringTask extends AsyncTask<String,Void,String> {

			private String Description = "";
			
			@Override
			protected void onPostExecute(String result) {
				textfinished = true;
				setText();
				super.onPostExecute(result);
			}
			
			@Override
			protected String doInBackground(String... urls) {
				for(int i=0;i<urls.length;i++)
				{
					try 
					{
						/** 
						 * important UTF8 to download as image (not ASCII)
						 * */
						URL url = new URL(urls[i]);
						InputStream inputStream = url.openConnection().getInputStream();
						InputStreamReader is = new InputStreamReader(inputStream, "UTF8");
						BufferedReader br = new BufferedReader(is);
						
						String aux = br.readLine();
						while(aux != null)
						{
							Description += aux ;
							aux = br.readLine();
						}
						inputStream.close();
						is.close();
						br.close();
					} catch (Exception e) {
						e.printStackTrace();
					} 
				
				}
				return Description;
			}
		}
	 
	 
}
 
	 
	 

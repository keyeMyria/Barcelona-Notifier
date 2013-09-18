package com.example.test;




import java.io.IOException;
import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapsActivity extends MapActivity {
	
	private  DatabaseHelper mDbHelper = new DatabaseHelper(this);
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        getActionBar(); 
        new ChargeOverlays().execute();
       
    }
    
    /**
     *  here we will load the overlays of map in background with every category cursor (stored on sharedpreferences)
     *   */
    public class ChargeOverlays extends AsyncTask<Void, Void, Void>
    {
    	private GeoPoint geopoint;
    	private OverlayItem overlayItem ;
    	
		@Override
		protected Void doInBackground(Void... params) {
			
			try {
        		mDbHelper.createDataBase();
        	
			} catch (IOException ioe) {
        		throw new Error("unable to create database");	
			}
        
			try { 
        		mDbHelper.openDataBase();
        		
			} catch (SQLException sqle) {
        		throw sqle;	
			}
			
			
			
			MapView mapView = (MapView) findViewById(R.id.mapview);
	       /** load overlays with DB data*/
	        List<Overlay> mapOverlays = mapView.getOverlays();
	        
	       /** change for every cursor */
	        Cursor cursor = mDbHelper.allOverlays();
	        Drawable drawable = getResources().getDrawable(R.drawable.map_icon);
	        
	        
	        MapOverlays itemizedoverlaychurch = new MapOverlays(drawable, getBaseContext());
	        
	        
	        
	       do{
	        		
		        geopoint = new GeoPoint((int) (cursor.getDouble(2)*1e6  ) , 
		        							  (int) (cursor.getDouble(3)*1e6 ));
		        overlayItem = new OverlayItem(geopoint, cursor.getString(1), cursor.getString(4) 
		        							+ " " + cursor.getString(5));
		        itemizedoverlaychurch.addOverlay(overlayItem);    
	       }while(cursor.moveToNext());
	      
	       mapOverlays.add(itemizedoverlaychurch);
	        	       
	        /** add the geopoint, the name of the spot and the url with description */
	       
	        mDbHelper.close();
			return null;
		}
    	
    }
    
    
    
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}



}
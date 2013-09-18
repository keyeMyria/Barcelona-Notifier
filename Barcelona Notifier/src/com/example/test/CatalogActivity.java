package com.example.test;


import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

public class CatalogActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.catalog);		
		GridView gridView = (GridView) findViewById(R.id.gridview);
		gridView.setAdapter(new ImageAdapter(this));
	}

	
	
	
	
}

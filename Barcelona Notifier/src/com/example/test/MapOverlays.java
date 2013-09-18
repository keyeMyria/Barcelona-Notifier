package com.example.test;


import java.util.ArrayList;
import java.util.StringTokenizer;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MapOverlays extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
    private Context mContext;
	
	public MapOverlays(Drawable defaultMarker, Context context) {
		
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}
	
	public void addOverlay(OverlayItem overlay)
	{
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int index) {
		
		return mOverlays.get(index);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		
		OverlayItem item = mOverlays.get(index);
		Intent intent = new Intent(mContext, DescriptionActivity.class);
		
		StringTokenizer tokens = new StringTokenizer(item.getSnippet());
		intent.putExtra("Image_URL", tokens.nextToken());
		intent.putExtra("Description_URL", tokens.nextToken());
		intent.putExtra("title", item.getTitle() );
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		mContext.startActivity(intent);
		
		return true;
	}

}

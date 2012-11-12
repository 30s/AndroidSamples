package com.baidu.mapapi.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MapView;

public class ClickMapView extends MapView {

	private MKSearch mSearch;
	
	public ClickMapView(Context arg0, AttributeSet arg1, int arg2) {
		super(arg0, arg1, arg2);
	}

	public ClickMapView(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
	}

	public ClickMapView(Context context) {
		super(context);
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		GeoPoint pt = this.getProjection().fromPixels(x, y);
		mSearch.reverseGeocode(pt);
		Toast.makeText(getContext(), pt.getLatitudeE6() + "-" + pt.getLongitudeE6(), Toast.LENGTH_SHORT).show();
		return super.onTouchEvent(event);
	}

	public MKSearch getmSearch() {
		return mSearch;
	}

	public void setmSearch(MKSearch mSearch) {
		this.mSearch = mSearch;
	}
}

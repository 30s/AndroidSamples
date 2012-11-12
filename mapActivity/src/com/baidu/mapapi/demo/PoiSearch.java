package com.baidu.mapapi.demo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.OverlayItem;
import com.baidu.mapapi.PoiOverlay;
import com.baidu.mapapi.Projection;
import com.baidu.mapapi.demo.R;


public class PoiSearch extends MapActivity {
	Button mBtnSearch = null;	// ������ť
	Button mSuggestionSearch = null;  //suggestion����
	ListView mSuggestionList = null;
	public static String mStrSuggestions[] = {};
	static View mPopView = null;	// ���markʱ����������View
	
	int iZoom = 0;
	
	static ClickMapView mMapView = null;	// ��ͼView
	MKSearch mSearch = new MKSearch();	// ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.poisearch);
        
		BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey, new BMapApiDemoApp.MyGeneralListener());
		}
		app.mBMapMan.start();
        // ���ʹ�õ�ͼSDK�����ʼ����ͼActivity
        super.initMapActivity(app.mBMapMan);
        
        mSearch.init(app.mBMapMan, new MKSearchListener() {
			
			@Override
			public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGetAddrResult(MKAddrInfo addr, int error) {
				if ( error != 0 ) {
					return;
				} 
				Toast.makeText(getApplicationContext(), 
						addr.addressComponents.city +
						addr.addressComponents.district +
						addr.addressComponents.street, Toast.LENGTH_SHORT).show();
			}
		});
        
        mMapView = (ClickMapView)findViewById(R.id.bmapView);
        mMapView.setBuiltInZoomControls(true);
        //���������Ŷ��������Ҳ��ʾoverlay,Ĭ��Ϊ������
        mMapView.setDrawOverlayWhenZooming(true);
        mMapView.setmSearch(mSearch);
        
 		 mBtnSearch = (Button)findViewById(R.id.search);
         
         OnClickListener clickListener = new OnClickListener(){
 			public void onClick(View v) {
 				SearchButtonProcess(v);
 			}
         };
         mBtnSearch.setOnClickListener(clickListener);
	}
	void SearchButtonProcess(View v) {
		
			Intent intent = null;
			intent = new Intent(PoiSearch.this, MapViewDemo.class);
			this.startActivity(intent);
	}

	
	@Override
	protected void onPause() {
		BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
		app.mBMapMan.stop();
		super.onPause();
	}
	@Override
	protected void onResume() {
		BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
		app.mBMapMan.start();
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}

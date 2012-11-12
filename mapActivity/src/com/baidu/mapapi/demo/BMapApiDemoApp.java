package com.baidu.mapapi.demo;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.*;

public class BMapApiDemoApp extends Application {
	
	static BMapApiDemoApp mDemoApp;
	
	//�ٶ�MapAPI�Ĺ�����
	BMapManager mBMapMan = null;
	
	// ��ȨKey
	// TODO: ���������Key,
	// �����ַ��http://dev.baidu.com/wiki/static/imap/key/
	String mStrKey = "113F01D53FD215E3F623B894F7C1AC9F2680A74C";
	boolean m_bKeyRight = true;	// ��ȨKey��ȷ����֤ͨ��
	
	// �����¼�������������ͨ�������������Ȩ��֤�����
	static class MyGeneralListener implements MKGeneralListener {
		@Override
		public void onGetNetworkState(int iError) {
			Log.d("MyGeneralListener", "onGetNetworkState error is "+ iError);
			Toast.makeText(BMapApiDemoApp.mDemoApp.getApplicationContext(), "��������������",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onGetPermissionState(int iError) {
			Log.d("MyGeneralListener", "onGetPermissionState error is "+ iError);
			if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
				// ��ȨKey����
				Toast.makeText(BMapApiDemoApp.mDemoApp.getApplicationContext(), 
						"����BMapApiDemoApp.java�ļ�������ȷ����ȨKey��",
						Toast.LENGTH_LONG).show();
				BMapApiDemoApp.mDemoApp.m_bKeyRight = false;
			}
		}
	}

	@Override
    public void onCreate() {
		Log.v("BMapApiDemoApp", "onCreate");
		mDemoApp = this;
		mBMapMan = new BMapManager(this);
		mBMapMan.init(this.mStrKey, new MyGeneralListener());
		mBMapMan.getLocationManager().setNotifyInternal(10, 5);

		
		super.onCreate();
	}

	@Override
	//��������app���˳�֮ǰ����mapadpi��destroy()��������ظ���ʼ��������ʱ�����
	public void onTerminate() {
		// TODO Auto-generated method stub
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onTerminate();
	}

}

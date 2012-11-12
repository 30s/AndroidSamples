package com.example.phoneeventsdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class PhoneActivity extends Activity
{
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        final TelephonyManager phoneManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);        
        PhoneStateListener phoneStateListener = new PhoneStateListener() {
        	@Override
        	public void onCallStateChanged(int state, String incomingNumber) { 
        		super.onCallStateChanged(state, incomingNumber);        		        		
        		
        		switch (state) {
        		case TelephonyManager.CALL_STATE_IDLE:
        			Toast.makeText(getApplicationContext(), "idle",	Toast.LENGTH_SHORT).show();
        			break;
        		case TelephonyManager.CALL_STATE_RINGING:
        			// Toast.makeText(getApplicationContext(), "ringing",	Toast.LENGTH_SHORT).show();
        			break;
        		case TelephonyManager.CALL_STATE_OFFHOOK:
        			Toast.makeText(getApplicationContext(), "off hook",	Toast.LENGTH_SHORT).show();
        			break;
        		default:
        			break;
        		}
        	}
        	
        	@Override
        	public void onCallForwardingIndicatorChanged(boolean cfi) {
        		super.onCallForwardingIndicatorChanged(cfi);
        		if ( cfi ) {
        			Toast.makeText(getApplicationContext(), "forwarding call",	Toast.LENGTH_SHORT).show();
        		} else {
        			Toast.makeText(getApplicationContext(), "no forwarding call",	Toast.LENGTH_SHORT).show();
        		}
        	}
        };
        phoneManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        setContentView(R.layout.main);
    }
        
}

package com.ax003d.smsdemo;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        findViewById(R.id.btn_send_sms).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		if ( v.getId() == R.id.btn_send_sms ) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:123456"));
			intent.putExtra("sms_body", "hello world");
			startActivity(intent);
		}
	}

    
}

package com.example.cameracapture;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraCaptureActivity extends Activity implements OnClickListener
{
	private final int CAMERA_CAPTURE = 1;
	private final int PIC_CROP = 2;
	private Uri uri_pic;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button btn_capture = (Button) findViewById(R.id.capture_btn);
        btn_capture.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		if ( v.getId() == R.id.capture_btn ) {
			try {
				Intent cpature_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cpature_intent, CAMERA_CAPTURE);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(this, 
						"Capturing image is not support in your device!", 
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {	
		super.onActivityResult(requestCode, resultCode, data);
		if ( resultCode == RESULT_OK ) {
			if ( requestCode == CAMERA_CAPTURE ) {
				uri_pic = data.getData();
				perform_crop();
			} else if ( requestCode == PIC_CROP ) {
				Bundle extras = data.getExtras();
				Bitmap the_pic = extras.getParcelable("data");
				ImageView view_pic = (ImageView) findViewById(R.id.picture);
				view_pic.setImageBitmap(the_pic);
			}
		}
	}

	private void perform_crop() {
		try {
			Intent crop_intent = new Intent("com.android.camera.action.CROP");
			crop_intent.setDataAndType(uri_pic, "image/*");
			crop_intent.putExtra("crop", "true");
			crop_intent.putExtra("aspectX", 1);
			crop_intent.putExtra("aspectY", 1);
			crop_intent.putExtra("outputX", 256);
			crop_intent.putExtra("outputY", 256);
			crop_intent.putExtra("return-data", true);
			startActivityForResult(crop_intent, PIC_CROP);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, 
					"Cropping image is not support in your device!", 
					Toast.LENGTH_SHORT).show();
		}
	}
}

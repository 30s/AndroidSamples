package com.example.cameracapture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraCaptureActivity extends Activity implements OnClickListener {
	private final int CAMERA_CAPTURE = 1;
	private final int PIC_CROP = 2;
	private Uri uri_pic;
	private Bitmap bmp;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button btn_capture = (Button) findViewById(R.id.capture_btn);
		Button btn_save = (Button) findViewById(R.id.btn_save);
		btn_capture.setOnClickListener(this);
		btn_save.setOnClickListener(this);
	}

	private void saveBitmap() {
		FileOutputStream out = null;
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File storageDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_DCIM);
		File avatar = new File(storageDir.getAbsolutePath() + "/Camera/"
				+ timeStamp + ".png");		
		try {
			avatar.createNewFile();
			if (avatar.exists() && avatar.canWrite()) {
				out = new FileOutputStream(avatar);
				bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
				Toast.makeText(this, avatar.getAbsolutePath(),
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	} // saveBitmap

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.capture_btn) {
			try {
				Intent cpature_intent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cpature_intent, CAMERA_CAPTURE);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(this,
						"Capturing image is not support in your device!",
						Toast.LENGTH_SHORT).show();
			}
		} else if (v.getId() == R.id.btn_save && (bmp != null)) {
			saveBitmap();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == CAMERA_CAPTURE) {
				uri_pic = data.getData();
				perform_crop();
			} else if (requestCode == PIC_CROP) {
				Bundle extras = data.getExtras();
				bmp = extras.getParcelable("data");
				ImageView view_pic = (ImageView) findViewById(R.id.picture);
				view_pic.setImageBitmap(bmp);
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

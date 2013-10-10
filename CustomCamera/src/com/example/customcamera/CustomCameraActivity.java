package com.example.customcamera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.customcamera.utils.Utils;
import com.example.customcamera.views.CameraPreview;

public class CustomCameraActivity extends Activity {

	protected static final String TAG = "CustomCameraActivity";
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	
	public static final int RESIZE_PREVIEW = 1;

	private Camera mCamera;
	private CameraPreview mPreview;
	private MediaRecorder mMediaRecorder;
	private boolean isRecording = false;
	private Button recordButton;
	private FrameLayout preview;
	private CustomCameraHandler ccHandler;

	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
			if (pictureFile == null) {
				Log.d(TAG,
						"Error creating media file, check storage permissions: ");
				return;
			}

			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();
				mCamera.startPreview();
			} catch (FileNotFoundException e) {
				Log.d(TAG, "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d(TAG, "Error accessing file: " + e.getMessage());
			}
		}
	};

	public class CustomCameraHandler extends Handler {
		private final WeakReference<CustomCameraActivity> mActivity;
		
		public CustomCameraHandler(CustomCameraActivity activity) {
			mActivity = new WeakReference<CustomCameraActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			CustomCameraActivity activity = mActivity.get();
			if (activity == null) {
				return;
			}
			
			switch (msg.what) {
			case RESIZE_PREVIEW:
				ArrayList<Integer> size = Utils.getVideoFrameSize();
				activity.setPreviewSize(size.get(0), size.get(1));
				break;
			} 
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_camera);

		ccHandler = new CustomCameraHandler(this);
		// Create an instance of Camera
		mCamera = getCameraInstance();

		// Create our Preview view and set it as the content of our activity.
		mPreview = new CameraPreview(this, mCamera);
		mPreview.setHandler(ccHandler);
		preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mPreview);

		Button captureButton = (Button) findViewById(R.id.button_capture);
		captureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// get an image from the camera
				mCamera.takePicture(null, null, mPicture);
			}
		});

		recordButton = (Button) findViewById(R.id.button_record);
		recordButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isRecording) {
					// stop recording and release camera
					mMediaRecorder.stop(); // stop the recording
					releaseMediaRecorder(); // release the MediaRecorder
											// object
					mPreview.setMode(CameraPreview.MODE_PICTURE);

					// inform the user that recording has stopped
					setCaptureButtonText("Record");
					isRecording = false;
				} else {
					// initialize video camera
					mPreview.setMode(CameraPreview.MODE_VIDEO);
					if (prepareVideoRecorder()) {
						// Camera is available and unlocked, MediaRecorder is
						// prepared,
						// now you can start recording
						mMediaRecorder.start();

						// inform the user that recording has started
						setCaptureButtonText("Stop");
						isRecording = true;
					} else {
						// prepare didn't work, release the camera
						releaseMediaRecorder();
						mPreview.setMode(CameraPreview.MODE_PICTURE);
						// inform user
					}
				}
			}
		});
	}

	public void setPreviewSize(int width, int height) {
		preview.setLayoutParams(new LinearLayout.LayoutParams(preview
				.getHeight() * width / height, preview.getHeight()));
	}

	private void setCaptureButtonText(String string) {
		recordButton.setText(string);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_custom_camera, menu);
		return true;
	}

	/** Check if this device has a camera */
	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"MyCameraApp");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}

	private boolean prepareVideoRecorder() {
		Log.d(TAG, "prepare video recorder");

		if (mCamera == null) {
			mCamera = getCameraInstance();
		}
		mMediaRecorder = new MediaRecorder();

		// Step 1: Unlock and set camera to MediaRecorder
		mCamera.unlock();
		mMediaRecorder.setCamera(mCamera);

		// Step 2: Set sources
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

		// Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
		CamcorderProfile cp = CamcorderProfile
				.get(CamcorderProfile.QUALITY_LOW);
		try {
			CamcorderProfile cp480 = CamcorderProfile
					.get(CamcorderProfile.QUALITY_480P);
			if (cp480 != null) {
				cp = cp480;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d("cc", "width: " + cp.videoFrameWidth);
		Log.d("cc", "height: " + cp.videoFrameHeight);
		setPreviewSize(cp.videoFrameWidth, cp.videoFrameHeight);
		mMediaRecorder.setProfile(cp);
		// mMediaRecorder.setVideoFrameRate(10);

		// Step 4: Set output file
		mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO)
				.toString());
		mMediaRecorder.setMaxFileSize(1024 * 1024); // 1M

		// Step 5: Set the preview output
		mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

		// Step 6: Prepare configured MediaRecorder
		try {
			mMediaRecorder.prepare();
		} catch (IllegalStateException e) {
			Log.d(TAG,
					"IllegalStateException preparing MediaRecorder: "
							+ e.getMessage());
			releaseMediaRecorder();
			return false;
		} catch (IOException e) {
			Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
			releaseMediaRecorder();
			return false;
		}
		return true;
	}

	private void releaseMediaRecorder() {
		if (mMediaRecorder != null) {
			mMediaRecorder.reset(); // clear recorder configuration
			mMediaRecorder.release(); // release the recorder object
			mMediaRecorder = null;
			mCamera.lock(); // lock camera for later use
		}
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseMediaRecorder(); // if you are using MediaRecorder, release it
								// first
		releaseCamera(); // release the camera immediately on pause event
	}

}

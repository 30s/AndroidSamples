package com.example.audiorecorder;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

public class MainActivity extends Activity implements OnClickListener {

	private MediaRecorder mRecorder;
	private String mSound;
	private MediaPlayer mPlayer;
	private Visualizer mVisualizer;
	private ProgressBar prog_volumn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.btn_start).setOnClickListener(this);
		findViewById(R.id.btn_stop).setOnClickListener(this);
		findViewById(R.id.btn_play).setOnClickListener(this);
		findViewById(R.id.btn_pause).setOnClickListener(this);
		findViewById(R.id.btn_stop_play).setOnClickListener(this);
		prog_volumn = (ProgressBar) findViewById(R.id.prog_volumn);
		prog_volumn.setMax(0xFF);
		
		mRecorder = new MediaRecorder();
		mSound = Environment.getExternalStorageDirectory()
				+ "/audiorecorder.3gp";
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private void setupVisualizer() {
		mVisualizer = new Visualizer(mPlayer.getAudioSessionId());
		mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
		mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
			
			@Override
			public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform,
					int samplingRate) {
				if (waveform == null) {
					return;
				}
				byte max = waveform[0];
				for (int i=1; i < waveform.length; i++) {
					prog_volumn.setProgress(waveform[i]);
					if (waveform[i] > max) {
						max = waveform[i];
					}
				}
				Log.d("wave", "max: " + max);
				// prog_volumn.setProgress(max);
			}
			
			@Override
			public void onFftDataCapture(Visualizer visualizer, byte[] fft,
					int samplingRate) {	
			}
		}, Visualizer.getMaxCaptureRate() / 2, true, false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_start:
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mRecorder.setOutputFile(mSound);
			try {
				mRecorder.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mRecorder.start();
			break;
		case R.id.btn_stop:
			mRecorder.stop();
			mRecorder.release();
			break;
		case R.id.btn_play:
			mPlayer = new MediaPlayer();
			try {
				mPlayer.setDataSource(mSound);
				mPlayer.prepare();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			setupVisualizer();
			mVisualizer.setEnabled(true);
			mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {				
				@Override
				public void onCompletion(MediaPlayer mp) {
					mVisualizer.setEnabled(false);
				}
			});
			mPlayer.start();
			break;
		case R.id.btn_pause:
			mPlayer.pause();
			break;
		case R.id.btn_stop_play:
			mPlayer.stop();
			mPlayer.reset();
			break;
		}
	}

}

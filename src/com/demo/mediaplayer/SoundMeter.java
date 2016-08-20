package com.demo.mediaplayer;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class SoundMeter {

	private String VoicePath;
	private String VoiceName;
	private static MediaRecorder mRecorder;
	private String TAG = "SoundMeter";

	public SoundMeter() {
		// TODO Auto-generated constructor stub
		VoicePath = null;
	}

	public String getVoiceName() {
		return VoiceName;
	}

	public void start() {
		VoiceName = UUID.randomUUID().toString() + ".amr";
		String state = android.os.Environment.getExternalStorageState();
		if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
			Log.i(TAG, "SD Card is not mounted,It is  " + state + ".");
		}

		if (mRecorder == null) {
			VoicePath = Environment.getExternalStorageDirectory().getPath()
					+ "/MicroChat/Record/" + VoiceName;

			File directory = new File(VoicePath).getParentFile();
			if (!directory.exists() && !directory.mkdirs()) {
				Log.i(TAG, "Path to file could not be created");
			}

			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mRecorder.setOutputFile(VoicePath);

			try {
				mRecorder.prepare();
				mRecorder.start();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d(TAG, "startRecord");
		}
	}

	public void stop() {
		if (mRecorder != null) {
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
			Log.d(TAG, "stopRecord");
		} else {
			Log.e(TAG, "Recorder is null!");
		}
	}
}
